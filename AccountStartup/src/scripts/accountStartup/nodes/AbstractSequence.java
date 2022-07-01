package scripts.accountStartup.nodes;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.frameworks.behaviortree.BehaviorTreeStatus;
import org.tribot.script.sdk.frameworks.behaviortree.IBehaviorNode;
import org.tribot.script.sdk.frameworks.behaviortree.IParentNode;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.SequenceNode;

import static scripts.appApi.Behaviors.sequence;

/* Written by IvanEOD 6/29/2022, at 12:32 PM */
public interface AbstractSequence extends IParentNode {

    SequenceNode getSequence();


    @NotNull
    @Override
    default String getName() {
        return getSequence().getName();
    }

    @Override
    default void setName(@NotNull String s) {
        getSequence().setName(s);
    }

    @NotNull
    @Override
    default String getTreeString(@NotNull String s) {
        return getSequence().getTreeString(s);
    }

    @NotNull
    @Override
    default BehaviorTreeStatus tick() {
        return getSequence().tick();
    }

    @NotNull
    @Override
    default <T extends IBehaviorNode> T initNode(
            @NotNull String s,
            @NotNull T t,
            @NotNull Function1<? super T, Unit> function1
    ) {
        return getSequence().initNode(s, t, function1);
    }
}
