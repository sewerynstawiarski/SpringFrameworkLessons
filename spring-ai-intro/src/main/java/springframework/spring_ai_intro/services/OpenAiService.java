package springframework.spring_ai_intro.services;

import springframework.spring_ai_intro.model.*;

public interface OpenAiService {
    String getAnswer(String question);
    Answer getAnswer(Question question);

    GetCapitalResponse getCapital(GetCapitalRequest getCapitalRequest);
    GetCapitalWithInfo getCapitalWithInfo(GetCapitalRequest getCapitalRequest);
}
