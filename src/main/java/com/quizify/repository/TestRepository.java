package com.quizify.repository;

import com.quizify.model.Test;
import com.quizify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Test getTestById(long id);
    List<Test> getTestsByCreatedBy(User user);
}
