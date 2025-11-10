/**
 * Configuração da API
 */
const API_CONFIG = {
    // URL base da API
    BASE_URL: 'http://localhost:8080/api',
    
    // Endpoints
    ENDPOINTS: {
        AUTH: {
            REGISTER: '/auth/register',
            LOGIN: '/auth/login'
        },
        EVENTS: {
            LIST: '/events',
            CREATE: '/events',
            GET: (id) => `/events/${id}`,
            UPDATE: (id) => `/events/${id}`,
            DELETE: (id) => `/events/${id}`,
            BY_STATUS: (status) => `/events/status/${status}`,
            UPDATE_STATUS: (id) => `/events/${id}/status`
        },
        PARTICIPANTS: {
            LIST: (eventId) => `/events/${eventId}/participants`,
            CREATE: (eventId) => `/events/${eventId}/participants`,
            GET: (eventId, participantId) => `/events/${eventId}/participants/${participantId}`,
            DELETE: (eventId, participantId) => `/events/${eventId}/participants/${participantId}`,
            COUNT: (eventId) => `/events/${eventId}/participants/count`
        },
        DRAW: {
            PERFORM: (eventId) => `/events/${eventId}/draw`,
            LIST: (eventId) => `/events/${eventId}/draw`,
            GET_FOR_PARTICIPANT: (eventId, participantId) => `/events/${eventId}/draw/participant/${participantId}`,
            DELETE: (eventId) => `/events/${eventId}/draw`
        }
    },
    
    // Chaves do localStorage
    STORAGE_KEYS: {
        TOKEN: 'amigo_secreto_token',
        USER: 'amigo_secreto_user'
    }
};

/**
 * Classe para gerenciar autenticação
 */
class AuthManager {
    static saveToken(token) {
        localStorage.setItem(API_CONFIG.STORAGE_KEYS.TOKEN, token);
    }
    
    static getToken() {
        return localStorage.getItem(API_CONFIG.STORAGE_KEYS.TOKEN);
    }
    
    static removeToken() {
        localStorage.removeItem(API_CONFIG.STORAGE_KEYS.TOKEN);
    }
    
    static saveUser(user) {
        localStorage.setItem(API_CONFIG.STORAGE_KEYS.USER, JSON.stringify(user));
    }
    
    static getUser() {
        const user = localStorage.getItem(API_CONFIG.STORAGE_KEYS.USER);
        return user ? JSON.parse(user) : null;
    }
    
    static removeUser() {
        localStorage.removeItem(API_CONFIG.STORAGE_KEYS.USER);
    }
    
    static isAuthenticated() {
        return !!this.getToken();
    }
    
    static logout() {
        this.removeToken();
        this.removeUser();
        window.location.href = 'login.html';
    }
    
    static getAuthHeaders() {
        const token = this.getToken();
        return token ? {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        } : {
            'Content-Type': 'application/json'
        };
    }
}

/**
 * Classe para fazer requisições à API
 */
class ApiClient {
    static async request(endpoint, options = {}) {
        const url = `${API_CONFIG.BASE_URL}${endpoint}`;
        const config = {
            ...options,
            headers: {
                ...AuthManager.getAuthHeaders(),
                ...options.headers
            }
        };
        
        try {
            const response = await fetch(url, config);
            
            // Se não autorizado, fazer logout
            if (response.status === 401) {
                AuthManager.logout();
                return null;
            }
            
            // Se erro, lançar exceção com mensagem
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Erro na requisição');
            }
            
            // Se resposta vazia (204), retornar null
            if (response.status === 204) {
                return null;
            }
            
            return await response.json();
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    }
    
    static async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }
    
    static async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }
    
    static async put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }
    
    static async patch(endpoint, data) {
        return this.request(endpoint, {
            method: 'PATCH',
            body: JSON.stringify(data)
        });
    }
    
    static async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

/**
 * Utilitários
 */
class Utils {
    static formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR');
    }
    
    static formatDateTime(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('pt-BR');
    }
    
    static showError(message) {
        alert(`Erro: ${message}`);
    }
    
    static showSuccess(message) {
        alert(message);
    }
    
    static getStatusBadge(status) {
        const badges = {
            'PENDING': '<span class="badge bg-warning">Pendente</span>',
            'DRAWN': '<span class="badge bg-success">Sorteado</span>',
            'COMPLETED': '<span class="badge bg-primary">Concluído</span>',
            'CANCELLED': '<span class="badge bg-danger">Cancelado</span>'
        };
        return badges[status] || '<span class="badge bg-secondary">Desconhecido</span>';
    }
}

