package com.bidhaa.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
@Table(name="tbprivilege")
@SQLDelete(sql = "UPDATE tbprivilege SET status=false WHERE id=?")
@Entity
@Data
@RequiredArgsConstructor
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
