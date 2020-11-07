package com.scpi.clothes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Proxy(lazy = false)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(targetEntity = Clothes.class, fetch = FetchType.EAGER)
    private List<Clothes> clothes;

    //price
    // and more information such as location, delivery method, etc.
}
