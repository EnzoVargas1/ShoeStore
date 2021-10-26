package com.ShoeStore.ShoeStore.security.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ShoeStore.ShoeStore.models.Cart;
import com.ShoeStore.ShoeStore.models.Customer;
import com.ShoeStore.ShoeStore.models.Order;
import com.ShoeStore.ShoeStore.models.PaymentForm;
import com.ShoeStore.ShoeStore.models.Shoe;
import com.ShoeStore.ShoeStore.security.util.JwtUtil;
import com.ShoeStore.ShoeStore.services.CartService;
import com.ShoeStore.ShoeStore.services.OrderService;
import com.ShoeStore.exceptions.CartIsEmptyException;
import com.ShoeStore.exceptions.NoResourceFoundException;
import com.ShoeStore.exceptions.ProductNotFoundException;
import com.ShoeStore.ShoeStore.security.models.User;
import com.ShoeStore.ShoeStore.security.services.UserService;


@CrossOrigin(origins = "*")
@RestController
public class UserController {
	
	@Autowired
    JwtUtil jwtTokenUtil;
	
	@Autowired
     UserService userService;
	
	@Autowired
    CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/Me")
	@Secured("ROLE_USER")
	public Customer getAccountHolderById() {
		String username = jwtTokenUtil.getCurrentUserName();
		System.out.println("username: "+username);
		User user = userService.getUserByUserName(username);
		return user.getCustomer();
	}
	
	@GetMapping("/Me/cart")
	@Secured("ROLE_USER")
	public Cart getCart() {
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return user.getCustomer().getCart();
	}
	
	
	@PostMapping("/Me/cart/addshoe")
    @Secured("ROLE_USER")
	@ResponseStatus(HttpStatus.OK)
	public Cart addToCart(@RequestBody Shoe shoe) throws NoResourceFoundException, ProductNotFoundException {
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		//Customer customer = user.getCustomer();
		
		return cartService.addShoe(shoe, user.getCustomer().getId());
		
	}
	
	@PutMapping("/Me/cart/removeshoe")
	@Secured("ROLE_USER")
	@ResponseStatus(HttpStatus.OK)
	public Cart removeShoe(@RequestBody@Valid Shoe shoe) throws NoResourceFoundException, 
	ProductNotFoundException {
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return cartService.removeShoe(shoe, user.getCustomer().getId());
	}
	
	@PutMapping("/Me/cart/clear")
	@Secured("ROLE_USER")
	@ResponseStatus(HttpStatus.OK)
	public Cart removeAllProducts() throws NoResourceFoundException, 
	CartIsEmptyException, ProductNotFoundException{

		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return cartService.clearCart(user.getCustomer().getId());
	}
	
	@Secured("ROLE_USER")
	@GetMapping("/Me/orders")
	public List<Order> getOrders() throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return orderService.getCustomerOrders(user.getCustomer().getId());
	}
	
	@PostMapping("/Me/orders/add")
	@Secured("ROLE_USER")
	@ResponseStatus(HttpStatus.CREATED)
	public List<Order> addOrder(@RequestBody@Valid PaymentForm payment) throws NoResourceFoundException, CartIsEmptyException {
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return orderService.addOrder(user.getCustomer().getId(), payment);
	}
	
	
	@Secured("ROLE_USER")
	@DeleteMapping("/Me/orders/{orderId}/remove")
	@ResponseStatus(HttpStatus.CREATED)
	public List<Order> removeOrder(@PathVariable long orderId) throws NoResourceFoundException, CartIsEmptyException {
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return orderService.deleteOrder(orderId);
	}
	
	
}
