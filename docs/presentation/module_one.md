<section>
  <strong>Spring Client com RestTemplate</strong>
</section>

<section>
  <strong>1. Introdução ao Conceito de Cliente HTTP em Java</strong>

  <ul>
    <li>Um <strong>cliente HTTP</strong> é um software ou biblioteca que faz requisições a um servidor e processa respostas, usando o protocolo HTTP.</li>
    <li>Gerencia métodos (GET, POST, etc.), URL, cabeçalhos (headers) e corpo (body) de requisições, se necessário.</li>
  </ul>
</section>

<section>
  <strong>1.2 Fluxo Geral de uma Chamada HTTP</strong>

  <ul>
    <li><strong>Montagem da Requisição:</strong> Define método HTTP, URL, cabeçalhos e, opcionalmente, body.</li>
    <li><strong>Envio da Requisição:</strong> Abre conexão e envia pela rede (TCP/SSL).</li>
    <li><strong>Processamento no Servidor:</strong> O servidor verifica e retorna código HTTP (200, 404, etc.) e possivelmente um JSON/XML.</li>
    <li><strong>Recebimento da Resposta:</strong> O cliente lê código HTTP, cabeçalhos e converte body, se houver.</li>
  </ul>
</section>

<section>
  <strong>1.3 Comunicação Síncrona</strong>

  <ul>
    <li>O cliente fica em <strong>bloco de espera</strong> até receber a resposta.</li>
    <li>É útil quando precisamos de resposta imediata para continuar o fluxo.</li>
    <li>Pode causar bloqueio excessivo em cenários de alta concorrência.</li>
  </ul>
</section>

<section>
  <strong>2. Por que Fazer Chamadas HTTP de um Backend a Outro Backend?</strong>

  <ul>
    <li><strong>Integração de Serviços</strong>: Microsserviços trocando dados.</li>
    <li><strong>Consumir APIs Externas</strong>: Pagamentos, geolocalização, etc.</li>
    <li><strong>Escalabilidade e Manutenção</strong>: Separar responsabilidades em serviços distintos.</li>
    <li><strong>Reuso</strong>: Mesmo serviço pode ser chamado por várias aplicações.</li>
  </ul>
</section>

<section>
  <strong>3. Apresentando o RestTemplate</strong>

  <ul>
    <li><strong>Definição:</strong> Cliente HTTP síncrono do Spring, simples de usar.</li>
    <li><strong>Por que Usar?</strong>
      <ul>
        <li>Abordagem imperativa.</li>
        <li>Configuração rápida.</li>
        <li>Conversores de mensagem para JSON, XML etc.</li>
      </ul>
    </li>
    <li><strong>Métodos Principais:</strong>
      <ul>
        <li>getForObject(url, Class&lt;T&gt;)</li>
        <li>postForObject(url, request, Class&lt;T&gt;)</li>
        <li>exchange(url, HttpMethod, HttpEntity, Class&lt;T&gt;)</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>4. Boas Práticas e Cuidados ao Fazer Chamadas HTTP</strong>

  <ul>
    <li><strong>Definição de Timeouts:</strong> Connection e Read Timeout para evitar bloqueios.</li>
    <li><strong>Tratamento de Erros (4xx, 5xx):</strong> Usar HttpClientErrorException, HttpServerErrorException e logs claros.</li>
    <li><strong>Código Limpo:</strong> Centralizar lógica de chamadas em um serviço especializado.</li>
    <li><strong>Evitar Chamadas Excessivas / Caching:</strong> Cache ou circuit breaker para reduzir uso repetido de APIs.</li>
    <li><strong>SSL/TLS e Segurança:</strong> Garantir certificados válidos e protocolos modernos.</li>
  </ul>
</section>

<section>
  <strong>5. Requisitos Não Funcionais</strong>

  <ul>
    <li><strong>Desempenho e Escalabilidade:</strong> Cada requisição externa adiciona latência.</li>
    <li><strong>Observabilidade:</strong> Logs detalhados e métricas (sucesso, falha, latência).</li>
    <li><strong>Resiliência:</strong> Retries, fallback e circuit breaker para cenários de indisponibilidade.</li>
  </ul>
</section>

<section>
  <strong>6. Conclusão</strong>

  <ul>
    <li><strong>RestTemplate:</strong> Uma solução síncrona e simples no ecossistema Spring.</li>
    <li>Configurações indispensáveis: Timeouts, tratamento de erros, logs.</li>
    <li>Requisitos não funcionais: Desempenho, segurança, observabilidade.</li>
    <li><strong>Próximos Passos:</strong> Criar um projeto Spring Boot, consumir APIs públicas e testar cenários de erro.</li>
  </ul>
</section>