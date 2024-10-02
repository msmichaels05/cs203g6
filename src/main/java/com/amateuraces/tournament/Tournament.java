package com.amateuraces.tournament;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Tournament {
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;
    
    @NotNull(message = "TOurnament name should not be null")
    // null elements are considered valid, so we need a size constraints too
    private String name;

    private String location;
    private Date registrationStartDate;
    
    
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Match> matches;
    
    public Tournament(String title){
        this.title = title;
    }
    
}