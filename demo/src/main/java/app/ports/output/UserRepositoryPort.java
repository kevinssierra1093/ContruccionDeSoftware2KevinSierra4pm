package app.ports.output;

import app.models.ClientSystem;
import app.models.UserStatus;

import java.util.Optional;

/**
 * Puerto de salida (driven port) para la persistencia de usuarios del sistema.
 */
public interface UserRepositoryPort {

    /**
     * Guarda un nuevo usuario.
     *
     * @param user Objeto ClientSystem a persistir.
     * @return El usuario guardado con su ID asignado.
     */
    ClientSystem save(ClientSystem user);

    /**
     * Busca un usuario por su ID.
     *
     * @param userId ID del usuario.
     * @return El usuario si existe.
     */
    Optional<ClientSystem> findById(long userId);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return El usuario si existe.
     */
    Optional<ClientSystem> findByUsername(String username);

    /**
     * Verifica si ya existe un usuario con ese nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return true si ya existe.
     */
    boolean existsByUsername(String username);

    /**
     * Actualiza el estado de un usuario.
     *
     * @param userId    ID del usuario.
     * @param newStatus Nuevo estado.
     */
    void updateStatus(long userId, UserStatus newStatus);

    /**
     * Actualiza la contraseña encriptada de un usuario.
     *
     * @param userId          ID del usuario.
     * @param encryptedPassword Nueva contraseña ya encriptada.
     */
    void updatePassword(long userId, String encryptedPassword);
}
