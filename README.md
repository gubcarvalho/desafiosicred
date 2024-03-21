**Tarefa bonus 3 - Perfomance tests**

Normalmente utilizo o JMeter para fazer os testes de performance. No entanto nao houve tempo de implementar neste desafio



**Tarefa bonus 4 - Versionamento da API**

Para versionamento dos endpoints temos algumas estrategias:

- URI versioning: define na uri do endpoint a versão da api desejada
- request parameter versioning: utiliza um parametros para especificar a versão 
- custom header versioning: utiliza um custom header para especificar a versão
- content negotiation (accept header): aproveita o cabeçalho HTTP Accept para especificar a versão da API desejada.

No entanto estas estrategias sao utilizadas em desenvolvimento de APIs. Em microservicos administramos a versão atraves da imagem disponibilizada no kubernetes.

**_Obs: tambem podemos utilizar as estrategias de versionamento mesmo administrando a imagem do microservico tendo assim uma politica hibrida de versionamento._**