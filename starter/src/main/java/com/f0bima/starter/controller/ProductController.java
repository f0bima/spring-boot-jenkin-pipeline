package com.f0bima.starter.controller;

import com.f0bima.starter.dto.ProductRequest;
import com.f0bima.starter.dto.ProductResponse;
import com.f0bima.starter.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Endpoints CRUD manajemen produk")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Tambah produk baru", description = "Menyimpan entitas produk baru ke dalam database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produk berhasil dibuat"),
            @ApiResponse(responseCode = "400", description = "Payload request tidak valid")
    })
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Daftar semua produk", description = "Mengambil semua daftar produk yang tersedia")
    @ApiResponse(responseCode = "200", description = "Daftar produk berhasil diambil")
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detail produk berdasarkan ID", description = "Mengambil data detail produk berdasarkan ID produk")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produk ditemukan"),
            @ApiResponse(responseCode = "404", description = "Produk dengan ID tersebut tidak ditemukan")
    })
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui produk", description = "Memperbarui nama dan harga produk berdasarkan ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produk berhasil diperbarui"),
            @ApiResponse(responseCode = "404", description = "Produk tidak ditemukan")
    })
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus produk", description = "Menghapus produk dari database berdasarkan ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produk berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Produk tidak ditemukan")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
