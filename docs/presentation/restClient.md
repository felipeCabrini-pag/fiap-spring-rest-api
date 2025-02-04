<section>
  <strong>Rest Client Declarativo (Spring 6) com Java 21</strong>
</section>

<section>
  <strong>1. Introdução ao Novo Rest Client no Spring 6</strong>

  <ul>
    <li><strong>O que é?</strong>
      - Novo recurso do Spring 6 (e Spring Boot 3) que permite criar <em>interfaces HTTP</em> usando anotações como <code>@HttpExchange</code>, <code>@GetExchange</code>, <code>@PostExchange</code>.
    </li>
    <li><strong>Diferença em Relação ao OpenFeign</strong>
      - É uma solução <em>nativa</em> do Spring, sem dependência extra de Feign.  
      - Usa internamente o <strong>WebClient</strong> para fazer as requisições.
    </li>
  </ul>
</section>

<section>
  <strong>2. Cenário de Uso</strong>

  <ul>
    <li>Precisamos de um <strong>cliente HTTP declarativo</strong> para consumir APIs.</li>
    <li>Queremos <strong>menos configuração</strong> e sem dependências de terceiros, aproveitando o ecossistema do Spring 6.</li>
    <li><strong>Java 21</strong>: Podemos combinar esse approach com <em>virtual threads</em> se parte do código for imperativo ou se quisermos experimentá-las para handling de blocos de I/O.</li>
  </ul>
</section>

<section>
  <strong>3. Configuração Básica</strong>

  <ul>
    <li><strong>Dependências</strong>: 
      - <code>spring-boot-starter-webflux</code> ou <code>spring-boot-starter-web</code> (se quiser o WebClient disponível).
      - O suporte a <code>@HttpExchange</code> já vem no Spring Framework 6 / Boot 3.
    </li>
    <li><strong>Habilitando a Interface HTTP</strong>:
      <pre><code>
&#64;SpringBootApplication
public class MyApplication {
   public static void main(String[] args) {
       SpringApplication.run(MyApplication.class, args);
   }
}
      </code></pre>
      <p>Não é necessária uma anotação extra como <code>@EnableFeignClients</code>; o Spring 6 detecta as interfaces <code>@HttpExchange</code> se configuradas corretamente.</p>
    </li>
  </ul>
</section>

<section>
  <strong>4. Criando uma Interface Rest Client</strong>

  <p><em>Exemplo:</em> Consumir uma API de <code>/products</code>.</p>
  <pre><code>
&#64;HttpExchange("/products")
public interface ProductClient {

    &#64;GetExchange("/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

    &#64;GetExchange
    List&lt;ProductDTO&gt; listProducts(@RequestParam("category") String category);

    &#64;PostExchange
    ProductDTO createProduct(@RequestBody ProductRequest request);
}
  </code></pre>

  <ul>
    <li>As anotações <code>@GetExchange</code>, <code>@PostExchange</code> definem o <strong>método HTTP</strong> e mapeamento de variáveis de rota, query params e corpo.</li>
    <li><code>@HttpExchange("/products")</code> define o path base.</li>
  </ul>
</section>

<section>
  <strong>5. Injetando a Interface e Fazendo Chamadas</strong>

  <pre><code>
&#64;Service
public class ProductService {

    // O Spring 6 gera um proxy dessa interface
    private final ProductClient productClient;

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public ProductDTO findProduct(Long id) {
        return productClient.getProductById(id);
    }

    public List&lt;ProductDTO&gt; findProductsByCategory(String cat) {
        return productClient.listProducts(cat);
    }

    public ProductDTO addProduct(ProductRequest request) {
        return productClient.createProduct(request);
    }
}
  </code></pre>

  <ul>
    <li>Basta injetar a interface no construtor; o Spring a implementa usando <strong>WebClient</strong> internamente.</li>
    <li><code>productClient.getProductById(id)</code> faz a requisição HTTP <em>síncrona ou reativa</em> (dependendo da configuração do subjacente) e retorna um objeto Java.</li>
  </ul>
