package com.scpi.clothes;

import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ClothesApplicationTests {
    @Autowired
    ClothesRepository clothesRepository;

    @Autowired
    ClothesOrderRepository clothesOrderRepository;

    @Test
    void clothesTest() {
        Clothes clothes = new Clothes();
        clothes.setInventory(200L);
        clothesRepository.save(clothes);
        Assert.assertEquals(200L, (long) clothesRepository.getOne(1L).getInventory());
    }

    @Test
    void clothesOrderTest() {

    }

    @Test
    void contextLoads() {
    }

}
