package bio.ferlab.cqdg.auth;

import bio.ferlab.cqdg.properties.BioProperties;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class JwkProviderService {

  private final JwkProvider provider;
  
  public JwkProviderService(BioProperties bioProperties) throws MalformedURLException {
    final String url = StringUtils.appendIfMissing(bioProperties.getAuthServerUrl(), "/") + "realms/" + bioProperties.getAuthRealm()
        + "/protocol/openid-connect/certs";
    this.provider = new JwkProviderBuilder(new URL(url)).build();
  }
  
  public Jwk get(String keyId) throws JwkException {
    return this.provider.get(keyId);
  }
}
