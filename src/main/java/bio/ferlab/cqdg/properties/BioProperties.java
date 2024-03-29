package bio.ferlab.cqdg.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "bio")
@Data
public class BioProperties {
    private final boolean isAuthEnabled;
    private final String authServerUrl;
    private final String internalServerUrl;
    private final String authRealm;
    private final Integer authRetry;
    private final Long authLeeway;
    private final boolean isDisableSslValidation;
    private final boolean isAuditsEnabled;
    private final boolean isTaggingEnabled;
    private final boolean isServiceRequestRoutingEnabled;
    private final boolean isTaggingQueryParam;
    private final boolean isAuthorizationEnabled;
    private final String authClientId;
//    private final String authSystemId;
    private final String authClientSecret;
    private final boolean isNanuqEnabled;
    public BioProperties(
            @Value("${bio.auth.enabled}") boolean isAuthEnabled,
            @Value("${bio.auth.server-url}") String authServerUrl,
            @Value("${bio.auth.internal-server-url}") String internalServerUrl,
            @Value("${bio.auth.realm}") String authRealm,
            @Value("${bio.auth.retry}") Integer authRetry,
            @Value("${bio.auth.leeway}") Long authLeeway,
            @Value("${bio.auth.disable-ssl-validation}") boolean isDisableSslValidation,
            @Value("${bio.audits.enabled}") boolean isAuditsEnabled,
            @Value("${bio.tagging.enabled}") boolean isTaggingEnabled,
            @Value("${bio.tagging.queryParam}") boolean isTaggingQueryParam,
            @Value("${bio.auth.authorization.enabled}") boolean isAuthorizationEnabled,
            @Value("${bio.service-request-routing.enabled}") boolean isServiceRequestRoutingEnabled,
            @Value("${bio.auth.authorization.client-id}") String authClientId,
            @Value("${bio.auth.authorization.client-secret}") String authClientSecret,
//            @Value("${bio.auth.authorization.system-id}") String authSystemId,
            @Value("${bio.nanuq.enabled}") boolean isNanuqEnabled

    ) {
        this.isAuthEnabled = isAuthEnabled;
        this.authServerUrl = authServerUrl;
        this.internalServerUrl = internalServerUrl;
        this.authRealm = authRealm;
        this.authRetry = authRetry;
        this.authLeeway = Optional.ofNullable(authLeeway).orElse(0L);
        this.isDisableSslValidation = isDisableSslValidation;
        this.isAuditsEnabled = isAuditsEnabled;
        this.isTaggingEnabled = isTaggingEnabled;
        this.isTaggingQueryParam = isTaggingQueryParam;
        this.isAuthorizationEnabled = isAuthorizationEnabled;
        this.authClientId = authClientId;
        this.authClientSecret = authClientSecret;
//        this.authSystemId = authSystemId;
        this.isNanuqEnabled = isNanuqEnabled;
        this.isServiceRequestRoutingEnabled = isServiceRequestRoutingEnabled;
    }
}
