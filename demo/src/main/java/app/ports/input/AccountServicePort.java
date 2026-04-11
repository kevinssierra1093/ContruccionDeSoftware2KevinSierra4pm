package app.ports.input;

import app.models.AccountStatement;
import app.models.AccountType;

import java.util.List;

/**
 * Puerto de entrada (driving port) para la gestión de cuentas bancarias.
 * Define los casos de uso disponibles desde el exterior (controllers, etc.).
 */
public interface AccountServicePort {

    /**
     * Abre una nueva cuenta bancaria para un cliente.
     *
     * @param clientId   ID del cliente propietario de la cuenta.
     * @param accountType Tipo de cuenta a abrir (ahorros, corriente, etc.).
     * @return Número de cuenta generado.
     */
    String openAccount(long clientId, AccountType accountType);

    /**
     * Cierra una cuenta bancaria existente.
     *
     * @param accountNumber Número de cuenta a cerrar.
     */
    void closeAccount(String accountNumber);

    /**
     * Bloquea una cuenta bancaria.
     *
     * @param accountNumber Número de cuenta a bloquear.
     */
    void blockAccount(String accountNumber);

    /**
     * Cambia el estado de una cuenta bancaria.
     *
     * @param accountNumber Número de cuenta.
     * @param newStatus     Nuevo estado a asignar.
     */
    void changeAccountStatus(String accountNumber, AccountStatement newStatus);

    /**
     * Consulta el saldo actual de una cuenta.
     *
     * @param accountNumber Número de cuenta.
     * @return Saldo disponible.
     */
    double getBalance(String accountNumber);

    /**
     * Obtiene todos los números de cuenta asociados a un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de números de cuenta.
     */
    List<String> getAccountsByClient(long clientId);
}
