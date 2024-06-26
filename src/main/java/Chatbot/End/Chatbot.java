package Chatbot.End;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Chatbot {
    private final List<String> asstHistory;      // history of all responses from LLM
    private final List<String> userHistory;    // history of all user prompts sent to LLM
    private String instruction = "You are a extremely helpful Java expert and will respond as one.";        // additional behavior (system)
    private String completion_format = "Please respond in Brazilian Portuguese";      // style or language of output
    private List<String> context = new ArrayList<>();

    private OpenAiChatModel service;

    Chatbot(String apikey) {
        service = new OpenAiChatModel.OpenAiChatModelBuilder()
                .apiKey(apikey)
                .timeout(Duration.ofSeconds(30))
                .maxTokens(4000)
                .build();
        asstHistory = new ArrayList<>();
        userHistory = new ArrayList<>();
    }

    /**  *****************************************************************************************
     * getCompletions() - create the prompt from the messages, get the result, adjust the histories
     * @param prompt
     * @return
     */
    public List<String> getCompletions(String prompt) {
        List<String> resultsFromLLM = new ArrayList<>();  // results coming back from LLM
        List<ChatMessage> messages = new ArrayList<>();

        // step 1 - how to behave
        ChatMessage systemMessage = SystemMessage.from(instruction);
        messages.add(systemMessage);

        // step 2 - prepend context (user and assistant msgs)
        addContext(messages);

        // step 3 - add the user's actual prompt
        final ChatMessage userMessage = UserMessage.from(prompt);
        messages.add(userMessage);

        // step 4 - specify the output format
        ChatMessage format = SystemMessage.from(completion_format);
        messages.add(format);

        showMessages(messages);     // Just to show the whole prompt sent to the LLM

        // Get the completions from the LLM
        Response<AiMessage> completion = service.generate(messages);

        // Collect the completions into a List
            resultsFromLLM.add(completion.content().text());

        appendAsstHistory(resultsFromLLM.get(0));   // Add the Assistant (LLM) response to the Asst history
                                                    // For this example, just add the 1st completion to the Asst history
        appendUserHistory(prompt);                  // Add the User's prompt to the Prompt history

        return resultsFromLLM;
    }

    /**
     * getCompletion() - convenience method to retrieve only the first completion
     * @param prompt
     * @return
     */
    public String getCompletion(String prompt) {
        return getCompletions(prompt).get(0);       // just the first one for now
    }

    /** *************************************************************************************
     * addContext - add the histories to the current Context
     * @param msg
     */
    public void addContext(List<ChatMessage> msg) {
        addUserHistory(msg);       // add the user prompt history
        addAsstHistory(msg);       // add the LLM (assistant) history
    }

    public void addAsstHistory(List<ChatMessage> msg) {
        for (int i = 0; i < asstHistory.size(); i++) {
            AiMessage p = AiMessage.from(asstHistory.get(i));
            msg.add(p);
        }
    }
    public void addUserHistory(List<ChatMessage> msg) {
        for (int i = 0; i < userHistory.size(); i++) {
            ChatMessage p = UserMessage.from(userHistory.get(i));
            msg.add(p);
        }
    }

    /**
     * appendAsstHistory() - Add a string to the Assistant history
     * @param asst
     * @return
     */
    public Boolean appendAsstHistory(String asst) {
        return this.asstHistory.add(asst);
    }

    /**
     * appendUserHistory() - Add a string to the User history
     * @param prompt
     * @return
     */
    public Boolean appendUserHistory(String prompt) {
        return this.userHistory.add(prompt);
    }

    /**
     * showMesssages() - useful display of all ChatMessages in a list
     * @param mlist
     */
    public static void showMessages(List<ChatMessage> mlist) {
        System.out.println("+START-----------------------------------------------------+ [" + mlist.size() + "]");
        for (ChatMessage cm : mlist) {
            switch (cm.type()) {
                case SYSTEM:
                    System.out.println("SYSTEM: " + cm.text());
                    break;
                case USER:
                    System.out.println(cm.text());
                    break;
                case AI:
                    System.out.println("  ASST: " + cm.text());
                    break;
                default:
                    System.out.println("UNDEFINED ROLE!!!!");
                    break;
            }
        }
        // mlist.forEach(cm -> System.out.println("MSG: " + cm.getContent().toString()));
        System.out.println("+END-------------------------------------------------------+");
    }
}

