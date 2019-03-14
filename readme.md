# POC-ARQUITETURA
##### Este projeto tem como objetivo criar um campo de treinamento para a criação de um projeto arquitetural multi-modulo que atendam as seguintes questões:
Esse projeto se espelhar-a em arquitetura de microserviços, tentando separar todos os endpoins em projetos distintos, porém, ainda assim, utilizará a mesma base de dados.

1. Projeto com spring boot.
1. Testes unitarios.
1. Testes de integração.
1. Segurança com spring security
    1. Basic
    1. OAuth 2.0
    1. Token/JWT
1. Acesso a banco de dados com JPA
1. Acesso a um segundo banco de dados com JPA.
1. Acesso a banco de dados utilizando query nativa e/ou jdbc
1. Controle de acesso a nivel de grupo de permissões
1. Utilização de spring batch
1. Varias frentes web :
     1. springmvc/thymeleaf
     1. vue.js/vuetify
     1. angular
     1. vaadin
1. Telemetria dos acessos ou outras utilizações
1. Log/Histórico de alteração de registro
1. Padrão dos logs de erros
1. Projeto multimodulo:
    * core: responsável com conter o basico das classes de configurações, classes utils e afins para aproveitamento nos outros projetos
    * bean: conter os beans e entidades de dados (separação é útil em multiprojetos)
    * util: classes utilitarias para para facilitar alguma utilização.
    * api: projeto com os endpoints de apis rest. 
    * ws: projeto com os endpoints de apis soap.
    * web: projetos de paginas de acesso ao sistema.

    
    
----
Algumas coisas foram depreciadas entre a versão 1 e 2 do spring boot
