package me.lerch.alexa.apache.nifi.skill.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import me.lerch.alexa.apache.nifi.skill.utils.ConfigUtils;
import me.lerch.alexa.apache.nifi.skill.utils.NifiRestApiUtils;
import me.lerch.alexa.apache.nifi.skill.wrapper.AbstractIntentHandler;
/**
 * Created by Kay on 25.04.2016.
 */
public class NifiGetProvenanceIntentHandler extends AbstractIntentHandler {
    private final String intentName = ConfigUtils.getAlexaIntentGetProvenance();
    private final String SlotNameKeyword = ConfigUtils.getAlexaSlotKeyword();
    private String nifiRestApiUrl = ConfigUtils.getNifiApiUrl();
    private String processorIdevaluateTwitter = ConfigUtils.getNifiProcessorIdEvaluateJson();

    @Override
    public String getIntentName() {
        return intentName;
    }

    @Override
    public SpeechletResponse handleIntentRequest(Intent intent, Session session) {
        String searchTerm = intent.getSlots().containsKey(SlotNameKeyword) ?
                intent.getSlot(SlotNameKeyword).getValue() : null;

        if (searchTerm == null)
            return getErrorResponse("No search-term was provided.");

        String clientId = this.getClass().getSimpleName();

        try {
            Long tweetCount = NifiRestApiUtils.countSearchHitsInProvenanceRepository(nifiRestApiUrl, clientId, "twitter.text", searchTerm.toLowerCase(), processorIdevaluateTwitter, 10);
            return getResponse(tweetCount, searchTerm);
        } catch (Exception e) {
            return getErrorResponse();
        }
    }

    private SpeechletResponse getResponse(Long tweetCount, String searchTerm) {
        String strContent = "I found " + tweetCount + " tweets about " + searchTerm + " in the <phoneme alphabet=\"ipa\" ph=\"naɪfaɪ\">NiFi</phoneme> provenance repository.";
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml("<speak>" + strContent + "</speak>");

        SimpleCard card = new SimpleCard();
        card.setContent(strContent);

        SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
        response.setShouldEndSession(false);
        return response;
    }
}
