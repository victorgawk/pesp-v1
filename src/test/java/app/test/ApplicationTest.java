package app.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.shaded.json.JSONObject;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApplicationTest {

	@Value("${auth0.audience}")
	private String audience;

	@Value("${auth0.clientId}")
	private String clientId;

	@Value("${auth0.clientSecret}")
	private String clientSecret;

	@Value("${auth0.grantType}")
	private String grantType;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;
	
	protected HttpHeaders authorizationHeaders;

	@BeforeAll
	public void getAuthorizationHeaders() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientId);
		map.add("client_secret", clientSecret);
		map.add("audience", audience);
		map.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		RestTemplate restTemplate = new RestTemplate();
		JSONObject jsonObj = restTemplate.postForObject(issuer + "oauth/token", entity, JSONObject.class);

		authorizationHeaders =  new HttpHeaders();
		authorizationHeaders.add("Authorization", "Bearer " + jsonObj.get("access_token"));
	}

}
