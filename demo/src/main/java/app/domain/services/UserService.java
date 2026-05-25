package app.domain.services;

import app.domain.models.ClientSystem;
import app.domain.models.UserStatus;
import app.domain.models.TypeWorker;
import app.domain.ports.input.LogbookServicePort;
import app.domain.ports.input.UserServicePort;
import app.domain.ports.output.UserRepositoryPort;

/**
 * Servicio de aplicación para la gestión de usuarios y autenticación.
 * Implementa UserServicePort. La generación de tokens y el cifrado de
 * contraseñas se delegan a componentes de infraestructura (inyectados
 * como interfaces, no como dependencias concretas).
 */
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepository;
    private final LogbookServicePort logbookService;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenGeneratorPort tokenGenerator;

    public UserService(UserRepositoryPort userRepository,
                       LogbookServicePort logbookService,
                       PasswordEncoderPort passwordEncoder,
                       TokenGeneratorPort tokenGenerator) {
        this.userRepository = userRepository;
        this.logbookService = logbookService;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public ClientSystem registerUser(long relatedId, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el nombre: " + username);
        }

        ClientSystem user = new ClientSystem();
        user.setRelatedID(relatedId);
        // El nombre de usuario se almacena en el campo 'document' como identificador único.
        user.setDocument(username);
        // La contraseña se encripta antes de persistir.
        String encoded = passwordEncoder.encode(password);
        user.setEmail(encoded); // campo usado temporalmente; reemplazar con campo dedicado.
        user.setActive(true);

        ClientSystem saved = userRepository.save(user);

        logbookService.registerOperation(
                "USER_REGISTERED", saved.getId(), TypeWorker.OperationalUseroftheCompany,
                String.valueOf(saved.getId()),
                "Registro de usuario para cliente ID " + relatedId
        );

        return saved;
    }

    @Override
    public String login(String username, String password) {
        ClientSystem user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Credenciales inválidas."));

        if (!user.isActive()) {
            throw new IllegalStateException("El usuario está inactivo o suspendido.");
        }

        // La contraseña almacenada (en email de forma temporal) se compara con la ingresada.
        if (!passwordEncoder.matches(password, user.getEmail())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        String token = tokenGenerator.generateToken(user.getId(), username);

        logbookService.registerOperation(
                "USER_LOGIN", user.getId(), TypeWorker.OperationalUseroftheCompany,
                String.valueOf(user.getId()),
                "Inicio de sesión del usuario " + username
        );

        return token;
    }

    @Override
    public void logout(String token) {
        // Invalidación del token delegada al componente de infraestructura.
        tokenGenerator.invalidateToken(token);
        logbookService.registerOperation(
                "USER_LOGOUT", 0, TypeWorker.OperationalUseroftheCompany,
                token, "Cierre de sesión con token " + token
        );
    }

    @Override
    public void changeUserStatus(long userId, UserStatus newStatus) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un usuario con ID: " + userId));
        userRepository.updateStatus(userId, newStatus);

        logbookService.registerOperation(
                "USER_STATUS_CHANGE", userId, TypeWorker.ApprovingUser,
                String.valueOf(userId),
                "Cambio de estado de usuario a " + newStatus.name()
        );
    }

    @Override
    public void changePassword(long userId, String oldPassword, String newPassword) {
        ClientSystem user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un usuario con ID: " + userId));

        if (!passwordEncoder.matches(oldPassword, user.getEmail())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        userRepository.updatePassword(userId, passwordEncoder.encode(newPassword));

        logbookService.registerOperation(
                "USER_PASSWORD_CHANGE", userId, TypeWorker.OperationalUseroftheCompany,
                String.valueOf(userId), "Cambio de contraseña del usuario " + userId
        );
    }

    @Override
    public ClientSystem findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // -------------------------------------------------------------------------
    // Sub-puertos de infraestructura (también driven ports)
    // -------------------------------------------------------------------------

    /**
     * Puerto de salida para el cifrado de contraseñas.
     * La implementación concreta usará BCrypt u otro algoritmo.
     */
    public interface PasswordEncoderPort {
        String encode(String rawPassword);
        boolean matches(String rawPassword, String encodedPassword);
    }

    /**
     * Puerto de salida para la generación e invalidación de tokens.
     * La implementación concreta usará JWT u otro mecanismo.
     */
    public interface TokenGeneratorPort {
        String generateToken(long userId, String username);
        void invalidateToken(String token);
    }
}
