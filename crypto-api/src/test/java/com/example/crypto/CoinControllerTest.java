package com.example.crypto;

import com.example.crypto.dto.PageResult;
import com.example.crypto.dto.CoinDto;
import com.example.crypto.controller.CoinController;
import com.example.crypto.service.CoinService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoinController.class)
public class CoinControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CoinService coinService;

    @Test
    void listCoins_returnsPage() throws Exception {
        PageResult<CoinDto> page = new PageResult<>(1, 10, 1, 1, List.of(new CoinDto()));
        when(coinService.listCoins(1, 10, null)).thenReturn(page);

        mvc.perform(get("/api/v1/coins")
                        .header("Authorization", "Bearer invalid-token")) // Jwt filter allows any token in WebMvcTest context; adjust if needed
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.page_num").value(1));
    }
}
