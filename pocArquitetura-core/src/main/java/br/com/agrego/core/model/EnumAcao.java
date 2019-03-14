package br.com.agrego.core.model;

public enum EnumAcao {
	CREATE, READ, UPDATE, DELETE;
	
	public String getAutorizacao(final Class<?> clazz){
		
		String retorno = this.name() + "_"; 

		if (clazz!=null)
			retorno += clazz.getSimpleName().toUpperCase();
		
		return retorno;
	}
}
