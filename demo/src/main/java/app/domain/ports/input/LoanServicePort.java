package app.domain.ports.input;

import app.domain.models.Loan;
import app.domain.models.LoanStatus;

import java.util.List;

/**
 * Puerto de entrada (driving port) para la gestión de préstamos.
 */
public interface LoanServicePort {

    /**
     * Registra una solicitud de préstamo para un cliente.
     *
     * @param clientId     ID del cliente solicitante.
     * @param loanType     Tipo de préstamo (personal, hipotecario, etc.).
     * @param amount       Monto solicitado.
     * @param termMonths   Plazo en meses.
     * @return El préstamo creado en estado PENDING.
     */
    Loan requestLoan(long clientId, String loanType, double amount, int termMonths);

    /**
     * Aprueba un préstamo pendiente, definiendo el monto aprobado y la tasa de interés.
     *
     * @param loanId          ID del préstamo.
     * @param approvedAmount  Monto aprobado por el analista.
     * @param interestRate    Tasa de interés asignada.
     */
    void approveLoan(long loanId, double approvedAmount, double interestRate);

    /**
     * Rechaza un préstamo pendiente.
     *
     * @param loanId ID del préstamo.
     * @param reason Motivo del rechazo (para registrar en bitácora).
     */
    void rejectLoan(long loanId, String reason);

    /**
     * Desembolsa un préstamo aprobado hacia una cuenta destino.
     *
     * @param loanId                      ID del préstamo.
     * @param disbursementDestinationAccount Cuenta de destino del desembolso.
     */
    void disburseLoan(long loanId, long disbursementDestinationAccount);

    /**
     * Consulta el estado actual de un préstamo.
     *
     * @param loanId ID del préstamo.
     * @return Estado actual del préstamo.
     */
    LoanStatus getLoanStatus(long loanId);

    /**
     * Obtiene todos los préstamos asociados a un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de préstamos.
     */
    List<Loan> getLoansByClient(long clientId);
}
