# 🚀 Amigo Secreto Backend

Back-end da aplicação Amigo Secreto desenvolvido com Java 21 LTS e Spring Boot 3.2.

---

## 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas** (Layered Architecture) com separação clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│         Controller Layer                │  ← REST API (JSON)
├─────────────────────────────────────────┤
│         Service Layer                   │  ← Lógica de Negócio
├─────────────────────────────────────────┤
│         Repository Layer                │  ← Acesso a Dados (JPA)
├─────────────────────────────────────────┤
│         Database Layer                  │  ← Oracle Autonomous DB
└─────────────────────────────────────────┘
```

---

## 📦 Estrutura de Pacotes

```
com.amigosecreto/
├── config/              # Configurações (Security, CORS, Swagger)
├── controller/          # REST Controllers
├── service/             # Lógica de negócio
├── repository/          # Repositories (Spring Data JPA)
├── model/               # Entidades JPA
├── dto/                 # Data Transfer Objects
│   ├── request/         # DTOs de entrada
│   └── response/        # DTOs de saída
├── security/            # Segurança (JWT, Filters)
├── exception/           # Tratamento de exceções
└── AmigoSecretoApplication.java
```

---

## 🔧 Tecnologias

- **Java 21 LTS** - Versão LTS mais recente
- **Spring Boot 3.2** - Framework principal
- **Spring Data JPA** - ORM
- **Spring Security** - Autenticação e autorização
- **JWT (jjwt 0.12.3)** - Tokens de autenticação
- **Oracle JDBC 21.9** - Driver para Oracle Database
- **Flyway** - Migrations de banco de dados
- **Lombok** - Redução de boilerplate
- **MapStruct 1.5.5** - Mapeamento de DTOs
- **Springdoc OpenAPI 2.2** - Documentação Swagger
- **Maven** - Build e dependências

---

## 🚀 Como Executar

### **Pré-requisitos**

- Java 21 LTS ou superior
- Maven 3.8+
- Oracle Autonomous Database (ou PostgreSQL para testes)

### **1. Configurar Banco de Dados**

#### **Oracle Autonomous Database**

1. Baixe o Wallet do Autonomous Database
2. Extraia em uma pasta (ex: `~/wallet`)
3. Configure as variáveis de ambiente:

```bash
export TNS_ADMIN=/caminho/para/wallet
export DB_USERNAME=ADMIN
export DB_PASSWORD=sua_senha
```

#### **Ou edite `application-dev.yml`**

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@amigosecreto_high?TNS_ADMIN=/caminho/para/wallet
    username: ADMIN
    password: sua_senha
```

### **2. Compilar e Executar**

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run

# Ou executar o JAR diretamente
java -jar target/amigo-secreto-backend-0.0.1-SNAPSHOT.jar
```

### **3. Acessar**

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

---

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar com cobertura
mvn test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html
```

---

## 📚 Endpoints da API

### **Autenticação**

```http
POST /api/auth/register
POST /api/auth/login
```

### **Eventos**

```http
GET    /api/events
GET    /api/events/{id}
POST   /api/events
PUT    /api/events/{id}
DELETE /api/events/{id}
```

### **Participantes**

```http
GET    /api/events/{eventId}/participants
POST   /api/events/{eventId}/participants
PUT    /api/participants/{id}
DELETE /api/participants/{id}
```

### **Sorteio**

```http
POST /api/events/{eventId}/draw
GET  /api/events/{eventId}/draw
```

**Documentação completa**: Acesse o Swagger UI

---

## 🗄️ Migrations

As migrations são gerenciadas pelo **Flyway** e estão em:

```
src/main/resources/db/migration/
├── V1__create_users_table.sql
├── V2__create_events_table.sql
├── V3__create_participants_table.sql
├── V4__create_draws_table.sql
└── V5__seed_data.sql
```

**Convenção de nomenclatura**: `V{número}__{descrição}.sql`

---

## 🔐 Segurança

### **Autenticação JWT**

1. Usuário faz login em `/api/auth/login`
2. Recebe um token JWT
3. Envia o token no header: `Authorization: Bearer {token}`

### **Configuração**

```yaml
jwt:
  secret: sua-chave-secreta
  expiration: 86400000  # 24 horas
```

**⚠️ IMPORTANTE**: Em produção, use uma chave forte e armazene em variável de ambiente!

---

## 🐳 Docker

### **Build da imagem**

```bash
docker build -t amigo-secreto-backend .
```

### **Executar container**

```bash
docker run -p 8080:8080 \
  -e TNS_ADMIN=/app/wallet \
  -e DB_USERNAME=ADMIN \
  -e DB_PASSWORD=senha \
  -v /caminho/para/wallet:/app/wallet:ro \
  amigo-secreto-backend
```

---

## 📝 Profiles

### **dev** (Desenvolvimento)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- Logs detalhados
- H2 Console habilitado (se usar H2)
- CORS permissivo

### **prod** (Produção)

```bash
java -jar app.jar --spring.profiles.active=prod
```

- Logs mínimos
- Segurança reforçada
- CORS restrito

---

## 🛠️ Desenvolvimento

### **Adicionar nova entidade**

1. Criar classe em `model/`
2. Criar repository em `repository/`
3. Criar migration em `db/migration/`
4. Criar DTOs em `dto/request` e `dto/response`
5. Criar service em `service/`
6. Criar controller em `controller/`
7. Adicionar testes

### **Padrões de Código**

- **Nomenclatura**: Classes em PascalCase, métodos em camelCase
- **Idioma**: Código em inglês, comentários em português
- **SOLID**: Seguir princípios SOLID
- **Clean Code**: Métodos pequenos e focados
- **DRY**: Não repetir código

---

## 📊 Monitoramento

### **Actuator** (Habilitado em dev)

```http
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

---

## 🤝 Contribuindo

1. Crie uma branch: `git checkout -b feature/minha-feature`
2. Commit: `git commit -m 'feat: adicionar minha feature'`
3. Push: `git push origin feature/minha-feature`
4. Abra um Pull Request

---

## 📄 Licença

MIT License - veja [LICENSE](../LICENSE)

---

**Desenvolvido por Andre Teixeira**

