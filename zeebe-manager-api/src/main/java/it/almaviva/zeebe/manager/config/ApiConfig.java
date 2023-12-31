package it.almaviva.zeebe.manager.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApiConfig{

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .forCodeGeneration(true);

        docket = docket.select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build() .protocols(Sets.newHashSet("http", "https"));
        return docket;
    }
    
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ZeebeManagerBeAPI")
                .description("Zeebe Manager BE API for file storage persistence and versioning")
                .version(ApiConfig.class.getPackage().getImplementationVersion())
                .build();
    }
}