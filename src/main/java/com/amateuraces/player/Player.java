package com.amateuraces.player;

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
public class Player{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;

<<<<<<< HEAD
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;
    private String name;
    private String email;
    private String password;
=======
   
    @Size(min = 1, max = 15)
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
    private String phoneNumber;
  
    private int age;
  
    private String gender;
    private int elo = 1500;  // Starting ELO
    private int matchesPlayed;
    private int matchesWon;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    // Updated Constructor with wins and losses
<<<<<<< HEAD
    public Player(String name, String gender, int age, String email, String password, String phoneNumber, int matchesPlayed, int matchesWon) {
=======
    public Player( String name, String gender, int age, String email, String password, String phoneNumber, int matchesPlayed, int matchesWon) {
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.name = name;
        // this.elo = elo;
    }

    public Player( String name) {
        this.name = name;
    }

}