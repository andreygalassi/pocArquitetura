package br.com.agrego.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.Gson;

/**
 * Classe auxiliar para a gravação de logs/historico de alterações de registros
 * @author Andrey
 * @since 2019-01-23
 * @param <T>
 */
@Entity
@Table(name = "RTG_HISTORICO_WEB")
@SequenceGenerator(name = "SEQ_HISTORICO", sequenceName = "SEQ_HISTORICO", initialValue = 1, allocationSize = 1)
public class Historico<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORICO")
	@Column(name = "ID_HISTORICO")
	private Long id;

	/**
	 * nome da tabela ao qual o registro logado pertence
	 */
	@Column(name = "TABELA", length = 30)
	private String tabela;

	/**
	 * id do registro logado
	 */
	@Column(name = "ID_REFERENCIA", length = 20)
	private Long idReferencia;

	/**
	 * nome do usuário que fez a alteração
	 */
	@Column(name = "AGENTE", length = 20)
	private String agente;
	
	public String getAgente() {
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	/**
	 * data da gravação do log
	 */
	@Column(name = "DATA_EVENTO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	/**
	 * acao executada pelo log (INCLUSAO, ALTERACAO, EXCLUSAO)
	 */
	@Column(name = "ACAO")
	@Enumerated(EnumType.STRING)
	private EnumAcao acao;

	/**
	 * o documento json referente ao objeto logado
	 */
	@Lob
	@Column(name = "DOCUMENTO")
	private String documento;

	/**
	 * método para executar o parse do json gravado no objeto pesquisado
	 */
	//TODO talvez exista uma forma mais amigável e que seja eficiente que não dependa da passagem da classe como parametro para este metodo
	private static final Gson gson = new Gson();
	public T parseDocumento(Class<T> arg) {
		T bean = gson.fromJson(this.documento, arg);
		return bean;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public Long getIdReferencia() {
		return idReferencia;
	}

	public void setIdReferencia(Long idReferencia) {
		this.idReferencia = idReferencia;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public EnumAcao getAcao() {
		return acao;
	}

	public void setAcao(EnumAcao acao) {
		this.acao = acao;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}
}
