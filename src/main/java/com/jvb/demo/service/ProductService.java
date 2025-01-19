package com.jvb.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jvb.demo.dto.ProductDTO;
import com.jvb.demo.dto.SkuDTO;
@Service
public interface ProductService {
	Page<ProductDTO> searchProduct(String name, List<String> brands, List<String> types, float min_price, float max_price, Pageable pageable) throws Exception;
	ProductDTO findByID(long id);
	ProductDTO saveProduct(ProductDTO productDTO);
	ProductDTO updateProduct(ProductDTO productDTO) throws Exception;
	void updatePriceAndQuantity(List<SkuDTO> skuDTOs) throws Exception;
	List<Object[]> getTop5SellingProductRevenua();
	Page<ProductDTO> getAllBestSellingProduct(String name, List<String> brands, List<String> types, float min_price, float max_price, Pageable pageable);
}
