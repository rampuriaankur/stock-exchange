package com.ank.model;

import com.ank.engine.MarketEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Trader {

    private MarketEngine marketEngine;
    private String id;


    @Autowired
    public Trader(MarketEngine marketEngine) {
        this.marketEngine = marketEngine;
    }

    public Trader setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void submitOrder(Order order) {
        order.setTrader(this);
        marketEngine.placeLimitOrder(order);
    }

}


