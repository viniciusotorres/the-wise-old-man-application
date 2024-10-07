package com.example.the_wise_old_man.controller.question;

import com.example.the_wise_old_man.dto.question.ResponseCorrect;
import com.example.the_wise_old_man.dto.question.ResponseQuestion;
import com.example.the_wise_old_man.model.Question;
import com.example.the_wise_old_man.repository.QuestionRepository;
import com.example.the_wise_old_man.service.players.PlayersService;
import com.example.the_wise_old_man.service.trivia.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/questions")
public class QuestionCotnroller {

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PlayersService playersService;

    @GetMapping("/random-main")
    public ResponseEntity<List<Question>> getQuestions(@RequestParam int amount) {
        List<Question> questions = triviaService.getQuestions(amount);
        return ResponseEntity.ok(questions);
    }



    @PostMapping("/check")
    public ResponseEntity<ResponseCorrect> checkAnswer(@RequestBody ResponseQuestion answerRequest) {
        Question question = questionRepository.findById(Long.valueOf(answerRequest.questionId())).orElse(null);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        boolean isCorrect = !question.getIncorrectAnswers().contains(answerRequest.answer());

        int xpGained = 0;
        if (isCorrect) {
            xpGained = 200;
            playersService.updateXpOfPlayer(answerRequest.playerId(), xpGained);
        }

        ResponseCorrect response = new ResponseCorrect(
                isCorrect ? "Parabéns! Você acertou..." : "Opa! Infelizmente está incorreta.!",
                xpGained,
                answerRequest.playerId()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/random")
    public ResponseEntity<Question> getRandomQuestion() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Question randomQuestion = questions.get(new Random().nextInt(questions.size()));
        return ResponseEntity.ok(randomQuestion);
    }
}
