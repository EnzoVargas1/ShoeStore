package com.ShoeStore.ShoeStore.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ShoeStore.ShoeStore.models.Cart;
import com.ShoeStore.ShoeStore.models.Customer;
import com.ShoeStore.ShoeStore.models.Product;
import com.ShoeStore.ShoeStore.models.Shoe;
import com.ShoeStore.ShoeStore.repository.CartRepository;
import com.ShoeStore.ShoeStore.repository.CustomerRepository;
import com.ShoeStore.ShoeStore.repository.ShoeRepository;
import com.ShoeStore.exceptions.CartIsEmptyException;
import com.ShoeStore.exceptions.NoResourceFoundException;
import com.ShoeStore.exceptions.ProductNotFoundException;

@Service
public class CartServiceImpl implements CartService {
   
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ShoeRepository shoeRepository;
	

	
	@Override
	public Cart clearCart(int id) throws NoResourceFoundException, CartIsEmptyException, ProductNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepository.findById(id).orElse(null);
		
		if(customer == null) {
			throw new NoResourceFoundException("No Resource Found Exception");
		}
		
		Shoe shoe = null;
		
		if(customer.getCart().getProducts().size() == 0) {
			throw new CartIsEmptyException("Cart Is Already Empty");
		}
		for(int i = 0; i < customer.getCart().getProducts().size(); i++) {
			 if(customer.getCart().getProducts().get(i).getCategory().equals("shoe")) {
				 shoe = (Shoe)customer.getCart().getProducts().get(i);
				 shoe.setCart(null);
				 shoe.setInStock(true);
				 shoeRepository.save(shoe);
			 }
		}
		cartRepository.delete(customer.getCart());
		Cart newCart = new Cart();
		customer.setCart(newCart);
		newCart.setCustomer(customer);
		//customerRepository.save(customer);
		cartRepository.save(customer.getCart());

	    return customer.getCart();
	}

	@Override
	public Cart addShoe(Shoe shoe, int id) throws NoResourceFoundException, ProductNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepository.findById(id).orElse(null);
		if(customer == null) {
			throw new NoResourceFoundException("No Resource Found Exception");
		}
		shoe = shoeRepository.findById(shoe.getId()).orElse(null);
		if(!shoe.isInStock()) {
			throw new ProductNotFoundException("This item is out of stock");
		}
		
		if(!shoe.isInStock()) {
			throw new ProductNotFoundException("This item is out of stock");
		}
		customer.getCart().addItem(shoe);
		shoe.setCart(customer.getCart());
		shoeRepository.save(shoe);
		return cartRepository.save(customer.getCart());
		
	}
	
	@Override
	public Cart removeShoe(Shoe shoe, int id) throws NoResourceFoundException, ProductNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepository.findById(id).orElse(null);
		if(customer == null) {
			throw new NoResourceFoundException("No Resource Found Exception");
		}
		shoe = shoeRepository.findById(shoe.getId()).orElse(null);
		
		if(shoe == null) {
			throw new ProductNotFoundException("Product not found");
		}
		
		customer.getCart().removeItem(shoe);
		
		shoe.setCart(null);
		shoeRepository.save(shoe);
		return cartRepository.save(customer.getCart());
	}

}
