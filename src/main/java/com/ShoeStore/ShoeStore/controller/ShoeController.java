package com.ShoeStore.ShoeStore.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ShoeStore.ShoeStore.models.SKUGenerator;
import com.ShoeStore.ShoeStore.models.Shoe;
import com.ShoeStore.ShoeStore.services.ShoeService;
import com.ShoeStore.exceptions.ProductNotFoundException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = "*")
public class ShoeController {
	Logger logs = LoggerFactory.getLogger(ShoeController.class);
	
	@Autowired
	ShoeService shoeService;
	
	@GetMapping(value ="/shoes")
	public List<Shoe> getShoes() {
		return shoeService.getShoes();
	}
	
	@GetMapping(value ="/differentshoes")
	public List<Shoe>getNonDuplicateShoes(){
		return shoeService.getNonDuplicateShoes();
	}
	
	@PostMapping("/shoe")
	@ResponseStatus(HttpStatus.CREATED)
	public List<Shoe> addShoe(@RequestBody@Valid Shoe shoe) {
		
		return shoeService.addShoe(shoe);
	}
	
	@GetMapping("/shoe/{id}")
	public Shoe getShoeById(@PathVariable int id) throws ProductNotFoundException {
		return shoeService.getShoeById(id);
	} 
	
	@DeleteMapping("/shoe/{id}")
	public Shoe removeShoe(@PathVariable int id) throws ProductNotFoundException {
		return shoeService.removeShoeById(id);
	}
	
	@GetMapping("/shoes/")
	public List<Shoe> getShoesBySku(@RequestParam String sku) {
		return shoeService.getShoesBySKU(sku);
	}
	
	@GetMapping("/shoe/")
	public Shoe getShoeBySku(@RequestParam String sku) throws ProductNotFoundException {
		return shoeService.getShoeBySKU(sku);
	}
	
}
