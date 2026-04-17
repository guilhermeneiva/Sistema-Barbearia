# Sistema Barbearia — API REST

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" />
  <img src="https://img.shields.io/badge/JWT-Auth0-000000?style=for-the-badge&logo=jsonwebtokens" />
  <img src="https://img.shields.io/badge/status-em%20desenvolvimento-yellow?style=for-the-badge" />
</p>

> API REST completa para gerenciamento de barbearia, com controle de agendamentos, clientes, barbeiros e serviços. O sistema implementa regras de negócio rigorosas para proteger a agenda dos profissionais e garantir a integridade dos dados.

---

## Sobre o Projeto

O **Sistema Barbearia** é uma API REST desenvolvida para gerenciar as operações do dia a dia de uma barbearia. O objetivo principal foi ir além de um simples CRUD, construindo uma aplicação com inteligência de negócio real — validando conflitos de horário, horários de funcionamento, controle de cancelamentos e um sistema de autenticação baseado em papéis (RBAC).

---

## Tecnologias

| Tecnologia | Finalidade |
|---|---|
| **Java 21** | Linguagem principal |
| **Spring Boot 4** | Framework base |
| **Spring Data JPA** | Persistência e ORM |
| **Spring Security** | Autenticação e autorização |
| **PostgreSQL 15** | Banco de dados relacional |
| **JWT (Auth0)** | Tokens de autenticação stateless |
| **MapStruct** | Mapeamento de entidades para DTOs |
| **Lombok** | Redução de boilerplate |
| **Bean Validation** | Validação de entrada de dados |
| **Springdoc OpenAPI** | Documentação automática (Swagger UI) |
| **Docker Compose** | Orquestração do ambiente de desenvolvimento |

---

## Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
Controller  →  Service  →  Repository  →  Banco de Dados
     ↑              ↑
    DTOs         Entities
  (Request/     (Mapeadas
  Response)    pelo MapStruct)
