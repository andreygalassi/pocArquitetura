package br.com.agrego.core.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CORE_PERFIL")
@Getter @Setter @NoArgsConstructor @ToString
@EqualsAndHashCode(of = {"id"})
public class Perfil implements GrantedAuthority, Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;

	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="CORE_PERFIL_PERMISSAO",
		joinColumns=@JoinColumn(name="idPerfil"),
		inverseJoinColumns=@JoinColumn(name="idPermissao"))
	private Set<Permissao> permissoes = new HashSet<>();

	@Override
	@Transient
	public String getAuthority() {
		return "ROLE_"+nome.toUpperCase();
	}

}
