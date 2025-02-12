<section>
  <strong>Exercício 1: Consulta Básica à PokéAPI</strong>
  <p><strong>Objetivo:</strong> Criar uma aplicação Spring Boot que disponibilize endpoints para consultar dados de Pokémon diretamente da PokéAPI (https://pokeapi.co/).</p>
</section>

<section>
  <strong>Exercício 1: Requisitos</strong>
  <ul>
    <li>Desenvolver um endpoint (por exemplo, GET /pokemon/{id} ou /pokemon/{name}) que receba o identificador ou nome do Pokémon e retorne os dados obtidos da PokéAPI.</li>
    <li>Implementar a lógica de consumo da API utilizando RestTemplate ou WebClient.</li>
    <li>Tratar erros de requisição, como casos onde o Pokémon não é encontrado.</li>
  </ul>
</section>

<section>
  <strong>Exercício 1: Gamification e Entrega</strong>
  <ul>
    <li><strong>Gamification:</strong> Adicionar um elemento simples de "descoberta" – por exemplo, um endpoint GET /pokemon/random que retorne um Pokémon aleatório da PokéAPI, incentivando o usuário a explorar diferentes Pokémon.</li>
    <li><strong>Entrega:</strong> Código fonte da aplicação, documentação básica dos endpoints e instruções para execução.</li>
  </ul>
</section>

<section>
  <strong>Exercício 2: Captura e Gerenciamento de Pokémon (Intermediário)</strong>
  <p><strong>Objetivo:</strong> Desenvolver uma aplicação Spring Boot que ofereça endpoints para "capturar" Pokémon, integrando a lógica de consumo da PokéAPI com regras de gamificação.</p>
</section>

<section>
  <strong>Exercício 2: Requisitos</strong>
  <ul>
    <li>Criar um endpoint (por exemplo, POST /capture) que, ao ser chamado, consuma a PokéAPI para obter dados de um Pokémon (pode ser aleatório ou com base em um parâmetro) e simule a captura deste Pokémon.</li>
    <li>Implementar regras simples de gamificação:
      <ul>
        <li>Atribuir pontos ao usuário com base no Pokémon capturado (por exemplo, pontos maiores para Pokémon raros ou com atributos específicos).</li>
        <li>Retornar uma mensagem personalizada indicando o sucesso da captura e os pontos obtidos.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>Exercício 2: Endpoints Adicionais</strong>
  <ul>
    <li>Listar Pokémon capturados durante a sessão ou simulação.</li>
    <li>Consultar detalhes de um Pokémon capturado (utilizando o ID ou nome obtido da PokéAPI).</li>
    <li>Implementar tratamento de erros e validação dos parâmetros de entrada.</li>
  </ul>
</section>

<section>
  <strong>Exercício 2: Gamification e Entrega</strong>
  <ul>
    <li><strong>Gamification:</strong> Incluir elementos de competição, por exemplo, exibir uma mensagem de "Parabéns, você capturou um [nome do Pokémon] e ganhou X pontos!".</li>
    <li><strong>Entrega:</strong> Código fonte da aplicação, documentação dos endpoints e um breve manual de como testar as funcionalidades de captura.</li>
  </ul>
</section>

<section>
  <strong>Exercício 3: Sistema Completo de Captura e Gerenciamento de Pokémon (Avançado)</strong>
  <p><strong>Objetivo:</strong> Construir uma aplicação Spring Boot completa que integre a PokéAPI, armazene os dados dos Pokémon capturados em um banco de dados com JPA e implemente um sistema de gamificação com perfis de treinador.</p>
</section>

<section>
  <strong>Exercício 3: Requisitos Funcionais (Parte 1)</strong>
  <ul>
    <li>Criar um conjunto de endpoints REST para:
      <ul>
        <li>Capturar um Pokémon (por exemplo, POST /capture) – ao ser chamado, o endpoint deverá:
          <ul>
            <li>Consultar a PokéAPI para obter os dados de um Pokémon (aleatório ou com base em critérios definidos pelo usuário).</li>
            <li>Aplicar regras de gamificação, como atribuição de pontos ou níveis de raridade.</li>
            <li>Salvar os dados do Pokémon capturado no banco de dados utilizando JPA, associando-o a um perfil de treinador.</li>
          </ul>
        </li>
        <li>Listar os Pokémon capturados (GET /pokemons) – permitindo filtrar por treinador, data, ou tipo.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>Exercício 3: Requisitos Funcionais (Parte 2)</strong>
  <ul>
    <li>Consultar o perfil do treinador (GET /trainer/{id}) – mostrando informações como pontos acumulados, número de Pokémon capturados e ranking.</li>
    <li>Atualizar ou liberar (excluir) um Pokémon capturado, simulando uma ação de "liberação" ou "troca" no jogo.</li>
    <li>Implementar autenticação simples (pode ser via tokens ou identificação de usuário) para associar as capturas aos treinadores.</li>
    <li>Tratar erros e realizar validações (por exemplo, impedir captura duplicada se aplicável, tratar indisponibilidade da PokéAPI, etc.).</li>
  </ul>
</section>

<section>
  <strong>Exercício 3: Requisitos Não Funcionais</strong>
  <ul>
    <li>Persistência: Utilizar Spring Data JPA para gerenciar entidades como Pokémon e Treinador.</li>
    <li>Escalabilidade: Considerar o uso de conexões e cache para minimizar chamadas repetidas à PokéAPI.</li>
    <li>Observabilidade: Implementar logs detalhados e métricas para monitorar o desempenho dos endpoints e a integridade dos dados.</li>
  </ul>
</section>

<section>
  <strong>Exercício 3: Gamification</strong>
  <ul>
    <li>Criar um sistema de pontos e rankings onde os treinadores acumulam pontos com base no valor, raridade e atributos dos Pokémon capturados.</li>
    <li>Implementar um leaderboard (por exemplo, GET /leaderboard) que exiba os melhores treinadores.</li>
    <li>Oferecer recompensas ou mensagens motivacionais, como "Você evoluiu para um Treinador de Elite!" quando atingir certos marcos.</li>
  </ul>
</section>

<section>
  <strong>Exercício 3: Entrega</strong>
  <ul>
    <li>Código fonte completo da aplicação.</li>
    <li>Documentação detalhada dos endpoints.</li>
    <li>Diagramas do modelo de dados (entidades JPA).</li>
    <li>Manual de instruções para teste (incluindo setup do banco de dados).</li>
  </ul>
</section>