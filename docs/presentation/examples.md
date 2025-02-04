<section>
  <strong>Exemplos Práticos: Consumindo SWAPI com Diferentes Tecnologias</strong>
  <p>Vamos chamar a API pública <code>https://swapi.dev/</code> para obter os dados do personagem de ID 1 (Luke Skywalker) usando três abordagens:</p>
</section>

<section>
  <strong>1. RestTemplate</strong>
  <p><strong>Descrição:</strong> Utiliza uma abordagem imperativa e síncrona. Ideal para aplicações onde o bloqueio da thread não é um problema ou em projetos legados.</p>
  <p><strong>Exemplo de Código:</strong></p>
  <pre><code class="java">
// Exemplo simples com RestTemplate
RestTemplate restTemplate = new RestTemplate();
String url = "https://swapi.dev/api/people/1/";
Map&lt;String, Object&gt; response = restTemplate.getForObject(url, Map.class);
System.out.println("Nome: " + response.get("name"));
  </code></pre>
  <p><strong>Bom Exemplo:</strong> Configurar o RestTemplate como um bean centralizado com timeouts e tratamento de erros.</p>
  <pre><code class="java">
&#64;Configuration
public class RestTemplateConfig {
    &#64;Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos para conectar
        factory.setReadTimeout(5000);    // 5 segundos para ler dados
        return new RestTemplate(factory);
    }
}

&#64;Service
public class SwapiRestTemplateService {
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://swapi.dev/api";

    public SwapiRestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map&lt;String, Object&gt; getPerson(int id) {
        String url = BASE_URL + "/people/" + id + "/";
        return restTemplate.getForObject(url, Map.class);
    }
}
  </code></pre>
  <p><strong>Mau Exemplo:</strong> Instanciar RestTemplate diretamente dentro do método, sem configuração ou tratamento de erros.</p>
  <pre><code class="java">
// Mau exemplo: instanciando RestTemplate diretamente sem timeouts ou tratamento centralizado
&#64;RestController
public class SimpleController {
    &#64;GetMapping("/test")
    public String test() {
        RestTemplate rt = new RestTemplate();
        return rt.getForObject("https://swapi.dev/api/people/1/", String.class);
    }
}
  </code></pre>
</section>

<section>
  <strong>2. OpenFeign</strong>
  <p><strong>Descrição:</strong> Abordagem declarativa que reduz boilerplate. Define interfaces com anotações para mapear endpoints, e o Spring Cloud gera a implementação automaticamente.</p>
  <p><strong>Exemplo de Interface Feign:</strong></p>
  <pre><code class="java">
&#64;FeignClient(name = "swapiClient", url = "https://swapi.dev/api")
public interface SwapiFeignClient {
    &#64;GetMapping("/people/{id}/")
    Map&lt;String, Object&gt; getPerson(@PathVariable("id") int id);
}
  </code></pre>
  <p><strong>Exemplo de Serviço que Consome a Interface:</strong></p>
  <pre><code class="java">
&#64;Service
public class SwapiFeignService {
    private final SwapiFeignClient swapiFeignClient;

    public SwapiFeignService(SwapiFeignClient swapiFeignClient) {
         this.swapiFeignClient = swapiFeignClient;
    }

    public Map&lt;String, Object&gt; getPerson(int id) {
         return swapiFeignClient.getPerson(id);
    }
}
  </code></pre>
  <p><strong>Bom Exemplo:</strong> Uso de DTOs específicos e tratamento adequado de erros com <code>ErrorDecoder</code> (não mostrado aqui para brevidade).</p>
  <p><strong>Mau Exemplo:</strong> Usar tipos genéricos (como <code>Object</code>) e não definir parâmetros corretamente.</p>
</section>

<section>
  <strong>3. Rest Client Declarativo (Spring 6) com Java 21</strong>
  <p><strong>Descrição:</strong> Nova abordagem nativa do Spring 6 para criar clientes HTTP declarativos sem dependências externas. Utiliza anotações como <code>@HttpExchange</code> e integra internamente com <code>WebClient</code>.</p>
  <p><strong>Exemplo de Interface Rest Client:</strong></p>
  <pre><code class="java">
&#64;HttpExchange("/people")
public interface SwapiRestClient {
    &#64;GetExchange("/{id}/")
    Map&lt;String, Object&gt; getPerson(@PathVariable("id") int id);
}
  </code></pre>
  <p><strong>Exemplo de Serviço que Consome o Rest Client:</strong></p>
  <pre><code class="java">
&#64;Service
public class SwapiRestClientService {
    private final SwapiRestClient swapiRestClient;

    public SwapiRestClientService(SwapiRestClient swapiRestClient) {
         this.swapiRestClient = swapiRestClient;
    }

    public Map&lt;String, Object&gt; getPerson(int id) {
         return swapiRestClient.getPerson(id);
    }
}
  </code></pre>
  <p><strong>Observação:</strong> Em Java 21, se o método for declarado de forma imperativa (retornando <code>Map&lt;String, Object&gt;</code> diretamente), as chamadas podem bloquear threads. Entretanto, usando <em>virtual threads</em> você pode minimizar o impacto do bloqueio.</p>
  <p><strong>Mau Exemplo:</strong> Não utilizar as anotações corretamente, por exemplo:</p>
  <pre><code class="java">
&#64;HttpExchange("/people")
public interface BadSwapiClient {
    // Falta de @PathVariable e de definição clara de método
    &#64;GetExchange
    Object getPerson(int id);
}
  </code></pre>
</section>

<section>
  <strong>Conclusão dos Exemplos</strong>
  <ul>
    <li><strong>RestTemplate</strong>: Bom para projetos legados e chamadas simples, mas requer mais configuração manual.</li>
    <li><strong>OpenFeign</strong>: Oferece uma abordagem declarativa e menos boilerplate, ideal para microsserviços no ecossistema Spring Cloud.</li>
    <li><strong>Rest Client (Spring 6)</strong>: Abordagem nativa moderna que unifica a simplicidade do declarativo com o poder do WebClient, sendo uma opção promissora para novos projetos.</li>
  </ul>
  <p>Esses exemplos demonstram como consumir a API SWAPI de forma consistente e organizada, cada um com suas vantagens e particularidades.</p>
</section>