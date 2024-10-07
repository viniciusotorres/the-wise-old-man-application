package com.example.the_wise_old_man.repository;

import com.example.the_wise_old_man.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}