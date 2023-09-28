package com.bidhaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name="tbroles")
@SQLDelete(sql = "UPDATE roles SET status=false WHERE id=?")
@Data
@RequiredArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbroles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("roles")
    private Collection<Privilege> privileges;
}