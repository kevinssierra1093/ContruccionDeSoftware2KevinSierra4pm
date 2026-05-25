package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

//Bitácora.
public class Logbook {

    private long idLogbook;//Identificador único de la bitácora.
    private String operationType;//Categoria de la operación realizada.
    private LocalDateTime timestamp;//Marca de tiempo de la operación.
    private long userId;//Identificador del usuario que realizó la operación.
    private TypeWorker userRol;//Tipo de trabajador que realizó la operación.
    private String Affected_Product_ID;//Identificador del producto afectado por la operación.
    private String description;//Descripción detallada de la operación realizada.

}