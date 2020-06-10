<h1>Teste Sistema de Ponto</h1>
Swagger para visualização e testes da API: http://localhost:[port]/swagger-ui.html

Na configuração padrão da aplicação, caso a porta não seja alterada no application.properties: http://localhost:9876/swagger-ui.html

Descrição Básica da API
Usuário
A API do Usuário

GET
/api/users/{id} - Obtém o usuário por id
PUT
/api/users/{id} - Atualiza informações do usuário
Exemplo de Request Body

{
  "nome": "Roberto",
  "cpf": "444.333.555-66"
  "email": "r.miyahara@hotmail.com"
}
DELETE
/api/users/{id} - Remove usuário por id
GET
/api/users - Obtém todos os usuários cadastrados
POST
/api/users - Adiciona novo usuário
Exemplo de Request Body

{
  "nome": "Roberto",
  "cpf": "444.333.555-66"
  "email": "r.miyahara@hotmail.com"
}
Controle de Ponto
A API do Controle de Ponto

GET
/api/punchClock - Obtém todos os pontos registrados
POST
/api/punchClock - Adiciona novo registro de ponto
Exemplo de Request Body

{
  "idUsuario": 1
}
GET
/api/punchClock/userId/{id} - Busca registro de ponto por id do usuário
DELETE
/api/punchClock/{id} - Remove registro de ponto por id
