package com.f0bima.starter.repository;

import com.f0bima.starter.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("save() & findById() - Harus berhasil menyimpan dan mengambil entitas Product dari database H2")
    void saveAndFindById_ShouldWork() {
        Product product = new Product("Webcam HD", new BigDecimal("450000.00"));
        Product saved = productRepository.save(product);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());

        Optional<Product> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Webcam HD", found.get().getName());
        assertEquals(new BigDecimal("450000.00"), found.get().getPrice());
    }

    @Test
    @DisplayName("deleteById() - Harus berhasil menghapus produk dari database")
    void deleteById_ShouldRemoveProduct() {
        Product product = new Product("USB Hub", new BigDecimal("120000.00"));
        Product saved = productRepository.save(product);

        productRepository.deleteById(saved.getId());

        Optional<Product> found = productRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }
}
