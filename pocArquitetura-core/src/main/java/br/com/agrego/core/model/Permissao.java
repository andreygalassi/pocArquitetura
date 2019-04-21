package br.com.agrego.core.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CORE_USUARIO")
@Getter @Setter @NoArgsConstructor @ToString
@EqualsAndHashCode(of = {"id"})
public class Permissao implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private EnumAcao acao;
	
	public static Permissao newInstance(String nome) {
		Permissao p = new Permissao();
		p.setNome(nome);
		return p;
	}

	@Override
	@Transient
	public String getAuthority() {
		return nome.toUpperCase();
	}

}
