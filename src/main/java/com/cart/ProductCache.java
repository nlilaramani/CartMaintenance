/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author itexps
 */
@Component
public class ProductCache {
    @Autowired
   ProductCacheRepo redisRepository;
    public void save(ProductContract pc){
        Product p=new Product();
        p.setProductId(pc.getId());
        p.setDescription(pc.getDescription());
        p.setName(pc.getName());
        p.setPrice(pc.getPrice());
        redisRepository.save(p);
    }
    
    public  Iterable<Product> getAll(){
        return redisRepository.findAll();
    }
    
    public ProductCache(){
        
    }
}
