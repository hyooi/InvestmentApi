package com.kello.investment.controller.api;

import com.kello.investment.service.InvestingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvestingController.class)
class InvestingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InvestingService service;

  @Test
  @DisplayName("")
  void test() {
  }

}