package com.bidhaa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE tbproducts SET status=false WHERE id=?")
@Table(name = "tbproducts")
public class Product implements Serializable,ParentEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private String tags;
    @CreationTimestamp
    private Date dateCreated;

    @UpdateTimestamp
    private Date dateUpdated;

    @Column(columnDefinition = "boolean default false",nullable = false)
    public boolean status;

    @PreRemove
    public void deleteProduct () {
        this.status = false;
    }

    @PrePersist
    public void saveProduct () {
        this.status = true;
    }
    @Override
    public boolean getStatus() {
        return this.status;
    }
}
