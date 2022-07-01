package scripts.accountStartup.nodes;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.frameworks.behaviortree.BehaviorTreeStatus;
import org.tribot.script.sdk.frameworks.behaviortree.IBehaviorNode;
import org.tribot.script.sdk.frameworks.behaviortree.IParentNode;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.SelectorNode;

import static scripts.appApi.Behaviors.selector;

/* Written by IvanEOD 6/29/2022, at 12:29 PM */
public interface AbstractSelector extends IParentNode {

    SelectorNode getSelector();

    @NotNull
    @Override
    default String getName() {
        return getSelector().getName();
    }

    @Override
    default void setName(@NotNull String s) {
        getSelector().setName(s);
    }

    @NotNull
    @Override
    default String getTreeString(@NotNull String s) {
        return getSelector().getTreeString(s);
    }

    @NotNull
    @Override
    default BehaviorTreeStatus tick() {
        return getSelector().tick();
    }

    @NotNull
    @Override
    default <T extends IBehaviorNode> T initNode(
            @NotNull String s,
            @NotNull T t,
            @NotNull Function1<? super T, Unit> function1
    ) {
        return getSelector().initNode(s, t, function1);
    }
}
