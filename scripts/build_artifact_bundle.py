#!/usr/bin/env python3
from __future__ import annotations

import argparse
from datetime import datetime
from typing import List, Literal, Optional

from git import NULL_TREE, Repo
from pydantic import BaseModel, Field


class PeerEvaluation(BaseModel):
    pass


class ArtifactBundle(BaseModel):
    team_id: str
    sprint_id: str
    commits: List["CommitInfo"] = Field(default_factory=list)
    jira_tickets: List["JiraTicket"] = Field(default_factory=list)
    peer_evaluations: List[PeerEvaluation] = Field(default_factory=list)


class FileDiff(BaseModel):
    filename: str = Field(description="Path of the changed file")
    change_type: Literal["added", "modified", "deleted", "renamed"] = "modified"
    lines_added: int = Field(default=0, ge=0)
    lines_deleted: int = Field(default=0, ge=0)


class CommitInfo(BaseModel):
    sha: str = Field(description="Commit hash — needed for evidence citations in rubric justifications")
    author: str = Field(description="Student handle or name")
    message: str = Field(description="Commit message text")
    timestamp: datetime = Field(description="When the commit was made")
    lines_added: int = Field(default=0, ge=0, description="Aggregate total, kept for quick-glance prompts")
    lines_deleted: int = Field(default=0, ge=0, description="Aggregate total, kept for quick-glance prompts")
    files_changed: list[FileDiff] = Field(default_factory=list)


class JiraTicket(BaseModel):
    ticket_id: str = Field(description="e.g. SPRINT-101")
    summary: str = Field(description="Title of task")
    assignee: str = Field(description="Student assigned")
    status: Literal["To Do", "In Progress", "In Review", "Done", "Blocked"] = Field(
        description="Match these exactly to your own Jira board's column names once you set it up"
    )
    timestamp: datetime = Field(
        description="When this ticket's current status was last set -- mirrors CommitInfo's timestamp field"
    )
    issue_type: Literal["Bug", "Feature", "Story", "Task", "Epic", "Sub-task"] = Field(
        description="Type of Jira ticket"
    )
    story_points: Optional[int] = Field(
        default=None,
        ge=0,
        description="None means not estimated — that's a planning-quality signal, not a value to guess at",
    )
    sprint_id: Optional[str] = Field(
        default=None,
        description="Sprint this ticket is currently assigned to. None for backlog items not yet pulled into a sprint.",
    )


def _safe_int(value: object) -> int:
    return int(value) if isinstance(value, int) and value >= 0 else 0


def _diff_change_type(diff_item) -> Literal["added", "modified", "deleted", "renamed"]:
    if diff_item.renamed_file:
        return "renamed"
    if diff_item.new_file:
        return "added"
    if diff_item.deleted_file:
        return "deleted"
    return "modified"


def _diff_filename(diff_item) -> str:
    if diff_item.renamed_file:
        return diff_item.b_path or diff_item.a_path or "unknown"
    if diff_item.deleted_file:
        return diff_item.a_path or "unknown"
    return diff_item.b_path or diff_item.a_path or "unknown"


def _normalize_stat_keys(stats_by_file: dict[str, dict]) -> dict[str, dict]:
    normalized: dict[str, dict] = {}
    for path, values in stats_by_file.items():
        normalized[path] = values
        if "=>" in path:
            compact = path.replace("{", "").replace("}", "")
            left, right = [part.strip() for part in compact.split("=>", maxsplit=1)]
            normalized[right] = values
            normalized[left] = values
    return normalized


def build_artifact_bundle(
    repo_path: str,
    team_id: str,
    sprint_id: str,
    since: Optional[str] = None,
    until: Optional[str] = None,
    from_commit: Optional[str] = None,
    to_commit: str = "HEAD",
) -> ArtifactBundle:
    repo = Repo(repo_path)

    rev = to_commit
    if from_commit:
        rev = f"{from_commit}..{to_commit}"

    commits = list(repo.iter_commits(rev=rev, since=since, until=until))
    commits.reverse()

    commit_models: list[CommitInfo] = []
    for commit in commits:
        if commit.parents:
            diff_items = commit.parents[0].diff(commit, create_patch=False)
        else:
            diff_items = commit.diff(NULL_TREE, create_patch=False)

        stats = commit.stats.files or {}
        stats_lookup = _normalize_stat_keys(stats)

        file_diffs: list[FileDiff] = []
        for diff_item in diff_items:
            filename = _diff_filename(diff_item)
            stat_values = stats_lookup.get(filename, {})
            file_diffs.append(
                FileDiff(
                    filename=filename,
                    change_type=_diff_change_type(diff_item),
                    lines_added=_safe_int(stat_values.get("insertions")),
                    lines_deleted=_safe_int(stat_values.get("deletions")),
                )
            )

        totals = commit.stats.total or {}
        author = commit.author.name or commit.author.email or "unknown"

        commit_models.append(
            CommitInfo(
                sha=commit.hexsha,
                author=author,
                message=commit.message.strip(),
                timestamp=commit.committed_datetime.isoformat(),
                lines_added=_safe_int(totals.get("insertions")),
                lines_deleted=_safe_int(totals.get("deletions")),
                files_changed=file_diffs,
            )
        )

    return ArtifactBundle(team_id=team_id, sprint_id=sprint_id, commits=commit_models)


def main() -> None:
    parser = argparse.ArgumentParser(description="Build ArtifactBundle JSON from Git history")
    parser.add_argument("--repo-path", default=".", help="Path to git repository")
    parser.add_argument("--team-id", required=True)
    parser.add_argument("--sprint-id", required=True)
    parser.add_argument("--since", help="ISO datetime or git since string")
    parser.add_argument("--until", help="ISO datetime or git until string")
    parser.add_argument("--from-commit", help="Start commit (exclusive)")
    parser.add_argument("--to-commit", default="HEAD", help="End commit (inclusive)")
    args = parser.parse_args()

    bundle = build_artifact_bundle(
        repo_path=args.repo_path,
        team_id=args.team_id,
        sprint_id=args.sprint_id,
        since=args.since,
        until=args.until,
        from_commit=args.from_commit,
        to_commit=args.to_commit,
    )

    print(bundle.model_dump_json(indent=2))


if __name__ == "__main__":
    main()
