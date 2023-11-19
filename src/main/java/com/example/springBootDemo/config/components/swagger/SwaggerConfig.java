package com.example.springBootDemo.config.components.swagger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @所属模块 配置-swagger
 * @描述
 * @创建人 xiaoYe
 * @创建时间 2020/4/7 23:39
 * @备注
 */
@EnableOpenApi
@Slf4j
@Configuration
//@EnableSwagger2
//@Profile({"dev", "test"})
public class SwaggerConfig {
    @Value("${server.port}")
    private int port;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    @SneakyThrows
    public Docket createRestApi() {

        Docket docket = new Docket(DocumentationType.OAS_30)  // swagger2版本这里是DocumentationType.SWAGGER_2
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .enable(true)
//                .groupName("user")
                /*.order(Ordering.byPath())*/; // 接口排序方式



//        /*2.0配置*/
//        Docket docket =  new Docket(DocumentationType.SWAGGER_2).
//                useDefaultResponseMessages(false)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.regex("^(?!auth).*$"))
//                .build()
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
////                .enable(true)
////                .groupName("user")
////                .order(Ordering.byPath()); // 接口排序方式
//                ;

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        // 控制台输出Swagger3接口文档地址
        log.info("Swagger3接口文档地址(注意是否导入对应的jar包): http://{}:{}{}/swagger-ui/index.html", ipAddress, port, contextPath);
        // 控制台输出Knife4j增强接口文档地址
        log.info("Knife4j增强接口文档地址: http://{}:{}{}/doc.html", ipAddress, port, contextPath);
        return docket;
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList();
        apiKeyList.add(new ApiKey("x-auth-token", "x-auth-token", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("服务文档")
//                .description("超厉害的！会自动生成代码")
//                .termsOfServiceUrl("http://www.这是小夜的url.com")
//                .contact(new Contact("叶明权", "", ""))
                .version("1.0.0")
                .build();
    }


    private List<Response> globalResponse(){
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("401").description("未认证").build());
        responseList.add(new ResponseBuilder().code("403").description("请求被禁止").build());
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }

}
