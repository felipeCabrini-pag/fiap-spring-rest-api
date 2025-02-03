<section>
  <strong>Spring Client com RestTemplate</strong>
</section>

<section>
  <strong>1. Introdução ao Conceito de Cliente HTTP em Java</strong>

  <ul>
    <li>Um <strong>cliente HTTP</strong> é um software/biblioteca que faz requisições a um servidor e processa as respostas (HTTP).</li>
    <li>Geralmente controla <em>método</em> (GET, POST etc.), <em>URL</em>, <em>headers</em> e, opcionalmente, <em>body</em>.</li>
    <li>Fundamental em integrações entre aplicações e serviços (microsserviços, APIs públicas etc.).</li>
  </ul>
</section>

<section>
  <strong>1.2 Fluxo Geral de uma Chamada HTTP</strong>
  <ul>
    <li><strong>Montagem da Requisição:</strong> Definir método (GET, POST etc.), URL e cabeçalhos (ex.: JSON).</li>
    <li><strong>Envio da Requisição:</strong> Conexão (TCP/SSL), envio de dados pela rede.</li>
    <li><strong>Processamento no Servidor:</strong> O servidor valida, acessa BD, e retorna (200, 404 etc.) e, possivelmente, JSON.</li>
    <li><strong>Recebimento da Resposta:</strong> O cliente lê <em>status code</em>, headers e body.</li>
  </ul>
</section>

<section>
  <strong>1.3 Comunicação Síncrona</strong>
  <ul>
    <li>Cliente <strong>aguarda</strong> a resposta antes de prosseguir. Bloqueia a thread atual.</li>
    <li>Simples de entender e implementar, mas pode causar lentidão se o servidor remoto estiver lento.</li>
    <li>Para alto volume de requisições, avaliar abordagens reativas ou assíncronas.</li>
  </ul>
</section>

<section>
  <strong>2. Por que Fazer Chamadas HTTP de um Backend a Outro Backend?</strong>
  <ul>
    <li><strong>Integração de Serviços</strong>: Microsserviços conversam entre si via HTTP.</li>
    <li><strong>Consumir APIs Externas</strong>: Pagamentos (PayPal), geolocalização (Google Maps), etc.</li>
    <li><strong>Escalabilidade/Manutenção</strong>: Cada serviço pode ser atualizado independentemente.</li>
    <li><strong>Reuso</strong>: Múltiplas aplicações podem consumir o mesmo serviço.</li>
  </ul>
</section>

<section>
  <strong>3. Apresentando o RestTemplate</strong>
  <ul>
    <li><strong>Definição:</strong> Cliente HTTP <em>síncrono</em> do Spring, facilitando requisições REST.</li>
    <li><strong>Por que usar?</strong>
      <ul>
        <li>Abordagem imperativa.</li>
        <li>Configuração rápida.</li>
        <li>Conversores de mensagem (JSON ↔ POJO).</li>
      </ul>
    </li>
    <li><strong>Métodos Principais:</strong>
      <ul>
        <li><code>getForObject(url, Class&lt;T&gt;)</code></li>
        <li><code>postForObject(url, request, Class&lt;T&gt;)</code></li>
        <li><code>exchange(url, HttpMethod, HttpEntity, Class&lt;T&gt;)</code></li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>Exemplo Prático: Configurando RestTemplate</strong>

  <p><em>Bom Exemplo</em> (Separação de responsabilidades, Bean configurado, timeouts):</p>

  <pre><code>
  &#64;Configuration
  public class RestTemplateConfig {

      &#64;Bean
      public RestTemplate restTemplate() {
          // Exemplo: usando HttpComponents para configurar timeouts
          HttpComponentsClientHttpRequestFactory factory =
              new HttpComponentsClientHttpRequestFactory();
          factory.setConnectTimeout(5000);
          factory.setReadTimeout(5000);

          return new RestTemplate(factory);
      }
  }
  </code></pre>

  <p><em>Mau Exemplo</em> (Instanciando RestTemplate em cada método, sem timeout, poluindo o código):</p>

  <pre><code>
// Mau Exemplo: sem separação de responsabilidades, sem timeouts
&#64;RestController
public class MyController {

    &#64;GetMapping("/test")
    public String test() {
        RestTemplate rt = new RestTemplate();
        // nenhum timeout configurado
        return rt.getForObject("https://api.exemplo.com/data", String.class);
    }
}
  </code></pre>
</section>

<section>
  <strong>Exemplo Prático: Consumindo Endpoint com exchange()</strong>

  <pre><code>
&#64;Service
public class MyService {

    private final RestTemplate restTemplate;

    public MyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getData() {
        String url = "https://api.exemplo.com/data";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity&lt;Void&gt; requestEntity = new HttpEntity&lt;&gt;(headers);

        ResponseEntity&lt;String&gt; response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // Tratar erro adequadamente
            throw new RuntimeException("Erro ao consumir API externa");
        }
    }
}
  </code></pre>
