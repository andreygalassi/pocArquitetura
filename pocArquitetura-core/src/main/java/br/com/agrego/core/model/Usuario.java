package br.com.agrego.core.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import org.springframework.security.core.userdetails.UserDetails;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@MyLog(chave="Usuario")
@Entity
@Table(name = "CORE_USUARIO")
@Getter @Setter @NoArgsConstructor @ToString
@EqualsAndHashCode(of = {"id"})
public class Usuario implements UserDetails, Serializable {
	 
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String email;
	
	@Column(unique=true,updatable=false,nullable=false)
	private String username;
	private String password;
	
	private boolean accountNonExpired=true;
	private boolean accountNonLocked=true;
	private boolean credentialsNonExpired=true;
	private boolean enabled=true;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="CORE_USUARIO_PERFIL",
	joinColumns=@JoinColumn(name="idUsuario"),
	inverseJoinColumns=@JoinColumn(name="idPerfil"))
	private Set<Perfil> perfis = new HashSet<>();

	@Transient
	private Set<GrantedAuthority> authorities = new HashSet<>();
	
	@Transient
	public Set<GrantedAuthority> getAuthorities() {
		if (authorities.isEmpty()){
			for (Perfil perfil : perfis) {
				authorities.addAll(perfil.getPermissoes());
			}
		}
		return authorities;
	}
	@Transient
	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
