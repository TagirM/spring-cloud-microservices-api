package com.javastart.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.account.AccountApplication;
import com.javastart.account.controller.dto.AccountResponseDTO;
import com.javastart.account.entity.Account;
import com.javastart.account.repository.AccountRepository;
import com.javastart.account.service.AccountService;
import com.javastart.accountservice.config.SpringH2DatabaseConfig;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AccountApplication.class, SpringH2DatabaseConfig.class})
public class AccountControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static final String REQUEST = "{\n" +
            "    \"name\": \"Lori\",\n" +
            "    \"email\": \"lori@cat.xyz\", \n" +
            "    \"phone\": \"+79823876917\", \n" +
            "    \"bills\": [1]\n" +
            "}";

    @Test
    public void createAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/").content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        AccountResponseDTO accountResponseDTO = objectMapper.readValue(body, AccountResponseDTO.class);
//        List<Deposit> deposits = depositRepository.findDepositsByEmail("lori@cat.xyz");
        List<Account> accounts = accountRepository.findAccountsByName("Lori");

        Assertions.assertThat(accountResponseDTO.getName()).isEqualTo(accounts.get(0).getName());
//        Assertions.assertThat(accountResponseDTO.getAmount()).isEqualTo(BigDecimal.valueOf(3000));

    }

    private AccountResponseDTO createAccountResponseDTO() {
        return new AccountResponseDTO(1L,"Lori", "lori@cat.xyz"
                , "+79823876917", OffsetDateTime.now(), Arrays.asList(1L,2L,3L));
    }
}
