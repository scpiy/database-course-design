package com.scpi.clothes.model;

import lombok.Data;

@Data
public class Orders {
    Clothes clothes;
    Integer countOfCloth;

    public Orders(Clothes clothes, Integer countOfCloth) {
        this.clothes = clothes;
        this.countOfCloth = countOfCloth;
    }
}
