# 🏗️ Arquitetura do Sistema - Amigo Secreto

## Visão Geral

Sistema fullstack para gerenciamento de eventos de amigo secreto com autenticação JWT, desenvolvido seguindo princípios SOLID e Clean Architecture.

---

## 📊 Diagrama de Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                        FRONTEND                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  HTML5 + CSS3 + JavaScript (Vanilla)                   │ │
│  │  - index.html (landing page)                           │ │
│  │  - login.html / register.html                          │ │
│  │  - dashboard.html                                      │ │
│  │  - event-detail.html                                   │ │
│  │  - app.js (integração com API)                         │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ HTTP/REST
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                        BACKEND                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              PRESENTATION LAYER                         │ │
│  │  ┌──────────────────────────────────────────────────┐  │ │
│  │  │  Controllers (REST API)                          │  │ │
│  │  │  - AuthController                                │  │ │
│  │  │  - EventController                               │  │ │
│  │  │  - ParticipantController                         │  │ │
│  │  │  - DrawController                                │  │ │
│  │  └──────────────────────────────────────────────────┘  │ │
│  └────────────────────────────────────────────────────────┘ │
│                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              SECURITY LAYER                             │ │
│  │  - JwtAuthenticationFilter                             │ │
│  │  - JwtTokenProvider                                    │ │
│  │  - SecurityConfig                                      │ │
│  └────────────────────────────────────────────────────────┘ │
│                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              SERVICE LAYER                              │ │
│  │  - AuthService                                         │ │
│  │  - EventService                                        │ │
│  │  - ParticipantService                                  │ │
│  │  - DrawService (algoritmo de sorteio)                 │ │
│  └────────────────────────────────────────────────────────┘ │
│                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              REPOSITORY LAYER                           │ │
│  │  - UserRepository                                      │ │
│  │  - EventRepository                                     │ │
│  │  - ParticipantRepository                               │ │
│  │  - DrawRepository                                      │ │
│  └────────────────────────────────────────────────────────┘ │
│                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              DOMAIN LAYER                               │ │
│  │  - User (entidade)                                     │ │
│  │  - Event (entidade)                                    │ │
│  │  - Participant (entidade)                              │ │
│  │  - Draw (entidade)                                     │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ JDBC
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   DATABASE LAYER                             │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  Oracle Autonomous Database (ATP)                      │ │
│  │  - users                                               │ │
│  │  - events                                              │ │
│  │  - participants                                        │ │
│  │  - draws                                               │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Modelo de Dados

### Relacionamentos

```
USER (1) ──────→ (N) EVENT
                      │
                      ├──→ (N) PARTICIPANT
                      │
                      └──→ (N) DRAW
                               │
                               ├──→ (1) PARTICIPANT (giver)
                               │
                               └──→ (1) PARTICIPANT (receiver)
```

### Tabelas

**users**
- id (PK)
- username (UNIQUE)
- email (UNIQUE)
- password (BCrypt)
- created_at
- updated_at

**events**
- id (PK)
- name
- description
- event_date
- status (PENDING, DRAWN, COMPLETED, CANCELLED)
- user_id (FK → users)
- created_at
- updated_at

**participants**
- id (PK)
- name
- email
- event_id (FK → events)
- created_at
- updated_at
- UNIQUE(email, event_id)

**draws**
- id (PK)
- event_id (FK → events)
- giver_id (FK → participants)
- receiver_id (FK → participants)
- created_at
- CHECK(giver_id != receiver_id)

---

## 🔐 Segurança

### Autenticação

- **JWT (JSON Web Token)** para autenticação stateless
- **BCrypt** para hash de senhas
- **Spring Security** para gerenciamento de autenticação e autorização

### Autorização

- Usuário só pode acessar seus próprios recursos
- Validação em todas as camadas (Controller, Service)
- Uso de `SecurityContext` para obter usuário autenticado

### Proteção de Endpoints

| Endpoint | Público | Autenticado |
|----------|---------|-------------|
| POST /api/auth/register | ✅ | - |
| POST /api/auth/login | ✅ | - |
| GET /api/events | - | ✅ |
| POST /api/events | - | ✅ |
| Todos os outros | - | ✅ |

---

## 🎯 Padrões de Projeto

### 1. **Repository Pattern**
- Abstração da camada de dados
- Spring Data JPA para operações CRUD
- Queries customizadas com @Query

### 2. **DTO Pattern**
- Separação entre entidades e objetos de transferência
- Request DTOs para entrada
- Response DTOs para saída

### 3. **Service Layer Pattern**
- Lógica de negócio centralizada
- Transações gerenciadas com @Transactional
- Validações de regras de negócio

