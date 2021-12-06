package com.ShoeStore.ShoeStore.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ShoeStore.ShoeStore.security.models.User;
import com.ShoeStore.exceptions.OrderDoesNotExistException;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String number;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "customer")
	private Cart cart;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	private List<Order> orders = new ArrayList<Order>();
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "customer")
	private ShippingInfo shippingInfo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	public Customer() {}
	
	public Customer(String firstName, String lastName, String email, String number) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.number = number;
		//this.shippingInfo = shippingInfo;
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public ShippingInfo getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}
	
	public Order addOrder(Order order){
		orders.add(order);
		return order;
	}
	
	public Order removeOrder(Order order) throws OrderDoesNotExistException {
		try {
			orders.remove(order);
		} catch(Exception e) {
			throw new OrderDoesNotExistException("This Order does Not Exist");
		}
		return order;
	}
	
	public Order findOrder(Order order) {
		boolean orderExists = false;
		
		for(int i = 0; i < orders.size(); i++) {
			if(orders.get(i).getOrderNumber() == order.getOrderNumber())
				orderExists = true;
		}
		if(orderExists)return order;
		else return null;
	}
}
