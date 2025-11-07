# 🚀 Guia de Deploy no Oracle Cloud Infrastructure (OCI)

## Visão Geral

Este guia mostra como fazer deploy completo da aplicação Amigo Secreto no Oracle Cloud usando recursos **Always Free**.

---

## 📋 Pré-requisitos

- Conta no Oracle Cloud (gratuita)
- Git instalado
- Cliente SSH (ssh, PuTTY, etc.)

---

## 🎯 Arquitetura no OCI

```
┌─────────────────────────────────────────┐
│         Oracle Cloud (Always Free)       │
├─────────────────────────────────────────┤
│                                          │
│  ┌──────────────────────────────────┐   │
│  │   Compute VM (Ampere A1)         │   │
│  │   - 2 OCPUs, 12 GB RAM           │   │
│  │   - Ubuntu 22.04                 │   │
│  │   - Java 21 + Spring Boot        │   │
│  │   - Nginx (proxy reverso)        │   │
│  └──────────────────────────────────┘   │
│              │                           │
│              ▼                           │
│  ┌──────────────────────────────────┐   │
│  │   Autonomous Database (ATP)      │   │
│  │   - 1 OCPU, 20 GB storage        │   │
│  │   - PostgreSQL-compatible        │   │
│  └──────────────────────────────────┘   │
│                                          │
└─────────────────────────────────────────┘
```

---

## 📝 Passo 1: Criar Autonomous Database

### 1.1 - Acessar Console OCI

1. Acesse: https://cloud.oracle.com
2. Faça login
3. Vá para: **Menu → Oracle Database → Autonomous Transaction Processing**

### 1.2 - Criar Database

1. Clique em **Create Autonomous Database**
2. Preencha:
   - **Display name**: `amigo-secreto-db`
   - **Database name**: `amigosecreto`
   - **Workload type**: Transaction Processing
   - **Deployment type**: Shared Infrastructure
   - **Always Free**: ✅ Marcar
   - **Database version**: 19c ou 21c
   - **OCPU count**: 1
   - **Storage**: 20 GB
   - **Password**: Crie uma senha forte (ex: `SenhaForte123!`)
   - **Network access**: Secure access from everywhere
   - **License type**: License Included

3. Clique em **Create Autonomous Database**
4. Aguarde ~5 minutos até status **Available**

### 1.3 - Baixar Wallet

1. Na página do database, clique em **DB Connection**
2. Clique em **Download Wallet**
3. Defina uma senha para o wallet (ex: `WalletPass123!`)
4. Salve o arquivo `Wallet_amigosecreto.zip`

### 1.4 - Obter Connection String

1. Na página **DB Connection**, copie a **Connection String** do tipo **TNS**
2. Exemplo: `amigosecreto_high`

---

## 🖥️ Passo 2: Criar Compute Instance (VM)

### 2.1 - Criar VM

1. Vá para: **Menu → Compute → Instances**
2. Clique em **Create Instance**
3. Preencha:
   - **Name**: `amigo-secreto-vm`
   - **Image**: Ubuntu 22.04
   - **Shape**: Ampere (ARM) - VM.Standard.A1.Flex
   - **OCPUs**: 2
   - **Memory**: 12 GB
   - **Network**: Use default VCN
   - **Public IP**: Assign a public IPv4 address
   - **SSH keys**: Upload ou gere um par de chaves

4. Clique em **Create**
5. Aguarde ~3 minutos até status **Running**
6. Anote o **Public IP Address** (ex: `123.45.67.89`)

### 2.2 - Configurar Firewall (Security List)

1. Na página da VM, clique no **Subnet** (link)
2. Clique em **Default Security List**
3. Clique em **Add Ingress Rules**
4. Adicione as regras:

**Regra 1 - HTTP**:
- Source CIDR: `0.0.0.0/0`
- IP Protocol: TCP
- Destination Port Range: `80`

**Regra 2 - HTTPS**:
- Source CIDR: `0.0.0.0/0`
- IP Protocol: TCP
- Destination Port Range: `443`

**Regra 3 - Backend**:
- Source CIDR: `0.0.0.0/0`
- IP Protocol: TCP
- Destination Port Range: `8080`

---

## 🔧 Passo 3: Configurar VM

### 3.1 - Conectar via SSH

```bash
ssh -i ~/.ssh/sua_chave.pem ubuntu@123.45.67.89
```

### 3.2 - Atualizar Sistema

```bash
sudo apt update && sudo apt upgrade -y
```

### 3.3 - Instalar Java 21

```bash
sudo apt install -y openjdk-21-jdk
java -version
```

### 3.4 - Instalar Maven

```bash
sudo apt install -y maven
mvn -version
```

### 3.5 - Instalar Git

```bash
sudo apt install -y git
```

### 3.6 - Instalar Nginx

```bash
sudo apt install -y nginx
sudo systemctl enable nginx
sudo systemctl start nginx
```

### 3.7 - Configurar Firewall da VM

```bash
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp
sudo ufw enable
```

---

## 📦 Passo 4: Deploy da Aplicação

### 4.1 - Clonar Repositório

