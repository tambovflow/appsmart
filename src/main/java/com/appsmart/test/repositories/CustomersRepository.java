package com.appsmart.test.repositories;

import com.appsmart.test.domain.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

public interface CustomersRepository extends JpaRepository<CustomerEntity, UUID> {

    Page<CustomerEntity> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);

    Optional<CustomerEntity> findByIdAndIsDeleted(UUID customerId, Boolean isDeleted);

    // default methods
    default Page<CustomerEntity> getCustomersPage(Pageable pageable) {
        return findAllByIsDeleted(false, pageable);
    }

    default CustomerEntity getCustomerEntityById(UUID customerId) {
        Optional<CustomerEntity> customerEntityOptional = findByIdAndIsDeleted(customerId, false);

        if (!customerEntityOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return customerEntityOptional.get();
    }
}
