package com.example.mediaserver.modules.hello.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Default")
@RestController
@RequestMapping("/api-get")
public class GetApiController {
  @GetMapping("/hello")
  public String hello() {
    return "hello, this is media-server.";
  }

  @Operation(summary = "사용자 정보 조회", description = "사용자 ID를 통해 특정 사용자의 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
  @GetMapping("hello-spring/{userId}")
  public String helloSpring(
      @Parameter(description = "조회할 사용자의 ID", required = true) @PathVariable String userId) {
    return "hello-spring" + userId;
  }
}
