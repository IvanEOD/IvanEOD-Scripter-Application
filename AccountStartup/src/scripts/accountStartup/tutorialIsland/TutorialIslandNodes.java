package scripts.accountStartup.tutorialIsland;

import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.Widgets;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.SelectorNode;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.SequenceNode;
import scripts.accountStartup.classes.TutorialIslandOptions;
import scripts.accountStartup.tutorialIsland.TutorialIslandHelper.GuideRoomWidgets;

import java.util.concurrent.atomic.AtomicBoolean;

import static scripts.accountStartup.tutorialIsland.TutorialIslandHelper.*;
import static scripts.appApi.Behaviors.*;

/* Written by IvanEOD 6/28/2022, at 7:56 PM */
public class TutorialIslandNodes {




    public static SequenceNode getTutorialIslandNode(TutorialIslandOptions options) {






        return null;
    }


    private static SequenceNode guideRoom(String username) {
        return sequence(
//                condition(() -> getCurrentState().equals(TutorialIslandState.GUIDE_ROOM)),
//                selector(
//                        condition(() -> )
//                )

        );

    }


    private static SelectorNode selectName(String username) {
        return selector(
                condition(GuideRoomWidgets.NAME_TEXT_BOX::isNotVisible),
                condition(() -> {
                    AtomicBoolean shouldCancel = new AtomicBoolean(false);
                    Log.trace("Creating name");
                    getWidget(558, 12).ifPresent(widget -> shouldCancel.set(!tryName(widget, username)));
                    if (shouldCancel.get()) return true;
                    shouldCancel.set(clickSetName());
                    Waiting.waitUntil(GuideRoomWidgets.CHARACTER_CREATOR_SCREEN::isVisible);
                    return true;
                })
        );
    }


}
