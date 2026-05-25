package app.domain.models;
//cliente
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Client extends Person {
//La clase cliente hereda de la clase persona y agrega el código del cliente, el tipo de cliente y un indicador de si el cliente está activo o no.

    private Long idClient;
    private String clientType;
    private boolean isActive;
    
}
