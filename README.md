# 🎉 Amigo Secreto Fullstack

Sistema completo de gerenciamento de eventos de amigo secreto com front-end em HTML/CSS/JavaScript e back-end em Java + Spring Boot, integrado com Oracle Cloud Infrastructure.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![Oracle Cloud](https://img.shields.io/badge/Oracle%20Cloud-Free%20Tier-red)](https://www.oracle.com/cloud/free/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📋 Sobre o Projeto

Aplicação fullstack para gerenciar eventos de amigo secreto, permitindo que usuários:

- 🔐 Criem contas e façam login com autenticação JWT
- 🎊 Criem e gerenciem eventos de amigo secreto
- 👥 Adicionem participantes aos eventos
- 🎲 Realizem sorteios automáticos
- 📊 Visualizem histórico de sorteios

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────┐
│              ORACLE CLOUD (OCI)                     │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────┐         ┌──────────────────┐    │
│  │  Compute VM  │◄────────┤ Autonomous DB    │    │
│  │  (Ampere A1) │         │     (ATP)        │    │
│  │              │         │                  │    │
│  │ • Spring Boot│         │ • PostgreSQL     │    │
│  │ • Nginx      │         │   compatible     │    │
│  │ • Front-end  │         │ • 20 GB          │    │
│  └──────────────┘         └──────────────────┘    │
│         │                                          │
└─────────┼──────────────────────────────────────────┘
          │
          ▼
      INTERNET
```

---

## 🚀 Tecnologias Utilizadas

### **Back-end**
- **Java 21 LTS** - Linguagem de programação
- **Spring Boot 3.2** - Framework principal
- **Spring Data JPA** - ORM para acesso a dados
- **Spring Security** - Segurança e autenticação
- **JWT** - Autenticação stateless
- **Flyway** - Migrations de banco de dados
- **Oracle JDBC** - Driver para Oracle Database
- **Lombok** - Redução de boilerplate
- **MapStruct** - Mapeamento de DTOs
- **Swagger/OpenAPI** - Documentação da API
- **Maven** - Gerenciamento de dependências

### **Front-end**
- **HTML5** - Estrutura
- **CSS3** - Estilização
- **JavaScript (ES6+)** - Lógica e interatividade
- **Bootstrap 5** - Framework CSS
- **Fetch API** - Comunicação com back-end

### **Banco de Dados**
- **Oracle Autonomous Database (ATP)** - Banco de dados gerenciado
- **PostgreSQL-compatible mode** - Compatibilidade

### **Infraestrutura**
- **Oracle Cloud Infrastructure (OCI)** - Cloud provider
- **Docker** - Containerização
- **Nginx** - Servidor web e proxy reverso
- **Git/GitHub** - Controle de versão

---

## 📁 Estrutura do Projeto

```
amigo-secreto-fullstack/
│
├── frontend/                           # Aplicação front-end
│   ├── assets/                         # Imagens e recursos
│   ├── css/                            # Estilos CSS
│   ├── js/                             # Scripts JavaScript
│   ├── index.html                      # Página inicial
│   ├── login.html                      # Página de login
│   ├── dashboard.html                  # Dashboard do usuário
│   └── ...
│
├── backend/                            # Aplicação Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/amigosecreto/
│   │   │   │   ├── config/             # Configurações
│   │   │   │   ├── controller/         # REST Controllers
│   │   │   │   ├── service/            # Lógica de negócio
│   │   │   │   ├── repository/         # Acesso a dados
│   │   │   │   ├── model/              # Entidades JPA
│   │   │   │   ├── dto/                # Data Transfer Objects
│   │   │   │   ├── security/           # Segurança e JWT
│   │   │   │   └── exception/          # Tratamento de exceções
│   │   │   └── resources/
│   │   │       ├── application.yml     # Configuração principal
│   │   │       └── db/migration/       # Migrations Flyway
│   │   └── test/                       # Testes
│   ├── pom.xml                         # Dependências Maven
│   └── Dockerfile                      # Container do back-end
│
├── docs/                               # Documentação
│   ├── ARCHITECTURE.md                 # Arquitetura do sistema
│   ├── API.md                          # Documentação da API
│   ├── DEPLOY_OCI.md                   # Guia de deploy no OCI
│   └── DATABASE.md                     # Modelo de dados
│
├── scripts/                            # Scripts de automação
│   ├── deploy-backend.sh               # Deploy do back-end
│   ├── deploy-frontend.sh              # Deploy do front-end
│   └── setup-oci.sh                    # Setup do Oracle Cloud
│
├── docker-compose.yml                  # Orquestração de containers
├── .gitignore                          # Arquivos ignorados pelo Git
└── README.md                           # Este arquivo
```

---

## 🔧 Pré-requisitos

### **Para Desenvolvimento Local**
- Java 21 LTS ou superior
- Maven 3.8+
- Oracle Autonomous Database (ou PostgreSQL para testes)
- Git

### **Para Deploy no Oracle Cloud**
- Conta no Oracle Cloud Infrastructure (Free Tier)
- Oracle Cloud CLI (opcional)
- SSH key pair

---

## 🚀 Como Executar Localmente

### **1. Clonar o Repositório**

```bash
git clone https://github.com/AndreTeixeir/amigo-secreto-fullstack.git
cd amigo-secreto-fullstack
```

### **2. Configurar Banco de Dados**

#### **Opção A: Oracle Autonomous Database (Recomendado)**

1. Crie um Autonomous Database no Oracle Cloud
2. Baixe o Wallet (arquivo ZIP)
3. Extraia o Wallet em uma pasta (ex: `~/wallet`)
4. Configure as variáveis de ambiente:

```bash
export TNS_ADMIN=/caminho/para/wallet
export DB_USERNAME=ADMIN
export DB_PASSWORD=sua_senha
```

#### **Opção B: PostgreSQL Local (Para Testes)**

```bash
# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib

# Criar banco de dados
sudo -u postgres createdb amigosecreto

# Atualizar application-dev.yml com configurações do PostgreSQL
```

### **3. Executar o Back-end**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

O back-end estará disponível em: `http://localhost:8080`

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

### **4. Executar o Front-end**

Abra o arquivo `frontend/index.html` em um navegador ou use um servidor local:

```bash
cd frontend
python3 -m http.server 3000
```

O front-end estará disponível em: `http://localhost:3000`

---

## 🌐 Deploy no Oracle Cloud

Consulte o guia completo de deploy: [docs/DEPLOY_OCI.md](docs/DEPLOY_OCI.md)

### **Resumo dos Passos**

1. **Criar Autonomous Database** (Always Free)
2. **Criar Compute Instance** (Ampere A1 - Always Free)
3. **Configurar Firewall** (liberar portas 80 e 8080)
4. **Instalar Java e Maven** na VM
5. **Deploy do Back-end** (Spring Boot)
6. **Configurar Nginx** para servir front-end
7. **Configurar domínio** (opcional)

---

## 📚 Documentação da API

### **Autenticação**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Login e obter token JWT |

### **Eventos**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/events` | Listar eventos do usuário |
| GET | `/api/events/{id}` | Buscar evento específico |
| POST | `/api/events` | Criar novo evento |
| PUT | `/api/events/{id}` | Atualizar evento |
| DELETE | `/api/events/{id}` | Deletar evento |

### **Participantes**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/events/{eventId}/participants` | Listar participantes |
| POST | `/api/events/{eventId}/participants` | Adicionar participante |
| PUT | `/api/participants/{id}` | Atualizar participante |
| DELETE | `/api/participants/{id}` | Remover participante |

### **Sorteio**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/events/{eventId}/draw` | Realizar sorteio |
| GET | `/api/events/{eventId}/draw` | Ver resultado do sorteio |

**Documentação completa**: [docs/API.md](docs/API.md) ou acesse o Swagger UI

---

## 🗄️ Modelo de Dados

```
┌─────────────┐
│    USERS    │
├─────────────┤
│ id (PK)     │
│ username    │
│ email       │
│ password    │
└─────────────┘
       │
       │ 1:N
       ▼
┌─────────────┐
│   EVENTS    │
├─────────────┤
│ id (PK)     │
│ user_id (FK)│
│ name        │
│ description │
│ event_date  │
│ status      │
└─────────────┘
       │
       │ 1:N
       ▼
┌──────────────┐
│ PARTICIPANTS │
├──────────────┤
│ id (PK)      │
│ event_id (FK)│
│ name         │
│ email        │
└──────────────┘
       │
       │ 1:N
       ▼
┌─────────────┐
│    DRAWS    │
├─────────────┤
│ id (PK)     │
│ event_id    │
│ giver_id    │
│ receiver_id │
└─────────────┘
```

**Documentação completa**: [docs/DATABASE.md](docs/DATABASE.md)

---

## 🧪 Testes

```bash
# Executar todos os testes
cd backend
mvn test

# Executar testes com cobertura
mvn test jacoco:report
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga os passos:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adicionar MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👤 Autor

**Andre Teixeira**

- GitHub: [@AndreTeixeir](https://github.com/AndreTeixeir)
- LinkedIn: [Andre Teixeira](https://www.linkedin.com/in/andre-teixeira)

---

## 🙏 Agradecimentos

- Oracle Cloud Infrastructure pela plataforma Always Free
- Spring Boot pela excelente documentação
- Comunidade open source

---

## 📞 Suporte

Se você tiver alguma dúvida ou problema, abra uma [issue](https://github.com/AndreTeixeir/amigo-secreto-fullstack/issues) no GitHub.

---

**Desenvolvido com ❤️ e ☕ por Andre Teixeira**

