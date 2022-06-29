package scripts.appApi.classes;

import org.apache.commons.codec.binary.Base64;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Widget;
import org.tribot.script.sdk.util.Retry;

import java.nio.charset.StandardCharsets;
import java.util.*;
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
    private static final XS128 random = new XS128();
    //</editor-fold>

    public static XS128 getRandom() { return random; }


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

    public static <EntityType> EntityType selectRandom(List<EntityType> list) {
        var index = random(0, list.size() -1);
        return list.get(index);
    }


    public static int random() { return random.nextInt(); }
    public static int random(int max) { return random.nextInt(max); }
    public static long random(long range) { return random.nextLong(range); }
    public static int random(int min, int max) { return min + random.nextInt(max - min + 1); }
    public static boolean randomBoolean() { return random.nextBoolean(); }
    public static boolean randomBoolean(int chance) { return 100 - (random() * 100) < chance; }

    public static boolean roll() { return randomBoolean(); }
    public static boolean roll(int chance) { return randomBoolean(chance); }
    public static boolean roll(double percentValueEach, double count) {
        return roll((int) calculateChance(percentValueEach, count));
    }

    public static boolean roll(String debugString, double percentValueEach, double count) {
        StringBuilder resultString = new StringBuilder("Calculating chances for ")
                .append(debugString)
                .append(" at ")
                .append(percentValueEach)
                .append("% each for ")
                .append(count)
                .append(" counts: ");
        int chance = (int) calculateChance(percentValueEach, count);
        boolean results = roll(chance);
        resultString.append("(").append(chance).append("% odds) = ").append(results);
        Log.trace(resultString.toString());
        return results;
    }


    public static double calculateChance(double percentValueEach, double count) {
        return Math.min(percentValueEach * count, 100.0);
    }

    public static class XS128 extends Random {
        private static final double NORM_DOUBLE = 1.0 / (1L << 53);
        private static final double NORM_FLOAT = 1.0 / (1L << 24);
        private long seed0;
        private long seed1;

        public XS128() {
            setSeed(new Random().nextLong());
        }

        public XS128(long seed) {
            setSeed(seed);
        }

        public XS128(long seed0, long seed1) {
            setState(seed0, seed1);
        }

        @Override
        public long nextLong() {
            long s1 = this.seed0;
            final long s0 = this.seed1;
            this.seed0 = s0;
            s1 ^= s1 <<23;
            return (this.seed1 = (s1 ^ s0 ^ (s1 >>> 17) ^ (s0 >>> 26))) + s0;
        }

        @Override
        protected final int next(int bits) {
            return (int) (nextLong() & ((1L << bits) - 1));
        }

        @Override
        public int nextInt() {
            return (int) nextLong();
        }

        @Override
        public int nextInt(final int bound) {
            return (int) nextLong(bound);
        }

        public long nextLong(final long next) {
            if (next <= 0) throw new IllegalArgumentException("next must be positive");
            for (; ; ) {
                final long bits = nextLong() >>> 1;
                final long value = bits % next;
                if (bits - value + (next - 1) >= 0) return value;
            }
        }

        @Override
        public double nextDouble() {
            return (nextLong() >>> 11) * NORM_DOUBLE;
        }

        @Override
        public float nextFloat() {
            return (float) ((nextLong() >>> 40) * NORM_FLOAT);
        }

        @Override
        public boolean nextBoolean() {
            return (nextLong() & 1) != 0;
        }

        @Override
        public void nextBytes(final byte[] bytes) {
            int next = 0;
            int index = bytes.length;
            while (index != 0) {
                next = Math.min(index, 8);
                for (long bits = nextLong(); next-- != 0; bits >>= 8)
                    bytes[--index] = (byte) bits;
            }
        }

        @Override
        public synchronized void setSeed(long seed) {
            long seed0 = murmurHash3(seed == 0? Long.MIN_VALUE : seed);
            setState(seed0, murmurHash3(seed0));
        }

        public void setState(final long seed0, final long seed1) {
            this.seed0 = seed0;
            this.seed1 = seed1;
        }

        private static long murmurHash3(long x) {
            x ^= x >>> 33;
            x *= 0xff51afd7ed558ccdL;
            x ^= x >>> 33;
            x *= 0xc4ceb9fe1a85ec53L;
            x ^= x >>> 33;
            return x;
        }
    }

}
