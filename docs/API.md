# 📚 Documentação da API - Amigo Secreto

## Visão Geral

API RESTful para gerenciamento de eventos de amigo secreto com autenticação JWT.

**Base URL**: `http://localhost:8080/api`

**Autenticação**: JWT Bearer Token (exceto endpoints de autenticação)

---

## 🔐 Autenticação

### Registrar Usuário

```http
POST /auth/register
Content-Type: application/json

{
  "username": "andre",
  "email": "andre@example.com",
  "password": "senha123"
}
```

**Resposta (201)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "andre",
  "email": "andre@example.com"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "andre",
  "password": "senha123"
}
```

**Resposta (200)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "andre",
  "email": "andre@example.com"
}
```

---

## 📅 Eventos

### Criar Evento

```http
POST /events
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Amigo Secreto 2024",
  "description": "Natal da família",
  "eventDate": "2024-12-25"
}
```

**Resposta (201)**:
```json
{
  "id": 1,
  "name": "Amigo Secreto 2024",
  "description": "Natal da família",
  "eventDate": "2024-12-25",
  "status": "PENDING",
  "userId": 1,
  "username": "andre",
  "participantCount": 0,
  "createdAt": "2024-11-04T10:00:00",
  "updatedAt": "2024-11-04T10:00:00"
}
```

### Listar Eventos

```http
GET /events
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
[
  {
    "id": 1,
    "name": "Amigo Secreto 2024",
    "eventDate": "2024-12-25",
    "status": "PENDING",
    "participantCount": 5,
    ...
  }
]
```

### Buscar Evento

```http
GET /events/{id}
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
{
  "id": 1,
  "name": "Amigo Secreto 2024",
  ...
}
```

### Atualizar Evento

```http
PUT /events/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Amigo Secreto 2024 Atualizado",
  "description": "Limite R$ 50",
  "eventDate": "2024-12-25"
}
```

**Resposta (200)**:
```json
{
  "id": 1,
  "name": "Amigo Secreto 2024 Atualizado",
  ...
}
```

### Deletar Evento

```http
DELETE /events/{id}
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
{
  "message": "Evento deletado com sucesso"
}
```

### Listar por Status

```http
GET /events/status/{status}
Authorization: Bearer {token}
```

Status válidos: `PENDING`, `DRAWN`, `COMPLETED`, `CANCELLED`

### Atualizar Status

```http
PATCH /events/{id}/status?status=DRAWN
Authorization: Bearer {token}
```

---

## 👥 Participantes

### Adicionar Participante

```http
POST /events/{eventId}/participants
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com"
}
```

**Resposta (201)**:
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "eventId": 1,
  "eventName": "Amigo Secreto 2024",
  "createdAt": "2024-11-04T10:00:00",
  "updatedAt": "2024-11-04T10:00:00"
}
```

### Listar Participantes

```http
GET /events/{eventId}/participants
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
[
  {
    "id": 1,
    "name": "Ana Costa",
    "email": "ana@example.com",
    ...
  }
]
```

### Buscar Participante

```http
GET /events/{eventId}/participants/{participantId}
Authorization: Bearer {token}
```

### Remover Participante

```http
DELETE /events/{eventId}/participants/{participantId}
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
{
  "message": "Participante removido com sucesso"
}
```

### Contar Participantes

```http
GET /events/{eventId}/participants/count
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
{
  "count": 5
}
```

---

## 🎲 Sorteio

### Realizar Sorteio

```http
POST /events/{eventId}/draw
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
[
  {
    "id": 1,
    "eventId": 1,
    "eventName": "Amigo Secreto 2024",
    "giverId": 1,
    "giverName": "Ana Costa",
    "giverEmail": "ana@example.com",
    "receiverId": 3,
    "receiverName": "Pedro Lima",
    "receiverEmail": "pedro@example.com",
    "createdAt": "2024-11-04T11:00:00"
  }
]
```

### Listar Sorteios

```http
GET /events/{eventId}/draw
Authorization: Bearer {token}
```

### Buscar Sorteio de Participante

```http
GET /events/{eventId}/draw/participant/{participantId}
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
{
  "id": 1,
  "giverId": 1,
  "giverName": "Ana Costa",
  "receiverId": 3,
  "receiverName": "Pedro Lima",
  "receiverEmail": "pedro@example.com",
  ...
}
```

### Deletar Sorteios (Refazer)

```http
DELETE /events/{eventId}/draw
Authorization: Bearer {token}
```

**Resposta (200)**:
```json
{
  "message": "Sorteios deletados com sucesso. Evento voltou ao status PENDING."
}
```

---

## ⚠️ Códigos de Erro

| Código | Descrição |
|--------|-----------|
| 400 | Bad Request - Dados inválidos |
| 401 | Unauthorized - Token inválido ou ausente |
| 403 | Forbidden - Sem permissão |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

**Exemplo de Erro**:
```json
{
  "status": 400,
  "message": "É necessário no mínimo 3 participantes para realizar o sorteio",
  "timestamp": "2024-11-04T10:00:00"
}
```

---

## 📊 Status de Eventos

| Status | Descrição |
|--------|-----------|
| PENDING | Evento criado, aguardando participantes |
| DRAWN | Sorteio realizado |
| COMPLETED | Evento concluído |
| CANCELLED | Evento cancelado |

---

## 🔒 Regras de Autorização

- Usuário só pode acessar seus próprios eventos
- Usuário só pode gerenciar participantes de seus eventos
- Usuário só pode realizar sorteio de seus eventos
- Não é possível adicionar/remover participantes após sorteio
- Mínimo de 3 participantes para realizar sorteio
- Email deve ser único por evento

---

## 🧪 Exemplos de Uso com cURL

### Fluxo Completo

```bash
# 1. Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"andre","email":"andre@test.com","password":"senha123"}'

# 2. Login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"andre","password":"senha123"}' \
  | jq -r '.token')

# 3. Criar evento
EVENT_ID=$(curl -X POST http://localhost:8080/api/events \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Natal 2024","eventDate":"2024-12-25"}' \
  | jq -r '.id')

# 4. Adicionar participantes
curl -X POST http://localhost:8080/api/events/$EVENT_ID/participants \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Ana","email":"ana@test.com"}'

# 5. Realizar sorteio
curl -X POST http://localhost:8080/api/events/$EVENT_ID/draw \
  -H "Authorization: Bearer $TOKEN"
```

---

**Desenvolvido por Andre Teixeira**

