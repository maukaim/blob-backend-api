package com.maukaim.cryptohub.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;

import java.util.List;

@ConfigurationProperties("maukaim.swagger")
@Data
public class MaukaimSwaggerProperties {

    private Boolean enabled;
    private String includePatterns = "^/(?!error|basicerror|autoconfig|health).*$";
    private String title;
    private String description;
    private String termOfServiceUrl;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
    private String license;
    private String licenseUrl;
    private String version;
    private Boolean areTryItOutMethodsEnabled = true;
    private Boolean isStandardPatternsFilterEnabled = true;

    @Data
    public static class ClientCredentialsFlow { // aka application
        private String tokenEndpointUrl;
        private String clientSecret;
        private String clientId;
    }

    @Data
    public static class ResourceOwnerPasswordFlow { // aka password
        private String tokenEndpointUrl;
        private String clientSecret;
        private String clientId;
    }

    @Data
    public static class AuthorizationCodeFlow { // aka : accessCode
        private TokenEndpoint tokenEndpoint;
        private TokenRequestEndpoint tokenRequestEndpoint;
    }

    @Data
    public static class ImplicitFlow { // aka : implicit
        private String authorizeEndpointUrl;
        private String clientId;
        private String clientSecret = "LEFT_EMPTY_INTENTIONALLY";
    }

    @Data
    static class Security {
        private Boolean enabled = Boolean.FALSE;
        private String realm = "/";
        private String appName = "api-documentations";
        private String apiKey = "api-documentations-key";
        private List<Scope> globalScopes;
        private AcrValue acrValue = AcrValue.L2;
        private SupportedGrantType flow = SupportedGrantType.IMPLICIT;
        private ClientCredentialsFlow clientCredentialsFlow;
        private ResourceOwnerPasswordFlow resourceOwnerPasswordFlow;
        private AuthorizationCodeFlow authorizationCodeFlow;
        private ImplicitFlow implicitFlow;
    }

    @Data
    public static class Scope {
        private String name;
        private String description;
    }

    @Data
    public static class Protocol {
        private Boolean httpsOnly = false;
    }

    public enum AcrValue {
        SEAMLESS, L1, L2, L3, L4
    }


    public enum SupportedGrantType {
        IMPLICIT,
        CLIENT_CREDENTIALS,
        RESOURCE_OWNER_PASSWORD,
        AUTHORIZATION_CODE
    }
}
