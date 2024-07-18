package com.ead.authuser.services.implementations;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserServiceInterface;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService  implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCourseRepository userCourseRepository;

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(UserModel userModel) {

        List<UserCourseModel> userCourseModelList = userCourseRepository.findAllUserCourseIntoUser(userModel.getUserId());

        if(!userCourseModelList.isEmpty()){
            userCourseRepository.deleteAll(userCourseModelList);  // Deleta da tabela TB_USERS_COURSES todos os registros vinculados a esse userId
        }

//        TODO: Falta fazer a deleção também no outro micro-serviço Course, relacionado a esse 'userId' para manter a consistência dos dados em ambos micro-serviços

        userRepository.delete(userModel);
    }

    @Override
    public void save(UserModel entityModel) {
        userRepository.save(entityModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAllPaged(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);

    }

}
