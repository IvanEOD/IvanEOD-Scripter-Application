package scripts.accountStartup.nodes;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.GrandExchange;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.frameworks.behaviortree.BehaviorTreeStatus;
import org.tribot.script.sdk.frameworks.behaviortree.IBehaviorNode;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.SelectorNode;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.SequenceNode;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

import static java.util.function.Predicate.not;
import static scripts.appApi.Behaviors.*;

/* Written by IvanEOD 6/29/2022, at 12:28 PM */
public class PurchasingBond implements AbstractSequence {
    private static final Area grandExchange = Area.fromRectangle(
            new WorldTile(3158, 3495, 0),
            new WorldTile(3171, 3484, 0));

    private static final int bondId = 13190;

    private long offerPlacedTime = 0;


    @Override
    public SequenceNode getSequence() {
        return sequence(
            walkToGrandExchange,
            withdrawCash,
            openExchange


        );
    }

    private static final SelectorNode openBank = selector(
            condition(Bank::isOpen),
            sequence(
                selector(
                    condition(Bank::isNearby),
                    condition(GlobalWalking::walkToBank)
                ),
                condition(() -> Bank.open() && Waiting.waitUntil(3000, Bank::isOpen))
            )
    );

    private static final SequenceNode closeBank = sequence(
            condition(Bank::isOpen),
            condition(() -> Bank.close() && Waiting.waitUntil(3000, () -> !Bank.isOpen()))
    );

    private static final SelectorNode walkToGrandExchange = selector(
            condition(grandExchange::containsMyPlayer),
            condition(() -> GlobalWalking.walkTo(grandExchange.getRandomTile()))
    );

    private static final SelectorNode withdrawCash = selector(
            condition(() -> Inventory.getCount(995) >= 6_500_000),
            sequence(
                openBank,
                condition(() -> Bank.withdrawAll(995))
            )
    );

    private static final SelectorNode openExchange = selector(
            condition(GrandExchange::isOpen),
            sequence(
                    walkToGrandExchange,
                    closeBank,
                    condition(() -> GrandExchange.open() && Waiting.waitUntil(3000, GrandExchange::isOpen))
        )
    );

    private static final SelectorNode cancelBondOffer = selector(
            condition(PurchasingBond::hasBondOffer),
            sequence(
                    openExchange,
                    condition(() -> {
                        Query.grandExchangeOffers()
                                .itemIdEquals(bondId)
                                .typeEquals(GrandExchangeOffer.Type.BUY)
                                .findFirst()
                                .ifPresent(offer -> {
                                    GrandExchange.abort(offer.getSlot());
                                });
                        Waiting.waitUntil(2000, PurchasingBond::hasNoBondOffer);
                        //if (GrandExchange.)
                        return false;
                    })
            )
    );

    private final SequenceNode buyBond = sequence(
            condition(GrandExchange::isOpen),
            condition(() -> {

                if (hasBondOffer() && offerPlacedTime != 0) {
                    if (offerPlacedTime + 60000 < System.currentTimeMillis()) {
                        offerPlacedTime = 0;

                        Query.grandExchangeOffers()
                                .itemIdEquals(bondId)
                                .typeEquals(GrandExchangeOffer.Type.BUY)
                                .findFirst()
                                .ifPresent(offer -> {

                                });

                    }

                    return true;
                }


                var config = GrandExchange.CreateOfferConfig.builder()
                        .quantity(1)
                        .type(GrandExchangeOffer.Type.BUY)
                        .searchText("bond")
                        .price(6_500_000)
                        .itemId(bondId)
                        .build();

                var placed = GrandExchange.placeOffer(config);
                if (!placed) return false;
                offerPlacedTime = System.currentTimeMillis();
                return Waiting.waitUntil(2000, PurchasingBond::hasBondOffer);
            }),
            condition(() -> {



                return false;
            })
    );




    private static boolean hasBondOffer() {
        return Query.grandExchangeOffers()
                .itemIdEquals(bondId)
                .typeEquals(GrandExchangeOffer.Type.BUY)
                .isAny();

    }
    private static boolean hasNoBondOffer() { return !hasBondOffer(); }
}
