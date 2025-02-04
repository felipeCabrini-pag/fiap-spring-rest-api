<section>
  <strong>OpenFeign: Cliente HTTP Declarativo no Ecossistema Spring</strong>
</section>

<section>
  <strong>1. Introdução ao OpenFeign</strong>

  <ul>
    <li><strong>O que é?</strong>  
      - Um cliente HTTP <em>declarativo</em> que permite definir chamadas REST por meio de interfaces Java.  
      - Constrói automaticamente a lógica de requisição (URL, método, cabeçalhos, corpo) em tempo de execução.
    </li>
    <li><strong>Por que usar?</strong>  
      - Elimina <em>boilerplate</em> (menos código manual para requisições).  
      - Integra-se facilmente com <strong>Spring Cloud</strong> (Eureka, Circuit Breaker, Load Balancer).  
      - Facilita manutenção: basta ajustar anotações e parâmetros em interfaces, sem implementar a lógica HTTP repetidamente.
    </li>
  </ul>
</section>

<section>
  <strong>2. Exemplo de Cenário de Uso</strong>

  <ul>
    <li>Uma aplicação <em>Spring Boot</em> consome dados de um <strong>serviço externo</strong> (ex.: <em>API de usuários</em>).</li>
    <li><strong>Desafio:</strong> Vários endpoints (criação, listagem, busca detalhada, atualização etc.).</li>
    <li><strong>Objetivo:</strong> Reduzir códigos repetitivos de requisição, melhor organização e facilidade na manutenção.</li>
  </ul>
</section>

<section>
  <strong>3. Conceito Declarativo</strong>

  <ul>
    <li><em>Interface Java</em> define métodos que correspondem a cada endpoint.</li>
    <li>Anotações configuram o <strong>mapeamento</strong> (GET, POST, PUT) e informações como <code>@PathVariable</code>, <code>@RequestParam</code>, <code>@RequestBody</code>, entre outras.</li>
    <li><strong>OpenFeign</strong> gera uma implementação dinâmica que “preenche” esses detalhes de requisição HTTP.</li>
  </ul>
</section>

<section>
  <strong>4. Configuração Básica no Spring Boot</strong>

  <ul>
    <li><strong>Passo 1: Adicionar Dependência</strong>
      <pre><code>
&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-openfeign&lt;/artifactId&gt;
  &lt;version&gt;4.0.0&lt;/version&gt; &lt;!-- Ajustar se necessário --&gt;
&lt;/dependency&gt;
      </code></pre>
    </li>
    <li><strong>Passo 2: Habilitar o Feign</strong>
      <pre><code>
&#64;SpringBootApplication
&#64;EnableFeignClients
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
      </code></pre>
    </li>
    <li><strong>Resultado:</strong> O Spring fará o escaneamento de todas as interfaces com <code>@FeignClient</code> e criará objetos para injeção.</li>
  </ul>
</section>

<section>
  <strong>5. Criando uma Interface Feign</strong>

  <p><em>Bom Exemplo</em>: Separar bem a <code>url</code>, usar DTOs específicos e mapear métodos com clareza.</p>

  <pre><code>
&#64;FeignClient(
    name = "userClient",
    url = "https://api.exemplo.com/users"
)
public interface UserClient {

    &#64;GetMapping("/{id}")
    UserDTO getUser(@PathVariable("id") Long id);

    &#64;GetMapping
    List&lt;UserDTO&gt; listUsers(@RequestParam("active") boolean active);

    &#64;PostMapping
    UserDTO createUser(@RequestBody CreateUserRequest request);
}
  </code></pre>

  <p><em>Mau Exemplo</em>: Informar <code>Object</code> ao invés de DTO, caminhos “hardcoded” misturados, sem parâmetros corretamente anotados.</p>

  <pre><code>
&#64;FeignClient(name = "userClient", url = "https://api.exemplo.com")
public interface BadUserClient {

    &#64;GetMapping("/users/{id}")
    Object getUser(Long id);

    &#64;GetMapping("/users")
    Object listUsers(boolean active);

    &#64;PostMapping("/users")
    Object createUser(Object req);
}
  </code></pre>
</section>

<section>
  <strong>6. Consumindo a Interface Feign</strong>

  <pre><code>
