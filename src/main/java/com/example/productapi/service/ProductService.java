package com.example.productapi.service;

import com.example.productapi.dto.ProductDto;
import com.example.productapi.exception.InvalidQuantityException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.model.Category;
import com.example.productapi.model.Product;
import com.example.productapi.repository.CategoryRepository;
import com.example.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    static final int MAX_QUANTITY = 1000;

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepo.findAll(pageable).map(ProductService::toDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        return toDto(findProductOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchByName(String name) {
        return productRepo.findByNameContainingIgnoreCase(name).stream().map(ProductService::toDto).toList();
    }

    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        checkQuantity(dto.quantity());
        Product p = new Product();
        applyDto(p, dto);
        return toDto(productRepo.save(p));
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product p = findProductOrThrow(id);
        checkQuantity(dto.quantity());
        applyDto(p, dto);
        return toDto(productRepo.save(p));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepo.delete(findProductOrThrow(id));
    }

    private void applyDto(Product p, ProductDto dto) {
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setPrice(dto.price());
        p.setQuantity(dto.quantity() == null ? 0 : dto.quantity());
        p.setCategory(dto.categoryId() == null ? null : findCategoryOrThrow(dto.categoryId()));
    }

    private Product findProductOrThrow(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'id: " + id));
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable avec l'id: " + id));
    }

    private void checkQuantity(Integer quantity) {
        if (quantity != null && quantity > MAX_QUANTITY) {
            throw new InvalidQuantityException("La quantité ne peut pas dépasser " + MAX_QUANTITY + " (reçu : " + quantity + ")");
        }
    }

    static ProductDto toDto(Product p) {
        Long categoryId = p.getCategory() == null ? null : p.getCategory().getId();
        return new ProductDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity(), categoryId);
    }
}
