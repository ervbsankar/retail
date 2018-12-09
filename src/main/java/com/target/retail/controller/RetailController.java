package com.target.retail.controller;

import com.target.retail.model.Product;
import com.target.retail.model.ProductPrice;
import com.target.retail.service.ProductAPIService;
import com.target.retail.service.ProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class will delegate the requests to get product description/price and to update product price.
 */
@RestController
public class RetailController {

    @Autowired
    public RetailController(ProductAPIService apiService, ProductPriceService priceService) {
        this.apiService = apiService;
        this.priceService = priceService;
    }

    private ProductAPIService apiService;
    private ProductPriceService priceService;

    /**
     * Get product description and product price
     * @param id - product id
     * @return  - Product details
     */
    @GetMapping("/products/{id}")
    public Product getProducts(@PathVariable int id){
        Product product = new Product();
        product.setId(id);
        product.setName(apiService.getProductName(id));
        ProductPrice currentPrice = priceService.getProductPrice(id);
        product.setCurrentPrice(currentPrice);
        return product;
    }

    /**
     * Update product price
     * @param id - product Id
     * @param product - product details with updated Price and currency code
     * @return - updated product details
     */

    @PutMapping("/products/{id}")
    public ProductPrice updateProducts(@PathVariable int id, @RequestBody Product product){
        ProductPrice productPrice = product.getCurrentPrice();
        productPrice.setProductId(id);
        return priceService.updateProductPrice(productPrice);
    }
}
