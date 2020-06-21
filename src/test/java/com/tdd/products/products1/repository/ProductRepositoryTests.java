package com.tdd.products.products1.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.products.products1.model.Product;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private static File DATA_JSON = Paths.get("src", "test", "resources", "products.json").toFile();

    @BeforeEach
    public void setup() throws IOException {
        // Deserialize products from JSON file to Product array
        Product[] products = new ObjectMapper().readValue(DATA_JSON, Product[].class);

        // Save each product to database
        Arrays.stream(products).forEach(productRepository::save);
    }

    @AfterEach
    public void cleanup(){
        // Cleanup the database after each test
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Test produc with Id retrieved successfully")
    public void testProductWithIdRetreivedSucessfully(){
        //Giveb

        //When
        Product retrievedProduct = productRepository.findProductById(1);

        //Then
        Assertions.assertNotNull(retrievedProduct,"Product with Id 1 should exist");
        Assertions.assertEquals("First Product", retrievedProduct.getName());

    }

    @Test
    @DisplayName("Test product not found with non-existing id")
    public void testProductNotFoundForNonExistingId(){
        // Given two products in the database

        // When
        Product retrievedProduct = productRepository.findProductById(100);

        // Then
        Assertions.assertNull(retrievedProduct, "Product with id 100 should not exist");
    }

    @Test
    @DisplayName("Test product saved successfully")
    public void testProductSavedSuccessfully(){
        // Prepare mock product
        Product newProduct = new Product("New Product", "New Product Description", 8);

        // When
        Product savedProduct = productRepository.save(newProduct);

        // Then
        Assertions.assertNotNull(savedProduct, "Product should be saved");
        Assertions.assertNotNull(savedProduct.getId(), "Product should have an id when saved");
        Assertions.assertEquals(newProduct.getName(), savedProduct.getName());
    }

    @Test
    @DisplayName("Test product updated successfully")
    public void testProductUpdatedSuccessfully(){
        // Prepare the product
        Product productToUpdate = new Product(1, "Updated Product", "New Product Description", 20, 2);

        // When
        Product updatedProduct = productRepository.save(productToUpdate);

        // Then
        Assertions.assertEquals(productToUpdate.getName(), updatedProduct.getName());
        Assertions.assertEquals(java.util.Optional.of(2), updatedProduct.getVersion());
        Assertions.assertEquals(java.util.Optional.of(20), updatedProduct.getQuantity());
    }

    @Test
    @DisplayName("Test product deleted successfully")
    public void testProductDeletedSuccessfully(){
        // Given two products in the database


        // When
        productRepository.deleteById(1);

        // Then
        Assertions.assertEquals(1L, productRepository.count());
    }
}
