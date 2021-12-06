package com.ShoeStore.ShoeStore.services;

import java.util.List;

import com.ShoeStore.ShoeStore.models.Shoe;
import com.ShoeStore.exceptions.ProductNotFoundException;


public interface ShoeService {
	
	public List<Shoe> addShoe(Shoe shoe);
	public List<Shoe> getShoes();
	public Shoe getShoeById(int id) throws ProductNotFoundException;
	public Shoe removeShoeById(int id) throws ProductNotFoundException;
	public List<Shoe> getNonDuplicateShoes();
	public List<Shoe> getShoesBySKU(String sku);
	public Shoe getShoeBySKU(String sku)throws ProductNotFoundException;
	
}

