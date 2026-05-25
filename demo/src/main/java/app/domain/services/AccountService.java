package app.domain.services;

import app.domain.models.AccountStatement;
import app.domain.models.AccountType;
import app.domain.models.TypeWorker;
import app.domain.ports.input.AccountServicePort;
import app.domain.ports.input.LogbookServicePort;
import app.domain.ports.output.AccountRepositoryPort;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación para cuentas bancarias.
 * Implementa el puerto de entrada AccountServicePort y orquesta
 * la lógica de negocio usando los puertos de salida correspondientes.
 */
public class AccountService implements AccountServicePort {

    private final AccountRepositoryPort accountRepository;
    private final LogbookServicePort logbookService;

    public AccountService(AccountRepositoryPort accountRepository,
                          LogbookServicePort logbookService) {
        this.accountRepository = accountRepository;
        this.logbookService = logbookService;
    }

    @Override
    public String openAccount(long clientId, AccountType accountType) {
        String accountNumber = generateAccountNumber();
        accountRepository.save(accountNumber, clientId, accountType, 0.0, AccountStatement.ACTIVE);
        logbookService.registerOperation(
                "ACCOUNT_OPEN", clientId, TypeWorker.cashier,
                accountNumber, "Apertura de cuenta tipo " + accountType.name()
        );
        return accountNumber;
    }

    @Override
    public void closeAccount(String accountNumber) {
        validateAccountExists(accountNumber);
        accountRepository.updateStatus(accountNumber, AccountStatement.INACTIVE);
        logbookService.registerOperation(
                "ACCOUNT_CLOSE", 0, TypeWorker.cashier,
                accountNumber, "Cierre de cuenta " + accountNumber
        );
    }

    @Override
    public void blockAccount(String accountNumber) {
        validateAccountExists(accountNumber);
        accountRepository.updateStatus(accountNumber, AccountStatement.BLOCKED);
        logbookService.registerOperation(
                "ACCOUNT_BLOCK", 0, TypeWorker.ApprovingUser,
                accountNumber, "Bloqueo de cuenta " + accountNumber
        );
    }

    @Override
    public void changeAccountStatus(String accountNumber, AccountStatement newStatus) {
        validateAccountExists(accountNumber);
        accountRepository.updateStatus(accountNumber, newStatus);
        logbookService.registerOperation(
                "ACCOUNT_STATUS_CHANGE", 0, TypeWorker.ApprovingUser,
                accountNumber, "Cambio de estado a " + newStatus.name()
        );
    }

    @Override
    public double getBalance(String accountNumber) {
        validateAccountExists(accountNumber);
        // La implementación concreta del repositorio retornará el objeto con el saldo.
        // Aquí se delega la consulta al puerto de salida.
        throw new UnsupportedOperationException(
                "Implementar cuando el repositorio retorne un objeto tipado de cuenta.");
    }

    @Override
    public List<String> getAccountsByClient(long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    private String generateAccountNumber() {
        // Genera un número de cuenta único de 16 dígitos.
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                     .substring(0, 10);
    }

    private void validateAccountExists(String accountNumber) {
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw new IllegalArgumentException(
                    "No existe una cuenta con el número: " + accountNumber);
        }
    }
}
