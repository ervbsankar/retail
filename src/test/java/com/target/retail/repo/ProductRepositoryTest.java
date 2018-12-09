package com.target.retail.repo;

import com.mongodb.MongoClient;
import com.target.retail.model.ProductPrice;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.dao.DuplicateKeyException;

@RunWith(SpringRunner.class)
// @DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class ProductRepositoryTest {

    private static MongodExecutable mongodExecutable;
    private static MongoTemplate template;

    @BeforeClass
    public static void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        template = new MongoTemplate(new MongoClient(ip, port), "test");
    }

    @Test
    public void getProductPriceWhenProductNotAvailable() {
        ProductPrice obj = template.findOne(new Query(where("productId").is(123456)), ProductPrice.class);
        assertNull(obj);
    }

    @Test
    public void insertProductPrice() {
        ProductPrice obj = new ProductPrice(123456, 34.56, "USD");
        template.insert(obj);
    }

    @Test
    public void getProductWhenProductAvailable() {
        ProductPrice obj = template.findOne(new Query(where("productId").is(123456)), ProductPrice.class);
        assertNotNull(obj);
    }

    @Test
    public void getProductPriceWhenProductAvailable() {
        ProductPrice obj = template.findOne(new Query(where("productId").is(123456)), ProductPrice.class);
        assertEquals(34.56, obj.getValue(), 0.0);
    }

    @Test(expected = DuplicateKeyException.class)
    public void insertAgainSameProduct() {
        ProductPrice obj = template.findOne(new Query(where("productId").is(123456)), ProductPrice.class);
        template.insert(obj);
    }

    @Test
    public void updateProductPriceWhenProductAvailable() {
        Query query = new Query(where("productId").is(123456));
        Update update = new Update();
        update.set("value", 2100.23); update.set("currencyCode", "INR");
        ProductPrice price = template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ProductPrice.class);
        assertEquals(2100.23, price.getValue(), 0.0);
        assertEquals("INR","INR");
    }


    @Test
    public void updateProductPriceWhenProductPriceNotAvailable() {
        Query query = new Query(where("productId").is(13860428));
        Update update = new Update();
        update.set("value", 123.22); update.set("currencyCode", "USD");
        ProductPrice price = template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), ProductPrice.class);
        assertEquals(123.22, price.getValue(), 0.0);
        assertEquals("USD","USD");
    }

}