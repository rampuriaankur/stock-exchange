package com.ank.model;

import java.util.Date;
import java.util.UUID;

public abstract class Order implements Comparable<Order> {

    private UUID id;
    private Trader trader;
    protected String symbol;
    protected Date timestamp;
    protected int quantity;
    protected double price;
    protected OrderType type;

    public Order(String symbol, int quantity, double price) {
        id = UUID.randomUUID();
        this.symbol = symbol;
        this.timestamp = new Date();
        setQuantity(quantity);
        setPrice(price);
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderType getType() {
        return type;
    }

    public void setPrice(double price){
        if(price <= 0) {
            throw new IllegalArgumentException("price can't be negative!!");
        }
        this.price = price;
    }

    public  void setQuantity(int quantity){
        if(quantity <= 0) {
            throw new IllegalArgumentException("Order quantity can't be negative!!");
        }
        this.quantity = quantity;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    @Override
    public String toString() {
        String ownerName = trader == null ? "<unk>" : trader.getId();
        String orderType = type == OrderType.BUY ? orderType = "BUY" : "SELL";

        return String.format("%s %s %s %d %s %s", ownerName, symbol, orderType, quantity, price, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Order) {
            UUID thatOrder = ((Order) obj).getId();
            return id.equals(thatOrder);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
