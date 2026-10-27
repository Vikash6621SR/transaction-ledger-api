package com.example.ledger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionLedgerApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Verify that the Spring application starts successfully.
    }

    @Test
    void createAccountShouldReturnCreatedAndZeroBalance()
            throws Exception {

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Automated Test Account"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Automated Test Account"))
                .andExpect(jsonPath("$.balance")
                        .value(0));
    }

    @Test
    void missingAccountShouldReturnNotFound()
            throws Exception {

        mockMvc.perform(
                        get("/api/accounts/999999/balance"))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidPaginationShouldReturnBadRequest()
            throws Exception {

        // Create an account using the API first.
        String response = mockMvc.perform(
                        post("/api/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Pagination Test Account"
                                        }
                                        """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the generated account ID from the response.
        long accountId = com.fasterxml.jackson.databind.json.JsonMapper
                .builder()
                .build()
                .readTree(response)
                .get("id")
                .asLong();

        // Page numbers must start at 1.
        mockMvc.perform(
                        get("/api/accounts/{accountId}/transactions",
                                accountId)
                                .param("page", "0")
                                .param("limit", "10"))
                .andExpect(status().isBadRequest());
    }
}
