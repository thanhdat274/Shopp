package com.jvb.demo.service.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jvb.demo.dto.ImageDTO;
import com.jvb.demo.dto.ProductDTO;
import com.jvb.demo.dto.SkuDTO;
import com.jvb.demo.entity.Image;
import com.jvb.demo.entity.Product;
import com.jvb.demo.entity.Sku;
import com.jvb.demo.entity.Variant;
import com.jvb.demo.entity.VariantValue;
import com.jvb.demo.repository.ProductRepository;
import com.jvb.demo.repository.SkuRepository;
import com.jvb.demo.service.ImageService;
import com.jvb.demo.service.ProductService;

@Service
public class ProductServiceImp implements ProductService {
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private ImageService imageService;
	@Autowired
	private SkuRepository skuRepo;

	@Override
	public Page<ProductDTO> searchProduct(String name, List<String> brands, List<String> types, float min_price,
			float max_price, Pageable pageble) throws Exception {
		try {
			Page<Product> products = productRepo.findAll(name, brands, types, min_price, max_price, pageble);
			Page<ProductDTO> productDTO = products.map(product -> new ProductDTO(product));
			return productDTO;
		} catch (Exception e) {
			throw new Exception("DB Exception", e);
		}
	}

	@Override
	public ProductDTO findByID(long id) {
		try {
			Product product = productRepo.findById(id).get();
			return new ProductDTO(product);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public ProductDTO saveProduct(ProductDTO productDTO) {
		Product product = new Product(productDTO);
		List<ImageDTO> imageDTOs = productDTO.getImageDTO();
		List<Image> images = new ArrayList<>();
		for (ImageDTO imageDTO : imageDTOs) {
			if (imageDTO.getFile() != null) {
				String url = imageService.saveImage(imageDTO.getFile());
				if (url != null) {
					Image image = new Image(url);
					image.setProduct(product);
					images.add(image);
				}
			}
		}
		product.setImages(images);
		product.setCreate_date(LocalDateTime.now());
		// if product doesn't have variant, create default Sku
		if (productDTO.getSkuDTO() != null && productDTO.getVariantDTO() == null) {
			SkuDTO skuDTO = productDTO.getSkuDTO().get(0);
			Sku sku = new Sku();
			sku.setPrice(skuDTO.getPrice());
			sku.setQuantity(skuDTO.getQuantity());
			sku.setSku(skuDTO.getSku());
			sku.setProduct(product);
			product.setSkus(Arrays.asList(sku));
			Product result = productRepo.save(product);
			return new ProductDTO(result);
		} else {
			Product result = productRepo.save(product);
			List<Variant> variants = result.getVariants();
			List<List<VariantValue>> combination = mapVariantValue(variants);
			List<List<VariantValue>> result2 = VariantCombinator(combination, 0);
			List<Sku> skus = createProductSku(result2, result);
			skuRepo.saveAll(skus);
			return new ProductDTO(result);
		}
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDTO) throws Exception {
		try {
			Product product = productRepo.findById(productDTO.getId()).get();
			product.setName(productDTO.getName());
			product.setBrand(productDTO.getBrand());
			product.setType(productDTO.getType());
			product.setDescription(productDTO.getDescription());
			product.setStorage(productDTO.getStorage());
			product.setBattery(productDTO.getBattery());
			product.setRam(productDTO.getRam());
			product.setCpu(productDTO.getCpu());
			product.setOperating_system(productDTO.getOperating_system());
			product.setScreen_size(productDTO.getScreen_size());
			product.setScreen_reslution(productDTO.getScreen_reslution());
			product.setWeight(productDTO.getWeight());
			productRepo.save(product);
			updatePriceAndQuantity(productDTO.getSkuDTO());
			return productDTO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception", e);
		}
	}

	@Override
	public void updatePriceAndQuantity(List<SkuDTO> skuDTOs)throws Exception{
		try {
			for(SkuDTO skuDTO : skuDTOs){
				skuRepo.updateSku(skuDTO.getPrice(), skuDTO.getQuantity(), skuDTO.getId());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// return 	list[0][0]: color-red 		list[1][0]: size-M
	// 			list[0][1]: color-blue 		list[1][1]: size-L
	public List<List<VariantValue>> mapVariantValue(List<Variant> variants) {
		List<List<VariantValue>> result = new ArrayList<>();
		// Loop through list variants
		for (int i = 0; i < variants.size(); i++) {
			List<VariantValue> variantValues = new ArrayList<>();
			//// Loop through list value of each variants
			for (int j = 0; j < variants.get(i).getValues().size(); j++) {
				// match variant name to variant's value
				VariantValue variantValue = new VariantValue();
				variantValue.setVariant(variants.get(i));
				variantValue.setValue(variants.get(i).getValues().get(j));
				variantValues.add(variantValue);
			}
			result.add(variantValues);
		}
		return result;
	}

	// return list[0]: color-red, size-M 		list[2]: color-blue, size-M
	// 		  list[1]: color-red, size-L 		list[3]: color-blue, size-L
	public List<List<VariantValue>> VariantCombinator(List<List<VariantValue>> input, int i) {
		// stop condition
		if (i == input.size()) {
			// return a list with an empty list
			List<List<VariantValue>> result = new ArrayList<List<VariantValue>>();
			result.add(new ArrayList<VariantValue>());
			return result;
		}
		List<List<VariantValue>> result = new ArrayList<List<VariantValue>>();
		List<List<VariantValue>> recursive = VariantCombinator(input, i + 1);
		// for each element of the first list of input
		for (int j = 0; j < input.get(i).size(); j++) {
			// add the element to all combinations obtained for the rest of the lists
			for (int k = 0; k < recursive.size(); k++) {
				// copy a combination from recursive
				List<VariantValue> newList = new ArrayList<VariantValue>();
				// add element of the first list
				newList.add(input.get(i).get(j));
				for (VariantValue variantValue : recursive.get(k)) {
					newList.add(variantValue);
				}
				result.add(newList);
			}
		}
		return result;
	}
	//return	sku[0]: price=0, quantiy=0, color-red, size-M 
	//			sku[1]: price=0, quantiy=0, color-blue, size-L			
	public List<Sku> createProductSku(List<List<VariantValue>> input, Product product) {
		List<Sku> skus = new ArrayList<>();
		for (List<VariantValue> variantValues : input) {
			Sku sku = new Sku();
			List<VariantValue> list = new ArrayList<>();
			sku.setPrice(0f);
			sku.setQuantity(0);
			sku.setProduct(product);
			for (VariantValue variantValue : variantValues) {
				VariantValue temp = new VariantValue();
				temp.setSku(sku);
				temp.setVariant(variantValue.getVariant());
				temp.setValue(variantValue.getValue());
				list.add(temp);
			}
			sku.setVariant_values(list);
			skus.add(sku);
		}
		return skus;
	}

	//object[] =[productID, sum(price), sum(quantity), productName, thumbnail]
	@Override
	public List<Object[]> getTop5SellingProductRevenua() {
		return productRepo.getTop5SellingProductRevenua();
	}

	@Override
	public Page<ProductDTO> getAllBestSellingProduct(String name, List<String> brands, List<String> types, float min_price, float max_price, Pageable pageable) {
		Page<Product> products = productRepo.getAllBestSellingProduct(name,brands, types, min_price, max_price, pageable);
		Page<ProductDTO> productDTO = products.map(product -> new ProductDTO(product));
		return productDTO;
	}
}
