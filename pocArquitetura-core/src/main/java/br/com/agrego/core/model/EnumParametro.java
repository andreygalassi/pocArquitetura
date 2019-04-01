package br.com.agrego.core.model;

/**
 * Enum para facilitar a utilização dos parâmetros gravados em banco.
 * Esse ENUM funciona como um SINGLETON.
 * @author Andrey
 * @since 2019-02-04
 */
public enum EnumParametro {

//	EE_VERSAO_LAYOUT_DEFAULT("rtg.versaoLayout","08.0"),
//	ID_CONCILIADOR_DEFAULT("rtg.versaoLayout",0l),
//	DESABILITAR_VALIDACAO_SSO("rtg.desabilitarsso",0l),
//	AMBIENTE("SCHEMA","INDEFINIDO"),
	USUARIO_DEFAULT("rtg.usuarioDefault","USER_SYS"),
//	DOMINIOS_PERMITIDOS("rtg.dominiosPermitidos","@getnet.com.br;@centralgetnet.com.br"),
//	DOMINIO_DEFAULT("rtg.dominioDefault","@getnet.com.br"),
	;
//	
//	private static ParametroRepository repo;
//	
	private String chave;
	private Long valor;
	private String valorString;
	
	EnumParametro(String chave, Long valor, String valorString) {
		this.chave = chave;
		this.valor = valor;
		if (valor==null)  this.valor = 1l;
		this.valorString=valorString;
	}
	EnumParametro(String chave, String valorString) {
		this.chave = chave;
		this.valor = 1l;
		this.valorString=valorString;
	}
	EnumParametro(String chave, Long valor) {
		this.chave = chave;
		this.valor = valor;
		if (valor==null)  this.valor = 1l;
	}

	public String getChave() {
		return chave;
	}
//
//	/**
//	 * Retorna o valor numérico gravado no campo VALOR da tabela RTG_PARAMETRO_PROCESSAMENTO
//	 */
//	public Long getValor() {
//		if (repo!=null && repo.findOne(chave)!=null) return repo.findOne(chave).getValor();
//		return valor;
//	}
//	/**
//	 * Retorna o valor do campo VALOR que foi atualizado na inicialização da aplicação. 
//	 */
//	public Long getValorDefault() {
//		return valor;
//	}
//	/**
//	 * Retorna um boolean convertendo o valor numerico gravado no banco de dados no campo VALOR
//	 * true para valores positivos
//	 * false para zero ou valores negativos
//	 */
//	public Boolean getValorBoolean() {
//		Long val = getValor();
//		if (val!=null) return (val>0?true:false); 
//		return null;
//	}
//
//	/**
//	 * Retorna a string contida no campo DESC_PARAMETRO da tabela RTG_PARAMETRO_PROCESSAMENTO
//	 */
//	public String getValorString() {
//		if (repo!=null && repo.findOne(chave)!=null) return repo.findOne(chave).getValorString();
//		return valorString;
//	}
//	/**
//	 * Retorna o valor do campo DESC_PARAMETRO que foi atualizado na inicialização da aplicação. 
//	 */
//	public String getValorStringDefault() {
//		return valorString;
//	}
//
//	public static void setRepo(ParametroRepository repo) {
//		EnumParametro.repo = repo;
//	}
//	/**
//	 * Atualiza os valores desse enum de acordo com o objeto Parametro passado.
//	 * @param parametro
//	 */
//	public void atualizaParametro(Parametro parametro) {
//		if (parametro!=null) {
//			if (parametro.getValor()!=null)this.valor=parametro.getValor();
//			if (parametro.getValorString()!=null)this.valorString=parametro.getValorString();
//		}
//	}
	
	
}
