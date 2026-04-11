package app.services;

import app.models.Loan;
import app.models.LoanStatus;
import app.models.TypeWorker;
import app.ports.input.LoanServicePort;
import app.ports.input.LogbookServicePort;
import app.ports.output.LoanRepositoryPort;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de aplicación para la gestión de préstamos.
 * Implementa LoanServicePort y aplica las reglas de negocio del ciclo de vida
 * de un préstamo: solicitud → aprobación/rechazo → desembolso.
 */
public class LoanService implements LoanServicePort {

    private final LoanRepositoryPort loanRepository;
    private final LogbookServicePort logbookService;

    public LoanService(LoanRepositoryPort loanRepository,
                       LogbookServicePort logbookService) {
        this.loanRepository = loanRepository;
        this.logbookService = logbookService;
    }

    @Override
    public Loan requestLoan(long clientId, String loanType, double amount, int termMonths) {
        validateAmount(amount);
        validateTermMonths(termMonths);

        Loan loan = new Loan();
        loan.setClientId(clientId);
        loan.setLoanType(loanType);
        loan.setAmount(amount);
        loan.setTermMonths(termMonths);
        loan.setStatus(LoanStatus.PENDING);

        Loan saved = loanRepository.save(loan);

        logbookService.registerOperation(
                "LOAN_REQUESTED", clientId, TypeWorker.ProductAdvisor,
                String.valueOf(saved.getId_loan()),
                "Solicitud de préstamo tipo " + loanType + " por valor " + amount
        );

        return saved;
    }

    @Override
    public void approveLoan(long loanId, double approvedAmount, double interestRate) {
        Loan loan = findLoanOrThrow(loanId);
        validateStatus(loan, LoanStatus.PENDING);
        validateAmount(approvedAmount);

        loanRepository.updateApprovalDetails(loanId, approvedAmount, interestRate);
        loanRepository.updateStatus(loanId, LoanStatus.APPROVED);

        logbookService.registerOperation(
                "LOAN_APPROVED", loan.getClientId(), TypeWorker.InternalBankAnalyst,
                String.valueOf(loanId),
                "Préstamo aprobado por " + approvedAmount + " con tasa " + interestRate + "%"
        );
    }

    @Override
    public void rejectLoan(long loanId, String reason) {
        Loan loan = findLoanOrThrow(loanId);
        validateStatus(loan, LoanStatus.PENDING);

        loanRepository.updateStatus(loanId, LoanStatus.REJECTED);

        logbookService.registerOperation(
                "LOAN_REJECTED", loan.getClientId(), TypeWorker.InternalBankAnalyst,
                String.valueOf(loanId),
                "Préstamo rechazado. Motivo: " + reason
        );
    }

    @Override
    public void disburseLoan(long loanId, long disbursementDestinationAccount) {
        Loan loan = findLoanOrThrow(loanId);
        validateStatus(loan, LoanStatus.APPROVED);

        loan.setDisbursement_Destination_Account(disbursementDestinationAccount);
        loan.setDisbursement_Date(Date.valueOf(LocalDate.now()));
        loanRepository.updateStatus(loanId, LoanStatus.DISBURSED);

        logbookService.registerOperation(
                "LOAN_DISBURSED", loan.getClientId(), TypeWorker.ApprovingUser,
                String.valueOf(loanId),
                "Desembolso a cuenta " + disbursementDestinationAccount
        );
    }

    @Override
    public LoanStatus getLoanStatus(long loanId) {
        return findLoanOrThrow(loanId).getStatus();
    }

    @Override
    public List<Loan> getLoansByClient(long clientId) {
        return loanRepository.findByClientId(clientId);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    private Loan findLoanOrThrow(long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un préstamo con ID: " + loanId));
    }

    private void validateStatus(Loan loan, LoanStatus expected) {
        if (loan.getStatus() != expected) {
            throw new IllegalStateException(
                    "El préstamo debe estar en estado " + expected.name()
                    + " pero está en " + loan.getStatus().name());
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }
    }

    private void validateTermMonths(int termMonths) {
        if (termMonths <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a 0 meses.");
        }
    }
}
