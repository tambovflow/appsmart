package com.appsmart.test.integrations;

import com.appsmart.test.dto.CustomerDto;
import com.appsmart.test.models.requests.CustomerRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CustomersIntegrationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static UUID customerId;

    @Test
    public void a_createCustomer() throws Exception {
        CustomerRequest request = new CustomerRequest.Builder().title("Test Title").build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CustomerDto customerDto = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);
        assertThat(request.getTitle()).isEqualTo(customerDto.getTitle());

        customerId = customerDto.getId();
    }

    @Test
    public void b_getAllCustomers() throws Exception {
        MvcResult result = mvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
        });
        List<CustomerDto> customerDtos = objectMapper.convertValue(responseMap.get("content"), new TypeReference<List<CustomerDto>>() {
        });

        assertThat(customerDtos).isNotEmpty();

        log.info(customerDtos.toString());
    }

    @Test
    public void c_getCustomerById() throws Exception {
        MvcResult result = mvc.perform(get("/customers/{customerId}", customerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CustomerDto customerDto = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);

        assertThat(customerDto.getId()).isEqualTo(customerId);
        assertThat(customerDto.getIsDeleted()).isFalse();
    }

    @Test
    public void ca_getCustomerByIdAndReturn404() throws Exception {
        UUID randomId = UUID.randomUUID();
        mvc.perform(get("/customers/{customerId}", randomId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void cb_getCustomerByWrongIdAndReturn400() throws Exception {
        mvc.perform(get("/customers/{customerId}", "wrong_id"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void d_updateCustomer() throws Exception {

        MvcResult result = mvc.perform(get("/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andReturn();

        CustomerDto customerBefore = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);

        assertThat(customerBefore.getId()).isEqualTo(customerId);
        assertThat(customerBefore.getIsDeleted()).isFalse();

        CustomerRequest customerRequest = new CustomerRequest.Builder().title("New Title").build();
        String jsonRequest = objectMapper.writeValueAsString(customerRequest);

        result = mvc.perform(put("/customers/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        CustomerDto customerAfter = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerDto.class);

        assertThat(customerAfter.getId()).isEqualTo(customerBefore.getId());
        assertThat(customerAfter.getTitle()).isNotEqualTo(customerBefore.getTitle());
        assertThat(customerAfter.getModifiedAt()).isNotNull();
        assertThat(customerBefore.getModifiedAt()).isNull();
    }
}
