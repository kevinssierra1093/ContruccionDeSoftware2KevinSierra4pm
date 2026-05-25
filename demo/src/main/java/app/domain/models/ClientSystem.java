package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//cliente del sistema, que puede ser un cliente natural o jurídico, o un empleado de la empresa.
@Setter
@Getter
@NoArgsConstructor

public class ClientSystem extends Client {

    private long relatedID;// El ID relacionado con el cliente o empleado al que pertenece el usuario del
                           // sistema.
    private TypeClient systemRole;// El rol del cliente en el sistema, que puede ser natural o jurídico..

}
