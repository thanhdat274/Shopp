package com.jvb.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jvb.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query(value = "SELECT DISTINCT p FROM Product p "
			+ "JOIN Sku s ON p.id = s.product.id "
			+ "WHERE (:name is NULL or p.name LIKE %:name%) "
			+ "AND (COALESCE(:brands, NULL) is NULL OR p.brand in :brands) "
			+ "AND (COALESCE(:types, NULL) is NULL OR p.type in :types) "
			+ "AND (:min_price is NULL OR s.price >= :min_price) "
			+ "AND (:max_price is NULL OR s.price <= :max_price)")
	Page<Product> findAll(String name, List<String> brands, List<String> types, float min_price, float max_price, Pageable pageable);

	@Query(value = "SELECT product.id, SUM(detail.price),SUM(detail.quantity), product.name, product.thumbnail "
		+"FROM product "
		+"JOIN order_detail as detail ON detail.product_id = product.id "
		+"JOIN tbl_order as o ON detail.order_id = o.id "
		+"WHERE o.order_status = 'SHIPPED' "
		+"GROUP BY product.id "
		+"ORDER BY SUM(detail.quantity) DESC, SUM(detail.price) DESC LIMIT 5", nativeQuery = true)
	List<Object[]> getTop5SellingProductRevenua();

	@Query(value = "SELECT p FROM Product as p "
		+"JOIN Sku s ON p.id = s.product.id "
		+"JOIN OrderDetail as detail ON detail.product.id = p.id "
		+"JOIN Order as o ON o.id = detail.order.id "
		+"WHERE o.orderStatus = 'SHIPPED' AND (:name is NULL or p.name LIKE %:name%) "
		+"AND (COALESCE(:brands, NULL) is NULL OR p.brand in :brands) "
		+"AND (COALESCE(:types, NULL) is NULL OR p.type in :types) "
		+"AND (:min_price is NULL OR s.price >= :min_price) "
		+"AND (:max_price is NULL OR s.price <= :max_price)"
		+"GROUP BY p.id "
		+"ORDER BY SUM(detail.quantity) DESC, SUM(detail.price) DESC")
	Page<Product> getAllBestSellingProduct(String name, List<String> brands, List<String> types, float min_price, float max_price, Pageable pageable);
}
