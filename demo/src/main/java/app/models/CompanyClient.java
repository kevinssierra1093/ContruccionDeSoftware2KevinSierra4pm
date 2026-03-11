package app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CompanyClient extends Client {
    // La clase CompanyClient hereda de la clase Client y agrega el nombre de la
    // empresa y el número de identificación fiscal (NIT) de la empresa.

    private long idCompany;
    private String nameCompany;
    private String legalRepresentative;

}
