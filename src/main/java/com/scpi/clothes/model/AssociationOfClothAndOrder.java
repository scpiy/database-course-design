package com.scpi.clothes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "clothes_order_clothes")
@NoArgsConstructor
public class AssociationOfClothAndOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothesOrderId")
    private ClothesOrder order;

    @ManyToOne
    @JoinColumn(name = "clothId")
    private Clothes cloth;

    private Integer countOfCloth;
}

