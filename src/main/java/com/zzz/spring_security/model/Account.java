package com.zzz.spring_security.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "role")
    private String role;
}
