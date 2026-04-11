package app.ports.output;

import app.models.AccountStatement;
import app.models.AccountType;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (driven port) para la persistencia de cuentas bancarias.
 * La implementación real (JPA, en memoria, etc.) vive en la capa de infraestructura.
 */
public interface AccountRepositoryPort {

    /**
     * Guarda o actualiza una cuenta bancaria.
     * Se representa con un número de cuenta y sus atributos clave.
     *
     * @param accountNumber Número de cuenta.
     * @param clientId      ID del cliente propietario.
     * @param accountType   Tipo de cuenta.
     * @param balance       Saldo inicial o actualizado.
     * @param status        Estado de la cuenta.
     */
    void save(String accountNumber, long clientId, AccountType accountType,
              double balance, AccountStatement status);

    /**
     * Busca una cuenta por su número.
     *
     * @param accountNumber Número de cuenta.
     * @return Los datos de la cuenta si existe.
     */
    Optional<Object> findByAccountNumber(String accountNumber);

    /**
     * Lista todos los números de cuenta de un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de números de cuenta.
     */
    List<String> findByClientId(long clientId);

    /**
     * Actualiza el saldo de una cuenta.
     *
     * @param accountNumber Número de cuenta.
     * @param newBalance    Nuevo saldo.
     */
    void updateBalance(String accountNumber, double newBalance);

    /**
     * Actualiza el estado de una cuenta.
     *
     * @param accountNumber Número de cuenta.
     * @param newStatus     Nuevo estado.
     */
    void updateStatus(String accountNumber, AccountStatement newStatus);

    /**
     * Verifica si existe una cuenta con el número dado.
     *
     * @param accountNumber Número de cuenta.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByAccountNumber(String accountNumber);
}
