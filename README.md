# 🎁 Amigo Secreto - Sistema Fullstack

Sistema completo para gerenciamento de eventos de amigo secreto com autenticação JWT, desenvolvido com Java, Spring Boot e Oracle Cloud.

[![Java](https://img.shields.io/badge/Java-21_LTS-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Oracle Cloud](https://img.shields.io/badge/Oracle_Cloud-Always_Free-red.svg)](https://www.oracle.com/cloud/free/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📋 Sobre o Projeto

Sistema fullstack que permite aos usuários criar eventos de amigo secreto, adicionar participantes e realizar sorteios automáticos, garantindo que ninguém tire a si mesmo.

### ✨ Funcionalidades

- ✅ **Autenticação JWT** - Registro e login seguros
- ✅ **Gerenciamento de Eventos** - CRUD completo de eventos
- ✅ **Gerenciamento de Participantes** - Adicionar e remover participantes
- ✅ **Sorteio Automático** - Algoritmo inteligente que garante distribuição justa
- ✅ **Autorização por Usuário** - Cada usuário acessa apenas seus eventos
- ✅ **Validações Completas** - Email único, mínimo de participantes, etc.
- ✅ **API RESTful** - Endpoints bem documentados
- ✅ **Deploy em Cloud** - Pronto para Oracle Cloud Infrastructure

---

## 🏗️ Arquitetura

### Stack Tecnológica

**Backend**:
- Java 21 LTS
- Spring Boot 3.2 (Web, Security, Data JPA)
- Spring Security + JWT
- Flyway Migrations
- Oracle JDBC Driver
- Lombok, MapStruct
- Maven

**Frontend**:
- HTML5 + CSS3 + JavaScript (Vanilla)
- Bootstrap 5
- Fetch API para integração

**Banco de Dados**:
- Oracle Autonomous Database (ATP)
- PostgreSQL-compatible mode

**Infraestrutura**:
- Oracle Cloud Infrastructure (OCI)
- Compute VM (Ampere A1 - ARM)
- Nginx (proxy reverso)
- Systemd (gerenciamento de serviço)

### Modelo de Dados

```
USER (1) ──────→ (N) EVENT
                      │
                      ├──→ (N) PARTICIPANT
                      │
                      └──→ (N) DRAW
```

**4 tabelas** com relacionamentos **1:N** implementados.

---

## 🚀 Como Executar

### Pré-requisitos

- Java 21 LTS
- Maven 3.9+
- Oracle Autonomous Database (ou PostgreSQL para desenvolvimento)
- Git

### Desenvolvimento Local

```bash
# 1. Clonar repositório
git clone https://github.com/AndreTeixeir/amigo-secreto-fullstack.git
cd amigo-secreto-fullstack

# 2. Configurar banco de dados
# Edite backend/src/main/resources/application-dev.yml

# 3. Executar backend
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 4. Acessar aplicação
# Backend: http://localhost:8080
# Frontend: Abra frontend/index.html no navegador
```

### Deploy em Produção (Oracle Cloud)

Consulte o [Guia de Deploy](docs/DEPLOY_OCI.md) para instruções completas.

```bash
# Script automatizado de deploy
cd backend
../scripts/deploy.sh
```

---

## 📚 Documentação

- [📖 Documentação da API](docs/API.md) - Todos os endpoints e exemplos
- [🚀 Guia de Deploy OCI](docs/DEPLOY_OCI.md) - Deploy no Oracle Cloud
- [🏗️ Arquitetura do Sistema](docs/ARCHITECTURE.md) - Detalhes técnicos
- [⚙️ Configuração](backend/README.md) - Configuração do backend

---

## 🔐 API Endpoints

### Autenticação

```http
POST /api/auth/register  # Registrar usuário
POST /api/auth/login     # Login
```

### Eventos

```http
GET    /api/events           # Listar eventos
POST   /api/events           # Criar evento
GET    /api/events/{id}      # Buscar evento
PUT    /api/events/{id}      # Atualizar evento
DELETE /api/events/{id}      # Deletar evento
```

### Participantes

```http
GET    /api/events/{id}/participants              # Listar participantes
POST   /api/events/{id}/participants              # Adicionar participante
DELETE /api/events/{id}/participants/{pid}        # Remover participante
```

### Sorteio

```http
POST   /api/events/{id}/draw                      # Realizar sorteio
GET    /api/events/{id}/draw                      # Listar sorteios
GET    /api/events/{id}/draw/participant/{pid}    # Buscar sorteio de participante
DELETE /api/events/{id}/draw                      # Deletar sorteios (refazer)
```

Consulte a [documentação completa da API](docs/API.md) para mais detalhes.

---

## 🧪 Testes

### Executar Testes

```bash
cd backend
mvn test
```

### Testar API com cURL

```bash
# Registrar usuário
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"teste","email":"teste@test.com","password":"senha123"}'

# Login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"teste","password":"senha123"}' \
  | jq -r '.token')

# Criar evento
curl -X POST http://localhost:8080/api/events \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Natal 2024","eventDate":"2024-12-25"}'
```

---

## 🎲 Algoritmo de Sorteio

O sistema implementa um algoritmo inteligente que:

✅ Garante que ninguém tira a si mesmo
✅ Distribui aleatoriamente os participantes
✅ Valida mínimo de 3 participantes
✅ Permite refazer o sorteio
✅ Bloqueia modificações após sorteio

---

## 📊 Estrutura do Projeto

```
amigo-secreto-fullstack/
├── frontend/                 # Front-end (HTML/CSS/JS)
│   ├── assets/              # Imagens e recursos
│   ├── css/                 # Estilos
│   ├── js/                  # Scripts
│   └── index.html           # Página principal
│
├── backend/                 # Back-end (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/amigosecreto/
│   │   │   │       ├── config/          # Configurações
│   │   │   │       ├── controller/      # Controllers REST
│   │   │   │       ├── dto/             # DTOs
│   │   │   │       ├── exception/       # Exception handling
│   │   │   │       ├── model/           # Entidades JPA
│   │   │   │       ├── repository/      # Repositories
│   │   │   │       ├── security/        # Segurança e JWT
│   │   │   │       └── service/         # Lógica de negócio
│   │   │   └── resources/
│   │   │       ├── application.yml      # Configuração principal
│   │   │       ├── application-dev.yml  # Configuração dev
│   │   │       ├── application-prod.yml # Configuração prod
│   │   │       └── db/migration/        # Flyway migrations
│   │   └── test/                        # Testes
│   ├── pom.xml                          # Dependências Maven
│   └── Dockerfile                       # Container Docker
│
├── docs/                    # Documentação
│   ├── API.md              # Documentação da API
│   ├── DEPLOY_OCI.md       # Guia de deploy
│   └── ARCHITECTURE.md     # Arquitetura do sistema
│
├── scripts/                # Scripts de automação
│   └── deploy.sh           # Script de deploy
│
├── docker-compose.yml      # Orquestração Docker
└── README.md              # Este arquivo
```

---

## 🛡️ Segurança

- **JWT** para autenticação stateless
- **BCrypt** para hash de senhas
- **Spring Security** para autorização
- **Validações** em todas as camadas
- **CORS** configurado
- **HTTPS** recomendado em produção

---

## 💰 Custos (Oracle Cloud)

Este projeto utiliza apenas recursos **Always Free** da Oracle Cloud:

- ✅ Autonomous Database: 1 OCPU, 20 GB (gratuito)
- ✅ Compute VM: 2 OCPUs ARM, 12 GB RAM (gratuito)
- ✅ Networking: 10 TB/mês (gratuito)
- ✅ Storage: 200 GB (gratuito)

**Custo total**: R$ 0,00 💸

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'feat: adicionar nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abrir um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Andre Teixeira**

- GitHub: [@AndreTeixeir](https://github.com/AndreTeixeir)
- LinkedIn: [Andre Teixeira](https://linkedin.com/in/andre-teixeira)

---

## 🙏 Agradecimentos

- Oracle Cloud Infrastructure pela plataforma Always Free
- Spring Framework pela excelente documentação
- Comunidade open source

---

## 📞 Suporte

Se você tiver alguma dúvida ou problema:

1. Consulte a [documentação](docs/)
2. Abra uma [issue](https://github.com/AndreTeixeir/amigo-secreto-fullstack/issues)
3. Entre em contato via LinkedIn

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no GitHub!

**Desenvolvido com ❤️ por Andre Teixeira**

