package com.target.retail.service;

import com.target.retail.config.ProductApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

@Service
public class ProductAPIService {

    @Autowired
    public ProductAPIService(RestTemplateBuilder builder, ProductApiProperties prop) {
        this.builder = builder;
        this.prop = prop;
    }

    private RestTemplateBuilder builder;
    private ProductApiProperties prop;

    public String getProductName(int id) {
        String requestApi = this.prop.getApi() + id + this.prop.getParam();
        ResponseEntity<String> response = invokeRestCall(requestApi);
        String description = "";
        if (response.getStatusCode().equals(HttpStatus.MOVED_PERMANENTLY)) {
            try {
                response = invokeRestCall(response.getHeaders().getLocation().toString());
                try {
                    return parseJson(response);
                }catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                }
            } catch (NotFoundException e) {
                return "";
            }
        }
        return description;
    }

    private String parseJson(ResponseEntity<String> response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject json = jsonObject.getJSONObject("product").getJSONObject("item").getJSONObject("product_description");
        return json.get("title").toString();
    }

    private ResponseEntity<String> invokeRestCall(String requestApi) {
        return this.builder.errorHandler(new RestTemplateResponseErrorHandler()).build().getForEntity(requestApi, String.class);
    }


}