### 4. **Dependency Injection**
- Inversão de controle com Spring
- @Autowired para injeção de dependências
- Baixo acoplamento entre camadas

### 5. **Exception Handling**
- GlobalExceptionHandler para tratamento centralizado
- Respostas padronizadas de erro
- Logging de exceções

---

## 🔄 Fluxo de Requisição

### Exemplo: Criar Evento

```
1. Cliente envia POST /api/events
   ↓
2. JwtAuthenticationFilter valida token
   ↓
3. SecurityContext define usuário autenticado
   ↓
4. EventController recebe requisição
   ↓
5. @Valid valida EventRequest
   ↓
6. EventController chama EventService.createEvent()
   ↓
7. EventService obtém usuário do SecurityContext
   ↓
8. EventService cria entidade Event
   ↓
9. EventRepository.save() persiste no banco
   ↓
10. EventService converte para EventResponse
    ↓
11. EventController retorna ResponseEntity<EventResponse>
    ↓
12. Cliente recebe JSON com evento criado
```

---

## 🧪 Algoritmo de Sorteio

### Funcionamento

```java
1. Receber lista de participantes [A, B, C, D, E]
2. Criar duas listas: givers e receivers
3. Embaralhar receivers
4. Verificar se alguém tirou a si mesmo
5. Se sim, embaralhar novamente (até 100 tentativas)
6. Se não, criar sorteios:
   A → C
   B → E
   C → A
   D → B
   E → D
7. Salvar no banco de dados
8. Atualizar status do evento para DRAWN
```

### Garantias

✅ Ninguém tira a si mesmo
✅ Cada pessoa tira exatamente uma pessoa
✅ Cada pessoa é tirada por exatamente uma pessoa
✅ Distribuição aleatória

---

## 📦 Tecnologias Utilizadas

### Backend

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 LTS | Linguagem principal |
| Spring Boot | 3.2.x | Framework |
| Spring Security | 6.x | Autenticação/Autorização |
| Spring Data JPA | 3.x | Persistência |
| Flyway | 9.x | Migrations |
| JWT (jjwt) | 0.12.3 | Tokens JWT |
| Lombok | 1.18.x | Redução de boilerplate |
| Oracle JDBC | 21.9 | Driver do banco |
| Maven | 3.9.x | Build tool |

### Frontend

| Tecnologia | Uso |
|------------|-----|
| HTML5 | Estrutura |
| CSS3 | Estilização |
| JavaScript (Vanilla) | Lógica e integração com API |
| Bootstrap 5 | Framework CSS |

### Infraestrutura

| Tecnologia | Uso |
|------------|-----|
| Oracle Autonomous Database | Banco de dados |
| Oracle Compute (ARM) | Servidor de aplicação |
| Nginx | Proxy reverso |
| Systemd | Gerenciamento de serviço |
| Docker | Containerização (opcional) |

---

## 🚀 Deployment

### Ambientes

**Desenvolvimento (dev)**:
- H2 Database (em memória)
- Porta 8080
- CORS liberado
- Logs detalhados

**Produção (prod)**:
- Oracle Autonomous Database
- Porta 8080 (interno)
- Nginx na porta 80/443 (externo)
- Logs otimizados
- HTTPS (opcional)

### CI/CD (Futuro)

```
GitHub → GitHub Actions → Build → Test → Deploy → OCI
```

---

## 📈 Escalabilidade

### Horizontal

- Aplicação stateless (JWT)
- Pode rodar múltiplas instâncias
- Load balancer na frente

### Vertical

- Aumentar OCPUs da VM
- Aumentar OCPUs do database
- Dentro dos limites do Always Free

---

## 🔍 Monitoramento

### Logs

- Application logs via SLF4J
- Systemd journald
- Nginx access/error logs

### Métricas (Futuro)

- Spring Boot Actuator
- Prometheus
- Grafana

---

## 🛡️ Boas Práticas Implementadas

✅ **SOLID Principles**
✅ **Clean Code**
✅ **RESTful API**
✅ **Separation of Concerns**
✅ **Dependency Injection**
✅ **Exception Handling**
✅ **Input Validation**
✅ **Password Encryption**
✅ **JWT Authentication**
✅ **Database Migrations**
✅ **Transaction Management**
✅ **Logging**
✅ **Git Flow**
✅ **Conventional Commits**

---

## 📚 Documentação Adicional

- [API Documentation](API.md)
- [Deploy Guide](DEPLOY_OCI.md)
- [README Principal](../README.md)

---

**Desenvolvido por Andre Teixeira**

