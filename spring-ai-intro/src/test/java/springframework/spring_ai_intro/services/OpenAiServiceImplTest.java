package springframework.spring_ai_intro.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class OpenAiServiceImplTest {
    @Autowired
    OpenAiService openAiService;

    @Test
    void getAnswer() {
        String answer = openAiService.getAnswer("Tell me an older brother joke");
        System.out.println("Get the answer");
        System.out.println(answer);
    }
}