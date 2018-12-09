package com.target.retail.service;

import com.target.retail.config.ProductApiProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.NotFoundException;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest
public class ProductAPIServiceTest {

    @Autowired
    private RestTemplateBuilder builder;

    @Autowired
    private MockServerRestTemplateCustomizer customizer;

    private RestTemplate template;

    public void before() {

    }

    @Test(expected = NotFoundException.class)
    public void getRemoteCallWhen404Error() {
        assertNotNull(builder);
        template = this.builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
        this.customizer.customize(template);
        MockRestServiceServer server = this.customizer.getServer();
        server.expect(ExpectedCount.once(), requestTo("/v2/pdp/tcin/123456"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = template.getForEntity("/v2/pdp/tcin/123456", String.class);
        server.verify();
    }

    @Test
    public void getRemoteCallAndDescription() throws JSONException {
        assertNotNull(builder);
        Map<RestTemplate, MockRestServiceServer> servers = customizer.getServers();
        Iterator it= servers.keySet().iterator();
        RestTemplate template = null;
        MockRestServiceServer server = null;
        while(it.hasNext())  {
            template = (RestTemplate) it.next();
            server = servers.get(template);
        }
        server.expect(ExpectedCount.once(), requestTo("/v2/pdp/tcin/13860428"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{'product': {'item': {'product_description': {'title': 'The Big Lebowski (Blu-ray)'}}}}", MediaType.APPLICATION_JSON))
                ;
        ResponseEntity<String> response = template.getForEntity("/v2/pdp/tcin/13860428", String.class);
        server.verify();
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject json = jsonObject.getJSONObject("product").getJSONObject("item").getJSONObject("product_description");
        assertEquals("The Big Lebowski (Blu-ray)", json.get("title").toString());
    }

    @Test
    public void getRemoteCallWhen500Error() {
        assertNotNull(builder);
        Map<RestTemplate, MockRestServiceServer> servers = customizer.getServers();
        Iterator it= servers.keySet().iterator();
        RestTemplate template = null;
        MockRestServiceServer server = null;
        while(it.hasNext())  {
            template = (RestTemplate) it.next();
            server = servers.get(template);
        }
        server.expect(ExpectedCount.once(), requestTo("/v2/pdp/tcin/123456"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        ResponseEntity<String> response = template.getForEntity("/v2/pdp/tcin/123456", String.class);
        server.verify();
        assertNull(response.getBody());
    }

}