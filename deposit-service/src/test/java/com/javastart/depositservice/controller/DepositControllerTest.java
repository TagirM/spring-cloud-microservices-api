package com.javastart.depositservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.deposit.DepositApplication;
import com.javastart.deposit.controller.dto.DepositResponseDTO;
import com.javastart.deposit.entity.Deposit;
import com.javastart.deposit.repository.DepositRepository;
import com.javastart.deposit.rest.AccountServiceClient;
import com.javastart.deposit.rest.BillServiceClient;
import com.javastart.depositservice.config.SpringH2DatabaseConfig;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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

import java.math.BigDecimal;
import java.util.List;

import static com.javastart.depositservice.util.DepositUtil.createAccountResponseDTO;
import static com.javastart.depositservice.util.DepositUtil.createBillResponseDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DepositApplication.class, SpringH2DatabaseConfig.class})
public class DepositControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DepositRepository depositRepository;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @MockBean
    private BillServiceClient billServiceClient;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static final String REQUEST = "{\n" +
            "    \"billId\": 1,\n" +
            "    \"amount\": 3000\n" +
            "}";
    @Test
    public void createDeposit() throws Exception {
        Mockito.when(billServiceClient.getBillById(ArgumentMatchers.anyLong()))
                .thenReturn(createBillResponseDTO());
        Mockito.when(accountServiceClient.getAccountByID(ArgumentMatchers.anyLong()))
                .thenReturn(createAccountResponseDTO());
        MvcResult mvcResult = mockMvc.perform(post("/deposits").content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        List<Deposit> deposits = depositRepository.findDepositsByEmail("lori@cat.xyz");
        ObjectMapper objectMapper = new ObjectMapper();
        DepositResponseDTO depositResponseDTO = objectMapper.readValue(body, DepositResponseDTO.class);

        Assertions.assertThat(depositResponseDTO.getMail()).isEqualTo(deposits.get(0).getEmail());
        Assertions.assertThat(depositResponseDTO.getAmount()).isEqualTo(BigDecimal.valueOf(3000));
    }
}
