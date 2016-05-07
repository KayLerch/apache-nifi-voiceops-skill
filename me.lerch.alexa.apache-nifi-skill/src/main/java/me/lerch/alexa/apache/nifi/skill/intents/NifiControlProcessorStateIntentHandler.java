package me.lerch.alexa.apache.nifi.skill.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import me.lerch.alexa.apache.nifi.skill.utils.ConfigUtils;
import me.lerch.alexa.apache.nifi.skill.utils.NifiRestApiUtils;
import me.lerch.alexa.apache.nifi.skill.wrapper.AbstractIntentHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kay on 29.04.2016.
 */
public class NifiControlProcessorStateIntentHandler extends AbstractIntentHandler {
    private Map<String, String> processorIds = new HashMap<String, String>();
    private final String intentName = ConfigUtils.getAlexaIntentControlProcessor();
    private final String processorIdTwitter = ConfigUtils.getNifiProcessorIdGetTwitter();
    private final String SlotNameDesiredState = ConfigUtils.getAlexaSlotDesiredProcessorState();
    private final String SlotNameProcessorAlias = ConfigUtils.getAlexaSlotProcessAlias();
    private final String nifiRestApiUrl = ConfigUtils.getNifiApiUrl();

    public NifiControlProcessorStateIntentHandler() {
        processorIds.put("twitter", processorIdTwitter);
    }

    @Override
    public String getIntentName() {
        return intentName;
    }

    @Override
    public SpeechletResponse handleIntentRequest(Intent intent, Session session) {
        String desiredState = intent.getSlots().containsKey(SlotNameDesiredState) ?
                intent.getSlot(SlotNameDesiredState).getValue() : null;

        String processorAlias = intent.getSlots().containsKey(SlotNameProcessorAlias) ?
                intent.getSlot(SlotNameProcessorAlias).getValue() : null;

        if (desiredState == null || desiredState.isEmpty())
            return getErrorResponse("Could not get the desired state.");
        if (processorAlias == null || processorAlias.isEmpty() || !processorIds.containsKey(processorAlias))
            return getErrorResponse("Could not control this processor.");

        String processorId = processorIds.get(processorAlias);
        String clientId = this.getClass().getSimpleName();

        try {
            if ("launch".equals(desiredState)) {
                NifiRestApiUtils.startProcessor(nifiRestApiUrl, clientId, processorId);
                return getResponse("running");
            }
            else if ("stop".equals(desiredState)) {
                NifiRestApiUtils.stopProcessor(nifiRestApiUrl, clientId, processorId);
                return getResponse("stopped");
            }
            else {
                return getErrorResponse("I don't know how to put this processor in " + desiredState + " state.");
            }
        } catch (Exception e) {
            return getErrorResponse();
        }
    }

    private SpeechletResponse getResponse(String processorState) {
        String strContent = "<phoneme alphabet=\"ipa\" ph=\"naɪfaɪ\">NiFi</phoneme> processor is now in " + processorState + " state.";
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml("<speak>" + strContent + "</speak>");

        SimpleCard card = new SimpleCard();
        card.setContent(strContent);

        SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
        response.setShouldEndSession(false);
        return response;
    }
}
