/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author itexps
 */
@SpringBootApplication
@EnableBinding(value={Sink.class})
@EnableRedisRepositories() 
public class Application {
      @Bean
   public RestTemplate restTemplate() {
     return new RestTemplate();
   }
    	@Autowired 
        private ProductCache cache;
        @StreamListener(Sink.INPUT)
        public void getMessage(ProductContract pc){
            System.out.println("Received message");
            System.out.println(pc);
            cache.save(pc);
        }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
	JedisConnectionFactory jedisConnectionFactory() {
		//String hostname = serviceConfig.getRedisServer();
		//int port = Integer.parseInt(serviceConfig.getRedisPort());
	    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
	    //redisStandaloneConfiguration.setPassword(RedisPassword.of("yourRedisPasswordIfAny"));
	    return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}
    
}
