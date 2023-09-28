package com.bidhaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

import jakarta.persistence.*;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@SQLDelete(sql = "UPDATE tbusers SET status=false WHERE id=?")
@Table(name = "tbusers")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(15)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(15)")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String password;

    @Column(name = "phone_number", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String phoneNumber;

    @Column(name = "verification_code",columnDefinition = "VARCHAR(255)")
    private String verficationCode;

    @CreationTimestamp
    private Date dateCreated;

    @UpdateTimestamp
    private Date dateUpdated;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbusers_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("users")
    private Collection<Role> roles;


    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status;

    /**
     * Ensures status value is also updated in the
     * current session during deletion
     */
    @PreRemove
    public void deleteUser () {
        this.status = false;
    }

}
