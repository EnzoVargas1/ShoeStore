package com.ShoeStore.ShoeStore.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.ShoeStore.exceptions.ProductNotFoundException;

@MappedSuperclass
public abstract class Container {
	
	private int quantity;
	private double amount;
	
	public Container() {}
	
	public Container(int quantity, int amount){
		this.quantity = quantity;
		this.amount = amount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	protected Product process(Product item, boolean inStock, List<Product> items) throws ProductNotFoundException {
		item.setInStock(inStock);
		
		try {
			if(inStock) items.remove(item); 
			else items.add(item);
			
		}catch(Exception e) {
			throw new ProductNotFoundException("Not found");
		}
		
		setQuantity(getTotalOfProducts(items));
		setAmount(getTotalCostOfProducts(items));
		return item;
	}
	
	public double getTotalCostOfProducts(List<Product> items){
		int total = 0;
		for(int i = 0; i < items.size(); i++) {
			total+=items.get(i).getPrice();
		}
		return total;
	}
	
	public int getTotalOfProducts(List<Product> items) {
		return items.size();
	}
}
