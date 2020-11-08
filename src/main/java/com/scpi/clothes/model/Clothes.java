package com.scpi.clothes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Proxy(lazy = false)
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long inventory;
    private Long totalSales;

    @ManyToMany(targetEntity = Supplier.class, mappedBy = "clothes", fetch = FetchType.EAGER)
    private List<Supplier> supplier;

    private String type;
    private String audienceType;

    private Long springSales;
    private Long summerSales;
    private Long autumnSales;
    private Long winterSales;

    // TODO add a price field
}
