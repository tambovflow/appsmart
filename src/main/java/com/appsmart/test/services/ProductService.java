package com.appsmart.test.services;

import com.appsmart.test.domain.CustomerEntity;
import com.appsmart.test.domain.ProductEntity;
import com.appsmart.test.dto.ProductDto;
import com.appsmart.test.mapper.ProductsMapper;
import com.appsmart.test.models.requests.ProductRequest;
import com.appsmart.test.repositories.CustomersRepository;
import com.appsmart.test.repositories.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This is service for products CRUD
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final CustomersRepository customersRepository;
    private final ProductsRepository productsRepository;
    private final ProductsMapper productsMapper;

    public ProductDto getProductById(UUID productId) {

        log.info("get product by id");
        ProductEntity productEntity = productsRepository.getProductById(productId);
        return productsMapper.productEntityToDto(productEntity);
    }

    public Page<ProductDto> getProductsByCustomer(UUID customerId, Integer page, Integer size) {

        log.info("get products page by customer");
        CustomerEntity customerEntity = customersRepository.getCustomerEntityById(customerId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProductEntity> productEntities = productsRepository.getProductsByCustomer(customerEntity, pageable);

        return productEntities.map(productsMapper::productEntityToDto);
    }

    public ProductDto createProductForCustomer(UUID customerId, ProductRequest productRequest) {

        log.info("create product for customer");
        CustomerEntity customerEntity = customersRepository.getCustomerEntityById(customerId);

        ProductEntity productEntity = productsMapper.productRequestToEntity(productRequest);
        productEntity.setCustomer(customerEntity);

        productsRepository.save(productEntity);

        return productsMapper.productEntityToDto(productEntity);
    }

    @Transactional
    public ProductDto updateProductById(UUID productId, ProductRequest productRequest) {
        log.info("update product by id");

        ProductEntity productEntity = productsRepository.getProductById(productId);
        productsMapper.updateProductFromRequest(productRequest, productEntity);
        productEntity.setModifiedAt(LocalDateTime.now());

        return productsMapper.productEntityToDto(productEntity);
    }

    public void deleteProductById(UUID productId) {

        log.info("delete product by id");
        ProductEntity productEntity = productsRepository.getProductById(productId);
        productEntity.setIsDeleted(true);
        productEntity.setModifiedAt(LocalDateTime.now());
        productsRepository.save(productEntity);
    }
}
