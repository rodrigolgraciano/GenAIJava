package Chapter04.Begin;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static Utilities.Misc.getConfigProperties;

public class SimpleChatCompletion {
    public static void main(String[] args) throws IOException {

        String DEFAULT_CONFIG = "./src/main/resources/genai.properties";
        Properties prop = getConfigProperties(DEFAULT_CONFIG);
        if (prop == (Properties) null) {
            System.err.println("Cannot find OpenAI API key.  Your path to the properties is probably incorrect.");
            System.exit(1);
        }
        String token = prop.getProperty("chatgpt.apikey");

        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

        final List<ChatMessage> messages = new ArrayList<>();

        /*
         Create a System message and a User message
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), ... );
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), ...);
        */

        /* Add them to the messages List
        messages.add(...);
        messages.add(...);
        */

        /*
         Create a chatCompletionRequest with its builder

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(...model-name...)        // chat endpoint
                .messages(...your messages...)  // your chat “messages”
                .n(1)                          // num of completions
                .maxTokens(64)                 // max size of each completion
                .build();
        */

        /*
         Ask the LLM for completion

        List<ChatCompletionChoice> completions = service.createChatCompletion(chatCompletionRequest).getChoices();
        */

        /* Display the completions...
        for (ChatCompletionChoice s : completions) {
            System.out.println(s.getMessage().getContent().trim());
        }
         */
    }
}
