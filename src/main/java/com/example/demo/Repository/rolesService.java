package com.example.demo.Repository;

import com.example.demo.entity.roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class rolesService {
    @Autowired
    private rolesRepository rolesRepository;

    public List<roles> getRoles() {
        return rolesRepository.findAll();
    }
}
