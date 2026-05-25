package app.domain.models;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor

//préstamo
public class Loan {

    private long id_loan;// El ID del préstamo.
    private String loanType;// El tipo de préstamo, que puede ser personal, hipotecario, automotriz, entre otros.
    private long clientId;// El ID del cliente que solicitó el préstamo.

    private double amount;// El monto del préstamo.
    private double approvedamount;// El monto aprobado del préstamo.
    private double interestRate;// La tasa de interés del préstamo.
    private int termMonths;// El plazo del préstamo en meses.

    private LoanStatus status;// El estado del préstamo, que puede ser aprobado, rechazado, pendiente o desembolsado.
    private Date Approval_Date;// La fecha de aprobación del préstamo.
    private Date Disbursement_Date;// La fecha de desembolso del préstamo.
    private long Disbursement_Destination_Account;// El número de cuenta de destino del desembolso del préstamo.
    

   
}
