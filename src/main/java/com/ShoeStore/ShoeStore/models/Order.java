package com.ShoeStore.ShoeStore.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Ordered")
public class Order extends Container {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long orderNumber;
	
	@Column(name = "ordered_date")
	private LocalDateTime orderedDate;
	
	@Column(name = "is_delivered")
	private boolean delivered;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "order")
	private PaymentForm paymentForm;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
	private List<Product> products = new ArrayList<Product>();
	
	public Order() {}
	
	public Order(Customer customer) {
		orderedDate = LocalDateTime.now();
		customer.addOrder(this);
		setCustomer(customer);
		setProducts(customer.getCart().getProducts());
		setAmount(customer.getCart().getAmount() + 5.99);
		setQuantity(customer.getCart().getQuantity());
		setDelivered(false);
		//customer.getCart().clearCart();
	}

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDateTime getDate() {
		return orderedDate;
	}

	public void setDate(LocalDateTime d) {
		this.orderedDate = d;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public Product addProduct(Product product) {
		products.add(product);
		return product;
	}
	
	public Product removeProduct(Product product) {
		products.remove(product);
		return product;
	}

	public PaymentForm getPaymentForm() {
		return paymentForm;
	}

	public void setPaymentForm(PaymentForm paymentForm) {
		this.paymentForm = paymentForm;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	
}
