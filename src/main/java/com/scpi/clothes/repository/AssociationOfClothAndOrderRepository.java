package com.scpi.clothes.repository;

import com.scpi.clothes.model.AssociationOfClothAndOrder;
import com.scpi.clothes.model.ClothesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssociationOfClothAndOrderRepository extends JpaRepository<AssociationOfClothAndOrder, Long> {
    List<AssociationOfClothAndOrder> findAllByOrder(ClothesOrder clothesOrder);
}