</section>

<section>
  <strong>4. Boas Práticas e Cuidados ao Fazer Chamadas HTTP</strong>

  <ul>
    <li><strong>Definição de Timeouts</strong>: Evita bloqueios indesejados. Configurar <em>connectTimeout</em> e <em>readTimeout</em>.</li>
    <li><strong>Tratar Erros (4xx, 5xx)</strong>:
      <ul>
        <li><code>HttpClientErrorException</code> para erros do cliente.</li>
        <li><code>HttpServerErrorException</code> para erros do servidor.</li>
      </ul>
    </li>
    <li><strong>Código Limpo</strong>: Centralizar lógica de chamadas HTTP em serviços, evitando duplicação.</li>
    <li><strong>Evitar Chamadas Excessivas / Caching</strong>: Armazenar resultados se for viável, usar <em>Circuit Breaker</em> para lidar com falhas repetidas.</li>
    <li><strong>SSL/TLS e Segurança</strong>: Confirmar certificados válidos, usar TLS 1.2 ou superior.</li>
  </ul>
</section>

<section>
  <strong>Exemplo Prático: Tratamento de Erros</strong>

  <pre><code>
&#64;Service
public class DataService {

    private final RestTemplate restTemplate;

    public DataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String safeGetData() {
        String url = "https://api.exemplo.com/might-fail";
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            // 4xx
            return handle4xxError(e);
        } catch (HttpServerErrorException e) {
            // 5xx
            return handle5xxError(e);
        } catch (ResourceAccessException e) {
            // Falha na conexão, timeout etc.
            throw new RuntimeException("Falha de rede: " + e.getMessage());
        }
    }

    private String handle4xxError(HttpClientErrorException e) {
        // Log e retornar algo ao chamador (pode ser nulo, ou lançar exceção custom)
        return "Erro 4xx: " + e.getStatusCode();
    }

    private String handle5xxError(HttpServerErrorException e) {
        // Log e retornar fallback
        return "Erro 5xx: " + e.getStatusCode();
    }
}
  </code></pre>
</section>

<section>
  <strong>5. Requisitos Não Funcionais</strong>
  <ul>
    <li><strong>Desempenho e Escalabilidade</strong>: Cada requisição externa adiciona latência. Avaliar volume de chamadas.</li>
    <li><strong>Observabilidade</strong>:
      <ul>
        <li>Logs detalhados com URL, tempo de resposta, status code.</li>
        <li>Métricas de sucesso/falha e latência (Micrometer, Prometheus).</li>
      </ul>
    </li>
    <li><strong>Resiliência</strong>:
      <ul>
        <li>Usar <em>Retries</em> para falhas transitórias.</li>
        <li>Fallbacks para resposta padrão quando a API estiver indisponível.</li>
        <li><em>Circuit Breaker</em> (Resilience4j) para interromper chamadas sucessivas em caso de falhas repetidas.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>Bom vs. Mau Exemplo de Organização</strong>
  <p><em>Mau Exemplo:</em> Tudo no Controller, sem tratamento de erro, sem timeouts.</p>
  <pre><code>
// Mau Exemplo
&#64;RestController
public class SimpleController {

    &#64;GetMapping("/consume")
    public String consumeApi() {
        // Instanciando dentro do método
        RestTemplate rt = new RestTemplate();
        return rt.getForObject("https://api.sem-timeout.com/data", String.class);
    }
}
  </code></pre>
</section>

<section>
  <strong>Bom Exemplo de Organização</strong>
  <pre><code>
// Bom Exemplo
&#64;Configuration
public class MyConfig {

    &#64;Bean
    public RestTemplate restTemplate() {
        // configure timeouts, interceptors, etc.
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
}

&#64;Service
public class ApiService {
    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String consumeData() {
        String url = "https://api.com/data";
        // tratamento de erros, logs etc.
        return restTemplate.getForObject(url, String.class);
    }
}

&#64;RestController
public class MyController {
    private final ApiService apiService;

    public MyController(ApiService apiService) {
        this.apiService = apiService;
    }

    &#64;GetMapping("/consume")
    public String consumeApi() {
        return apiService.consumeData();
    }
}
  </code></pre>
</section>

<section>
  <strong>6. Conclusão</strong>
  <ul>
    <li><strong>RestTemplate:</strong> Uma solução síncrona e simples no ecossistema Spring.</li>
    <li><strong>Configurações Indispensáveis:</strong> Timeouts, tratamento de erros, logs.</li>
    <li><strong>Requisitos Não Funcionais:</strong> Desempenho, segurança, observabilidade, resiliência.</li>
    <li><strong>Próximos Passos:</strong> Testar diferentes cenários (API offline, latência alta) e integrar <em>Circuit Breaker</em> ou caching para maior robustez.</li>
  </ul>
</section>