package com.amigosecreto.repository;

import com.amigosecreto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo username.
     *
     * @param username Username do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca um usuário pelo email.
     *
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe um usuário com o username informado.
     *
     * @param username Username a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByUsername(String username);

    /**
     * Verifica se existe um usuário com o email informado.
     *
     * @param email Email a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByEmail(String email);
}

