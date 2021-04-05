package com.appsmart.test.controllers;

import com.appsmart.test.dto.CustomerDto;
import com.appsmart.test.models.requests.CustomerRequest;
import com.appsmart.test.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

    private final CustomerService customerService;

    /**
     * Get Customers method
     *
     * @param page - the number of page (not required param, default value 0)
     * @param size - size of customers in one page (not required, default value 10)
     * @return customers page object and code 200 if nor errors occur
     */

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getCustomers(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        Page<CustomerDto> customers = customerService.getCustomers(page, size);
        return ResponseEntity.ok(customers);
    }

    /**
     * Customer Create method
     *
     * @param request - the customer create request
     * @return created customer object with 201 response code if no errors occur
     */

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerRequest request) {

        CustomerDto customerDto = customerService.createCustomer(request);
        return new ResponseEntity<>(customerDto, HttpStatus.CREATED);
    }

    /**
     * Get Customer by id
     *
     * @param customerId - the id of customer
     * @return customer object with 200 response code if no errors occur
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@Valid @PathVariable(name = "customerId") UUID customerId) {

        CustomerDto customerDto = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customerDto);
    }


    /**
     * Update Customer by id
     *
     * @param customerId      - the id of customer
     * @param customerRequest - customer update request
     * @return customer object with 200 response code if no errors occur
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomerById(@Valid @PathVariable(name = "customerId") UUID customerId,
                                                          @Valid @RequestBody CustomerRequest customerRequest) {

        CustomerDto customerDto = customerService.updateCustomerById(customerId, customerRequest);
        return ResponseEntity.ok(customerDto);
    }


    /**
     * Delete Customer by id
     *
     * @param customerId - id of the customer
     * @return 204 response code (no content)
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<CustomerDto> deleteCustomerById(@Valid @PathVariable(name = "customerId") UUID customerId) {

        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }
}
