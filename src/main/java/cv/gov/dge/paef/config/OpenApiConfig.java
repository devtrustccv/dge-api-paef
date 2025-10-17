package cv.gov.dge.paef.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiDocs() {
        return new OpenAPI().info(new Info()
                .title("DGE API - PAEF")
                .description("API de Integração PAEF")
                .version("0.0.1"));
    }
}
