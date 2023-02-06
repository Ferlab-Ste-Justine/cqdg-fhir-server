package bio.ferlab.cqdg.utils;

import bio.ferlab.cqdg.auth.JwkProviderService;
import bio.ferlab.cqdg.context.ServiceContext;
import bio.ferlab.cqdg.exceptions.RptIntrospectionException;
import bio.ferlab.cqdg.properties.BioProperties;
import bio.ferlab.cqdg.user.RequesterData;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Locale;

@Component
public class TokenDecoder {
	private static final Logger log = LoggerFactory.getLogger(TokenDecoder.class);

	private final JwkProviderService provider;
	private final BioProperties bioProperties;

	public TokenDecoder(JwkProviderService jwkProviderService, BioProperties bioProperties) {
		this.provider = jwkProviderService;
		this.bioProperties = bioProperties;
	}

	public RequesterData decode(String authorization, Locale locale) {
		try {
			final var accessToken = Helpers.extractAccessTokenFromBearer(authorization);
			final DecodedJWT decodedJWT = JWT.decode(accessToken);
			ServiceContext.build(decodedJWT.getSubject(), locale);

			final Jwk jwk = provider.get(decodedJWT.getKeyId());

			final Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
			final Verification verifier = JWT.require(algorithm);

			verifier.withIssuer(bioProperties.getAuthServerUrl() + "/realms/" + bioProperties.getAuthRealm(),
				bioProperties.getInternalServerUrl() + "/realms/" + bioProperties.getAuthRealm()).acceptLeeway(bioProperties.getAuthLeeway()).build().verify(decodedJWT);
			final String decodedBody = new String(new Base64(true).decode(decodedJWT.getPayload()));
			return new ObjectMapper().readValue(decodedBody, RequesterData.class);
		} catch (JwkException e) {
			log.error("Failed to decode token", e);
			throw new RptIntrospectionException("token from another provider");
		} catch (JWTDecodeException | JsonProcessingException e) {
			log.error("Failed to decode token", e);
			throw new RptIntrospectionException("malformed token");
		} catch (JWTVerificationException e) {
			log.error("Failed to decode token", e);
			throw new RptIntrospectionException("token is expired");
		}
	}
}
