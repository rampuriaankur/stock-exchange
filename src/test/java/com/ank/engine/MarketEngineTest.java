package com.ank.engine;

import com.ank.model.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MarketEngineTest {


    @Autowired
    private MarketEngine marketEngine;

    @Autowired
    TraderConfig traderConfig;


    static final String SYMBOL = "ABC_SYMBOL_1";
    static final String SYMBOL_2 = "ABC_SYMBOL_2";

    SellOrder SELL_ORDER_1 = new SellOrder(SYMBOL, 100, 20.30);
    SellOrder SELL_ORDER_2 = new SellOrder(SYMBOL, 100, 20.25);
    SellOrder SELL_ORDER_3 = new SellOrder(SYMBOL, 200, 20.30);
    BuyOrder BUY_ORDER_1 = new BuyOrder(SYMBOL, 100, 20.15);
    BuyOrder BUY_ORDER_2 = new BuyOrder(SYMBOL, 200, 20.20);
    BuyOrder BUY_ORDER_3 = new BuyOrder(SYMBOL, 200, 20.15);

    @Test
    public void testSellOrders() throws Exception {
        OrderBook ob = new OrderBook(SYMBOL);
        ob.put(SELL_ORDER_1);
        Thread.sleep(100);
        ob.put(SELL_ORDER_2);
        Thread.sleep(100);
        ob.put(SELL_ORDER_3);

        assertEquals(SELL_ORDER_2, ob.popSellOrder(), "expected order 2");
        assertEquals(SELL_ORDER_1, ob.popSellOrder(), "expected order 1");
        assertEquals(SELL_ORDER_3, ob.popSellOrder(), "expected order 3");
        assertTrue(ob.peekSellOrder() == null, "order book not empty");
    }


    @Test
    public void testAddMatchingOrder() throws Exception {



        marketEngine.init(SYMBOL_2, loadOrder(SYMBOL_2));

        Trader traderA = traderConfig.buildTraderA();
        Trader traderB = traderConfig.buildTraderB();
        Trader traderC = traderConfig.buildTraderC();
        Trader traderD = traderConfig.buildTraderD();


        traderA.submitOrder(SELL_ORDER_1);
        Thread.sleep(100);
        traderA.submitOrder(SELL_ORDER_2);
        Thread.sleep(100);
        traderB.submitOrder(SELL_ORDER_3);
        Thread.sleep(100);
        traderC.submitOrder(BUY_ORDER_1);
        Thread.sleep(100);
        traderC.submitOrder(BUY_ORDER_2);
        Thread.sleep(100);
        traderD.submitOrder(BUY_ORDER_3);

        OrderBook ob = marketEngine.getOrderBookMap().get(SYMBOL);
        assertFalse(ob.matchAvailableOrder(), "There should be now match now");


        // make traderD submitting traderA matching buy order
        BuyOrder matchingOrder = new BuyOrder(SYMBOL, 250, 20.35);
        traderD.submitOrder(matchingOrder);

        assertTrue(ob.matchAvailableOrder(), "There should be traderA match now");
        System.out.println("Matching order: " + matchingOrder);

        marketEngine.executeLimitOrderMatch(SYMBOL);
        assertFalse(ob.isPending(SELL_ORDER_2), "Sell order 2 should be executed");
        assertTrue(ob.isPending(matchingOrder), "Matching order should still be pending");
        System.out.println("Matching order: " + matchingOrder);

        assertTrue(ob.matchAvailableOrder(), "There should be traderA match now");

        marketEngine.executeLimitOrderMatch(SYMBOL);
        assertFalse(ob.isPending(SELL_ORDER_1), "Sell order 1 should be executed");
        assertTrue(ob.isPending(matchingOrder), "Matching order should still be pending");
        System.out.println("Matching order: " + matchingOrder);

        assertTrue(ob.matchAvailableOrder(), "There should be traderA match now");

        marketEngine.executeLimitOrderMatch(SYMBOL);
        assertTrue(ob.isPending(SELL_ORDER_3), "Sell order 3 should still be pending");
        assertFalse(ob.isPending(matchingOrder), "Matching order should be executed");
        System.out.println("Matching order: " + matchingOrder);

        assertFalse(ob.matchAvailableOrder(), "No more matches expected");
        System.out.println("Matching order: " + matchingOrder);
    }

    @Test
    public void testBuyOrders() throws Exception {
        OrderBook ob = new OrderBook(SYMBOL);
        ob.put(BUY_ORDER_1);
        Thread.sleep(100);
        ob.put(BUY_ORDER_2);
        Thread.sleep(100);
        ob.put(BUY_ORDER_3);

        assertEquals(BUY_ORDER_2, ob.popBuyOrder(), "expected order O2");
        assertEquals(BUY_ORDER_1, ob.popBuyOrder(), "expected order O1");
        assertEquals(BUY_ORDER_3, ob.popBuyOrder(), "expected order O3");
        assertTrue(ob.peekBuyOrder() == null, "order book not empty");
    }

    //Mock data to load
    static public List<Order> loadOrder(String symbol) {
        SellOrder SELL_ORDER_1 = new SellOrder(symbol, 100, 20.30);
        SellOrder SELL_ORDER_2 = new SellOrder(symbol, 100, 20.25);
        SellOrder SELL_ORDER_3 = new SellOrder(symbol, 200, 20.30);
        BuyOrder BUY_ORDER_1 = new BuyOrder(symbol, 100, 20.15);
        BuyOrder BUY_ORDER_2 = new BuyOrder(symbol, 200, 20.20);
        BuyOrder BUY_ORDER_3 = new BuyOrder(symbol, 200, 20.15);

        List<Order> orderList = new ArrayList<>();
        orderList.add(SELL_ORDER_1);
        orderList.add(SELL_ORDER_2);
        orderList.add(SELL_ORDER_3);
        orderList.add(BUY_ORDER_1);
        orderList.add(BUY_ORDER_2);
        orderList.add(BUY_ORDER_3);
        return orderList;
    }



}