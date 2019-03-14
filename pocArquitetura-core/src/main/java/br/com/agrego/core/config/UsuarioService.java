package br.com.agrego.core.config;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.agrego.core.model.Usuario;

public interface UsuarioService extends UserDetailsService{

	Usuario loadUserByUsername(String username);

}