</section>

<section>
  <strong>6. Configurações Avançadas</strong>

  <ul>
    <li><strong>Base URL</strong>:
      <ul>
        <li>No momento, o <code>@HttpExchange</code> não tem um parâmetro para <em>baseUrl</em> integrado como no Feign. É comum configurar isso via <code>WebClient.Builder</code>.</li>
        <li>Podemos usar <em>component scanning</em> e beans específicos para cada API.</li>
      </ul>
    </li>
    <li><strong>HTTP Client Custom</strong>:
      <ul>
        <li>Podemos personalizar timeouts e conexões usando <code>ReactorClientHttpConnector</code> (Netty) ou outro conector.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>7. Maneiras de Habilitar o Rest Client no Spring Boot 3</strong>

  <ul>
    <li><strong>Por Conveção</strong> (Scan de Interfaces):
      <ul>
        <li>Colocar interfaces no mesmo pacote ou abaixo do <code>@SpringBootApplication</code>.</li>
      </ul>
    </li>
    <li><strong>Via Bean Manual</strong>:
      <pre><code>
@Configuration
public class ClientConfig {

    &#64;Bean
    ProductClient productClient(WebClient.Builder builder) {
        WebClient wc = builder
           .baseUrl("https://api.exemplo.com")
           .build();
        // Conversão da interface para proxy
        return HttpServiceProxyFactory.builder()
           .clientAdapter(WebClientAdapter.forClient(wc))
           .build()
           .createClient(ProductClient.class);
    }
}
      </code></pre>
      <p>Assim controlamos <em>exatamente</em> o baseUrl e as configurações do <code>WebClient</code>.</p>
    </li>
  </ul>
</section>

<section>
  <strong>8. Integração com Java 21 (Virtual Threads)</strong>

  <ul>
    <li><strong>Modo Bloqueante vs. Reativo</strong>:
      <ul>
        <li>Por padrão, esse client funciona de modo reativo (usando <code>Mono</code>, <code>Flux</code>) ou imperativo (retornando objetos diretos). Depende de como você declara o método (ex.: <code>ProductDTO</code> vs. <code>Mono&lt;ProductDTO&gt;</code>).</li>
      </ul>
    </li>
    <li><strong>Virtual Threads</strong>:
      <ul>
        <li>Se optar por assinaturas <strong>imperativas</strong> (retornando <code>ProductDTO</code> diretamente), as chamadas podem bloquear threads. Com Java 21 e virtual threads, esse bloqueio pesa menos.</li>
        <li>Exemplo:
          <pre><code>
// Java 21: executando no Virtual Thread
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
executor.submit(() -&gt; {
    ProductDTO p = productClient.getProductById(123L);
    System.out.println(p);
});
          </code></pre>
        </li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>9. Tratamento de Erros</strong>

  <ul>
    <li><strong>Exceções</strong>:
      <ul>
        <li>Se a resposta vier com 4xx ou 5xx, o proxy do Rest Client pode lançar exceções reativas do <em>WebClient</em> ou <code>WebClientResponseException</code> em modo imperativo.</li>
      </ul>
    </li>
    <li><strong>Manejo Global</strong>:
      <ul>
        <li>Ainda não há um <em>ErrorDecoder</em> nativo como Feign, mas podemos interceptar chamando <code>.filter(...)</code> no <code>WebClient</code> ou adicionando lógicas personalizadas.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>10. Exemplo Prático de Configuração Manual</strong>

  <pre><code>
&#64;Configuration
public class ProductClientConfig {

    &#64;Bean
    public WebClient productWebClient() {
        // Configura baseUrl, timeouts, etc.
        return WebClient.builder()
            .baseUrl("https://api.exemplo.com")
            .build();
    }

    &#64;Bean
    public ProductClient productClient(WebClient productWebClient) {
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(productWebClient))
            .build();

        return proxyFactory.createClient(ProductClient.class);
    }
}
  </code></pre>

  <ul>
    <li>Assim, criamos <code>ProductClient</code> como Bean para ser injetado onde precisarmos (por ex. <code>@Service</code>).</li>
    <li>A manipulação de timeouts/erros seria feita na configuração do <code>productWebClient</code>.</li>
  </ul>
