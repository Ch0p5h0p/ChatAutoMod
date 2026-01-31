package org.Ch0p5h0p.chatAutoMod.processor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static org.apache.logging.log4j.LogManager.getLogger;

public class Processor {

    public static String masterString;
    public static Pattern pattern;
    public static Map<String, BadWordConfig> badWords;
    public static Map<String, String> responses;
    public static List<String> severityOrder;

    public static void init(JavaPlugin plugin) throws IOException {
        Config.load(plugin);
        badWords=Config.badWords;
        responses=Config.responses;
        severityOrder=Config.severityOrder;

        masterString=RegEx.buildMegaString(badWords.keySet());

        pattern = RegEx.compile(masterString);
    }

    public static String maxSeverity(List<String> words) {
        int max=0;
        for (String word : words){
            int severity_rating=severityOrder.indexOf(badWords.get(word).severity);
            if (severity_rating>max) {
                max=severity_rating;
            }
        }
        getLogger().info("Max severity: "+severityOrder.get(max));
        return severityOrder.get(max);
    }

    public static String process(String text) {
        getLogger().info("Processing: "+text);
        List<String> detected=RegEx.containsBadWords(text, pattern, badWords.keySet());
        if (detected.size()!=0) {
            return responses.get(maxSeverity(detected));
        } else {
            return "None";
        }
    }

    public static String getMasterString() {
        return masterString;
    }
}
