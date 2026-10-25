package com.f0bima.starter.service;

import com.f0bima.starter.dto.ProductRequest;
import com.f0bima.starter.dto.ProductResponse;
import com.f0bima.starter.entity.Product;
import com.f0bima.starter.exception.ResourceNotFoundException;
import com.f0bima.starter.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("create() - Harus berhasil menyimpan dan mengembalikan ProductResponse")
    void create_ShouldReturnProductResponse() {
        // Arrange
        ProductRequest request = new ProductRequest("Laptop Gaming", new BigDecimal("15000000.00"));
        Product savedProduct = new Product(1L, "Laptop Gaming", new BigDecimal("15000000.00"), LocalDateTime.now());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductResponse response = productService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Laptop Gaming", response.getName());
        assertEquals(new BigDecimal("15000000.00"), response.getPrice());
        assertNotNull(response.getCreatedAt());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("findAll() - Harus mengembalikan semua daftar produk")
    void findAll_ShouldReturnListOfProductResponses() {
        // Arrange
        List<Product> products = List.of(
                new Product(1L, "Mouse Wireless", new BigDecimal("250000.00"), LocalDateTime.now()),
                new Product(2L, "Keyboard Mechanical", new BigDecimal("850000.00"), LocalDateTime.now())
        );

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<ProductResponse> responses = productService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Mouse Wireless", responses.get(0).getName());
        assertEquals("Keyboard Mechanical", responses.get(1).getName());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById() - Harus mengembalikan produk jika ID ditemukan")
    void findById_WhenIdExists_ShouldReturnProductResponse() {
        // Arrange
        Product product = new Product(1L, "Monitor 24 Inch", new BigDecimal("2100000.00"), LocalDateTime.now());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ProductResponse response = productService.findById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Monitor 24 Inch", response.getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById() - Harus melempar ResourceNotFoundException jika ID tidak ditemukan")
    void findById_WhenIdNotFound_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("update() - Harus berhasil memperbarui data produk jika ID ditemukan")
    void update_WhenIdExists_ShouldReturnUpdatedProductResponse() {
        // Arrange
        Product existingProduct = new Product(1L, "Old Name", new BigDecimal("100000.00"), LocalDateTime.now());
        ProductRequest updateRequest = new ProductRequest("New Name", new BigDecimal("150000.00"));
        Product updatedProduct = new Product(1L, "New Name", new BigDecimal("150000.00"), existingProduct.getCreatedAt());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        ProductResponse response = productService.update(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("New Name", response.getName());
        assertEquals(new BigDecimal("150000.00"), response.getPrice());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("update() - Harus melempar ResourceNotFoundException jika ID tidak ditemukan")
    void update_WhenIdNotFound_ShouldThrowException() {
        // Arrange
        ProductRequest request = new ProductRequest("Test", new BigDecimal("50000.00"));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(99L, request)
        );

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("delete() - Harus berhasil menghapus produk jika ID ditemukan")
    void delete_WhenIdExists_ShouldDeleteProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> productService.delete(1L));

        // Assert
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete() - Harus melempar ResourceNotFoundException jika ID tidak ditemukan")
    void delete_WhenIdNotFound_ShouldThrowException() {
        // Arrange
        when(productRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.delete(99L)
        );

        verify(productRepository, times(1)).existsById(99L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
