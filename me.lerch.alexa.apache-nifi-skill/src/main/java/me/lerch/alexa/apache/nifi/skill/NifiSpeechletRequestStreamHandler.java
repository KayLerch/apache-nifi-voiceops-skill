package me.lerch.alexa.apache.nifi.skill;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import me.lerch.alexa.apache.nifi.skill.speechlets.NifiSpeechlet;
import me.lerch.alexa.apache.nifi.skill.utils.ConfigUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kay on 27.04.2016.
 */
public class NifiSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();

    static {
        supportedApplicationIds.add(ConfigUtils.getAlexaAppId());
    }

    public NifiSpeechletRequestStreamHandler() {
        super(new NifiSpeechlet(), supportedApplicationIds);
    }
}
