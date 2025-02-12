Cenário de Uso

Você foi contratado para desenvolver uma aplicação que consome dados de uma API pública de Pokémons (PokéAPI: https://pokeapi.co/) e armazena informações de Pokémons capturados em um banco de dados. A aplicação deve oferecer endpoints para registrar novos Pokémons, listar os capturados e buscar informações detalhadas de cada um.

Configuração Inicial
1.	Crie um projeto Spring Boot utilizando o Spring Initializr com as seguintes dependências:
•	Spring Web
•	Spring Data JPA
•	H2 Database
•	Lombok (opcional)
•	As dependências específicas de cada cliente REST:
•	RestTemplate: Nenhuma adicional.
•	OpenFeign: Adicione spring-cloud-starter-openfeign.
•	Rest Client Declarativo (Spring 6): Nenhuma adicional, pois é nativo no Spring 6.
2.	Configure o banco de dados H2 no application.yml ou application.properties para persistir os dados localmente.


Parte 1: Implementação Usando RestTemplate

Objetivo: Utilizar o RestTemplate para consumir a PokéAPI e salvar as informações retornadas no banco de dados.

Tarefas:
1.	Criar um serviço usando RestTemplate:
•	Desenvolva um método que consuma a API https://pokeapi.co/api/v2/pokemon/{name} para obter informações de um Pokémon.
2.	Modelar as entidades JPA:
•	Crie uma entidade Pokemon com os seguintes atributos:
•	id
•	name
•	height
•	weight
•	types (uma lista de tipos de Pokémon)
3.	Salvar os dados retornados no banco de dados:
•	O método no serviço deve receber o nome do Pokémon, buscar as informações na API e salvar os dados no banco.
4.	Criar endpoints REST:
•	POST /pokemons/{name}: Captura um Pokémon e o salva no banco.
•	GET /pokemons: Lista todos os Pokémons capturados.
•	GET /pokemons/{id}: Retorna os detalhes de um Pokémon específico.
      Parte 2: Implementação Usando OpenFeign

Objetivo: Utilizar o OpenFeign para consumir a mesma API e integrar o resultado ao banco de dados.

Tarefas:
1.	Definir uma interface Feign:
•	Use a anotação @FeignClient para definir o cliente e os métodos de consumo:
•	GET /pokemon/{name} para buscar dados de um Pokémon.
2.	Modelar as entidades JPA:
•	Utilize a mesma entidade Pokemon criada na Parte 1.
3.	Criar o serviço de persistência:
•	Injete a interface Feign no serviço e utilize-a para obter os dados e salvá-los no banco.
4.	Criar endpoints REST:
•	Os mesmos endpoints da Parte 1 (POST /pokemons/{name}, GET /pokemons, GET /pokemons/{id}).
      Parte 3: Implementação Usando o Rest Client Declarativo do Spring 6

Objetivo: Utilizar o novo cliente REST nativo do Spring 6 para consumir a PokéAPI e armazenar os dados no banco.

Tarefas:
1.	Definir uma interface com @HttpExchange:
•	Crie métodos para buscar dados da PokéAPI usando as anotações @GetExchange.
2.	Modelar as entidades JPA:
•	Reutilize a entidade Pokemon das partes anteriores.
3.	Criar o serviço de persistência:
•	Injete a interface do Rest Client e utilize-a para buscar os dados de um Pokémon e salvá-los no banco.
4.	Criar endpoints REST:
•	Os mesmos endpoints das Partes 1 e 2.