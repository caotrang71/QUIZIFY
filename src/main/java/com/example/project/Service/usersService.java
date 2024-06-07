package com.example.project.Service;

import com.example.project.Repository.rolesRepository;
import com.example.project.Repository.usersRepository;
import com.example.project.entity.roles;
import com.example.project.entity.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class usersService {
    @Autowired
    private usersRepository repo;
    @Autowired
    private rolesRepository roleRepo;

    public List<users> getAllUsers() {
        List<users> usersList = repo.findAll();
        return usersList;
    }

    public List<users> getUserByName(String userName) {
        List<users> usersList = repo.findByUsername(userName);
        return usersList;
    }

    public void deleteUsers(int id) {
        repo.deleteById(id);
    }
    public roles changeRoles(int id,int roleId, roles newRoles) {
        Optional<roles> optionalRole = roleRepo.findById(roleId);

        if (optionalRole.isPresent()) {
            roles changeRole = optionalRole.get();
            changeRole.setType_of_role(newRoles.getType_of_role());
            return roleRepo.save(changeRole);

        } else {
            return null;
        }
    }

}
