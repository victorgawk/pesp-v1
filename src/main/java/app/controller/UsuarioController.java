package app.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.Usuario;
import app.repository.UsuarioRepository;

@RestController
public class UsuarioController {
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioController.class);

	private final UsuarioRepository repository;

	public UsuarioController(UsuarioRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/v1")
	public String v1() throws Exception {
		LOGGER.info("recuperando versao da API");
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("custom.properties"));
			return (String) prop.get("app-version");
		} catch (FileNotFoundException e) {
			return "DEVELOPMENT";
		}
	}

	@PostMapping("/users")
	public Usuario createUser(Usuario user) {
		LOGGER.info("criando novo usuario");
		return repository.save(user);
	}

	@PatchMapping("/users/{user_id}/enabled")
	public Usuario enableUser(@PathVariable(name = "user_id") Long userId) {
		return enableOrDisableUser(userId, true);
	}

	@PatchMapping("/users/{user_id}/disabled")
	public Usuario disableUser(@PathVariable(name = "user_id") Long userId) {
		return enableOrDisableUser(userId, false);
	}

	@DeleteMapping("/users/{user_id}")
	public Usuario deleteUser(@PathVariable(name = "user_id") Long userId) {
		Usuario usuario = repository.findById(userId).orElse(null);
		if (usuario == null) {
			LOGGER.error("usuario " + userId + " nao encontrado para remover");
			return null;
		}
		LOGGER.info("removendo usuario " + userId);
		repository.delete(usuario);
		return usuario;
	}

	@GetMapping("/users")
	public List<Usuario> listUsers() {
		LOGGER.info("listando os usuarios cadastrados");
		return repository.findAll();
	}

	private Usuario enableOrDisableUser(Long userId, boolean flag) {
		Usuario usuario = repository.findById(userId).orElse(null);
		if (usuario == null) {
			LOGGER.error("usuario " + userId + "nao encontrado para " + (flag ? "h" : "des") + "abilitar");
			return null;
		}
		LOGGER.info((flag ? "h" : "des") + "abilitando usuario " + userId);
		usuario.setEnabled(flag);
		repository.save(usuario);
		return usuario;
	}

}
