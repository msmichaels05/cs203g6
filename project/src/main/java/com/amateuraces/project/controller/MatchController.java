package com.amateuraces.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/matches")
public class MatchController {

    // private MatchService matchService; to be used

    // Constructor Injection

    @GetMapping("/{id}")
    public String viewMatchDetails(@PathVariable String id, Model model) {
        // Show match details
        return "";
    }

    @GetMapping("/{id}/record-result")
    public String recordMatchResultPage(@PathVariable String id, Model model) {
        // Return match result form
        return "";
    }

    // @PostMapping("/{id}/record-result")
    // public String recordMatchResult(@ModelAttribute MatchResultDTO resultDTO) {
    //     // Handle recording match result
    // }

    @PostMapping("/{id}/reschedule")
    public String rescheduleMatch(@PathVariable String id, @RequestParam String newDate) {
        // Handle match rescheduling
        return "";
    }

    @PostMapping("/{id}/cancel")
    public String cancelMatch(@PathVariable String id) {
        // Handle match cancellation
        return "";
    }
}