```bash
cd /home/ubuntu
git clone https://github.com/AndreTeixeir/amigo-secreto-fullstack.git
cd amigo-secreto-fullstack
```

### 4.2 - Configurar Wallet do Oracle

```bash
# Criar diretório para wallet
mkdir -p /home/ubuntu/wallet

# Copiar wallet (você precisa fazer upload do arquivo)
# Use scp do seu computador:
# scp -i ~/.ssh/sua_chave.pem Wallet_amigosecreto.zip ubuntu@123.45.67.89:/home/ubuntu/wallet/

# Descompactar wallet
cd /home/ubuntu/wallet
unzip Wallet_amigosecreto.zip

# Definir permissões
chmod 600 /home/ubuntu/wallet/*
```

### 4.3 - Configurar application-prod.yml

Edite o arquivo:

```bash
nano backend/src/main/resources/application-prod.yml
```

Atualize com suas credenciais:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@amigosecreto_high?TNS_ADMIN=/home/ubuntu/wallet
    username: ADMIN
    password: SuaSenhaDoDatabase
    driver-class-name: oracle.jdbc.OracleDriver
  
  jpa:
    database-platform: org.hibernate.dialect.OracleDialect
    hibernate:
      ddl-auto: validate
    show-sql: false

jwt:
  secret: ${JWT_SECRET:seu-secret-super-seguro-de-pelo-menos-256-bits-aqui}
  expiration: 86400000

server:
  port: 8080
```

### 4.4 - Build da Aplicação

```bash
cd /home/ubuntu/amigo-secreto-fullstack/backend
mvn clean package -DskipTests
```

### 4.5 - Criar Serviço Systemd

```bash
sudo nano /etc/systemd/system/amigo-secreto.service
```

Conteúdo:

```ini
[Unit]
Description=Amigo Secreto Backend
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/home/ubuntu/amigo-secreto-fullstack/backend
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod target/amigo-secreto-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10

Environment="JAVA_HOME=/usr/lib/jvm/java-21-openjdk-arm64"
Environment="JWT_SECRET=seu-secret-super-seguro-aqui"

[Install]
WantedBy=multi-user.target
```

Salve e ative:

```bash
sudo systemctl daemon-reload
sudo systemctl enable amigo-secreto
sudo systemctl start amigo-secreto
sudo systemctl status amigo-secreto
```

### 4.6 - Configurar Nginx

```bash
sudo nano /etc/nginx/sites-available/amigo-secreto
```

Conteúdo:

```nginx
server {
    listen 80;
    server_name 123.45.67.89;  # Seu IP público

    # Frontend
    location / {
        root /home/ubuntu/amigo-secreto-fullstack/frontend;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # Backend API
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Ativar configuração:

```bash
sudo ln -s /etc/nginx/sites-available/amigo-secreto /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

---

## ✅ Passo 5: Testar Aplicação

### 5.1 - Testar Backend

```bash
curl http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"teste","email":"teste@test.com","password":"senha123"}'
```

### 5.2 - Testar Frontend

Acesse no navegador: `http://123.45.67.89`

---

## 🔄 Atualizações

### Atualizar Código

```bash
cd /home/ubuntu/amigo-secreto-fullstack
git pull origin main
cd backend
mvn clean package -DskipTests
sudo systemctl restart amigo-secreto
```

---

## 📊 Monitoramento

### Ver Logs

```bash
# Logs do backend
sudo journalctl -u amigo-secreto -f

# Logs do Nginx
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### Status dos Serviços

```bash
sudo systemctl status amigo-secreto
sudo systemctl status nginx
```

---

## 🛠️ Troubleshooting

### Backend não inicia

```bash
# Ver logs detalhados
sudo journalctl -u amigo-secreto -n 100 --no-pager

# Verificar se porta 8080 está livre
sudo netstat -tulpn | grep 8080

# Testar conexão com database
cd /home/ubuntu/wallet
sqlplus ADMIN@amigosecreto_high
```

### Erro de conexão com database

1. Verifique se wallet está no caminho correto
2. Verifique credenciais no `application-prod.yml`
3. Verifique se Flyway migrations rodaram:

```bash
cd /home/ubuntu/amigo-secreto-fullstack/backend
mvn flyway:info -Dflyway.configFiles=src/main/resources/application-prod.yml
```

---

## 💰 Custos

**Recursos Always Free utilizados**:
- ✅ Autonomous Database: 1 OCPU, 20 GB (gratuito para sempre)
- ✅ Compute VM: 2 OCPUs ARM, 12 GB RAM (gratuito para sempre)
- ✅ Networking: 10 TB/mês (gratuito)
- ✅ Storage: 200 GB (gratuito)

**Custo total**: R$ 0,00 (Always Free)

---

## 🔒 Segurança

### Recomendações

1. ✅ Trocar senha padrão do database
2. ✅ Usar variáveis de ambiente para JWT_SECRET
3. ✅ Configurar HTTPS com Let's Encrypt
4. ✅ Restringir acesso SSH apenas a IPs conhecidos
5. ✅ Fazer backup regular do database

### Configurar HTTPS (Opcional)

```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d seudominio.com
```

---

**Desenvolvido por Andre Teixeira**

