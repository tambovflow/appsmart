package com.appsmart.test.controllers;

import com.appsmart.test.dto.ProductDto;
import com.appsmart.test.models.requests.ProductRequest;
import com.appsmart.test.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    /**
     * Get Product by id
     *
     * @param productId - the id of product
     * @return product object with 200 code response if no errors occur
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getProductById(@Valid @PathVariable(name = "productId") UUID productId) {

        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    /**
     * Get Products by Customer
     *
     * @param customerId - the id of customer
     * @param page       - the number of page (not required param, default value 0)
     * @param size       - size of products in one page (not required, default value 10)
     * @return products page object and 200 code response if no errors occur
     */
    @GetMapping("/customers/{customerId}/products")
    public ResponseEntity<Page<ProductDto>> getProductsByCustomer(@Valid @PathVariable(name = "customerId") UUID customerId,
                                                                  @Valid @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                  @Valid @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        Page<ProductDto> productDtos = productService.getProductsByCustomer(customerId, page, size);
        return ResponseEntity.ok(productDtos);
    }

    /**
     * Create Product for Customer
     *
     * @param customerId     - the id of customer
     * @param productRequest - creating product request
     * @return created product object with 201 code response if no errors occur
     */

    @PostMapping("/customers/{customerId}/products")
    public ResponseEntity<ProductDto> createProductForCustomer(@Valid @PathVariable(name = "customerId") UUID customerId,
                                                               @Valid @RequestBody ProductRequest productRequest) {

        ProductDto productDto = productService.createProductForCustomer(customerId, productRequest);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    /**
     * Update Product by id
     *
     * @param productId      - the product id
     * @param productRequest - product update request
     * @return updated product with 200 code response if no errors occur
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateProductById(@Valid @PathVariable(name = "productId") UUID productId,
                                                        @Valid @RequestBody ProductRequest productRequest) {
        ProductDto productDto = productService.updateProductById(productId, productRequest);
        return ResponseEntity.ok(productDto);
    }

    /**
     * Delete Product by id
     *
     * @param productId - the product id
     * @return 204 code response (no content)
     */

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductDto> deleteProductById(@Valid @PathVariable(name = "productId") UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

}
