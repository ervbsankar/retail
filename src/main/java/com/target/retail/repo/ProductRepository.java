package com.target.retail.repo;

import com.target.retail.model.Product;
import com.target.retail.model.ProductPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class ProductRepository {

    @Autowired
    public ProductRepository(MongoTemplate template) {
        this.template = template;
    }

    private MongoTemplate template;

    public ProductPrice getProductPrice(int productId) {
        return template.findOne(new Query(where("productId").is(productId)), ProductPrice.class);
    }

    public ProductPrice updateProductPrice(ProductPrice productPrice) {
        Query query = new Query(where("productId").is(productPrice.getProductId()));
        Update update = new Update();
        update.set("value", productPrice.getValue());
        update.set("currencyCode", productPrice.getCurrencyCode());
        return template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), ProductPrice.class);
    }
}
