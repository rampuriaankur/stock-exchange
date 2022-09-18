package com.ank.engine;

import com.ank.model.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraderConfig {

    @Autowired
    MarketEngine marketEngine;


    @Bean
    public Trader buildTraderA() {
        return new Trader( marketEngine).setId("A");
    }

    @Bean
    public Trader buildTraderB() {
        return new Trader(marketEngine).setId("B");

    }

    @Bean
    public Trader buildTraderC() {
        return new Trader( marketEngine).setId("C");
    }

    @Bean
    public Trader buildTraderD() {
        return new Trader( marketEngine).setId("D");
    }

}
