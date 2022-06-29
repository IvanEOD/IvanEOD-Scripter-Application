package scripts.accountStartup.tutorialIsland;

import org.tribot.script.sdk.GameState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/* Written by IvanEOD 6/28/2022, at 9:43 PM */
public enum TutorialIslandState {
    GUIDE_ROOM(1, 2, 3, 7),
    SURVIVAL_AREA(10, 20, 30, 40, 50, 60, 70, 80, 90),
    KITCHEN_AREA(120, 130, 140, 150, 160),
    QUEST_GUIDE(170, 200, 220, 230, 240),
    MINING_AREA(250, 260, 270, 300, 310, 320, 330, 340, 350),
    COMBAT_AREA(360, 370, 470, 390, 400, 405, 410, 420, 430, 440, 450, 460, 480, 490),
    BANK_AREA(500, 510, 520, 525, 530, 532, 531, 540),
    PRIEST( 550, 570, 600, 560, 580),
    MAGIC_AREA(610, 620, 630, 640, 650, 670),
    COMPLETED(1000),
    UNKNOWN;

    private final List<Integer> indexes;

    TutorialIslandState(int... indexes) {
        this.indexes = Arrays.stream(indexes).boxed().collect(Collectors.toList());
    }

    public static TutorialIslandState getCurrentState() {
        int setting = GameState.getSetting(281);
        for (TutorialIslandState value : values()) {
            if (value.indexes.contains(setting)) return value;
        }
        return UNKNOWN;
    }

}
