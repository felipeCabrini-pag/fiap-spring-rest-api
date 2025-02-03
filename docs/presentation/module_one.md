## Spring Client com RestTemplate

---

### 1. Introdução ao Conceito de Cliente HTTP em Java

#### 1.1 O que é um Cliente HTTP?
- Um **cliente HTTP** é um software ou biblioteca que faz requisições a um servidor e processa respostas, usando o protocolo **HTTP**.
- Ele gerencia **métodos** (GET, POST, etc.), **URL**, **cabeçalhos** (headers) e, quando necessário, **corpo** (body) de requisições.

#### 1.2 Fluxo Geral de uma Chamada HTTP
1. **Montagem da Requisição**  
   - Define método HTTP, URL, cabeçalhos e corpo (opcional).
2. **Envio da Requisição**  
   - Abre conexão (TCP/SSL), envia a requisição pela rede.
3. **Processamento no Servidor**  
   - O servidor valida e processa, retornando código HTTP (200, 404 etc.) e possivelmente um corpo (JSON/XML).
4. **Recebimento da Resposta**  
   - O cliente lê o **código HTTP**, cabeçalhos e converte o **body** se houver.

#### 1.3 Comunicação Síncrona
- **Bloco de Espera**: O cliente aguarda a resposta antes de prosseguir.
- **Uso Comum**: Fluxos que necessitam de resposta imediata.
- **Desvantagem**: Pode bloquear muitas threads em cenários de alto tráfego.

---

### 2. Por que Fazer Chamadas HTTP de um Backend a Outro Backend?

- **Integração de Serviços (Microsserviços)**  
  - Sistemas especializados que trocam dados entre si.
- **Consumir APIs Externas**  
  - Integração com serviços de terceiros (pagamentos, mapas etc.).
- **Escalabilidade e Manutenibilidade**  
  - Cada serviço funciona de forma autônoma.
- **Reuso**  
  - Várias aplicações podem aproveitar as mesmas funcionalidades expostas.

---

### 3. Apresentando o RestTemplate

#### 3.1 Definição
- **RestTemplate**: Classe do Spring que fornece um cliente HTTP **síncrono** de fácil uso.

#### 3.2 Por que Usá-lo?
1. **Abordagem Imperativa**  
2. **Configuração Rápida**  
3. **Conversores de Mensagem** (JSON ↔ POJO, XML etc.)

#### 3.3 Métodos Principais
- `getForObject(url, Class<T>)`
- `postForObject(url, request, Class<T>)`
- `exchange(url, HttpMethod, HttpEntity, Class<T>)`

---

### 4. Boas Práticas e Cuidados ao Fazer Chamadas HTTP

#### 4.1 Definição de Timeouts
- **Connection Timeout**: tempo máximo para estabelecer conexão.
- **Read Timeout**: tempo máximo para receber dados.

#### 4.2 Tratar Erros de Forma Apropriada
- **HTTP 4xx**: `HttpClientErrorException`.
- **HTTP 5xx**: `HttpServerErrorException`.
- **Logs** e retornos de status corretos ao cliente interno.

#### 4.3 Estrutura de Código Limpa
- Centralizar chamadas HTTP em um serviço especializado.
- Evitar duplicar lógica de endpoint e tratamento de erro.

#### 4.4 Evitar Chamadas Excessivas / Caching
- Armazenar respostas em cache quando for possível.
- Usar **Circuit Breaker** (ex.: Resilience4j) para falhas repetidas.

#### 4.5 Configuração de SSL/TLS e Segurança
- Garantir certificados confiáveis.
- Evitar protocolos obsoletos (TLS 1.0, 1.1).

---

### 5. Requisitos Não Funcionais

#### 5.1 Desempenho e Escalabilidade
- Cada requisição externa gera latência.
- Verificar se o estilo síncrono atende a demanda (ou usar *reativo* se necessário).

#### 5.2 Observabilidade
- **Logs Detalhados**: registrar tempo de resposta, status code.
- **Métricas**: monitorar sucesso/falha, latência média.
- **Tracing Distribuído**: em ambientes de microsserviços (Zipkin, Jaeger).

#### 5.3 Resiliência
- **Retries**: repetir em falhas temporárias.
- **Fallback**: retornar dados default se a API estiver indisponível.
- **Circuit Breaker**: abrir o circuito para evitar sobrecarga em caso de falhas sucessivas.

---

### 6. Conclusão

1. **RestTemplate**: Uma solução síncrona, simples e amplamente utilizada no ecossistema Spring.
2. **Configurações Indispensáveis**: Timeouts, tratamento de erros, logs.
3. **Requisitos Não Funcionais**: Desempenho, segurança, observabilidade e resiliência.
4. **Próximos Passos**:  
   - Criar um projeto Spring Boot com Bean `RestTemplate`.  
   - Integrar a APIs públicas.  
   - Testar cenários de erro (timeout, indisponibilidade).  
   - Considerar uso de Circuit Breaker e caching para maior robustez.