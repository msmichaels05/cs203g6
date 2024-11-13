package com.amateuraces.admin;

import com.amateuraces.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id;
    @NotNull(message = "name required")
    private String name;
    @NotNull(message = "phone number required")
    private String phoneNumber;

    @OneToOne
    @MapsId
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    public Admin(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}