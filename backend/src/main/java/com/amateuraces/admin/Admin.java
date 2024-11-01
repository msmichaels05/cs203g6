package com.amateuraces.admin;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.amateuraces.user.*;

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
}