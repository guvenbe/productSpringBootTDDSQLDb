package com.tdd.products.products1.repository;

import com.tdd.products.products1.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findProductById(Integer id);
    Product findProductByIdAndName(Integer id, String name);
}
