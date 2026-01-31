package org.originsrebirth.chatAutoMod.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Config {
    public static Map<String, BadWordConfig> badWords;
    public static Map<String, String> responses;
    public static List<String> severityOrder;

    public static void loadBadwords(File configDir) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        badWords = mapper.readValue(Paths.get(
                configDir.toString(), "badwords.json").toFile(),
                new TypeReference<Map<String, BadWordConfig>>() {}
        );
    }

    public static void loadResponseData(File configDir) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> jsonMap = mapper.readValue(Paths.get(configDir.toString(),"responses.json").toFile(), Map.class);

        Map<String, String> jsonResponses = (Map<String, String>) jsonMap.get("responses");
        List<String> jsonSeverityOrder = (List<String>) jsonMap.get("severityOrder");

        responses=jsonResponses;
        severityOrder=jsonSeverityOrder;
    }

    public static void load(JavaPlugin plugin) throws IOException {
        File configDir = new File(plugin.getDataFolder(), "config");
        if (!configDir.exists()) configDir.mkdirs();

        loadBadwords(configDir);
        loadResponseData(configDir);
    }
}
