package me.lerch.alexa.apache.nifi.skill.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Kay on 29.04.2016.
 */
public class NifiRestApiUtils {
    private static final String PATH_PROCESSOR = "/controller/process-groups/root/processors";
    private static final String PATH_REVISION = "/controller/revision";
    private static final String PATH_PROVENANCE = "/controller/provenance";

    private static final String JSON_PATH_PROVENANCE_RESULTS_TOTAL = "/provenance/results/totalCount";
    private static final String JSON_PATH_PROVENANCE_FINISHED = "/provenance/finished";
    private static final String JSON_PATH_PROVENANCE_URI = "/provenance/uri";
    private static final String JSON_PATH_REVISION_VERSION = "/revision/version";
    private static final String JSON_PATH_PROCESSOR_TWITTERTERMS = "/processor/config/properties/Terms to Filter On";

    private static final String PROCESS_STATE_STOPPED = "STOPPED";
    private static final String PROCESS_STATE_RUNNING = "RUNNING";

    private static int getRevision(String nifiRestApiEndpoint) throws IOException {
        String endpointGetRevision = nifiRestApiEndpoint + PATH_REVISION;
        HttpResponse revResponse = HttpUtils.getJsonHttp(endpointGetRevision);
        HttpEntity revEntity = revResponse.getEntity();
        String revision = IOUtils.toString(revEntity.getContent());
        return JsonUtils.extractJson(revision, JSON_PATH_REVISION_VERSION).intValue();
    }

    public static HttpResponse startProcessor(String nifiRestApiEndpoint, String clientId, String processorId) throws IOException {
        return updateProcessorState(nifiRestApiEndpoint, clientId, processorId, PROCESS_STATE_RUNNING);
    }

    public static HttpResponse stopProcessor(String nifiRestApiEndpoint, String clientId, String processorId) throws IOException {
        return updateProcessorState(nifiRestApiEndpoint, clientId, processorId, PROCESS_STATE_STOPPED);
    }

    public static void updateTwitterTerms(String nifiRestApiEndpoint, String clientId, String processorIdgetTwitter, String processorIdevaluateTwitter, String terms) throws IOException {
        String json = "{\"revision\": " +
                "{\"clientId\": \"" + clientId + "\", " +
                "\"version\": " + getRevision(nifiRestApiEndpoint) + "}," +
                "\"processor\": { " +
                "\"id\": \"" + processorIdgetTwitter + "\", " +
                "\"config\": " +
                "{ \"properties\": { \"Terms to Filter On\": \"" + terms + "\"}}}}";
        putProcessor(nifiRestApiEndpoint, processorIdgetTwitter, json);

        String[] term = terms.split(",");
        String json2 = "{\"revision\": " +
                "{\"clientId\": \"" + clientId + "\", " +
                "\"version\": " + getRevision(nifiRestApiEndpoint) + "}," +
                "\"processor\": { " +
                "\"id\": \"" + processorIdevaluateTwitter + "\", " +
                "\"config\": " +
                "{ \"properties\": { " +
                "\"twitter.keyword1\": \"$[?(@.text =~ /^.*" + (term.length > 0 ? term[0] : UUID.randomUUID()) + ".*$/i)].id_str\"," +
                "\"twitter.keyword2\": \"$[?(@.text =~ /^.*" + (term.length > 1 ? term[1] : UUID.randomUUID()) + ".*$/i)].id_str\"," +
                "\"twitter.keyword3\": \"$[?(@.text =~ /^.*" + (term.length > 2 ? term[2] : UUID.randomUUID()) + ".*$/i)].id_str\"," +
                "\"twitter.keyword4\": \"$[?(@.text =~ /^.*" + (term.length > 3 ? term[3] : UUID.randomUUID()) + ".*$/i)].id_str\"" +
                "}}}}";
        putProcessor(nifiRestApiEndpoint, processorIdevaluateTwitter, json2);
     }

    public static String getTwitterTerms(String nifiRestApiEndpoint, String clientId, String processorId) throws IOException {
        HttpResponse response = HttpUtils.getJsonHttp(nifiRestApiEndpoint + PATH_PROCESSOR + "/" + processorId);
        return JsonUtils.extractJsonValue(response, JSON_PATH_PROCESSOR_TWITTERTERMS);
    }

    public static Long countSearchHitsInProvenanceRepository(String nifiRestApiEndpoint, String clientId, String searchKey, String searchTerm, String processorId, Integer maxResults) throws IOException {
        JsonNode jsonResponse = searchProvenanceRepository(nifiRestApiEndpoint, clientId, searchKey, searchTerm, processorId, maxResults);
        return jsonResponse.at(JSON_PATH_PROVENANCE_RESULTS_TOTAL).asLong();
    }

    public static JsonNode searchProvenanceRepository(String nifiRestApiEndpoint, String clientId, String searchKey, String searchTerm, String processorId, Integer maxResults) throws IOException {
        String json = "{\"revision\": " +
                "{\"clientId\": \"" + clientId + "\"},"  +
                "\"provenance\": " +
                "{\"request\": " +
                "{\"searchTerms\": " +
                "{\"" + searchKey + "\": \"*" + searchTerm + "*\", \"processorId\": \"" + processorId + "\"}," +
                "\"maxResults\": " + maxResults + " } } }";
        // initiate query
        HttpResponse response = HttpUtils.postJsonHttp(nifiRestApiEndpoint + PATH_PROVENANCE, json);
        String resultUri = JsonUtils.extractJsonValue(response, JSON_PATH_PROVENANCE_URI);
        // get results
        Boolean queryCompleted = false;
        JsonNode jsonRoot = null;
        while (!queryCompleted) {
            HttpResponse responseGet = HttpUtils.getJsonHttp(resultUri);
            HttpEntity entity = responseGet.getEntity();
            ObjectMapper m = new ObjectMapper();
            jsonRoot = m.readTree(IOUtils.toString(entity.getContent(), "UTF-8"));
            queryCompleted = jsonRoot.at(JSON_PATH_PROVENANCE_FINISHED).asBoolean();
        }
        // delete query
        HttpUtils.deleteJsonHttp(resultUri);
        return jsonRoot;
    }

    private static HttpResponse updateProcessorState(String nifiRestApiEndpoint, String clientId, String processorId, String state) throws IOException {
        String json = "{\"revision\": " +
                "{\"clientId\": \"" + clientId + "\", " +
                "\"version\": " + NifiRestApiUtils.getRevision(nifiRestApiEndpoint) + "}," +
                "\"processor\": { " +
                "\"id\": \"" + processorId + "\", " +
                "\"state\": \"" + state + "\"}}";
        return putProcessor(nifiRestApiEndpoint, processorId, json);
    }

    private static HttpResponse putProcessor(String nifiRestApiEndpoint, String processorId, String jsonBody) throws IOException {
        String endpointPutProcessor = nifiRestApiEndpoint + PATH_PROCESSOR + "/" + processorId;
        return HttpUtils.putJsonHttp(endpointPutProcessor, jsonBody);
    }
}
