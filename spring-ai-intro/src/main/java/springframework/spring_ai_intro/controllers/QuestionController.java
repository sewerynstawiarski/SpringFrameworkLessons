package springframework.spring_ai_intro.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springframework.spring_ai_intro.model.Answer;
import springframework.spring_ai_intro.model.GetCapitalRequest;
import springframework.spring_ai_intro.model.Question;
import springframework.spring_ai_intro.services.OpenAiService;

@RestController
public class QuestionController {

    private final OpenAiService openAiService;

    public QuestionController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAiService.getAnswer(question);
    }
    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return openAiService.getCapital(getCapitalRequest);
    }
}
