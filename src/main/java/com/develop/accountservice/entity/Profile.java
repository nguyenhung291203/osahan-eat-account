package com.develop.accountservice.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    @Id
    String id;

    @Column(nullable = false, length = 100)
    String fullName;

    boolean gender;
    String avatar;

    @Column(name = "data_of_birth")
    LocalDate dateOfBirth;

    @Column(length = 255)
    String address;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;
}
