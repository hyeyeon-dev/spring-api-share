package demo.api.core.swagger;

import demo.api.server.utils.NetworkUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Configuration
public class Workaround implements WebMvcOpenApiTransformationFilter {

    @Value("${spring.swagger.base-url}")
    private String baseUrl;

    @Value("${spring.swagger.base-url-desc}")
    private String baseUrlDesc;

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openApi = context.getSpecification();

        baseUrl = baseUrl.replace("{localIp}", NetworkUtils.getServerIp());

        Server baseServer = new Server();
        baseServer.setDescription(baseUrlDesc);
        baseServer.setUrl(baseUrl);

        openApi.setServers(Arrays.asList(baseServer));

        return openApi;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType.equals(DocumentationType.OAS_30);
    }
}
