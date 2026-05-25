package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

//transferencia
public class Transfer {

    private long idTransfer;//id de la transferencia
    private long originAccount;//cuenta de origen
    private long destinationAccount;//cuenta de destino
    private double amount;//monto
    private LocalDateTime creationDate;//fecha de creacion
    private LocalDateTime approvalDate;//fecha de aprobacion
    private TransferStatus status;//estado de la transferencia
    private long creator_User_ID;//ID del usuario que crea la transferencia
    private long approver_User_ID;//ID del usuario que aprueba la transferencia

}
