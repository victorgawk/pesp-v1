package app.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;

import app.util.JSONUtil;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest extends ApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testV1() throws Exception {
		String expected = null;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("custom.properties"));
			expected = (String) prop.get("app-version");
		} catch (FileNotFoundException e) {
			expected = "DEVELOPMENT";
		}

		this.mockMvc
			.perform(get("/v1").headers(authorizationHeaders))
			.andExpect(status().isOk())
			.andExpect(content().string(expected));
	}

	@Test
	void testPostUsers() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post("/users").headers(authorizationHeaders)).andReturn();
		JSONObject obj = (JSONObject) JSONUtil.parse(mvcResult.getResponse().getContentAsString());
		assertNotNull(obj.get("id"));
		assertNotNull(obj.get("enabled"));
	}
	
	@Test
	void testPatchUsersEnabled() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post("/users").headers(authorizationHeaders)).andReturn();
		JSONObject obj = (JSONObject) JSONUtil.parse(mvcResult.getResponse().getContentAsString());
		obj.put("enabled", true);

		this.mockMvc
			.perform(patch("/users/" + obj.get("id") + "/enabled").headers(authorizationHeaders))
			.andExpect(status().isOk())
			.andExpect(content().json(obj.toString()));
	}

	@Test
	void testPatchUsersDisabled() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post("/users").headers(authorizationHeaders)).andReturn();
		JSONObject obj = (JSONObject) JSONUtil.parse(mvcResult.getResponse().getContentAsString());
		obj.put("enabled", false);

		this.mockMvc
			.perform(patch("/users/" + obj.get("id") + "/disabled").headers(authorizationHeaders))
			.andExpect(status().isOk())
			.andExpect(content().json(obj.toString()));
	}
	
	@Test
	void testDeleteUsers() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post("/users").headers(authorizationHeaders)).andReturn();
		JSONObject obj = (JSONObject) JSONUtil.parse(mvcResult.getResponse().getContentAsString());

		this.mockMvc
			.perform(delete("/users/" + obj.get("id")).headers(authorizationHeaders))
			.andExpect(status().isOk())
			.andExpect(content().json(obj.toString()));
	}

	@Test
	void testGetUsers() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/users").headers(authorizationHeaders)).andReturn();
		JSONArray obj = (JSONArray) JSONUtil.parse(mvcResult.getResponse().getContentAsString());
		assertNotNull(obj);
	}

}
