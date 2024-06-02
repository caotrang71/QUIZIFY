package com.example.project.Service;

import com.example.project.entity.roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class rolesService {
    @Autowired
    private com.example.project.Repository.rolesRepository rolesRepository;

    public List<roles> getRoles() {
        return rolesRepository.findAll();
    }

    public roles updateRole(int id, roles newRole) {
        Optional<roles> Oroles = rolesRepository.findById(id);
        if (Oroles.isPresent()) {
            roles changeRoles = Oroles.get();
            changeRoles.setType_of_role(newRole.getType_of_role());
            return rolesRepository.save(changeRoles);
        }else {
            return null;
        }
    }

    public void deleteRole(int id) {
        rolesRepository.deleteById(id);
    }
}
