package app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class UserSystem extends Client {

    private long relatedID;// El ID relacionado con el cliente o empleado al que pertenece el usuario del
                           // sistema.
    private TypeClient systemRole;// El rol del usuario en el sistema, que puede ser un cliente o un empleado.

}
