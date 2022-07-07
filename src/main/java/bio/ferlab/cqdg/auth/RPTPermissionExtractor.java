package bio.ferlab.cqdg.auth;

import bio.ferlab.cqdg.KeycloakClient;
import bio.ferlab.cqdg.exceptions.RptIntrospectionException;
import bio.ferlab.cqdg.utils.Helpers;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;


@Component
public class RPTPermissionExtractor {
	private final KeycloakClient client;

	public RPTPermissionExtractor(KeycloakClient client) {
		this.client = client;
	}

	public UserPermissions extract(RequestDetails requestDetails) {
		final var bearer = requestDetails.getHeader(HttpHeaders.AUTHORIZATION);
		final var rpt = Helpers.extractAccessTokenFromBearer(bearer);
		final var response = this.client.introspectRpt(rpt);

		if (Optional.ofNullable(response.getPermissions()).isEmpty()) {
			throw new RptIntrospectionException("rpt token is required");
		}

		if (!response.isActive()) {
			throw new RptIntrospectionException("token is expired");
		}

		final var builder = new UserPermissionsBuilder();
		Optional.ofNullable(response.getPermissions())
			.orElse(Collections.emptyList())
			.forEach(permission -> builder.allowResource(permission.getResourceName(), permission.getScopes()));
		return builder.build();
	}
}
