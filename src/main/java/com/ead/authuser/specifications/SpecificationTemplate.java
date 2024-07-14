package com.ead.authuser.specifications;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import lombok.ToString;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.UUID;

/** Classe para especificar os filtros que não estão mapeados na Controller */
@ToString
public class SpecificationTemplate {

    @And({  // Combina múltiplos campos para filtrar
        @Spec(path = "userType", spec = EqualIgnoreCase.class), // Filtra pelo Enum UserType pelo valor exato usando o EqualsIgnoreCase.class
        @Spec(path = "userStatus", spec = Equal.class), // Filtra pelo Enum UserStatus pelo valor exato usando o Equals.class
        @Spec(path = "email", spec = Like.class), // Filtra pelo campo email onde contém o valor semelhante ao valor especificado usando o Like.class
        @Spec(path = "fullName", spec = Like.class) // Filtra pelo campo fullName onde contém o valor semelhante ao valor especificado usando o Like.class
    })
    public interface UserSpec extends Specification<UserModel> { }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<UserModel, UserCourseModel> userProd = root.join("usersCourses"); //Realiza uma junção (join) entre UserModel e UserCourseModel com base na associação do atributo usersCourses.
            return criteriaBuilder.equal(userProd.get("courseId"), courseId); // Adiciona uma condição where à consulta, especificando que o 'courseId' no UserCourseModel deve ser igual ao 'courseId' fornecido como parâmetro.
        });
    }
}
