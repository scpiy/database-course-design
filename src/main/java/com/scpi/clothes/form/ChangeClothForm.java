package com.scpi.clothes.form;

import com.scpi.clothes.model.Clothes;
import lombok.Data;

@Data
public class ChangeClothForm {
    private Long clothId;
    private String clothName;
    private String clothType;
    private String audienceType;
    private Long inventory;
    private Long price;

    public Clothes toCloth() {
        Clothes clothes = new Clothes();
        clothes.setId(clothId);
        clothes.setName(clothName);
        clothes.setType(clothType);
        clothes.setAudienceType(audienceType);
        clothes.setInventory(inventory);
        clothes.setPrice(price);
        return clothes;
    }
}
