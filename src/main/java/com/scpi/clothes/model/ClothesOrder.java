package com.scpi.clothes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Proxy(lazy = false)
public class ClothesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @ManyToOne
    private Long userId;

    @ManyToMany(targetEntity = Clothes.class, fetch = FetchType.EAGER)
    private List<Clothes> clothes;

    private Date createAt;
    private boolean payment;

    // price

    @PrePersist
    void createAt() {
        this.createAt = new Date();
    }
}
