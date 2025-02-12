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
</section>

<section>
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
    <li><strong>Passo 1: Adicionar Dependência</strong></li>
  </ul>
  <pre><code>
&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-openfeign&lt;/artifactId&gt;
  &lt;version&gt;4.0.0&lt;/version&gt; &lt;!-- Ajustar se necessário --&gt;
&lt;/dependency&gt;
  </code></pre>
</section>

<section>
  <ul>
    <li><strong>Passo 2: Habilitar o Feign</strong></li>
  </ul>
  <pre><code>
&#64;SpringBootApplication
&#64;EnableFeignClients
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
  </code></pre>
  <ul>
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
</section>

<section>
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
</section>

<section>
  <ul>
    <li>A chamada a <code>userClient.getUser(userId)</code> faz toda a requisição HTTP automaticamente.</li>
    <li>Detalhes de URL, cabeçalhos, serialização JSON etc. ficam ocultos na implementação gerada pelo Feign.</li>
  </ul>
</section>

<section>
  <strong>7. Tratamento de Erros e Customizações</strong>
</section>

<section>
  <ul>
    <li><strong>FeignException:</strong>
      <ul>
        <li><code>FeignException.BadRequest</code> (400), <code>FeignException.Unauthorized</code> (401), etc.</li>
        <li>Permite capturar <em>status code</em> e <em>body</em> da resposta de erro.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <ul>
    <li><strong>ErrorDecoder:</strong></li>
  </ul>
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
</section>

<section>
  <ul>
    <li><strong>Configuração no Spring:</strong></li>
  </ul>
  <pre><code>
&#64;Configuration
public class FeignConfig {

    &#64;Bean
    public ErrorDecoder errorDecoder() {
        return new MyErrorDecoder();
    }
}
  </code></pre>
</section>

---

### **Principais ajustes:**
- Dividi as seções extensas em várias menores, cada uma com foco em uma parte específica.
- Mantive blocos de código e explicações de maneira organizada para uma melhor apresentação. 
- Agora, o conteúdo está ajustado para garantir a legibilidade ideal no **reveal.js**.