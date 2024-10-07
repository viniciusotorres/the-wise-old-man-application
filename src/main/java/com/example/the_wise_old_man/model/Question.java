package com.example.the_wise_old_man.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String type;

    private String difficulty;

    private String questionText;

    @ElementCollection
    private List<String> correctAnswers;

    @ElementCollection
    private List<String> incorrectAnswers;
}