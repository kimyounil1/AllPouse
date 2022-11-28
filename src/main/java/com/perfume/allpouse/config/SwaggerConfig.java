package com.perfume.allpouse.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.perfume.allpouse.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .alternateTypeRules(
                        AlternateTypeRules.newRule(Pageable.class, Page.class)
                );
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("X-AUTH-TOKEN", authorizationScopes));
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AllPouse Swagger")
                .description("All Pouse API V1 Swagger")
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("X-AUTH-TOKEN", "X-AUTH-TOKEN", "header");
    }

    // Swagger Pageable 파라미터 수정
    @Getter
    @Setter
    @ApiModel
    static class Page {

        @ApiModelProperty(value = "페이지 번호")
        private Integer page;

        @ApiModelProperty(value = "한 페이지당 데이터 개수")
        private Integer size;

        @ApiModelProperty(value = "정렬 방법 (ex. id,ASC")
        private String sort;

    }
}
