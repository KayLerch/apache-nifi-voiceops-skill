package me.lerch.alexa.apache.nifi.skill.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import me.lerch.alexa.apache.nifi.skill.utils.ConfigUtils;
import me.lerch.alexa.apache.nifi.skill.utils.NifiRestApiUtils;
import me.lerch.alexa.apache.nifi.skill.wrapper.AbstractIntentHandler;
import java.io.IOException;
/**
 * Created by Kay on 28.04.2016.
 */
public class NifiUpdateTwitterKeywordsIntentHandler extends AbstractIntentHandler {
    private final String intentName = ConfigUtils.getAlexaIntentUpdateKeywords();
    private final String processorIdgetTwitter = ConfigUtils.getNifiProcessorIdGetTwitter();
    private String processorIdevaluateTwitter = ConfigUtils.getNifiProcessorIdEvaluateJson();
    private final String SlotNameKeywordA = ConfigUtils.getAlexaSlotKeywordA();
    private final String SlotNameKeywordB = ConfigUtils.getAlexaSlotKeywordB();
    private final String SlotNameKeywordC = ConfigUtils.getAlexaSlotKeywordC();
    private final String SlotNameKeywordD = ConfigUtils.getAlexaSlotKeywordD();
    private final String nifiRestApiUrl = ConfigUtils.getNifiApiUrl();

    @Override
    public String getIntentName() {
        return intentName;
    }

    @Override
    public SpeechletResponse handleIntentRequest(Intent intent, Session session) {
        String keywordA = intent.getSlots().containsKey(SlotNameKeywordA) ?
                intent.getSlot(SlotNameKeywordA).getValue() : null;
        String keywordB = intent.getSlots().containsKey(SlotNameKeywordB) ?
                intent.getSlot(SlotNameKeywordB).getValue() : null;
        String keywordC = intent.getSlots().containsKey(SlotNameKeywordC) ?
                intent.getSlot(SlotNameKeywordC).getValue() : null;
        String keywordD = intent.getSlots().containsKey(SlotNameKeywordD) ?
                intent.getSlot(SlotNameKeywordD).getValue() : null;

        if (keywordA == null && keywordB == null && keywordC == null && keywordD == null)
            return getErrorResponse("Not all keywords were provided.");

        try {
            updateKeywords(keywordA, keywordB, keywordC, keywordD);
            return getResponse(keywordA, keywordB, keywordC, keywordD);
        } catch (Exception e) {
            return getErrorResponse();
        }
    }

    private void updateKeywords(String keyword1, String keyword2, String keyword3, String keyword4) throws IOException {
        String terms =
                (keyword1 != null && !keyword1.isEmpty() ? keyword1 + "," : "") +
                        (keyword2 != null && !keyword2.isEmpty() ? keyword2 + "," : "") +
                                (keyword3 != null && !keyword3.isEmpty() ? keyword3 + "," : "") +
                                        (keyword4 != null && !keyword4.isEmpty() ? keyword4 : "");

        if (terms.endsWith(",")) terms = terms.substring(0, terms.length() - 1);
        if (terms.isEmpty()) throw new IOException("No keywords were provided.");

        String clientId = this.getClass().getSimpleName();

        // stop processors
        NifiRestApiUtils.stopProcessor(nifiRestApiUrl, clientId, processorIdgetTwitter);
        NifiRestApiUtils.stopProcessor(nifiRestApiUrl, clientId, processorIdevaluateTwitter);

        // update twitter terms
        NifiRestApiUtils.updateTwitterTerms(nifiRestApiUrl, clientId, processorIdgetTwitter, processorIdevaluateTwitter, terms);

        // start processor
        NifiRestApiUtils.startProcessor(nifiRestApiUrl, clientId, processorIdevaluateTwitter);
        NifiRestApiUtils.startProcessor(nifiRestApiUrl, clientId, processorIdgetTwitter);
    }

    private SpeechletResponse getResponse(String keyword1, String keyword2, String keyword3, String keyword4) {
        String strContent = "Now indicating tweets about " + keyword1 + " with white, about " + keyword2 + " with green, about " + keyword3 + " with red, about " + keyword4 + " with yellow light.";
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml("<speak>" + strContent + "</speak>");

        SimpleCard card = new SimpleCard();
        card.setContent(strContent);

        SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
        response.setShouldEndSession(false);
        return response;
    }
}
