package com.scpi.clothes.repository;

import com.scpi.clothes.model.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findAllByAudienceType(String type);

    @Query("select audienceType from Clothes group by audienceType")
    List<String> findAllAudience();
}
