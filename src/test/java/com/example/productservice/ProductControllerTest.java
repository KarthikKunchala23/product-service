package com.example.productservice;

import com.example.productservice.controller.ProductController;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.service.ProductService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnProducts() throws Exception {

        ProductResponse product =
                new ProductResponse(
                        1L,
                        "MacBook Air",
                        "Apple laptop",
                        new BigDecimal("1199.99"),
                        10
                );

        when(productService.getAllProducts())
                .thenReturn(List.of(product));

        mockMvc.perform(
                        get("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("MacBook Air"));
    }
}