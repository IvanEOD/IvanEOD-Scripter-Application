package scripts.appApi.classes;

import org.apache.commons.codec.binary.Base64;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Widget;
import org.tribot.script.sdk.util.Retry;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* Written by IvanEOD 6/24/2022, at 7:20 AM */
public class Utility {

    //<editor-fold desc="Private Static Fields">
    private static final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");
    private static final Set<String> STOP_WORDS =
            Stream.of("a", "an", "the", "and", "but", "for", "at", "by", "to", "or")
                    .collect(Collectors.toSet());
    //</editor-fold>

    /**
     * Converts "stringsLikeThis" to "Strings Like This"
     */
    public static String toTitleCase(String input) {
        var words = findWordsInMixedCase(input);
        List<String> product = new ArrayList<>();
        for (int index = 0; index < words.size(); index++) {
            var currentWord = words.get(index);
            if (index == 0 || !STOP_WORDS.contains(currentWord.toLowerCase()))
                product.add(capitalizeFirstLetter(currentWord));
            else product.add(currentWord.toLowerCase());
        }
        return String.join(" ", product);
    }

    /**
     * Separate the words from strings like "separateTheWordsFromStringsLike"
     */
    private static List<String> findWordsInMixedCase(String input) {
        Matcher matcher = WORD_FINDER.matcher(input);
        List<String> words = new ArrayList<>();
        while (matcher.find()) words.add(matcher.group(0));
        return words;
    }
    public static String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Checks if "Click to Play" button is on screen, clicks as needed. Waits until logged in to continue.
     */
    public static boolean waitUntilLoggedIn() {
        boolean success = Retry.retry(5, () -> {
            if (isClickToPlayVisible()) clickClickToPlay();
            return Waiting.waitUntil(Login::isLoggedIn);
        });
        if (!success) {
            Login.login();
            waitUntilLoggedIn();
        }
        return Login.isLoggedIn();
    }

    //<editor-fold desc="waitUntilLoggedIn support methods">
    private static Optional<Widget> getClickToPlayButton() {
        return Query.widgets().inIndexPath(378, 78).findFirst();
    }
    private static boolean isClickToPlayVisible() {
        return getClickToPlayButton().map(Widget::isVisible).orElse(false);
    }
    private static void clickClickToPlay() {
        getClickToPlayButton().ifPresent(button -> button.click("Play"));
    }
    //</editor-fold>



    //<editor-fold desc="Base 64 Encoding / Decoding Methods">
    public static String getEncodedBase64(String input) {
        return new String(getEncodeBase64Bytes(input));
    }

    private static byte[] getEncodeBase64Bytes(String string) {
        return Base64.encodeBase64(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String getDecodedBase64(String input) {
        return getDecodedBase64Bytes(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String getDecodedBase64Bytes(byte[] base64) {
        byte[] decoded = Base64.decodeBase64(base64);
        return new String(decoded);
    }
    //</editor-fold>


}
