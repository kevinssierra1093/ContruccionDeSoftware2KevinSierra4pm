package app.domain.ports.output;

import app.domain.models.Loan;
import app.domain.models.LoanStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (driven port) para la persistencia de préstamos.
 */
public interface LoanRepositoryPort {

    /**
     * Guarda un nuevo préstamo.
     *
     * @param loan Objeto Loan a persistir.
     * @return El préstamo guardado con su ID asignado.
     */
    Loan save(Loan loan);

    /**
     * Busca un préstamo por su ID.
     *
     * @param loanId ID del préstamo.
     * @return El préstamo si existe.
     */
    Optional<Loan> findById(long loanId);

    /**
     * Lista todos los préstamos de un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de préstamos.
     */
    List<Loan> findByClientId(long clientId);

    /**
     * Actualiza el estado de un préstamo.
     *
     * @param loanId    ID del préstamo.
     * @param newStatus Nuevo estado.
     */
    void updateStatus(long loanId, LoanStatus newStatus);

    /**
     * Actualiza los detalles de aprobación de un préstamo.
     *
     * @param loanId         ID del préstamo.
     * @param approvedAmount Monto aprobado.
     * @param interestRate   Tasa de interés.
     */
    void updateApprovalDetails(long loanId, double approvedAmount, double interestRate);
}
