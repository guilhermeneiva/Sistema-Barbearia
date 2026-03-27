# 💈 BarberFlow API - Sistema de Agendamentos

API REST para gerenciamento de barbearia, desenvolvida com **Java 17** e **Spring Boot**. O foco do projeto é garantir a integridade da agenda através de regras de negócio rigorosas e validações de conflitos.

## 🚀 Tecnologias
* **Java 17**
* **Spring Boot 3**
* **Spring Data JPA** (PostgreSQL)
* **Bean Validation** (@Future, @Valid, etc)
* **MapStruct** (Mapeamento de DTOs)
* **Lombok** (Produtividade)

## 🧠 Regras de Negócio (Destaques)

Este projeto não é um simples CRUD. Ele possui inteligência para proteger o tempo dos barbeiros:

* **Validação de Conflito de Intervalo:** O sistema impede agendamentos que se sobreponham. A lógica utiliza a intersecção de intervalos ($A_{start} < B_{end}$ e $A_{end} > B_{start}$) para garantir que um barbeiro nunca tenha dois clientes ao mesmo tempo.
* **Horário de Funcionamento:** Bloqueio automático de agendamentos fora do horário comercial (08:00 - 18:00) e em dias que a barbearia não abre (Domingos).
* **Cancelamento Inteligente:** Implementado via `PATCH`, o cancelamento só é permitido com no mínimo **2 horas de antecedência**, evitando prejuízos por desistências de última hora.
* **Proteção de Retroatividade:** Uso de `@Future` para garantir que agendamentos e cancelamentos só ocorram para datas futuras.

## 📑 Endpoints Principais

### Agendamentos
* `POST /agendamentos` - Cria um novo agendamento (Valida conflitos, horários e dias).
* `GET /agendamentos` - Listagem geral com paginação.
* `GET /agendamentos/barbeiro/{id}` - Agenda específica de um profissional.
* `GET /agendamentos/barbeiro/{id}/data?data=YYYY-MM-DD` - Filtro por data e barbeiro.
* `PATCH /agendamentos/{id}/cancelar` - Cancela um horário (Aplica regra de 2h).

## 🛠️ Como rodar o projeto
1. Clone o repositório: `git clone https://github.com/seu-usuario/seu-repositorio.git`
2. Configure o banco PostgreSQL no `application.properties`.
3. Execute: `./mvnw spring-boot:run`

---
Desenvolvido por **Guilherme Neiva** 🚀
# Sistema_Barbearia