</section>

<section>
  <strong>11. Bom vs. Mau Exemplo de Uso</strong>

  <p><em>Mau Exemplo</em>: Declarar interface sem anotações corretas, usar objetos genéricos, sem <code>@PathVariable</code> ou <code>@RequestBody</code> adequados.</p>

  <pre><code>
&#64;HttpExchange("/products")
public interface BadProductClient {

    &#64;GetExchange
    Object getStuff(Long id); // Falta @PathVariable, sem tipo claro

    &#64;PostExchange
    Object createStuff(Object req); // Não define @RequestBody
}
  </code></pre>

  <p><em>Bom Exemplo</em>: Parametrizar corretamente as requisições, usar DTOs tipados.</p>

  <pre><code>
&#64;HttpExchange("/products")
public interface GoodProductClient {

    &#64;GetExchange("/{id}")
    ProductDTO getProduct(@PathVariable("id") Long productId);

    &#64;PostExchange
    ProductDTO createProduct(@RequestBody ProductRequest req);
}
  </code></pre>
</section>

<section>
  <strong>12. Observabilidade e Resiliência</strong>

  <ul>
    <li><strong>Logs e Métricas</strong>:
      <ul>
        <li>Podemos configurar <em>WebClient</em> para logs detalhados, e integrar <em>Micrometer</em> para métricas de latência e taxa de erro.</li>
      </ul>
    </li>
    <li><strong>Circuit Breaker</strong> (Resilience4j):
      <ul>
        <li>Anotar métodos do service que chamam <code>productClient</code> com <code>@CircuitBreaker</code>, definindo fallback em caso de falhas repetidas.</li>
        <li>Importante em produção para evitar sobrecarga de um serviço externo instável.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>13. Comparação com Feign e WebClient Puro</strong>

  <ul>
    <li><strong>Feign</strong>:
      <ul>
        <li>Cliente HTTP declarativo, mas depende de uma lib externa <em>(OpenFeign)</em>.</li>
        <li>Mais maduro em configurações como <em>ErrorDecoder</em>, interceptors etc.</li>
      </ul>
    </li>
    <li><strong>WebClient Puro</strong>:
      <ul>
        <li>Flexível e reativo, mas imperativo repetitivo para cada endpoint.</li>
      </ul>
    </li>
    <li><strong>Rest Client Declarativo (Spring 6)</strong>:
      <ul>
        <li>Solução nativa do Spring, estilo Feign, mas usando <em>WebClient</em> internamente.</li>
        <li>Ainda está evoluindo. É simples para casos básicos, mas recursos avançados estão em desenvolvimento.</li>
      </ul>
    </li>
  </ul>
</section>

<section>
  <strong>14. Conclusão</strong>

  <ul>
    <li><strong>Rest Client Declarativo (Spring 6)</strong>:
      <ul>
        <li>Permite interfaces <code>@HttpExchange</code> sem dependência do Feign.</li>
        <li>Utiliza <em>WebClient</em> para realizar as requisições, combinando melhor com arquiteturas reativas.</li>
      </ul>
    </li>
    <li><strong>Vantagens</strong>:
      <ul>
        <li><em>Menos boilerplate</em> que <em>WebClient</em> puro (chamadas encapsuladas na interface).</li>
        <li>Nativo do Spring 6, com suporte oficial e sem libs adicionais.</li>
      </ul>
    </li>
    <li><strong>Boas Práticas</strong>:
      <ul>
        <li>Definir DTOs e anotações de forma clara <code>(@PathVariable, @RequestParam, @RequestBody)</code>.</li>
        <li>Configurar <em>WebClient</em> adequadamente (timeouts, SSL, logs).</li>
        <li>Manter <em>observabilidade</em> e <em>resiliência</em> (Circuit Breaker, Retry, logs, métricas).</li>
      </ul>
    </li>
  </ul>
</section>