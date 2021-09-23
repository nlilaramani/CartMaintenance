/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author itexps
 */
@RestController
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
   @Autowired
    private ProductCache cache;
   @Autowired
   private ProductCacheRepo repo;
   @Autowired
   private RestTemplate restTemplate;
   @Autowired
   private Tracer tracer;
    
   private static int[] list;
   @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public void addProducts(@RequestBody ProductsContract products){
        logger.debug("Adding products to cart");
        logger.debug("span:"+tracer.currentSpan().toString());
        list=products.getList();
        for (int i:list){
            HttpHeaders headers=new HttpHeaders();
            headers.add("Authorization", "Basic YWRtaW46YWRtaW4=");
            headers.add("x-b3-traceId",tracer.currentSpan().toString());
            HttpEntity<String> entity=new HttpEntity<String>(headers);
            ResponseEntity<Product> responseEntity;
            responseEntity = 
                    restTemplate.exchange("http://localhost:8000/products/"+i, HttpMethod.GET, entity,Product.class);
            Product p=(Product)responseEntity.getBody();
            logger.debug(p.getName());
        }
    }

   @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public List<Product> getCart(){
        
        List<Product> plist=new ArrayList<Product>();
        for(int i:list){
            Product p=repo.findById(i).get();
            plist.add(p);
        }
        return plist;
    }
}
