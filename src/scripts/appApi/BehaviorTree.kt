package scripts

import org.tribot.script.sdk.frameworks.behaviortree.*
import org.tribot.script.sdk.frameworks.behaviortree.nodes.*
import java.util.function.BooleanSupplier
import java.util.function.Supplier

/* Written by IvanEOD 6/7/2022, at 7:21 PM */

// Don't need to mess with this file, it's just the wrapper for the Behaviors.


fun newBehaviorTree(root: IBehaviorNode) : IBehaviorNode {
    return behaviorTree { behavior(root) }
}
fun newSequence(vararg nodes: IBehaviorNode) : SequenceNode {
    val sequenceNode = SequenceNode()
    for (node in nodes) sequenceNode.behavior(node)
    return sequenceNode;
}
fun newSelector(vararg nodes: IBehaviorNode) : SelectorNode {
    val selectorNode = SelectorNode()
    for (node in nodes) selectorNode.behavior(node)
    return selectorNode
}
fun newBehavior(supplier: Supplier<BehaviorTreeStatus>) : IBehaviorNode {
    return behaviorTree {
        behavior("", supplier::get)
    }
}
fun newCondition(supplier: BooleanSupplier) : IBehaviorNode {
    return behaviorTree {
        condition("", supplier::getAsBoolean)
    }
}
fun newPerform(runnable: Runnable) : IBehaviorNode {
    return behaviorTree {
        perform { runnable.run() }
    }
}
fun newInverter(node: IBehaviorNode) : InverterNode {
    val inverterNode = InverterNode()
    inverterNode.behavior(node);
    return inverterNode;
}
fun newRepeater(node: IBehaviorNode) : RepeaterNode {
    val repeaterNode = RepeaterNode()
    repeaterNode.behavior(node)
    return repeaterNode
}
fun newSucceeder(node: IBehaviorNode) : SucceederNode {
    val succeederNode = SucceederNode()
    succeederNode.behavior(node)
    return succeederNode
}
fun newRepeatUntil(status: BehaviorTreeStatus, node: IBehaviorNode): RepeatUntilNode {
    val repeatUntilNode = RepeatUntilNode("", status)
    repeatUntilNode.behavior(node);
    return repeatUntilNode;
}
fun newRepeatUntil(condition: BooleanSupplier, node: IBehaviorNode): RepeatUntilNode {
    val repeatUntilNode = RepeatUntilNode("", condition::getAsBoolean)
    repeatUntilNode.behavior(node);
    return repeatUntilNode;
}
fun newRepeatUntil(status: BehaviorTreeStatus, condition: BooleanSupplier, node: IBehaviorNode): RepeatUntilNode {
    val repeatUntilNode = RepeatUntilNode("", condition::getAsBoolean, status)
    repeatUntilNode.behavior(node);
    return repeatUntilNode;
}
fun newConditional(observerAbort: ObserverAbort, condition: BooleanSupplier, node: IBehaviorNode): ConditionalNode {
    val conditionalNode = ConditionalNode("", observerAbort, condition::getAsBoolean)
    conditionalNode.behavior(node);
    return conditionalNode;
}
