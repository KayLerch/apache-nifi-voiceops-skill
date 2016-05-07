package me.lerch.alexa.apache.nifi.skill.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import me.lerch.alexa.apache.nifi.skill.utils.ConfigUtils;
import me.lerch.alexa.apache.nifi.skill.utils.NifiRestApiUtils;
import me.lerch.alexa.apache.nifi.skill.wrapper.AbstractIntentHandler;

import java.util.ArrayList;

/**
 * Created by Kay on 29.04.2016.
 */
public class NifiGetTwitterKeywordsIntentHandler extends AbstractIntentHandler {
    private final String intentName = ConfigUtils.getAlexaIntentGetKeywords();
    private final String SlotNameColor = ConfigUtils.getAlexaSlotColor();
    private final String processorIdTwitter = ConfigUtils.getNifiProcessorIdGetTwitter();
    private String nifiRestApiUrl = ConfigUtils.getNifiApiUrl();

    @Override
    public String getIntentName() {
        return intentName;
    }

    @Override
    public SpeechletResponse handleIntentRequest(Intent intent, Session session) {
        String color = intent.getSlots().containsKey(SlotNameColor) ?
                intent.getSlot(SlotNameColor).getValue() : null;

        String clientId = this.getClass().getSimpleName();

        try {
            String twitterKeywords = NifiRestApiUtils.getTwitterTerms(nifiRestApiUrl, clientId, processorIdTwitter);
            return getResponse(twitterKeywords, color);
        } catch (Exception e) {
            return getErrorResponse(e.getMessage());
        }
    }

    private SpeechletResponse getResponse(String twitterKeywords, String color) {
        String[] keywords = twitterKeywords.split(",");

        ArrayList<String> indications = new ArrayList<>();
        if (keywords.length > 0 && (color == null || "white".equals(color))) {
            indications.add("about " + keywords[0] + " with white light");
        }
        if (keywords.length > 1 && (color == null || "green".equals(color))) {
            indications.add("about " + keywords[1] + " with green light");
        }
        if (keywords.length > 2 && (color == null || "red".equals(color))) {
            indications.add("about " + keywords[2] + " with red light");
        }
        if (keywords.length > 3 && (color == null || "yellow".equals(color))) {
            indications.add("about " + keywords[3] + " with yellow light");
        }

        String indicationsText = "";
        for (int i = 1; i <= indications.size(); i++) {
            indicationsText += indications.get(i - 1) + ((i < indications.size()) ? ", " : "");
        }

        String strContent = "Indicating tweets " + indicationsText + ".";
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml("<speak>" + strContent + "</speak>");

        SimpleCard card = new SimpleCard();
        card.setContent(strContent);

        SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
        response.setShouldEndSession(false);
        return response;
    }
}
