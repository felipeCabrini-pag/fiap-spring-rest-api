<section>
  <strong>Tabela Comparativa: RestTemplate vs. OpenFeign vs. Rest Client (Spring 6)</strong>
</section>

<section>
| **Aspecto**                   | **RestTemplate**                                      | **OpenFeign**                                             | **Rest Client (Spring 6)**                                 |
|------------------------------|------------------------------------------------------|-----------------------------------------------------------|------------------------------------------------------------|
| **Estilo de Programação**     | Imperativo: uso direto de métodos como `getForObject()` e `exchange()` | Declarativo: define-se uma interface com `@FeignClient` e anotações | Declarativo: usa interfaces com anotações nativas como `@HttpExchange` |
</section>

<section>
| **Dependências**              | Nativo do Spring Web, sem dependências extras        | Requer `spring-cloud-starter-openfeign`                   | Nativo do Spring 6, sem dependências externas              |
| **Síncrono vs. Reativo**      | Síncrono (bloqueante)                                | Síncrono (bloqueante) por padrão                          | Pode ser síncrono ou reativo, dependendo da configuração   |
</section>

<section>
| **Boilerplate**               | Requer mais código para configuração manual          | Menos código repetitivo: a implementação é gerada automaticamente | Menos boilerplate que WebClient puro: define endpoints de forma declarativa |
</section>

<section>
| **Integração com Spring Cloud** | Integração manual                                    | Nativa: suporta service discovery, load balancing e circuit breakers | Integração limitada: pode ser combinada manualmente com outras soluções  |
| **Configuração de Erros**      | Via `ResponseErrorHandler` e timeouts manuais        | Via `ErrorDecoder` configurável                                 | Via `onStatus` e configuração do WebClient interno          |
</section>

<section>
| **Uso de DTOs**               | Requer mapeamento manual                             | Facilita o uso de DTOs com parâmetros declarados             | Semelhante ao Feign: usa anotações para definir DTOs       |
| **Resiliência**               | Integração manual com bibliotecas                    | Nativa com circuit breakers (Resilience4j, Hystrix)          | Depende da configuração do WebClient                       |
</section>

<section>
| **Cenários de Uso**           | Projetos legados ou com chamadas pontuais            | Microsserviços no ecossistema Spring Cloud                   | Novos projetos que desejam clientes nativos declarativos   |
| **Pontos Fortes**             | Simples e amplamente conhecido                       | Menos boilerplate, fácil de integrar                         | Nativo, com suporte síncrono e reativo                     |
</section>

<section>
| **Pontos Fracos**             | Bloqueante, pode gerar sobrecarga                    | Bloqueante por padrão, depende de uma lib externa            | Relativamente novo, algumas funcionalidades ainda evoluindo |
</section>