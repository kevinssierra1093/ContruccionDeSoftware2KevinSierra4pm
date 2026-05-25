package app.domain.services;

import app.domain.models.Transfer;
import app.domain.models.TransferStatus;
import app.domain.models.TypeWorker;
import app.domain.ports.input.LogbookServicePort;
import app.domain.ports.input.TransferServicePort;
import app.domain.ports.output.AccountRepositoryPort;
import app.domain.ports.output.TransferRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para transferencias bancarias.
 * Implementa TransferServicePort y coordina la lógica de negocio
 * entre el repositorio de transferencias y el de cuentas.
 */
public class TransferService implements TransferServicePort {

    private final TransferRepositoryPort transferRepository;
    private final AccountRepositoryPort accountRepository;
    private final LogbookServicePort logbookService;

    public TransferService(TransferRepositoryPort transferRepository,
                           AccountRepositoryPort accountRepository,
                           LogbookServicePort logbookService) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.logbookService = logbookService;
    }

    @Override
    public Transfer createTransfer(long originAccount, long destinationAccount,
                                   double amount, long creatorUserId) {
        validateAmount(amount);
        validateAccountExists(String.valueOf(originAccount));
        validateAccountExists(String.valueOf(destinationAccount));

        Transfer transfer = new Transfer();
        transfer.setOriginAccount(originAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setAmount(amount);
        transfer.setCreationDate(LocalDateTime.now());
        transfer.setStatus(TransferStatus.PENDING);
        transfer.setCreator_User_ID(creatorUserId);

        Transfer saved = transferRepository.save(transfer);

        logbookService.registerOperation(
                "TRANSFER_CREATED", creatorUserId, TypeWorker.cashier,
                String.valueOf(saved.getIdTransfer()),
                "Transferencia de " + amount + " desde cuenta " + originAccount
                + " hacia " + destinationAccount
        );

        return saved;
    }

    @Override
    public void approveTransfer(long transferId, long approverUserId) {
        Transfer transfer = findTransferOrThrow(transferId);
        validateStatus(transfer, TransferStatus.PENDING);

        // Aquí iría la lógica de débito/crédito entre cuentas cuando el
        // repositorio de cuentas maneje objetos tipados con saldo.
        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setApprovalDate(LocalDateTime.now());
        transfer.setApprover_User_ID(approverUserId);
        transferRepository.updateStatus(transferId, TransferStatus.COMPLETED);

        logbookService.registerOperation(
                "TRANSFER_APPROVED", approverUserId, TypeWorker.ApprovingUser,
                String.valueOf(transferId),
                "Transferencia " + transferId + " aprobada por usuario " + approverUserId
        );
    }

    @Override
    public void cancelTransfer(long transferId) {
        Transfer transfer = findTransferOrThrow(transferId);
        validateStatus(transfer, TransferStatus.PENDING);

        transferRepository.updateStatus(transferId, TransferStatus.CANCELLED);

        logbookService.registerOperation(
                "TRANSFER_CANCELLED", transfer.getCreator_User_ID(), TypeWorker.cashier,
                String.valueOf(transferId),
                "Transferencia " + transferId + " cancelada."
        );
    }

    @Override
    public TransferStatus getTransferStatus(long transferId) {
        return findTransferOrThrow(transferId).getStatus();
    }

    @Override
    public List<Transfer> getTransfersByAccount(long accountNumber) {
        return transferRepository.findByAccount(accountNumber);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    private Transfer findTransferOrThrow(long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una transferencia con ID: " + transferId));
    }

    private void validateStatus(Transfer transfer, TransferStatus expected) {
        if (transfer.getStatus() != expected) {
            throw new IllegalStateException(
                    "La transferencia debe estar en estado " + expected.name()
                    + " pero está en " + transfer.getStatus().name());
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto de la transferencia debe ser mayor a 0.");
        }
    }

    private void validateAccountExists(String accountNumber) {
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw new IllegalArgumentException(
                    "No existe una cuenta con el número: " + accountNumber);
        }
    }
}
