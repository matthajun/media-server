package com.example.mediaserver.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openApi() {
    return new OpenAPI().components(new Components()).info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("Media server API")
        .description("Media server API 명세서")
        .version("1.0.0");
  }

  private Stream<Operation> getAllOperations(PathItem pathItem) {
    return Stream.of(
            Optional.ofNullable(pathItem.getGet()),
            Optional.ofNullable(pathItem.getPost()),
            Optional.ofNullable(pathItem.getPut()),
            Optional.ofNullable(pathItem.getDelete()),
            Optional.ofNullable(pathItem.getPatch()),
            Optional.ofNullable(pathItem.getHead()),
            Optional.ofNullable(pathItem.getOptions()),
            Optional.ofNullable(pathItem.getTrace()))
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @Bean
  public OpenApiCustomizer sortAndPageableCustomizer() {
    return openApi ->
        openApi.getPaths().values().stream()
            .flatMap(this::getAllOperations)
            .filter(
                operation ->
                    operation.getParameters() != null
                        && operation.getParameters().stream()
                            .anyMatch(
                                param ->
                                    param.getIn() != null
                                        && param.getIn().equals("query")
                                        && (param.getName().equals("pageable")
                                            || param.getName().equals("page")
                                            || param.getName().equals("size")
                                            || param.getName().equals("sort"))))
            .forEach(
                operation -> {
                  operation.setParameters(
                      operation.getParameters().stream()
                          .filter(
                              param ->
                                  !param.getName().equals("pageable")
                                      && !param.getName().equals("page")
                                      && !param.getName().equals("size")
                                      && !param.getName().equals("sort"))
                          .collect(Collectors.toCollection(ArrayList::new)));

                  operation.addParametersItem(
                      new Parameter()
                          .name("page")
                          .in("query")
                          .description("페이지 번호 (0부터 시작)")
                          .schema(new Schema<>().type("integer").example(Integer.valueOf(0))));

                  operation.addParametersItem(
                      new Parameter()
                          .name("size")
                          .in("query")
                          .description("한 페이지당 항목 수")
                          .schema(new Schema<>().type("integer").example(Integer.valueOf(10))));

                  operation.addParametersItem(
                      new Parameter()
                          .name("sort")
                          .in("query")
                          .description(
                              "정렬 기준 (예: field1,asc / field2,desc). 다중 정렬 가능 (예: field1,asc&sort=field2,desc)")
                          .schema(new Schema<>().type("string").example("name,asc")));
                });
  }
}
