package com.example.project.Service;

import com.example.project.Repository.rolesRepository;
import com.example.project.Repository.usersRepository;
import com.example.project.entity.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private usersRepository repo;
    @Autowired
    private rolesRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<users> getAllUsers() {
        List<users> usersList = repo.findAll();
        return usersList;
    }

    public void deleteUsers(int id) {
        repo.deleteById(id);
    }

    public void updateRole(int id,int roleID) {
        users user = repo.findById(id).orElse(null);

        if (user != null) {
            user.setRole_id(roleID);
            repo.save(user);
        }
    }

    public String encodePassword(String password) {
            return passwordEncoder.encode(password);
    }

    public boolean checkAccount(String email) {
        List<users> listUser =repo.findByEmail(email);
        if (listUser.isEmpty()) {
            return true;
        }else {
            return false;
        }
    }


}
