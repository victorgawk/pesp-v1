package app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.shaded.json.JSONObject;

import app.model.Operacao;
import app.model.Usuario;
import app.repository.OperacaoRepository;
import app.repository.UsuarioRepository;

@RestController
public class OperacaoController {
	
	private static final Logger LOGGER = LogManager.getLogger(OperacaoController.class);

	private final OperacaoRepository operacaoRepository;

	private final UsuarioRepository usuarioRepository;

	public OperacaoController(OperacaoRepository operacaoRepository, UsuarioRepository usuarioRepository) {
		this.operacaoRepository = operacaoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/operations")
	public List<Operacao> listOperations() {
		LOGGER.info("listando todas as operacoes realizadas");
		return operacaoRepository.findAll();
	}

	@PostMapping("/operations")
	public JSONObject createOperation(@RequestBody JSONObject operacaoJson) throws Exception {
		Long userId = ((Number) operacaoJson.get("user_id")).longValue();
		Usuario usuario = usuarioRepository.findById(userId).orElse(null);
		if (usuario == null) {
			LOGGER.error("usuario " + userId + " nao encontrado para criar operacao");
			return null;
		}

		Operacao operacao = new Operacao();
		operacao.setUsuario(usuario);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		operacao.setDepartureDate(formato.parse(operacaoJson.getAsString("departureDate")));
		operacao.setReturnDate(formato.parse(operacaoJson.getAsString("returnDate")));
		operacao.setDestination(operacaoJson.getAsString("destination"));
		operacaoRepository.save(operacao);

		operacaoJson.put("operation_id", operacao.getId());
		return operacaoJson;
	}
	
	@PatchMapping("/operations/cancel")
	public JSONObject cancelOperation(@RequestBody JSONObject operacaoJson) throws Exception {
		Long userId = ((Number) operacaoJson.get("user_id")).longValue();
		Usuario usuario = usuarioRepository.findById(userId).orElse(null);
		if (usuario == null) {
			LOGGER.error("usuario " + userId + " nao encontrado para cancelar operacao");
			return null;
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		Date departureDate = formato.parse(operacaoJson.getAsString("departureDate"));
		Date returnDate = formato.parse(operacaoJson.getAsString("returnDate"));
		String destination = operacaoJson.getAsString("destination");
		
		List<Operacao> operacoes = operacaoRepository.findByDepartureDateAndReturnDateAndDestinationAndUsuario(departureDate, returnDate, destination, usuario);
		if (operacoes.isEmpty()) {
			LOGGER.error("operacao " + operacaoJson + " nao encontrada para cancelar");
			return null;
		}
		Operacao operacao = operacoes.get(0);
		operacao.setCancelled(true);
		operacaoRepository.save(operacao);

		operacaoJson.put("operation_id", operacao.getId());
		return operacaoJson;
	}

}
