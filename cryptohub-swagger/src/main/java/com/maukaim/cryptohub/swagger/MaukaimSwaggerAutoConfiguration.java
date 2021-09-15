package com.maukaim.cryptohub.swagger;

import com.maukaim.cryptohub.swagger.config.MaukaimSwaggerProperties;
import com.maukaim.cryptohub.swagger.config.DocketCustomizer;
import com.maukaim.cryptohub.swagger.config.SwaggerConfigCustomizer;
import com.maukaim.cryptohub.swagger.config.springfox.SwaggerUiWebFluxConfiguration;
import com.maukaim.cryptohub.swagger.config.springfox.SwaggerUiWebMvcConfiguration;
import com.maukaim.cryptohub.swagger.config.springfox.SpringfoxConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties({
        SpringfoxConfigurationProperties.class,
        MaukaimSwaggerProperties.class
})
@ConditionalOnProperty(prefix = "maukaim.swagger", value = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
        OpenApiDocumentationConfiguration.class,
        SpringDataRestConfiguration.class, // Only one not necessary
        BeanValidatorPluginsConfiguration.class,
        Swagger2DocumentationConfiguration.class,
        SwaggerUiWebFluxConfiguration.class,
        SwaggerUiWebMvcConfiguration.class
})
@AutoConfigureAfter({
        WebMvcAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class})
public class MaukaimSwaggerAutoConfiguration {

    public static final String securitySchemaOauth2 = "oauth2scheme";

    @Autowired
    private MaukaimSwaggerProperties swaggerProperties;

    @Autowired(required = false)
    private List<SwaggerConfigCustomizer> configurationCustomizers = Collections.emptyList();

    @Autowired(required = false)
    private List<DocketCustomizer> docketCustomizers = Collections.emptyList();

    @PostConstruct
    void customizeConfiguration(){
        this.configurationCustomizers.forEach(customizer -> customizer.customize(this.swaggerProperties));
    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(
                "Julien Elkaim",
                "maukaim.com",
                "julienelk@gmail.com"
        );
        return new ApiInfo(
                "Blob Swagger",
                "Swagger used by Blob's backend for dev purpose.",
                "1.0",
                "-",
                contact,
                "MIT License",
                "http://https://opensource.org/licenses/mit-license.php",
                new ArrayList<>());
//        return new ApiInfoBuilder().build();
    }

}
