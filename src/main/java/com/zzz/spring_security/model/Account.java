package com.zzz.spring_security.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    // If frontend team don't want to handle authorities
    // You can use "@JsonIgnore" with this attribute.
}
