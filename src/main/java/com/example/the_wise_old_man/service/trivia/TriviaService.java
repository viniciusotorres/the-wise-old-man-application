package com.example.the_wise_old_man.service.trivia;

import com.example.the_wise_old_man.model.Question;
import com.example.the_wise_old_man.repository.QuestionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TriviaService {

    @Autowired
    private QuestionRepository questionRepository;

    private final String apiUrl = "https://the-trivia-api.com/api/questions";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TriviaService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<Question> getQuestions(int amount) {
        String url = apiUrl + "?limit=" + amount;
        String response = restTemplate.getForObject(url, String.class);
        return parseQuestions(response);
    }

    private List<Question> parseQuestions(String response) {
        List<Question> questions = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            for (JsonNode node : root) {
                Question question = new Question();
                question.setCategory(getTextValue(node, "category"));
                question.setType(getTextValue(node, "type"));
                question.setDifficulty(getTextValue(node, "difficulty"));
                question.setQuestionText(getTextValue(node, "question"));
                question.setCorrectAnswers(List.of(getTextValue(node, "correctAnswer")));
                List<String> incorrectAnswers = new ArrayList<>();
                for (JsonNode incorrectAnswer : node.path("incorrectAnswers")) {
                    incorrectAnswers.add(incorrectAnswer.asText());
                }
                question.setIncorrectAnswers(incorrectAnswers);
                questions.add(question);
                questionRepository.save(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null ? field.asText() : null;
    }
}