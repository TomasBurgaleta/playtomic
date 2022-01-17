package com.playtomic.tests.wallet.api;

import com.github.javafaker.Faker;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.service.StripeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.math.BigDecimal;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class StripeServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StripeService stripeService;

    private Faker faker = new Faker();

    @Test
    public void eso_test() throws Exception {

        String creditCard = faker.random().hex(15);
        BigDecimal amount = new BigDecimal(faker.random().nextInt(6,20));

        ChargeRequest body = new ChargeRequest(creditCard, amount);

        stripeService.charge(body);

//        this.mockMvc
//                .perform(MockMvcRequestBuilders.get("/api/departments/{department_id}", 1)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.departmentId").value(1));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/v1/stripe-simulator/charges", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());




    }

}