&#64;Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserDTO fetchUserById(Long userId) {
        return userClient.getUser(userId);
    }

    public List&lt;UserDTO&gt; fetchActiveUsers() {
        return userClient.listUsers(true);
    }

    public UserDTO registerUser(CreateUserRequest req) {
        return userClient.createUser(req);
    }
}
  </code></pre>

  <ul>
    <li>A chamada a <code>userClient.getUser(userId)</code> faz toda a requisição HTTP automaticamente.</li>
    <li>Detalhes de URL, cabeçalhos, serialização JSON etc. ficam ocultos na implementação gerada pelo Feign.</li>
  </ul>
</section>

<section>
  <strong>7. Tratamento de Erros e Customizações</strong>

  <ul>
    <li><strong>FeignException</strong>:
      <ul>
        <li><code>FeignException.BadRequest</code> (400), <code>FeignException.Unauthorized</code> (401), etc.</li>
        <li>Permite capturar <em>status code</em> e <em>body</em> da resposta de erro.</li>
      </ul>
    </li>
    <li><strong>ErrorDecoder</strong>:
      <pre><code>
public class MyErrorDecoder implements ErrorDecoder {
    &#64;Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new NotFoundException("Recurso não encontrado");
        }
        return new FeignException.InternalServerError("Erro inesperado", 
                 response.request(), null, response);
    }
}
      </code></pre>
    </li>
    <li><strong>Configuração no Spring</strong>:
      <pre><code>
&#64;Configuration
public class FeignConfig {

    &#64;Bean
    public ErrorDecoder errorDecoder() {
        return new MyErrorDecoder();
    }
}
      </code></pre>
      <p>Associar a config via <code>@FeignClient(configuration = FeignConfig.class)</code> ou globalmente.</p>
    </li>
  </ul>
</section>

<section>
  <strong>8. Integração com Spring Cloud</strong>

  <ul>
    <li><strong>Service Discovery (Eureka)</strong>:
      <ul>
        <li>Em vez de <code>url</code>, define-se <code>@FeignClient(name = "user-service")</code>.</li>
        <li>O <em>Load Balancer</em> encontra a instância real (ex.: <code>http://user-service:8080</code>).</li>
      </ul>
    </li>
    <li><strong>Circuit Breaker</strong>:
      <ul>
        <li>Usar Resilience4j/Hystrix: <code>@CircuitBreaker</code> para fallback.</li>
        <li>Exemplo:
          <pre><code>
&#64;CircuitBreaker(name = "userClientBreaker", fallbackMethod = "fallbackUser")
public UserDTO fetchUser(Long id) {
  return userClient.getUser(id);
}
          </code></pre>
        </li>
      </ul>
    </li>
    <li><strong>Autoconfiguração Simplificada</strong>:
      <ul>
        <li>Muitas configurações (timeouts, interceptors) podem ser definidas em <em>application.yml</em>.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>9. Boas Práticas no Uso do Feign</strong>

  <ul>
    <li><strong>Definir DTOs Claros</strong>:
      <ul>
        <li>Evitar usar <code>Object</code> ou <code>Map</code> em produção; preferir classes POJO.</li>
      </ul>
    </li>
    <li><strong>Modularizar Interfaces</strong>:
      <ul>
        <li>Para grandes APIs, divida-as em interfaces menores (ex.: <code>UserClient</code>, <code>OrderClient</code>), mantendo coesão.</li>
      </ul>
    </li>
    <li><strong>Log e Métricas</strong>:
      <ul>
        <li>Habilitar logs de Feign (ex.: <code>feignLoggerLevel=FULL</code>) para depuração.</li>
        <li>Integrar Micrometer para coletar latência, taxa de erro, etc.</li>
      </ul>
    </li>
    <li><strong>Segurança</strong>:
      <ul>
        <li>Adicionar <code>RequestInterceptors</code> para inserir tokens JWT, cabeçalhos de autenticação, etc.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>Exemplo Prático: Interceptor para JWT</strong>

  <pre><code>
&#64;Configuration
public class FeignSecurityConfig {

    &#64;Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -&gt; {
            String token = "Bearer " + retrieveToken();
            requestTemplate.header("Authorization", token);
        };
    }
}

// Associar no FeignClient:
// &#64;FeignClient(name = "userClient", configuration = FeignSecurityConfig.class)
  </code></pre>
</section>

