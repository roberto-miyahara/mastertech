<h1>Teste Sistema de Ponto</h1>
<p>Swagger para visualização e testes da API: http://localhost:[port]/swagger-ui.html</p>

<p>Na configuração padrão da aplicação, caso a porta não seja alterada no application.properties: <a href="http://localhost:9876/swagger-ui.html" rel="nofollow">http://localhost:9876/swagger-ui.html</a></p>

<h2>Descrição Básica da API</h2>
<h2>Usuário</h2>
<p>A API do Usuário</p>
<ul>
  <li>
    <h4>GET</h4>
<pre>
<code>/api/users/{id} - Obtém o usuário por id</code></pre>
<h4>PUT</h4>
    <pre><code>/api/users/{id} - Atualiza informações do usuário</code></pre>
  </li>
  </ul>
  
<p><strong>Exemplo de Request Body</strong></p>

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
<h2>Controle de Ponto</h2>
A API do Controle de Ponto

<h4>GET</h4>
/api/punchClock - Obtém todos os pontos registrados
<h4>POST</h4>
/api/punchClock - Adiciona novo registro de ponto
<p><strongExemplo de Request Body</p></strong>

{
  "idUsuario": 1
}
<h4>GET</h4>
/api/punchClock/userId/{id} - Busca registro de ponto por id do usuário
<h4>DELETE</h4>
/api/punchClock/{id} - Remove registro de ponto por id
