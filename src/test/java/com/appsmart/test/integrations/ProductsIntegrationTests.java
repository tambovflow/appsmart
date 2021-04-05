package com.appsmart.test.integrations;

import com.appsmart.test.dto.CustomerDto;
import com.appsmart.test.dto.ProductDto;
import com.appsmart.test.models.requests.CustomerRequest;
import com.appsmart.test.models.requests.ProductRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductsIntegrationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static UUID customerId;
    private static ProductDto product;

    @Test
    public void a_getCustomerForCustomerList() throws Exception {

        MvcResult result = mvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
        });
        List<CustomerDto> customerDtos = objectMapper.convertValue(responseMap.get("content"), new TypeReference<List<CustomerDto>>() {
        });

        if (customerDtos.isEmpty()) {

            CustomerRequest customerRequest = new CustomerRequest
                    .Builder()
                    .title("Any Title")
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(customerRequest);

            result = mvc.perform(post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isCreated())
                    .andReturn();

            CustomerDto customerDto = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);
            customerId = customerDto.getId();

        } else {
            customerId = customerDtos.stream().findAny().get().getId();
        }
    }

    @Test
    public void b_createProjectsFromCustomer() throws Exception {
        ProductRequest productRequest = new ProductRequest
                .Builder(10)
                .description("Any description")
                .title("Any title").build();
        String jsonRequest = objectMapper.writeValueAsString(productRequest);
        MvcResult result = null;
        for (int i = 0; i < 5; i++) {
            result = mvc.perform(post("/customers/{customerId}/products", customerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();
        }

        ProductDto productDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDto.class);

        assertThat(productDto.getId()).isNotNull();
        assertThat(productDto.getPrice()).isEqualTo(productRequest.getPrice());
        assertThat(productDto.getDescription()).isEqualTo(productRequest.getDescription());
        assertThat(productDto.getTitle()).isEqualTo(productRequest.getTitle());

        product = productDto;
    }

    @Test
    public void c_getAllCustomerProducts() throws Exception {
        MvcResult result = mvc.perform(get("/customers/{customerId}/products?page=0&size=100000", customerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
        });
        List<ProductDto> productDtos = objectMapper.convertValue(responseMap.get("content"), new TypeReference<List<ProductDto>>() {
        });

        assertThat(productDtos).isNotEmpty();
        assertThat(productDtos.stream().anyMatch(x -> x.getId().equals(product.getId()))).isTrue();
    }

    @Test
    public void d_updateProductById() throws Exception {

        ProductRequest productRequest = new ProductRequest
                .Builder(20.11111)
                .title("New Title")
                .description("New Description").build();

        String jsonRequest = objectMapper.writeValueAsString(productRequest);
        MvcResult result = mvc.perform(put("/products/{productId}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ProductDto productDtoAfter = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDto.class);

        assertThat(product.getId()).isEqualTo(productDtoAfter.getId());
        assertThat(product.getCreatedAt()).isEqualTo(productDtoAfter.getCreatedAt());
        assertThat(product.getTitle()).isNotEqualTo(productDtoAfter.getTitle());
        assertThat(product.getDescription()).isNotEqualTo(productDtoAfter.getDescription());
        assertThat(productDtoAfter.getModifiedAt()).isNotNull();
    }

    @Test
    public void zz_deleteProduct() throws Exception {
        mvc.perform(delete("/products/{productId}", product.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());


        mvc.perform(get("/products/{productId}", product.getId()))
                .andExpect(status().isNotFound());

        MvcResult result = mvc.perform(get("/customers/{customerId}/products?page=0&size=100000", customerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
        });
        List<ProductDto> productDtos = objectMapper.convertValue(responseMap.get("content"), new TypeReference<List<ProductDto>>() {
        });

        assertThat(productDtos).isNotEmpty();
        assertThat(productDtos.stream().anyMatch(x -> x.getId().equals(product.getId()))).isFalse();
    }
}
