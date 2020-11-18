package com.scpi.clothes.form;

import com.scpi.clothes.model.Clothes;
import lombok.Data;

@Data
public class AddClothForm {
    private String name;
    private String type;
    private String audienceType;
    private Long inventory;
    private Long price;

    public Clothes toCloth() {
        Clothes clothes = new Clothes();
        clothes.setName(name);
        clothes.setType(type);
        clothes.setAudienceType(audienceType);
        clothes.setInventory(inventory);
        clothes.setPrice(price);
        clothes.setTotalSales(0L);
        clothes.setSpringSales(0L);
        clothes.setSummerSales(0L);
        clothes.setAutumnSales(0L);
        clothes.setWinterSales(0L);
        return clothes;
    }

}
