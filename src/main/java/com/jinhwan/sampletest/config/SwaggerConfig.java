package com.jinhwan.sampletest.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	 @Bean
	    public Docket api() {
		 
		 return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(getApiInfo())                  // swagger 정보
	                .produces(getProduceContentTypes())     // response content-type 설정
	                .select()                               // return ApiSelectoorBuilder(화면 관리)
	                .apis(RequestHandlerSelectors.basePackage("com.jinhwan.sampletest"))
	                .paths(PathSelectors.any())
	                .build()
	                .securityContexts(Arrays.asList(securityContext()))
		 			.securitySchemes(Arrays.asList(apiKey()));

	 }

	 
	   /**
	     * 선택항목
	     * Swagger UI에서 보여지는 정보
	     */
	    private ApiInfo getApiInfo() {
	        return new ApiInfoBuilder()
	                .title("ApiSampleTest")           // swagger 제목
	                .description("Rest Api Test")     // swagger 설명
	                .version("2.9.2")            	  // swagger 버전
	                .contact(new Contact("JinHwan", "", "kiyacoke@gmail.com"))    // 작성자정보
	                .build();
	    }
	    
	    private Set<String> getProduceContentTypes() {
	        Set<String> produces = new HashSet<>();
	        produces.add("application/json; charset=UTF-8");
	        return produces;
	    }
	    
	    private ApiKey apiKey() {
	        return new ApiKey("JWT", "Authorization", "header");
	    }
	    
	    private SecurityContext securityContext() {
	        return springfox
	                .documentation
	                .spi.service
	                .contexts
	                .SecurityContext
	                .builder()
	                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	    }
	    
	    List<SecurityReference> defaultAuth() {
	        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	    }
}
