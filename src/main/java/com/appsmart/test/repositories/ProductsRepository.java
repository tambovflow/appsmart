package com.appsmart.test.repositories;


import com.appsmart.test.domain.CustomerEntity;
import com.appsmart.test.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByIdAndIsDeleted(UUID productId, Boolean isDeleted);

    Page<ProductEntity> findByCustomerAndIsDeleted(CustomerEntity customer, Boolean isDeleted, Pageable pageable);

    //default methods
    default ProductEntity getProductById(UUID productId) {

        Optional<ProductEntity> productEntityOptional = findByIdAndIsDeleted(productId, false);

        if (!productEntityOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return productEntityOptional.get();
    }

    default Page<ProductEntity> getProductsByCustomer(CustomerEntity customer, Pageable pageable) {
        return findByCustomerAndIsDeleted(customer, false, pageable);
    }
}
