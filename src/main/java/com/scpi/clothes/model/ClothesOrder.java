package com.scpi.clothes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
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

    @ManyToOne
    private User ownUser;

    // TODO there should contain the number of clothes
//    @ManyToMany(targetEntity = Clothes.class, fetch = FetchType.EAGER)
//    @JoinTable(name = "clothes_order_clothes",
//                joinColumns = @JoinColumn(referencedColumnName = "id", name = "clothes_order_id"),
//                inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "clothes_id"))
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<AssociationOfClothAndOrder> clothes;

    private Date createAt;
    private boolean payment;

    private Long amount;

    @PrePersist
    void createAt() {
        this.createAt = new Date();
    }
}
