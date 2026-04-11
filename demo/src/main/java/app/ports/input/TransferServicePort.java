package app.ports.input;

import app.models.Transfer;
import app.models.TransferStatus;

import java.util.List;

/**
 * Puerto de entrada (driving port) para la gestión de transferencias.
 */
public interface TransferServicePort {

    /**
     * Crea y registra una nueva transferencia en estado PENDING.
     *
     * @param originAccount      Cuenta de origen.
     * @param destinationAccount Cuenta de destino.
     * @param amount             Monto a transferir.
     * @param creatorUserId      ID del usuario que crea la transferencia.
     * @return La transferencia creada.
     */
    Transfer createTransfer(long originAccount, long destinationAccount,
                            double amount, long creatorUserId);

    /**
     * Aprueba una transferencia pendiente y ejecuta el movimiento de fondos.
     *
     * @param transferId     ID de la transferencia.
     * @param approverUserId ID del usuario aprobador.
     */
    void approveTransfer(long transferId, long approverUserId);

    /**
     * Cancela una transferencia que aún esté en estado PENDING.
     *
     * @param transferId ID de la transferencia.
     */
    void cancelTransfer(long transferId);

    /**
     * Consulta el estado actual de una transferencia.
     *
     * @param transferId ID de la transferencia.
     * @return Estado actual.
     */
    TransferStatus getTransferStatus(long transferId);

    /**
     * Obtiene el historial de transferencias de una cuenta (como origen o destino).
     *
     * @param accountNumber Número de cuenta.
     * @return Lista de transferencias asociadas.
     */
    List<Transfer> getTransfersByAccount(long accountNumber);
}
