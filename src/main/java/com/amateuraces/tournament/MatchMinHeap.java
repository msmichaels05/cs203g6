package com.amateuraces.tournament;

import com.amateuraces.player.Player;
import com.amateuraces.match.Match;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

//import org.hibernate.mapping.List;
import java.util.List;
import java.util.ArrayList;

public class MatchMinHeap {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private List<Match> heap;
    
    private int size;

    public MatchMinHeap(int size) {
        this.heap = new ArrayList<Match>();
        this.size = 0;
    }

    // Get the parent index
    private int getParentIndex(int index) { //root is the final match
        return (index - 1) / 2; 
    }

    // Get the left child index
    private int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }

    // Get the right child index
    private int getRightChildIndex(int index) {
        return 2 * index + 2;
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        Match temp = heap.get(i);
        heap.set(i,heap.get(j));
        heap.set(j, temp);
    }

    // // Double the size of the array if it's full
    // private void resize() {
    //     Match[] newHeap = new Match[heap.length * 2];
    //     System.arraycopy(heap, 0, newHeap, 0, heap.length);
    //     heap = newHeap;
    // }

    public List<Match> getAllMatches() {return heap;}

    public int getIndex(Match match) {
        for (int i=0; i<heap.size(); i++) {
            if (heap.get(i).equals(match)) {
                return i;
            }
        }
        return -1; //invalid match
    }

    // Add a new match to the heap
    public void insert(Match match) {
        // if (size == heap.length) {
        //     resize();  // Resize the array if it's full
        // }
        heap.set(size, match);  // Add the new match to the end of the heap
        size++;
        heapifyUp(size - 1);  // Restore the heap property by moving the match up
    }

    // Remove the root (smallest matchId) from the heap
    public Match remove() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        Match root = heap.get(0);
        heap.set(0, heap.get(size - 1));  // Move the last match to the root
        size--;
        heapifyDown(0);  // Restore the heap property by moving the root down

        return root;  // Return the match with the smallest matchId
    }

    // Restore the heap property by moving the match at index up
    private void heapifyUp(int index) {
        int parentIndex = getParentIndex(index);

        while (index > 0 && heap.get(index).getId() < heap.get(parentIndex).getId()) {
            swap(index, parentIndex);  // Swap with the parent if the current matchId is smaller
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    // Restore the heap property by moving the match at index down
    private void heapifyDown(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);
        int smallestIndex = index;

        if (leftChildIndex < size && heap.get(leftChildIndex).getId() < heap.get(smallestIndex).getId()) {
            smallestIndex = leftChildIndex;
        }

        if (rightChildIndex < size && heap.get(rightChildIndex).getId() < heap.get(smallestIndex).getId()) {
            smallestIndex = rightChildIndex;
        }

        if (smallestIndex != index) {
            swap(index, smallestIndex);
            heapifyDown(smallestIndex);  // Recursively heapify down
        }
    }

    // Get the size of the heap
    public int size() {
        return size;
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Peek the root (smallest matchId) without removing it
    public Match peek() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    // Method to get the height of the heap
    private int getHeight() {
        if (size == 0) {
            return -1; // Empty heap has height -1
        }
        return (int) (Math.floor(Math.log(size) / Math.log(2)));
        // Alternatively, use the bitwise method:
        // int height = -1;
        // int nodes = size;
        // while (nodes > 0) {
        //     nodes = nodes >> 1;
        //     height++;
        // }
        // return height;
    }

    public List<Match> matchesInRound(int roundNo) {
        List<Match> nodesAtHeight = new ArrayList<>();
        if (getHeight() == -1) return nodesAtHeight; // If draw is not out yet / deadline for registration not up yet
        int h = getHeight() - roundNo + 1;
        int startIndex = (1 << h) - 1;             // Calculate starting index
        int endIndex = (1 << (h + 1)) - 2;         // Calculate ending index

        // Ensure indices are within the bounds of the heap size
        for (int i = startIndex; i <= endIndex && i < size; i++) {
            nodesAtHeight.add(heap.get(i));
        }

        return nodesAtHeight;
    }

    public Match recordMatchResult(Match match, int roundNo, Player winner, String score) {
        List<Match> matchesInRound = matchesInRound(roundNo);
        int matchIndex = -1;
        for (int i=0; i<matchesInRound.size(); i++) {
            if (matchesInRound.get(i).equals(match)) {
                matchIndex = i;
            }
        }

        if (matchIndex == -1) return null; //Invalid match

        //Update winner
        return null;
    }

    public Match updateNextRound(Match match, int roundNo, Player winner) {
        List<Match> matchesInRound = matchesInRound(roundNo);
        int matchIndex = -1;
        for (int i=0; i<matchesInRound.size(); i++) {
            if (matchesInRound.get(i).equals(match)) {
                matchIndex = i;
            }
        }

        if (matchIndex == -1) return null; //Invalid match
        
        int nextRoundMatchIndex = getParentIndex(matchIndex);
        if (getLeftChildIndex(nextRoundMatchIndex) == matchIndex) {
            heap.get(nextRoundMatchIndex).setPlayer1(winner);
        }
        if (getRightChildIndex(nextRoundMatchIndex) == matchIndex) {
            heap.get(nextRoundMatchIndex).setPlayer2(winner);
        }
        return heap.get(nextRoundMatchIndex);
    }

    public String printHeap() { //To print out the draw on console
        String printedDraw = "";
        if (size == 0) {
            System.out.println("The heap is empty.");
            return printedDraw;
        }
    
        int height = getHeight();
        int index = 0;
    
        for (int h = 0; h <= height; h++) {
            int levelSize = 1 << h; // Number of nodes at this level: 2^h
            printedDraw += "Level " + h + ":\n";
    
            for (int i = 0; i < levelSize && index < size; i++) {
                Match match = heap.get(index);
                String player1Name = (match.getPlayer1() != null) ? match.getPlayer1().getName() : "TBD";
                String player2Name = (match.getPlayer2() != null) ? match.getPlayer2().getName() : "TBD";
                printedDraw += "  Match ID " + index + ": " + player1Name + " vs. " + player2Name + "\n";
                index++;
            }
            printedDraw += "\n";
        }
        return printedDraw;
    }    
}

