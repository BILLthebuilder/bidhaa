package com.bidhaa.model;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
@Table(name="privilege")
@SQLDelete(sql = "UPDATE privilege SET status=false WHERE id=?")
public class Privilege implements Serializable {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String name;

    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
