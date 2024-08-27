package springframework.spring_ai_intro.services;

import springframework.spring_ai_intro.model.Answer;
import springframework.spring_ai_intro.model.GetCapitalRequest;
import springframework.spring_ai_intro.model.Question;

public interface OpenAiService {
    String getAnswer(String question);
    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalRequest getCapitalRequest);
}
