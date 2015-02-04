package com.datastax.poc;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.poc.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

/**
 * Created by patrick on 03/02/15.
 */
public class AppSession {

    private final UUID id;
    private JsonNode persistentRootNode;
    private JsonNode changeRootNode;
    private JsonNode currentNode;
    private ObjectMapper om;

    public AppSession(UUID id) {
        this.id = id;
        this.om = new ObjectMapper();
        this.persistentRootNode = this.om.createObjectNode();
        this.changeRootNode = this.om.createObjectNode();
        this.currentNode = this.changeRootNode;
    }

    public AppSession() {
        this(UUIDs.timeBased());
    }

    public UUID getId() {
        return id;
    }

    public AppSession addString(String name, String value) {
        ((ObjectNode) this.currentNode).put(name, value);
        return this;
    }

    public AppSession addInt(String name, int value) {
        ((ObjectNode) this.currentNode).put(name, value);
        return this;
    }

    public AppSession addLong(String name, long value) {
        ((ObjectNode) this.currentNode).put(name, value);
        return this;
    }

    public AppSession addBoolean(String name, boolean value) {
        ((ObjectNode) this.currentNode).put(name, value);
        return this;
    }

    public AppSession addObject(String name) {
        currentNode = ((ObjectNode) this.currentNode).putObject(name);
        return this;
    }

    public AppSession mergeJson(String jsonStr) {
        JsonNode addJson = JsonUtil.parseJson(jsonStr);
        JsonUtil.mergeJson((ObjectNode) changeRootNode, (ObjectNode) addJson);
        return this;
    }

    public AppSession select(String... path) {
        JsonNode previousNode = null;
        currentNode = changeRootNode;
        for (String childName:path) {
            previousNode = currentNode;
            currentNode = currentNode.path(childName);
            if (currentNode == null || currentNode.isMissingNode()) {
                currentNode = ((ObjectNode) previousNode).putObject(childName);
            }
        }
        return this;
    }

    public void mergeChanges() {
        JsonUtil.mergeJson((ObjectNode) persistentRootNode, (ObjectNode) this.changeRootNode);
        this.persistentRootNode = changeRootNode;
        this.changeRootNode = om.createObjectNode();
    }

    public String getChangeJson() {
        return this.changeRootNode.toString();
    }

    public String toString() {
        return this.persistentRootNode.toString();
    }

}