<section>
  <strong>10. Testando Feign Client</strong>

  <ul>
    <li><strong>Testes de Integração</strong>:
      <ul>
        <li>Levantar um <em>WireMock</em> ou <em>MockWebServer</em> local, simulando a API externa.</li>
        <li>Verificar se o Feign client lida corretamente com <code>200</code>, <code>404</code>, <code>500</code> etc.</li>
      </ul>
    </li>
    <li><strong>Testes Unitários</strong>:
      <ul>
        <li>Podem usar <code>@MockBean</code> no <code>UserClient</code> e verificar comportamento no <code>UserService</code>.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>Bom vs. Mau Exemplo em Testes Feign</strong>

  <p><em>Mau Exemplo</em>: Não testar cenários de erro, sem uso de mocks.</p>
  <pre><code>
// Mau: Confia 100% que a API externa sempre retorna 200
@Test
void testSuccessOnly() {
    assertNotNull(userClient.getUser(1L));
}
  </code></pre>

  <p><em>Bom Exemplo</em>: Usa WireMock para simular respostas diferentes (200, 404, 500) e verificar o tratamento de erro.</p>
  <pre><code>
&#64;SpringBootTest
&#64;AutoConfigureWireMock(port = 0)
class UserClientTest {

    &#64;Autowired
    private UserClient userClient;

    @Test
    void testGetUserReturnsOk() {
        stubFor(get(urlEqualTo("/users/1"))
          .willReturn(aResponse().withStatus(200)
                                 .withHeader("Content-Type", "application/json")
                                 .withBody("{\"id\":1,\"name\":\"John\"}")));

        UserDTO user = userClient.getUser(1L);
        assertEquals("John", user.getName());
    }

    @Test
    void testGetUserNotFound() {
        stubFor(get(urlEqualTo("/users/999"))
          .willReturn(aResponse().withStatus(404)));

        assertThrows(FeignException.NotFound.class, () -> userClient.getUser(999L));
    }
}
  </code></pre>
</section>

<section>
  <strong>11. Requisitos Não Funcionais e Observabilidade</strong>

  <ul>
    <li><strong>Logs Detalhados</strong>:  
      - Ajustar o nível de log (ex.: <code>logging.level.com.example.userClient=DEBUG</code>) para ver requisições/respostas HTTP.</li>
    <li><strong>Métricas e Tracing</strong>:  
      - Integração com <em>Micrometer</em> e <em>OpenTracing/OpenTelemetry</em> para analisar latência, throughput e possiveis gargalos.</li>
    <li><strong>Timeouts e Retries</strong>:
      - Configurar timeouts (ex.: <code>feign.client.config.default.connectTimeout=5000</code>, etc.).  
      - Usar <em>Retryer</em> do Feign (ou Circuit Breaker) para instabilidades transitórias.</li>
  </ul>
</section>

<section>
  <strong>12. Comparando OpenFeign e RestTemplate</strong>

  <ul>
    <li><strong>Estilo de Programação</strong>:
      <ul>
        <li>Feign: <em>Declarativo</em> (interfaces e anotações).</li>
        <li>RestTemplate: <em>Imperativo</em> (métodos diretos: getForObject, exchange, etc.).</li>
      </ul>
    </li>
    <li><strong>Quantidade de Código</strong>:
      <ul>
        <li>Feign tende a gerar menos código repetitivo, pois a implementação HTTP é automatizada.</li>
      </ul>
    </li>
    <li><strong>Integração Spring Cloud</strong>:
      <ul>
        <li>Feign é <em>nativamente</em> integrado ao ecossistema (discovery, circuit breaker etc.).</li>
        <li>RestTemplate requer configurações adicionais.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>13. Conclusão</strong>

  <ul>
    <li><strong>OpenFeign</strong> simplifica a construção de clientes HTTP por meio de interfaces declarativas, diminuindo <em>boilerplate</em>.</li>
    <li>Configurações como <em>ErrorDecoder</em>, <em>RequestInterceptor</em> e <em>Timeouts</em> permitem customização para cada cenário.</li>
    <li><strong>Boas Práticas</strong>:
      <ul>
        <li>Manter interfaces limpas e <em>DTOs</em> bem definidos.</li>
        <li>Tratar erros com <em>ErrorDecoder</em> e logs claros.</li>
        <li>Testar com WireMock ou <em>MockWebServer</em> para garantir robustez.</li>
      </ul>
    </li>
  </ul>
</section>