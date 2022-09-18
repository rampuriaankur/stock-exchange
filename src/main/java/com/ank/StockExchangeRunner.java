package com.ank;

import com.ank.engine.MarketEngine;
import com.ank.model.BuyOrder;
import com.ank.model.Order;
import com.ank.model.SellOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

import javax.annotation.PostConstruct;
import javax.swing.*;

@SpringBootApplication
public class StockExchangeRunner implements CommandLineRunner {

    @Autowired
    private MarketEngine marketEngine;

    public static void main(String[] args) {
        SpringApplication.run(StockExchangeRunner.class, args);
    }

    //Mock data to load
    static public List loadOrder(String symbol) {
        SellOrder SELL_ORDER_1 = new SellOrder("SYMBOL_1", 100, 20.30);
        SellOrder SELL_ORDER_2 = new SellOrder("SYMBOL_1", 100, 20.25);
        SellOrder SELL_ORDER_3 = new SellOrder("SYMBOL_1", 200, 20.30);
        BuyOrder BUY_ORDER_1 = new BuyOrder("SYMBOL_1", 100, 20.15);
        BuyOrder BUY_ORDER_2 = new BuyOrder("SYMBOL_1", 200, 20.20);
        BuyOrder BUY_ORDER_3 = new BuyOrder("SYMBOL_1", 200, 20.15);
        List<Order> orderList = new ArrayList<>();
        orderList.add(SELL_ORDER_1);
        orderList.add(SELL_ORDER_2);
        orderList.add(SELL_ORDER_3);
        orderList.add(BUY_ORDER_1);
        orderList.add(BUY_ORDER_2);
        orderList.add(BUY_ORDER_3);
        return orderList;
    }

    @Override
    public void run(String... args) throws Exception {

        marketEngine.init("SYMBOL_1", loadOrder("SYMBOL_1"));
    }

    @PostConstruct
    public void init(){


    }
}
