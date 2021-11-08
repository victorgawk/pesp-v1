package app.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;

import app.util.JSONUtil;

class OperacaoControllerTest extends ApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetOperations() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/operations").headers(authorizationHeaders)).andReturn();
		JSONArray obj = (JSONArray) JSONUtil.parse(mvcResult.getResponse().getContentAsString());
		assertNotNull(obj);
	}

	@Test
	void testPostOperations() throws Exception {
		MvcResult mvcResultUsuario = this.mockMvc.perform(post("/users").headers(authorizationHeaders)).andReturn();
		JSONObject usuarioJson = (JSONObject) JSONUtil.parse(mvcResultUsuario.getResponse().getContentAsString());

		JSONObject postData = new JSONObject();
		postData.put("departureDate", "2021-01-01");
		postData.put("returnDate", "2021-06-15");
		postData.put("destination", "marte");
		postData.put("user_id", usuarioJson.get("id"));
		
		MvcResult mvcResult = this.mockMvc.perform(post("/operations")
				.headers(authorizationHeaders)
				.content(postData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		JSONObject obj = (JSONObject) JSONUtil.parse(mvcResult.getResponse().getContentAsString());
		assertNotNull(obj.get("operation_id"));
	}

	@Test
	void testPatchOperationsCancel() throws Exception {
		MvcResult usersResult = this.mockMvc.perform(post("/users").headers(authorizationHeaders)).andReturn();
		JSONObject usuarioJson = (JSONObject) JSONUtil.parse(usersResult.getResponse().getContentAsString());

		JSONObject formData = new JSONObject();
		formData.put("departureDate", "2021-01-01");
		formData.put("returnDate", "2021-06-15");
		formData.put("destination", "marte");
		formData.put("user_id", usuarioJson.get("id"));

		MvcResult operationsResult = this.mockMvc.perform(post("/operations")
				.headers(authorizationHeaders)
				.content(formData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		JSONObject operacaoJsonCadastro = (JSONObject) JSONUtil.parse(operationsResult.getResponse().getContentAsString());

		MvcResult operationsCancelResult = this.mockMvc.perform(patch("/operations/cancel")
				.headers(authorizationHeaders)
				.content(formData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONObject operacaoCancelJson = (JSONObject) JSONUtil.parse(operationsCancelResult.getResponse().getContentAsString());
		assertEquals(operacaoJsonCadastro.get("operation_id"), operacaoCancelJson.get("operation_id"));
	}

}
