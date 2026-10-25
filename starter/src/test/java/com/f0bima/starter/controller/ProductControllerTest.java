package com.f0bima.starter.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.f0bima.starter.dto.ProductRequest;
import com.f0bima.starter.dto.ProductResponse;
import com.f0bima.starter.exception.ResourceNotFoundException;
import com.f0bima.starter.service.ProductService;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("POST /api/products - Harus mengembalikan HTTP 201 Created")
    void createProduct_ShouldReturn201() throws Exception {
        ProductRequest request = new ProductRequest("Smart TV", new BigDecimal("4500000.00"));
        ProductResponse response = new ProductResponse(1L, "Smart TV", new BigDecimal("4500000.00"), LocalDateTime.now());

        when(productService.create(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Smart TV"))
                .andExpect(jsonPath("$.price").value(4500000.00));

        verify(productService, times(1)).create(any(ProductRequest.class));
    }

    @Test
    @DisplayName("GET /api/products - Harus mengembalikan HTTP 200 OK beserta list produk")
    void findAllProducts_ShouldReturn200() throws Exception {
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "Mouse", new BigDecimal("100000.00"), LocalDateTime.now()),
                new ProductResponse(2L, "Keyboard", new BigDecimal("300000.00"), LocalDateTime.now())
        );

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Mouse"))
                .andExpect(jsonPath("$[1].name").value("Keyboard"));

        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/products/{id} - Harus mengembalikan HTTP 200 OK jika produk ditemukan")
    void findById_WhenExists_ShouldReturn200() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Headset", new BigDecimal("500000.00"), LocalDateTime.now());

        when(productService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Headset"));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/products/{id} - Harus mengembalikan HTTP 404 jika produk tidak ditemukan")
    void findById_WhenNotFound_ShouldReturn404() throws Exception {
        when(productService.findById(99L)).thenThrow(new ResourceNotFoundException("Product not found with id: 99"));

        mockMvc.perform(get("/api/products/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Harus mengembalikan HTTP 200 OK jika berhasil update")
    void updateProduct_ShouldReturn200() throws Exception {
        ProductRequest request = new ProductRequest("Speaker Bluetooth", new BigDecimal("750000.00"));
        ProductResponse response = new ProductResponse(1L, "Speaker Bluetooth", new BigDecimal("750000.00"), LocalDateTime.now());

        when(productService.update(eq(1L), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Speaker Bluetooth"))
                .andExpect(jsonPath("$.price").value(750000.00));

        verify(productService, times(1)).update(eq(1L), any(ProductRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Harus mengembalikan HTTP 204 No Content")
    void deleteProduct_ShouldReturn204() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(1L);
    }
}
