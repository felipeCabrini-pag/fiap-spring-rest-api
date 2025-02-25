# **Exercício Prático: Sistema de Gerenciamento de Hotéis com Spring Boot 3 e Thymeleaf**

## **Objetivo**
Desenvolver uma aplicação web simples utilizando Spring Boot 3 e Thymeleaf para gerenciar hotéis e suas reservas. O projeto deve permitir o cadastro de hotéis, a reserva de quartos e a visualização das reservas realizadas.

---

## 📝 **Descrição do Projeto**
Você deverá implementar um sistema de gerenciamento de hotéis com as seguintes funcionalidades:
- ✅ Cadastro de hotéis com nome, endereço e telefone.
- ✅ Visualização dos detalhes de um hotel e seus quartos disponíveis.
- ✅ Reserva de quartos com nome do cliente e período da reserva.
- ✅ Listagem das reservas realizadas com a opção de cancelamento.

---

## ✅ **Requisitos Funcionais**

### **1. Home (Página Inicial) - `/home`**
- Exibe a lista de hotéis cadastrados com nome, localização e número de quartos disponíveis.
- Possui botão para cadastrar um novo hotel.
- Cada hotel possui um link para exibir seus detalhes.

### **2. Cadastro/Edição de Hotel - `/hotels/new` e `/hotels/{id}/edit`**
- Formulário para cadastrar ou editar um hotel.
- Campos: Nome, endereço e telefone.
- Botão para salvar o hotel.

### **3. Detalhes do Hotel - `/hotels/{id}`**
- Exibe as informações do hotel selecionado.
- Lista os quartos disponíveis, exibindo número, tipo e preço.
- Botão para reservar um quarto.

### **4. Realização de Reserva - `/reservations/new`**
- Formulário para realizar uma reserva.
- Campos: Nome do cliente, data de entrada e data de saída.
- Botão para confirmar a reserva.

### **5. Listagem de Reservas - `/reservations`**
- Exibe a lista de todas as reservas realizadas.
- Campos: Nome do cliente, nome do hotel, número do quarto e período da reserva.
- Botão para cancelar uma reserva.

---

## 💡 **Regras de Negócio**
- O sistema deve validar os campos obrigatórios.
- Não é permitido realizar reservas em quartos já ocupados para o mesmo período.
- Ao reservar um quarto, o sistema deve atualizar a disponibilidade desse quarto.
- O cancelamento de uma reserva deve liberar o quarto para novas reservas.

---

## 🛠️ **Tecnologias Utilizadas**
- **Backend:** Spring Boot 3 (Spring Web, Spring Data JPA, Thymeleaf)
- **Frontend:** Thymeleaf (para renderizar as páginas HTML)
- **Banco de Dados:** H2 Database (em memória)

---

## 🌱 **Entrega**
- 📁 Código-fonte do projeto em um repositório Git.
- 📝 Documentação básica explicando como executar o projeto localmente.
- 🖼️ Prints das telas principais (Home, Cadastro de Hotel, Detalhes do Hotel, Realização de Reserva e Listagem de Reservas).

---

## **Critérios de Avaliação**
- ✅ Clareza do código e boas práticas de programação.
- ✅ Implementação correta das funcionalidades solicitadas.
- ✅ Layout simples, porém funcional e intuitivo.
- ✅ Tratamento básico de erros (ex.: campos obrigatórios, quarto já reservado, etc.).

---

## 💪 **Desafio Extra (Opcional)**
- Implementar um sistema de autenticação simples (login do administrador para gerenciar os hotéis).

---

⚡ **Dica:** Se precisar de ajuda para começar, peça o esqueleto inicial do projeto Spring Boot com as classes principais. Boa sorte! 😊