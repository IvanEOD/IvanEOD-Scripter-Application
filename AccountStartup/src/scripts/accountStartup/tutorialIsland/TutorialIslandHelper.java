package scripts.accountStartup.tutorialIsland;

import org.tribot.script.sdk.GameState;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Widget;
import org.tribot.script.sdk.util.Retry;
import scripts.appApi.classes.Utility;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static scripts.appApi.classes.Utility.roll;

/* Written by IvanEOD 6/28/2022, at 9:42 PM */
public class TutorialIslandHelper {

    private static final int MAX_CHARACTERS_IN_NAME = 12;

    public static TutorialIslandState getCurrentState() {
        return TutorialIslandState.getCurrentState();
    }

    public static int getIslandStep() {
        return GameState.getSetting(281);
    }

    public static boolean guideBoxContains(String text) {
        return Query.widgets()
                .inIndexPath(263, 1, 0)
                .findFirst()
                .flatMap(Widget::getText)
                .orElse("")
                .contains(text);
    }

    private static boolean tryName(Widget widget, String name) {
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicBoolean nameTaken = new AtomicBoolean(false);
        var currentText = widget.getText().orElse("");
        Log.trace("Typing display name");
        if (currentText.equals(name)) return true;
        widget.click();
        Keyboard.typeLine(name + "*", currentText);
        getNameTextBox().ifPresent(resultText -> {
            var text = resultText.getText().orElse("null");
            if (text.contains("Sorry, the display name")) {
                Log.trace("Chosen name was unavailable. ( " + name + " )");
                nameTaken.set(true);
                success.set(false);
            } else if (text.contains("Great!")) {
                Log.trace("Chosen name was accepted!. ( " + name + " )");
                success.set(true);
                Waiting.waitNormal(1200, 300);
            }
        });

        if (nameTaken.get()) return tryName(widget, generateName(name));
        return success.get();
    }

    private static boolean clickSetName() {
        Log.trace("Clicking set name");
        return getSetNameButton().map(button -> button.click("Set name")).orElse(false);
    }

    private static Optional<Widget> getSetNameButton() {
        return getWidget(558, 18);
    }

    private static Optional<Widget> getWidget(int... path) {
        return Query.widgets().inIndexPath(path).findFirst();
    }

    private static boolean characterCreatorIsOpen() {
        return getCharacterCreator().isPresent();
    }

    private static Optional<Widget> getCharacterCreator() {
        return getWidget(679, 3);
    }

    private static Optional<Widget> getNameTextBox() {
        return getWidget(558, 13);
    }

    public static boolean resetAppearance() {
        if (GuideRoomWidgets.FEMALE_BUTTON.click(1)) return GuideRoomWidgets.MALE_BUTTON.click(1);
        return false;
    }

    public static String generateName(String name) {
        if (name.length() < MAX_CHARACTERS_IN_NAME) {
            return name + Utility.random(0, 9);
        }

        return name.replace('o', '0')
                .replace('l', '1')
                .replace('e', '3')
                .replace('a', '4')
                .replace('t', '7');
    }

    public static boolean customizeCharacter() {
        var selectHairButton = Utility.roll()
                ? GuideRoomWidgets.HEAD_RIGHT : GuideRoomWidgets.HEAD_LEFT;
        var selectTorsoButton = Utility.roll()
                ? GuideRoomWidgets.TORSO_RIGHT : GuideRoomWidgets.TORSO_LEFT;
        var selectedHairColorButton = Utility.roll()
                ? GuideRoomWidgets.HAIR_COLOR_RIGHT : GuideRoomWidgets.HAIR_COLOR_LEFT;
        var selectTorsoColorButton = Utility.roll()
                ? GuideRoomWidgets.TORSO_COLOR_RIGHT : GuideRoomWidgets.TORSO_COLOR_LEFT;
        var selectedFeetButton = Utility.roll()
                ? GuideRoomWidgets.FEET_COLOR_RIGHT : GuideRoomWidgets.FEET_COLOR_LEFT;
        selectHairButton.click(Utility.selectRandom(List.of(3, 11, 13, 15, 16)));
        selectTorsoButton.click(Utility.selectRandom(List.of(0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13)));
        GuideRoomWidgets.ARMS_RIGHT.click(1);
        GuideRoomWidgets.HANDS_RIGHT.click(1);
        GuideRoomWidgets.LEGS_RIGHT.click(2);
        selectedHairColorButton.click(Utility.random(0, 14));
        selectTorsoColorButton.click(Utility.random(0, 13));
        GuideRoomWidgets.LEGS_COLOR_RIGHT.click(2);
        selectedFeetButton.click(Utility.random(0, 7));
        return GuideRoomWidgets.CONFIRM_CHARACTER_BUTTON.click("Confirm");
    }

    public enum GuideRoomWidgets {
        NAME_TEXT_BOX(558, 13),
        SET_NAME_BUTTON(558, 18),
        CHARACTER_CREATOR_SCREEN(679, 3),

        HEAD_LEFT(679, 12),
        HEAD_RIGHT(679, 13),

        JAW_LEFT(679, 16),
        JAW_RIGHT(679, 17),

        TORSO_LEFT(679, 20),
        TORSO_RIGHT(679, 21),

        ARMS_LEFT(679, 24),
        ARMS_RIGHT(679, 25),

        HANDS_LEFT(679, 28),
        HANDS_RIGHT(679, 29),

        LEGS_LEFT(679, 32),
        LEGS_RIGHT(679, 33),

        FEET_LEFT(679, 36),
        FEET_RIGHT(679, 37),

        HAIR_COLOR_LEFT(679, 43),
        HAIR_COLOR_RIGHT(679, 44),
        TORSO_COLOR_LEFT(679, 47),
        TORSO_COLOR_RIGHT(679, 48),
        LEGS_COLOR_LEFT(679, 51),
        LEGS_COLOR_RIGHT(679, 52),
        FEET_COLOR_LEFT(679, 55),
        FEET_COLOR_RIGHT(679, 56),
        SKIN_COLOR_LEFT(679, 59),
        SKIN_COLOR_RIGHT(679, 60),

        FEMALE_BUTTON(679, 65),
        MALE_BUTTON(679, 66),

        CONFIRM_CHARACTER_BUTTON(679, 68),

        ;

        private final int[] path;

        GuideRoomWidgets(int... path) {
            this.path = path;
        }

        public boolean isVisible() {
            return getWidget().isPresent();
        }

        public Optional<Widget> getWidget() {
            return Query.widgets().inIndexPath(path).findFirst();
        }

        public boolean click(int count) {
            return !Retry.retry(count, () -> {
                if (roll() && roll()) Waiting.waitNormal(1200, 400);
                return !click("Select");
            });

        }

        public boolean click(String action) {
            return getWidget().map(widget -> widget.click(action)).orElse(false);
        }

    }

}
