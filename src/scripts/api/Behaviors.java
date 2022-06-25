package scripts.api;

import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.frameworks.behaviortree.BehaviorTreeStatus;
import org.tribot.script.sdk.frameworks.behaviortree.IBehaviorNode;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.*;
import scripts.BehaviorTreeKt;

import java.util.function.BooleanSupplier;

/* Written by IvanEOD 6/16/2022, at 4:29 PM */
public class Behaviors {


    /*
    https://community.tribot.org/topic/83987-using-behavior-trees-in-your-scripts-kotlin/?tab=comments#comment-1016610

    These nodes just wrap the kotlin nodes in Nullable's tree.
    For better descriptions on how to use them, read his guide.
    Then just call them in Java.
     */

    public static IBehaviorNode behaviorTree(IBehaviorNode root) {
        return BehaviorTreeKt.newBehaviorTree(root);
    }
    public static IBehaviorNode behavior(IBehavior behavior) {
        return BehaviorTreeKt.newBehavior(behavior::tick);
    }

    // returns SUCCESS on true FAILURE on false
    // debug just traces the result of the condition after whatever you type as the debug.
    public static IBehaviorNode condition(String debug, BooleanSupplier condition) {
        return condition(() -> {
            var result = condition.getAsBoolean();
            Log.trace(debug + ": " + result);
            return result;
        });
    }
    public static IBehaviorNode condition(BooleanSupplier condition) {
        return BehaviorTreeKt.newCondition(condition);
    }
    // Runs nodes until one succeeds
    public static SelectorNode selector(IBehaviorNode... nodes) {
        return BehaviorTreeKt.newSelector(nodes);
    }
    // Runs nodes until one fails
    public static SequenceNode sequence(IBehaviorNode... nodes) {
        return BehaviorTreeKt.newSequence(nodes);
    }
    public static RepeaterNode repeater(IBehaviorNode node) {
        return BehaviorTreeKt.newRepeater(node);
    }
    public static RepeatUntilNode repeatUntil(BehaviorTreeStatus status, IBehaviorNode node) {
        return BehaviorTreeKt.newRepeatUntil(status, node);
    }
    public static RepeatUntilNode repeatUntil(BooleanSupplier condition, IBehaviorNode node) {
        return BehaviorTreeKt.newRepeatUntil(condition, node);
    }
    public static RepeatUntilNode repeatUntil(BehaviorTreeStatus status, BooleanSupplier condition, IBehaviorNode node) {
        return BehaviorTreeKt.newRepeatUntil(status, condition, node);
    }

    public static ConditionalNode conditional(ObserverAbort observerAbort, BooleanSupplier condition, IBehaviorNode node) {
        return BehaviorTreeKt.newConditional(observerAbort, condition, node);
    }

    // Use this inside a selector or sequence with the corresponding Policy and it will always run and then move to the next node
    public static IBehaviorNode inline(Policy policy, Runnable runnable) {
        return policy.equals(Policy.Selector) ? inverter(perform(runnable)) : perform(runnable);
    }

    public static InverterNode inverter(IBehaviorNode node) {
        return BehaviorTreeKt.newInverter(node);
    }
    public static IBehaviorNode perform(Runnable runnable) {
        return BehaviorTreeKt.newPerform(runnable);
    }
    public static SucceederNode succeeder(IBehaviorNode node) {
        return BehaviorTreeKt.newSucceeder(node);
    }
    // will automatically run the node and return true if the node returns SUCCESS
    public static boolean isSuccessful(IBehaviorNode node) { return node.tick().equals(BehaviorTreeStatus.SUCCESS); }
    // will automatically run the node and return true if the node returns FAILURE

    /**
     * Executes the node and returns true if the node was NOT successful
     * @param node Node to execute.
     * @return true if the node fails, false if it succeeds
     */
    public static boolean isNotSuccessful(IBehaviorNode node) { return !node.tick().equals(BehaviorTreeStatus.SUCCESS); }
    public enum Policy {
        Selector,
        Sequence
    }

    @FunctionalInterface
    public interface IBehavior {
        BehaviorTreeStatus tick();
    }


}