package me.lerch.alexa.apache.nifi.skill.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Kay on 07.05.2016.
 */
public class ConfigUtils {
    private static Properties properties = new Properties();
    private static final String propertiesFile = "app.properties";

    static {
        InputStream propertiesStream = ConfigUtils.class.getClassLoader().getResourceAsStream(propertiesFile);
        try {
            properties.load(propertiesStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (propertiesStream != null) {
                try {
                    propertiesStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getNifiApiUrl() {
        return properties.getProperty("NifiApiUrl");
    }

    public static String getNifiProcessorIdGetTwitter() {
        return properties.getProperty("NifiProcessorIdGetTwitter");
    }

    public static String getNifiProcessorIdEvaluateJson() {
        return properties.getProperty("NifiProcessorIdEvaluateJson");
    }

    public static String getAlexaAppId() {
        return properties.getProperty("AlexaAppId");
    }

    public static String getAlexaIntentGetProvenance() {
        return properties.getProperty("AlexaIntentGetProvenance");
    }

    public static String getAlexaIntentGetKeywords() {
        return properties.getProperty("AlexaIntentGetKeywords");
    }

    public static String getAlexaIntentUpdateKeywords() {
        return properties.getProperty("AlexaIntentUpdateKeywords");
    }

    public static String getAlexaIntentControlProcessor() {
        return properties.getProperty("AlexaIntentControlProcessor");
    }

    public static String getAlexaSlotDesiredProcessorState() {
        return properties.getProperty("AlexaSlotDesiredProcessorState");
    }

    public static String getAlexaSlotProcessAlias() {
        return properties.getProperty("AlexaSlotProcessAlias");
    }

    public static String getAlexaSlotKeyword() {
        return properties.getProperty("AlexaSlotKeyword");
    }

    public static String getAlexaSlotKeywordA() {
        return properties.getProperty("AlexaSlotKeywordA");
    }

    public static String getAlexaSlotKeywordB() {
        return properties.getProperty("AlexaSlotKeywordB");
    }

    public static String getAlexaSlotKeywordC() {
        return properties.getProperty("AlexaSlotKeywordC");
    }

    public static String getAlexaSlotKeywordD() {
        return properties.getProperty("AlexaSlotKeywordD");
    }

    public static String getAlexaSlotColor() {
        return properties.getProperty("AlexaSlotColor");
    }
}
