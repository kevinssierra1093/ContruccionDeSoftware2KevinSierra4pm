package app.ports.output;

import app.models.Transfer;
import app.models.TransferStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (driven port) para la persistencia de transferencias.
 */
public interface TransferRepositoryPort {

    /**
     * Guarda una nueva transferencia.
     *
     * @param transfer Objeto Transfer a persistir.
     * @return La transferencia guardada con su ID asignado.
     */
    Transfer save(Transfer transfer);

    /**
     * Busca una transferencia por su ID.
     *
     * @param transferId ID de la transferencia.
     * @return La transferencia si existe.
     */
    Optional<Transfer> findById(long transferId);

    /**
     * Lista todas las transferencias donde la cuenta es origen o destino.
     *
     * @param accountNumber Número de cuenta.
     * @return Lista de transferencias asociadas.
     */
    List<Transfer> findByAccount(long accountNumber);

    /**
     * Actualiza el estado de una transferencia.
     *
     * @param transferId ID de la transferencia.
     * @param newStatus  Nuevo estado.
     */
    void updateStatus(long transferId, TransferStatus newStatus);
}
