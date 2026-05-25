package app.domain.models;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// se importa el Getter, Setter y NoArgsConstructor de lombok para generar automáticamente los métodos getter, setter y el constructor sin argumentos.
@Setter
@Getter
@NoArgsConstructor
// se define la clase abstracta Person, que servirá como base para otras clases que representen personas en el sistema.
public abstract class Person {

    private long id;
    private String name;
    private String document;
    private String phone;
    private String email;
    private String address;
    private Date birthDate;

}
