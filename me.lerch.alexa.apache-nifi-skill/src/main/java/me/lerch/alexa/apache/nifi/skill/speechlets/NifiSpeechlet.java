package me.lerch.alexa.apache.nifi.skill.speechlets;

import com.amazon.speech.ui.SsmlOutputSpeech;
import me.lerch.alexa.apache.nifi.skill.intents.NifiControlProcessorStateIntentHandler;
import me.lerch.alexa.apache.nifi.skill.intents.NifiGetProvenanceIntentHandler;
import me.lerch.alexa.apache.nifi.skill.intents.NifiGetTwitterKeywordsIntentHandler;
import me.lerch.alexa.apache.nifi.skill.intents.NifiUpdateTwitterKeywordsIntentHandler;
import me.lerch.alexa.apache.nifi.skill.wrapper.IIntentHandler;
import me.lerch.alexa.apache.nifi.skill.wrapper.AbstractSpeechlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kay on 25.04.2016.
 */
public class NifiSpeechlet extends AbstractSpeechlet {

    private final List<IIntentHandler> intentHandlers;

    public NifiSpeechlet() {
        intentHandlers = new ArrayList<>();
        intentHandlers.add(new NifiGetProvenanceIntentHandler());
        intentHandlers.add(new NifiUpdateTwitterKeywordsIntentHandler());
        intentHandlers.add(new NifiControlProcessorStateIntentHandler());
        intentHandlers.add(new NifiGetTwitterKeywordsIntentHandler());
    }

    @Override
    public String getSampleSpeech() {
        return "Start twitter processor";
    }

    @Override
    public List<IIntentHandler> getIntentHandlers() {
        return intentHandlers;
    }

    @Override
    public SsmlOutputSpeech getWelcomeSpeech() {
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml("<speak>Welcome to Apache <phoneme alphabet=\"ipa\" ph=\"naɪfaɪ\">NiFi</phoneme> voice instructor.</speak>");
        return outputSpeech;
    }
}
