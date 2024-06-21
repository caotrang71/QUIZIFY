package com.quizify.repository;

import com.quizify.model.User;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;

import java.util.List;

public interface AccountRepository extends JpaAttributeConverter<User, Long> {
    List<User> findByFullNameContainingIgnoreCase(String fullName);
    List<User> findByEmail(String email);
}
