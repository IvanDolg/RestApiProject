package com.example.restapiproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@Setter
@Getter
@Builder
@Table(name = "tb_users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "country_id", nullable = false)
    private int country;

    @Column(name = "city_id", nullable = false)
    private int city;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "last_visit")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd't'hh:mm:ss'z'")
    private LocalDateTime lastVisitDate;
}