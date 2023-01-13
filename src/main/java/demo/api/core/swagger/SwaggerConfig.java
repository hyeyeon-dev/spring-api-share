package demo.api.core.swagger;

import com.fasterxml.classmate.TypeResolver;
import demo.api.server.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * Swagger 접근 : HOST/swagger-ui/index.html
 */
@Profile({"local"})
@Configuration
public class SwaggerConfig {
    TypeResolver typeResolver = new TypeResolver();

    @Value("${swagger.apiName}")
    private String apiName;

    @Value("${swagger.apiVersion}")
    private String apiVersion;

    @Value("${swagger.apiDescription}")
    private String apiDescription;

    @Value("${swagger.targetPackage}")
    private String targetPackage;

    @Value("${spring.swagger.enable}")
    private String enabled;

    @Value("${spring.swagger.base-url}")
    private String baseUrl;

    @Value("${spring.swagger.base-url-desc}")
    private String baseUrlDesc;

    @Bean
    public Docket api() {
        baseUrl = baseUrl.replace("{localIp}", NetworkUtils.getServerIp());

        Server serverUri = new Server(baseUrlDesc, baseUrl, "", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                //.servers(serverLocal, testServer)
                .servers(serverUri)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(targetPackage))
                .paths(PathSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .enable(Boolean.parseBoolean(enabled))
                .useDefaultResponseMessages(false)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(apiName)
                .version(apiVersion)
                .description(apiDescription)
                .build();
    }
}
