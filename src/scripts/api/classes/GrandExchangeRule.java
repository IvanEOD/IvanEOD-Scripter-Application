package scripts.api.classes;

import lombok.RequiredArgsConstructor;
import org.tribot.script.sdk.types.GrandExchangeOffer;

/* Written by IvanEOD 6/26/2022, at 3:41 PM */
public class GrandExchangeRule {

    private GrandExchangeOffer.Type type;
    private Integer id;
    private String name;

    private int pricePerItem = 1;
    private int quantity = 1;




    @RequiredArgsConstructor
    public enum PercentModifier {
        INCREASE(1.05),
        DECREASE(0.95);

        private final double multiplier;
        public double getAdjusted(double amount) {
            return amount * multiplier;
        }

    }


}
