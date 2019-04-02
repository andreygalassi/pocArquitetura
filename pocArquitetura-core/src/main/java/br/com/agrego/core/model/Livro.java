package br.com.agrego.core.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Livro implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@EqualsAndHashCode.Include
	private Long id;

	private String titulo;

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.MERGE)
	private Autor autor;
	
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getTitulo() {
//		return titulo;
//	}
//
//	public void setTitulo(String titulo) {
//		this.titulo = titulo;
//	}
//
//	public Autor getAutor() {
//		return autor;
//	}
//
//	public void setAutor(Autor autor) {
//		this.autor = autor;
//	}
//
//	@Override
//	public String toString() {
//		return String.format("Livro [id=%s, titulo=%s, autor=%s]", id, titulo, autor);
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Livro other = (Livro) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}
}
