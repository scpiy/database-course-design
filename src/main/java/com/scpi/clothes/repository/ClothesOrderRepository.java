package com.scpi.clothes.repository;

import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesOrderRepository extends JpaRepository<ClothesOrder, Long> {
    List<ClothesOrder> findAllByOwnUser(User ownUser);
    List<ClothesOrder> findAllByOwnUserOrderByCreateAtDesc(User ownUser);
}
