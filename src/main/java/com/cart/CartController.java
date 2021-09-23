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

/**
 *
 * @author itexps
 */
@RestController
public class CartController {
   @Autowired
    private ProductCache cache;
   @Autowired
   private ProductCacheRepo repo;
   private static int[] list;
   @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public void addProducts(@RequestBody ProductsContract products){
        list=products.getList();
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
