package br.com.agrego.core.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para identificar a entidade que deve ser logada no histórico.
 * @author Andrey
 * @since 2019-01-22
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {

    /**
     * (Obrigatório) Chave identificadora da tabela logada. Esse valor n deve ser repetido
     */
    String chave();

}
