package me.lerch.alexa.apache.nifi.skill.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by Kay on 29.04.2016.
 */
public class JsonUtils {
    public static String extractJsonValue(String json, String strJsonPath) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode jsonRoot = m.readTree(json);
        JsonNode jsonUri = jsonRoot.at(strJsonPath);
        return jsonUri != null ? jsonUri.textValue() : "";
    }

    public static JsonNode extractJson(String json, String strJsonPath) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode jsonRoot = m.readTree(json);
        return jsonRoot.at(strJsonPath);
    }

    public static String extractJsonValue(HttpResponse response, String strJsonPath) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            ObjectMapper m = new ObjectMapper();
            JsonNode jsonRoot = m.readTree(IOUtils.toString(entity.getContent(), "UTF-8"));
            JsonNode jsonUri = jsonRoot.at(strJsonPath);
            return jsonUri != null ? jsonUri.textValue() : "";
        }
        return "";
    }

    public static JsonNode extractJson(HttpResponse response, String strJsonPath) throws IOException {
        HttpEntity entity = response.getEntity();
        ObjectMapper m = new ObjectMapper();
        JsonNode jsonRoot = m.readTree(IOUtils.toString(entity.getContent(), "UTF-8"));
        return jsonRoot.at(strJsonPath);
    }
}
