package com.ShoeStore.ShoeStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ShoeStore.ShoeStore.models.Cart;
import com.ShoeStore.ShoeStore.models.SKUGenerator;
import com.ShoeStore.ShoeStore.models.Shoe;
import com.ShoeStore.ShoeStore.repository.ShoeRepository;
import com.ShoeStore.exceptions.ProductNotFoundException;

@SpringBootTest
class ShoeStoreApplicationTests {
	
	@Autowired
	ShoeRepository shoeRepository;

	@Test
	void contextLoads() {
	}
	
	
	@Test
	public void processItems() throws ProductNotFoundException {
		String color = "black";
		Shoe shoe = new Shoe("shoe", 9, color, color, color, color, "Nike", 90.00, "");
		shoe.setPrice(90.0);
		Cart cart = new Cart();
		
		assertEquals(cart.addItem(shoe), shoe);
		assertEquals(shoe.isInStock(), false);
		assertEquals(cart.getAmount(), 90);
		assertEquals(cart.getQuantity(), 1);
		
	}


}
