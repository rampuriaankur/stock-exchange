package com.ank.engine;

import com.ank.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketEngine {

    private Map<String, OrderBook> orderBookMap;

    public void init(String symbol, List<Order> orderList) {
        this.orderBookMap = new ConcurrentHashMap<>();
        OrderBook orderBook = new OrderBook(symbol);
        orderBookMap.put(symbol , orderBook);
        orderList.stream().forEach(order -> this.placeLimitOrder(order));
    }


    public void placeLimitOrder(Order order) {
        OrderBook orderBook = orderBookMap.get(order.getSymbol());
        if(orderBook!= null) {
            orderBook.put(order);
        }else {
            //Create a new orderbook for another symbol in map.
            OrderBook newOrderBook = new OrderBook(order.getSymbol());
            newOrderBook.put(order);
            orderBookMap.put(order.getSymbol(), newOrderBook);
        }


    }

    /**
     * Executes the first best (partial) match in this order book.
     * Trader of the matching buy/sell orders are notified accordingly.
     */
    public void executeLimitOrderMatch(String symbol) {
        OrderBook orderBook = orderBookMap.get(symbol);
        if (!orderBook.matchAvailableOrder()) {
            return;
        }
        BuyOrder buy = orderBook.getBuyQueue().peek();
        SellOrder sell = orderBook.getSellQueue().peek();

        // calculate quantity and price for matching order
        int quantity = Math.min(buy.getQuantity(), sell.getQuantity());
        double price = (buy.getPrice() + sell.getPrice()) / 2.0;

        // update order book and notify order owners
        orderBook.handleOrderBookUpdate(sell, quantity, price);
        orderBook.handleOrderBookUpdate(buy, quantity, price);
    }


    public Map<String, OrderBook> getOrderBookMap() {
        return orderBookMap;
    }
}
