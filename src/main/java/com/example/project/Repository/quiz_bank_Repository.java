package com.example.project.Repository;

import com.example.project.entity.quiz_banks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface quiz_bank_Repository extends JpaRepository<quiz_banks, Integer> {
    List<quiz_banks> findByBankNameContainingIgnoreCase(String bank_name);
    List<quiz_banks> findAllByOrderByBankNameAsc();
    List<quiz_banks> findAllByOrderByBankNameDesc();
    List<quiz_banks> findAllByOrderByCreateAtDesc();//moi nhat
    List<quiz_banks> findAllByOrderByCreateAtAsc();//cux nhat

}