```

- **Controllers**: Recebem as requisições HTTP, validam a entrada com `@Valid` e delegam para os Services.
- **Services**: Contêm toda a lógica de negócio e regras de domínio.
- **Repositories**: Interfaces JPA para acesso ao banco, com queries customizadas via `@Query` onde necessário.
- **DTOs**: Objetos de transferência de dados para desacoplar a API das entidades de domínio.
- **Mappers**: Conversão automática entre entidades e DTOs via MapStruct.
- **GlobalExceptionHandler**: Tratamento centralizado de erros com respostas padronizadas.

---

## Regras de Negócio

Este projeto implementa regras de negócio que vão além de um CRUD básico:

### Conflito de Agendamento
O sistema impede que um mesmo barbeiro tenha dois agendamentos sobrepostos. A lógica usa intersecção de intervalos de tempo:

```
A_inicio < B_fim  AND  A_fim > B_inicio
```

Isso garante que qualquer sobreposição — parcial ou total — seja detectada e bloqueada.

### Horário de Funcionamento
Agendamentos só são aceitos dentro do horário comercial da barbearia:
- **Dias**: Segunda a Sábado (Domingos bloqueados automaticamente)
- **Horário**: 08:00 às 17:30

### Cancelamento com Antecedência Mínima
O cancelamento de um agendamento só é permitido com **no mínimo 2 horas de antecedência**, evitando prejuízos por desistências de última hora. Regras adicionais:
- Não é possível cancelar um agendamento já cancelado.
- Não é possível cancelar agendamentos passados.
- O cliente só pode cancelar seus próprios agendamentos (verificação de identidade via JWT).

### Controle de Unicidade
- CPF único por barbeiro e por cliente.
- E-mail único para todos os usuários do sistema.
- Nome único por serviço.

### Autorização Baseada em Papéis (RBAC)
O sistema possui três papéis com permissões distintas: `ADMIN`, `BARBEIRO` e `CLIENTE`.

---

## Endpoints

### Autenticação (`/auth`)

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `POST` | `/auth/login` | Público | Realiza login e retorna um JWT |
| `POST` | `/auth/register` | Público | Registra um novo cliente |
| `POST` | `/auth/register/admin` | `ADMIN` | Cria um novo administrador |

### Agendamentos (`/agendamentos`)

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `POST` | `/agendamentos` | `CLIENTE`, `ADMIN` | Cria um novo agendamento com validação de conflito |
| `GET` | `/agendamentos` | `ADMIN` | Lista todos os agendamentos (paginado) |
| `GET` | `/agendamentos/meus-agendamentos` | `CLIENTE`, `ADMIN` | Lista os agendamentos do usuário logado |
| `GET` | `/agendamentos/barbeiro/{id}` | `BARBEIRO`, `ADMIN` | Lista agendamentos de um barbeiro específico |
| `GET` | `/agendamentos/barbeiro/{id}/data?data=YYYY-MM-DD` | `BARBEIRO`, `ADMIN` | Filtra agenda de um barbeiro por data |
| `PATCH` | `/agendamentos/{id}/cancelar` | `CLIENTE`, `ADMIN` | Cancela um agendamento (regra de 2h aplicada) |

### Barbeiros (`/barbeiros`)

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `POST` | `/barbeiros` | `ADMIN` | Cadastra um novo barbeiro |
| `GET` | `/barbeiros` | `CLIENTE`, `ADMIN` | Lista todos os barbeiros (paginado) |
| `GET` | `/barbeiros/{id}` | `CLIENTE`, `ADMIN` | Busca barbeiro por ID |
| `GET` | `/barbeiros/buscar?nome=` | `CLIENTE`, `ADMIN` | Busca barbeiro por nome |
| `GET` | `/barbeiros/buscarPorCPF?cpf=` | `CLIENTE`, `ADMIN` | Busca barbeiro por CPF |
| `PUT` | `/barbeiros/{id}` | `BARBEIRO` (próprio), `ADMIN` | Atualiza dados do barbeiro |
| `DELETE` | `/barbeiros/{id}` | `ADMIN` | Remove um barbeiro |

### Clientes (`/clientes`)

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `POST` | `/clientes` | Público | Cadastra um novo cliente |
| `GET` | `/clientes` | `ADMIN` | Lista todos os clientes (paginado) |
| `GET` | `/clientes/{id}` | `CLIENTE`, `ADMIN` | Busca cliente por ID |
| `GET` | `/clientes/buscar?nome=` | `CLIENTE`, `ADMIN` | Busca clientes por nome |
| `GET` | `/clientes/buscarPorCPF?cpf=` | `CLIENTE`, `ADMIN` | Busca cliente por CPF |
| `PUT` | `/clientes/{id}` | `CLIENTE` (próprio), `ADMIN` | Atualiza dados do cliente |
| `DELETE` | `/clientes/{id}` | `ADMIN` | Remove um cliente |

### Serviços (`/servicos`)

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `POST` | `/servicos` | `ADMIN` | Cadastra um novo serviço |
| `GET` | `/servicos` | `CLIENTE`, `ADMIN` | Lista todos os serviços (paginado) |
| `PUT` | `/servicos/{id}` | `ADMIN` | Atualiza um serviço |
| `DELETE` | `/servicos/{id}` | `ADMIN` | Remove um serviço |

---

## Segurança e Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação stateless. O fluxo é o seguinte:

1. O usuário faz `POST /auth/login` com e-mail e senha.
2. A API valida as credenciais e retorna um token JWT.
3. O token deve ser enviado no header de todas as requisições protegidas:
   ```
   Authorization: Bearer <seu_token>
   ```

O token carrega as claims de `userId`, `email` e `role`, permitindo que o sistema aplique regras de autorização como:
- Um cliente só pode editar ou cancelar os seus próprios registros.
- Barbeiros só podem atualizar o seu próprio perfil.

O `SecurityFilter` intercepta cada requisição para validar o token antes do processamento.

---

## 🛠️ Como Rodar o Projeto

### Pré-requisitos

- [Java 21+](https://adoptium.net/)
- [Docker e Docker Compose](https://www.docker.com/)
- [Maven](https://maven.apache.org/)

### Passo a Passo

**1. Clone o repositório:**
```bash
git clone https://github.com/seu-usuario/sistema-barbearia.git
cd sistema-barbearia
```

**2. Configure as variáveis de ambiente:**

Copie o arquivo de template e preencha os valores:
```bash
cp .env_template .env
```
Edite o arquivo `.env` com suas configurações (veja a seção abaixo).

**3. Suba o banco de dados com Docker:**
```bash
docker-compose up -d
```
Isso iniciará o PostgreSQL na porta `5433` e o PgAdmin na porta `8081`.

**4. Execute a aplicação:**
```bash
./mvnw spring-boot:run
```

**5. Acesse a documentação (Swagger UI):**
```
http://localhost:8080/swagger-ui/index.html
```

> **Nota:** Na primeira inicialização, a aplicação cria automaticamente um usuário administrador com as credenciais definidas nas variáveis `ADMIN_EMAIL` e `ADMIN_SENHA`.

---

## Variáveis de Ambiente

Configure o arquivo `.env` na raiz do projeto com base no `.env_template`:

```env
# Banco de Dados
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
DB_NAME=barbearia_db
DB_PORT=5433

# Segurança JWT
JWT_SECRET=seu_segredo_jwt_super_seguro

# Administrador Inicial
ADMIN_EMAIL=admin@barbearia.com
ADMIN_SENHA=suaSenhaAdmin

# PgAdmin (Interface Web do Banco)
PGADMIN_EMAIL=admin@admin.com
PGADMIN_PASSWORD=admin
```

---

## 📁 Estrutura do Projeto

```
src/main/java/com/guilhermeneiva/demo/
├── config/          # Configurações de segurança, JWT e filtros
├── controller/      # Endpoints REST
├── dto/
│   ├── request/     # DTOs de entrada
│   └── response/    # DTOs de saída
├── exception/       # Exceções customizadas de domínio
├── infra/           # Handler global de exceções e objetos de erro
├── mapper/          # Interfaces MapStruct (Entity <-> DTO)
└── model/
    ├── entity/      # Entidades JPA
    ├── enums/       # Enumerações (UserRole, StatusAgendamento)
    ├── repository/  # Interfaces Spring Data JPA
    └── service/     # Lógica de negócio
```

---

## 👨‍💻 Autor

Desenvolvido por **Guilherme Neiva**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Guilherme%20Neiva-0077B5?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/guilherme-neiva-dos-santos/)
