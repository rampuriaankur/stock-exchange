package com.ank.engine;

import com.ank.model.BuyOrder;
import com.ank.model.Order;
import com.ank.model.SellOrder;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Order book implementation. 
 * Before any trades can be executed buy orders and sell orders need to be put into the order book first. 
 */
public class OrderBook {

	private String symbol;
	private PriorityBlockingQueue<BuyOrder> buyQueue;
	private PriorityBlockingQueue<SellOrder> sellQueue;

	public OrderBook(String symbol) {
		this.setSymbol(symbol);
		buyQueue = new PriorityBlockingQueue<BuyOrder>();
		sellQueue = new PriorityBlockingQueue<SellOrder>();
	}


	public PriorityBlockingQueue<BuyOrder> getBuyQueue() {
		return buyQueue;
	}

	public void setBuyQueue(PriorityBlockingQueue<BuyOrder> buyQueue) {
		this.buyQueue = buyQueue;
	}

	public PriorityBlockingQueue<SellOrder> getSellQueue() {
		return sellQueue;
	}

	public void setSellQueue(PriorityBlockingQueue<SellOrder> sellQueue) {
		this.sellQueue = sellQueue;
	}


	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public BuyOrder peekBuyOrder() {
		return buyQueue.peek();
	}
	
	public BuyOrder popBuyOrder() {
		return buyQueue.poll();
	}
	
	public SellOrder peekSellOrder() {
		return sellQueue.peek();
	}
	
	public SellOrder popSellOrder() {
		return sellQueue.poll();
	}
	
	/**
	 * Adds the specified order to this order book.
	 */
	public void put(Order order) {
		synchronized (this) {
			order.setTimestamp(new Date());
			if (order instanceof BuyOrder) {
				buyQueue.add((BuyOrder) order);
			} else {
				sellQueue.add((SellOrder) order);
			}
		}
	}
	
	/**
	 * Removes the specified order from the order book.
	 */
	public boolean remove(Order order) {
		if(!isPending(order)) {
			return false;
		}
		synchronized (this) {
			if (order instanceof BuyOrder) {
				buyQueue.remove(order);
			} else {
				sellQueue.remove(order);
			}
		}
		
		return true;
	}

	/**
	 * Checks if the specified order is still in the order book.
	 */
	public boolean isPending(Order order) {
		if(order instanceof BuyOrder) {
			return buyQueue.contains(order);
		}
		else {
			return sellQueue.contains(order);
		}
	}

	/**
	 * Returns true if this order book contains (at least) one buy order that (partially) matches with a sell order.
	 * If no match is found, false is returned.
	 */
	public boolean matchAvailableOrder() {
		if(buyQueue.size() * sellQueue.size() == 0) {
			return false;
		}

		BuyOrder buy = buyQueue.peek();
		SellOrder sell = sellQueue.peek();

		// condition for partial matches
		if(buy.getPrice() < sell.getPrice()) {
			return false;
		}
		return true;
	}



	
	/**
	 * Handles updating the order book for the specified order and match parameters.
	 * @param order the order that is to be (partially) executed
	 * @param quantity the execution quantity 
	 * @param price the execution price
	 */
	public void handleOrderBookUpdate(Order order, int quantity, double price) {
		boolean fullExecution = quantity == order.getQuantity();
		
		// the full order is executed, remove the order from the order book
		if(fullExecution) {
			remove(order);
		}
		// partial match, update the remaining quantitys
		else {
			order.setQuantity(order.getQuantity() - quantity);
		}

		// owner notification
		String message = String.format("%s full: %s", order, fullExecution);
		orderExecuted(order, quantity, price, message);

	}

	public void orderExecuted(Order order, int quantity, double price,  String message) {
		System.out.println(message + " " + quantity + " " + price);
	}

}
