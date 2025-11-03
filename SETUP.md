# 🚀 Guia de Setup - Amigo Secreto Fullstack

## 📋 Pré-requisitos

- Git instalado
- Conta no GitHub
- Java 21 LTS (opcional, apenas para executar localmente)
- Maven 3.8+ (opcional, apenas para executar localmente)

---

## 🔧 Setup do Repositório

### **1. Clonar o Repositório**

```bash
git clone https://github.com/AndreTeixeir/amigo-secreto-fullstack.git
cd amigo-secreto-fullstack
```

### **2. Criar Branch Develop**

```bash
git checkout -b develop
```

### **3. Verificar Estrutura**

```bash
ls -la
```

Você deve ver:
- `frontend/` - Aplicação front-end
- `backend/` - Aplicação Spring Boot
- `docs/` - Documentação
- `scripts/` - Scripts de automação
- `docker-compose.yml`
- `README.md`

---

## 📝 Fazer Commit Inicial

```bash
# Adicionar todos os arquivos
git add .

# Fazer commit
git commit -m "feat: adicionar estrutura fullstack com Spring Boot e Oracle Cloud

- Reorganizar front-end existente para pasta frontend/
- Adicionar back-end Spring Boot 3.2 com Java 21 LTS
- Configurar Oracle Autonomous Database
- Implementar estrutura de pacotes (MVC + Security)
- Adicionar Flyway migrations
- Configurar Docker e docker-compose
- Adicionar documentação completa"

# Enviar para GitHub
git push -u origin develop
```

---

## ✅ Verificação

Acesse seu repositório no GitHub e verifique:

- [x] Branch `develop` criada
- [x] Estrutura de pastas organizada
- [x] README.md atualizado
- [x] Backend Spring Boot configurado

---

## 🚀 Próximos Passos

### **Executar Localmente (Opcional)**

#### **Backend**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Acesse: http://localhost:8080/swagger-ui.html

#### **Frontend**

```bash
cd frontend
python3 -m http.server 3000
```

Acesse: http://localhost:3000

---

## 🌐 Deploy no Oracle Cloud

Consulte: [docs/DEPLOY_OCI.md](docs/DEPLOY_OCI.md)

---

## 📚 Documentação

- **README.md** - Visão geral do projeto
- **backend/README.md** - Documentação do back-end
- **docs/API.md** - Documentação da API
- **docs/DATABASE.md** - Modelo de dados
- **docs/DEPLOY_OCI.md** - Guia de deploy

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch: `git checkout -b feature/minha-feature`
3. Commit: `git commit -m 'feat: adicionar minha feature'`
4. Push: `git push origin feature/minha-feature`
5. Abra um Pull Request

---

**Desenvolvido por Andre Teixeira**

