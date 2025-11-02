package com.amigosecreto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Classe principal da aplicação Amigo Secreto Backend.
 * 
 * Sistema de gerenciamento de eventos de amigo secreto com:
 * - Autenticação JWT
 * - CRUD de eventos e participantes
 * - Algoritmo de sorteio
 * - Integração com Oracle Autonomous Database
 * 
 * @author Andre Teixeira
 * @version 1.0
 * @since Java 21 LTS
 */
@SpringBootApplication
@EnableJpaAuditing
public class AmigoSecretoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmigoSecretoApplication.class, args);
        System.out.println("\n=================================================");
        System.out.println("🎉 Amigo Secreto Backend iniciado com sucesso!");
        System.out.println("📚 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("📄 API Docs: http://localhost:8080/api-docs");
        System.out.println("=================================================\n");
    }
}

