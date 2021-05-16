package com.kello.investment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CommonResponse<T> {

  private String resultCode;
  private String resultMessage;
  private T result;

  @Builder.Default
  private final LocalDateTime timestamp = LocalDateTime.now();
}
