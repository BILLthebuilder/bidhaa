package com.bidhaa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
@Table(name="tbprivilege")
@SQLDelete(sql = "UPDATE tbprivilege SET status=false WHERE id=?")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Privilege implements Serializable {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    private Collection<Role> roles;

    @Column(columnDefinition = "boolean default false",nullable = false)
    private boolean status;

    @PreRemove
    public void deletePrivillege () {
        this.status = false;
    }

    @PrePersist
    public void savePrivillege () {
        this.status = true;
    }
}
