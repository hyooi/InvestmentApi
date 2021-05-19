import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kello.investment.InvestmentApplication;
import com.kello.investment.entity.InvestingProduct;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.enums.RecruitingStatusEnum;
import com.kello.investment.enums.ResultCodeEnum;
import com.kello.investment.repository.InvestingProductRepository;
import com.kello.investment.repository.InvestingStatusRepository;
import java.time.LocalDateTime;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = InvestmentApplication.class)
@Transactional
@AutoConfigureMockMvc
public class InvestmentApplicationTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private InvestingProductRepository productRepository;

  @Autowired
  private InvestingStatusRepository statusRepository;

  @BeforeEach
  void init() {
    productRepository.deleteAll();
    statusRepository.deleteAll();

    productRepository.save(InvestingProduct.builder()
        .productId(3)
        .title("investment product for test")
        .startedAt(LocalDateTime.now().minusDays(1))
        .finishedAt(LocalDateTime.now().plusDays(1))
        .totalInvestingAmount(10000)
        .presentInvestingAmount(100)
        .build());

    statusRepository.save(InvestingStatus.builder()
        .productId(3)
        .userId(10)
        .investAmount(100)
        .investDate(LocalDateTime.now())
        .build());
  }

  @Test
  @DisplayName("전체 투자상품 조회API 테스트")
  @SneakyThrows
  void test_getAllInvestmentProducts() {
    mvc.perform(get("/api/products"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("resultCode", Matchers.is(ResultCodeEnum.NORMAL.getResultCode())))
        .andExpect(jsonPath("result[0].productId", Matchers.is(3)))
        .andExpect(jsonPath("result[0].title", Matchers.is("investment product for test")))
        .andExpect(jsonPath("result[0].totalInvestingAmount", Matchers.is(10000)))
        .andExpect(jsonPath("result[0].presentInvestingAmount", Matchers.is(100)))
        .andExpect(jsonPath("result[0].investorCnt", Matchers.is(1)))
        .andExpect(
            jsonPath("result[0].recruitingStatus",
                Matchers.is(RecruitingStatusEnum.RECRUITING.name())))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));
  }

  @Test
  @DisplayName("투자하기API 테스트")
  @SneakyThrows
  void test_invest() {
    mvc.perform(post("/api/invest/3/100")
        .header("X-USER-ID", 10))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("resultCode", Matchers.is(ResultCodeEnum.NORMAL.getResultCode())))
        .andExpect(jsonPath("result.productId", Matchers.is(3)))
        .andExpect(jsonPath("result.userId", Matchers.is(10)))
        .andExpect(jsonPath("result.investAmount", Matchers.is(200)))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));
  }

  @Test
  @DisplayName("나의투자상품조회API 테스트")
  @SneakyThrows
  void test_getMyInvestmentProduct() {
    mvc.perform(get("/api/product")
        .header("X-USER-ID", 10))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("resultCode", Matchers.is(ResultCodeEnum.NORMAL.getResultCode())))
        .andExpect(jsonPath("result[0].productId", Matchers.is(3)))
        .andExpect(jsonPath("result[0].title", Matchers.is("investment product for test")))
        .andExpect(jsonPath("result[0].totalInvestingAmount", Matchers.is(10000)))
        .andExpect(jsonPath("result[0].myInvestAmount", Matchers.is(100)))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));
  }
}
