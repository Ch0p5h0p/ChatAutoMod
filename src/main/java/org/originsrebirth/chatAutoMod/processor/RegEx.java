package org.originsrebirth.chatAutoMod.processor;

import java.util.*;
import java.util.regex.*;

import static org.apache.logging.log4j.LogManager.getLogger;

public class RegEx {

    public static final Map<Character, String> EQUIVALENCES = Map.ofEntries(
            Map.entry('a', "a@4*àáâãäåāăąȧạảǎȁȃȺɐ*"),
            Map.entry('e', "e3*èéêëēĕėęěȅȇẹẻẽɇɘ*"),
            Map.entry('i', "i!1*¡ìíîïīĭįǐȉȋɨɪ*"),
            Map.entry('o', "o0*óôöòœøōõ*"),
            Map.entry('u', "uv*ùúûüūŭůűųǔȕȗưɄʉ*"),
            Map.entry('b', "b8ßƄʙ"),
            Map.entry('c', "c(<¢ç©"),
            Map.entry('d', "dÐđ"),
            Map.entry('f', "fƒ"),
            Map.entry('g', "g9ɡğ"),
            Map.entry('h', "h#ħ"),
            Map.entry('j', "jʝ"),
            Map.entry('k', "kκʞ"),
            Map.entry('l',"l1|"),
            Map.entry('m', "mɱ"),
            Map.entry('n', "nñŋ"),
            Map.entry('p', "pρþ"),
            Map.entry('q', "q9"),
            Map.entry('r', "r®ʀ"),
            Map.entry('s',"s$5"),
            Map.entry('v', "v\\/"),
            Map.entry('w', "wvvω"),
            Map.entry('x', "x×χ"),
            Map.entry('y', "y¥γ"),
            Map.entry('z', "z2ʐ")
    );

    public static String toRegEx(String text) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (EQUIVALENCES.containsKey(c)) {
                string.append("[").append(EQUIVALENCES.get(c)).append("]");
            } else {
                string.append(c);
            }
            string.append("+[\\W_]*");
        }
        return string.toString();
    }

    // The arg type is Set<String> because I'll be getting the words (which are keys) from a map.
    public static String buildMegaString(Set<String> words){
        StringBuilder megastring = new StringBuilder();
        megastring.append("(?<![a-z0-9])(");
        int counter=0;
        for (String word : words) {
            megastring.append("(?<").append(word).append(">").append(toRegEx(word)).append(")");
            counter++;
            if (counter<words.size()) {
                megastring.append("|");
            }
        }
        megastring.append(")(?![a-z0-9])");
        return megastring.toString();
    }

    public static Pattern compile(String regex_string) {
        return Pattern.compile(regex_string, Pattern.CASE_INSENSITIVE);
    }

    public static List<String> containsBadWords(String string, Pattern pattern, Set<String> badwords) {
        Matcher matcher = pattern.matcher(string);
        List<String> detected = new ArrayList<>();

        while (matcher.find()) {
            for (String word: badwords) {
                String match = matcher.group(word);
                if (match != null) {
                    getLogger().info("Found: " + matcher.group());
                    detected.add(word);
                }
            }
        }

        return detected;
    }


}
