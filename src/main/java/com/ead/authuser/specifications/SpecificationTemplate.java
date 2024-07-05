package com.ead.authuser.specifications;

import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

/** Classe para especificar os filtros que não estão mapeados na Controller */
public class SpecificationTemplate {

    @And({  // Combina múltiplos campos para filtrar
        @Spec(path = "userType", spec = EqualIgnoreCase.class), // Filtra pelo Enum UserType pelo valor exato usando o EqualsIgnoreCase.class
        @Spec(path = "userStatus", spec = Equal.class), // Filtra pelo Enum UserStatus pelo valor exato usando o Equals.class
        @Spec(path = "email", spec = Like.class) // Filtra pelo campo email onde contém o valor semelhante ao valor especificado usando o Like.class
    })
    public interface UserSpec extends Specification<UserModel> {
    }
}
