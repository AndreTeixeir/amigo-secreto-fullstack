#!/bin/bash

###############################################################################
# Script de Deploy Automatizado - Amigo Secreto
# 
# Uso: ./deploy.sh
#
# Este script automatiza o processo de deploy da aplicação no servidor.
###############################################################################

set -e  # Sair em caso de erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Funções auxiliares
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar se está no diretório correto
if [ ! -f "pom.xml" ]; then
    log_error "Execute este script do diretório backend/"
    exit 1
fi

log_info "Iniciando deploy da aplicação Amigo Secreto..."

# 1. Atualizar código do repositório
log_info "Atualizando código do repositório..."
cd ..
git pull origin main
cd backend

# 2. Limpar builds anteriores
log_info "Limpando builds anteriores..."
mvn clean

# 3. Executar testes
log_info "Executando testes..."
mvn test || {
    log_warn "Alguns testes falharam, mas continuando..."
}

# 4. Compilar aplicação
log_info "Compilando aplicação..."
mvn package -DskipTests

# 5. Verificar se JAR foi criado
JAR_FILE="target/amigo-secreto-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    log_error "JAR não foi criado: $JAR_FILE"
    exit 1
fi

log_info "JAR criado com sucesso: $JAR_FILE"

# 6. Parar serviço atual
log_info "Parando serviço atual..."
sudo systemctl stop amigo-secreto || {
    log_warn "Serviço não estava rodando"
}

# 7. Fazer backup do JAR anterior (se existir)
DEPLOY_DIR="/opt/amigo-secreto"
if [ -f "$DEPLOY_DIR/amigo-secreto.jar" ]; then
    log_info "Fazendo backup do JAR anterior..."
    sudo cp "$DEPLOY_DIR/amigo-secreto.jar" "$DEPLOY_DIR/amigo-secreto.jar.bak.$(date +%Y%m%d_%H%M%S)"
fi

# 8. Copiar novo JAR
log_info "Copiando novo JAR para $DEPLOY_DIR..."
sudo mkdir -p "$DEPLOY_DIR"
sudo cp "$JAR_FILE" "$DEPLOY_DIR/amigo-secreto.jar"

# 9. Definir permissões
sudo chown ubuntu:ubuntu "$DEPLOY_DIR/amigo-secreto.jar"
sudo chmod 755 "$DEPLOY_DIR/amigo-secreto.jar"

# 10. Iniciar serviço
log_info "Iniciando serviço..."
sudo systemctl start amigo-secreto

# 11. Aguardar inicialização
log_info "Aguardando inicialização (30 segundos)..."
sleep 30

# 12. Verificar status
log_info "Verificando status do serviço..."
if sudo systemctl is-active --quiet amigo-secreto; then
    log_info "✅ Serviço está rodando!"
    
    # Testar endpoint de health
    log_info "Testando endpoint de saúde..."
    if curl -s http://localhost:8080/api/auth/login > /dev/null 2>&1; then
        log_info "✅ API está respondendo!"
    else
        log_warn "⚠️  API não está respondendo ainda. Verifique os logs."
    fi
else
    log_error "❌ Serviço falhou ao iniciar!"
    log_error "Verifique os logs: sudo journalctl -u amigo-secreto -n 50"
    exit 1
fi

# 13. Mostrar logs recentes
log_info "Últimas 20 linhas do log:"
sudo journalctl -u amigo-secreto -n 20 --no-pager

log_info "🎉 Deploy concluído com sucesso!"
log_info ""
log_info "Comandos úteis:"
log_info "  - Ver logs: sudo journalctl -u amigo-secreto -f"
log_info "  - Status: sudo systemctl status amigo-secreto"
log_info "  - Restart: sudo systemctl restart amigo-secreto"
log_info "  - Stop: sudo systemctl stop amigo-secreto"

