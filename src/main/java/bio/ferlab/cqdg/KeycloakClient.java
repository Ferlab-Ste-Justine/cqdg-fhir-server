package bio.ferlab.cqdg;

import bio.ferlab.cqdg.properties.BioProperties;
import com.github.dnault.xmlpatch.internal.Log;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KeycloakClient {

	private static final Logger log = LoggerFactory.getLogger(KeycloakClient.class);

	public static final String AUTH_CLIENT_SECRET_KEY = "secret";

	private final BioProperties bioProperties;

	private AuthzClient authzClient;

	public KeycloakClient(BioProperties bioProperties) {
		this.bioProperties = bioProperties;
	}

	public TokenIntrospectionResponse introspectRpt(String rpt) {
		return this.retryableIntrospectRpt(rpt, bioProperties.getAuthRetry());
	}

	private TokenIntrospectionResponse retryableIntrospectRpt(String rpt, int retry) {
		try {
			return this.getAuthzClient().protection().introspectRequestingPartyToken(rpt);
		} catch (Exception e) {
			log.error("Failed to introspect token: {}", e.getMessage());
			if (retry > 0) {
				log.info("Retry to introspect token: {}", retry);
				return retryableIntrospectRpt(rpt, retry - 1);
			} else {
				throw e;
			}
		}
	}

	private AuthzClient initAuthzClient() {
		try {
			Log.info("Init auth. client");
			final Configuration configuration = new Configuration();
			configuration.setRealm(bioProperties.getAuthRealm());
			configuration.setAuthServerUrl(bioProperties.getAuthServerUrl());
			configuration.setResource(bioProperties.getAuthClientId());
			configuration.getCredentials().put(AUTH_CLIENT_SECRET_KEY, bioProperties.getAuthClientSecret());

			this.authzClient = AuthzClient.create(configuration);
			return this.authzClient;
		} catch (Exception e) {
			log.error("Failed to init auth. client", e);
			throw e;
		}
	}

	private AuthzClient getAuthzClient() {
		return Optional.ofNullable(this.authzClient).orElseGet(this::initAuthzClient);
	}
}
