package app.ports.input;

import app.models.ClientSystem;
import app.models.UserStatus;

/**
 * Puerto de entrada (driving port) para la gestión de usuarios y autenticación.
 */
public interface UserServicePort {

    /**
     * Registra un nuevo usuario en el sistema, vinculado a un cliente natural o empresa.
     *
     * @param relatedId ID del cliente (natural o empresa) al que pertenece.
     * @param username  Nombre de usuario.
     * @param password  Contraseña en texto plano (será encriptada internamente).
     * @return El usuario del sistema creado.
     */
    ClientSystem registerUser(long relatedId, String username, String password);

    /**
     * Autentica un usuario y retorna un token de sesión.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña en texto plano.
     * @return Token de autenticación (JWT u otro mecanismo).
     */
    String login(String username, String password);

    /**
     * Cierra la sesión del usuario invalidando su token.
     *
     * @param token Token activo del usuario.
     */
    void logout(String token);

    /**
     * Cambia el estado de un usuario (activar, inactivar, suspender).
     *
     * @param userId    ID del usuario.
     * @param newStatus Nuevo estado a asignar.
     */
    void changeUserStatus(long userId, UserStatus newStatus);

    /**
     * Cambia la contraseña de un usuario autenticado.
     *
     * @param userId      ID del usuario.
     * @param oldPassword Contraseña actual.
     * @param newPassword Nueva contraseña.
     */
    void changePassword(long userId, String oldPassword, String newPassword);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @return El usuario encontrado, o null si no existe.
     */
    ClientSystem findByUsername(String username);
}
