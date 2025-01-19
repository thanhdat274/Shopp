package com.jvb.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jvb.demo.entity.Sku;

public interface SkuRepository extends JpaRepository<Sku, Long>{
    @Transactional
    @Modifying
    @Query(value = "update sku set price =:price, quantity =:quantity where id =:skuID", nativeQuery = true)
    int updateSku(Float price, Integer quantity, Long skuID);
}
