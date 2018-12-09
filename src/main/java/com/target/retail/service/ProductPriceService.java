package com.target.retail.service;

import com.target.retail.model.ProductPrice;
import com.target.retail.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceService {

    private ProductRepository repo;

    @Autowired
    public ProductPriceService(ProductRepository repo) {
        this.repo = repo;
    }

    public ProductPrice getProductPrice(int productId) {
        return this.repo.getProductPrice(productId);
    }

    public ProductPrice updateProductPrice(ProductPrice productPrice) {
        return this.repo.updateProductPrice(productPrice);
    }
}
