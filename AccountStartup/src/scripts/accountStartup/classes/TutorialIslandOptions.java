package scripts.accountStartup.classes;

import lombok.Data;

/* Written by IvanEOD 6/28/2022, at 7:57 PM */
@Data
public class TutorialIslandOptions {

    private AccountType accountType = AccountType.NORMAL;
    private String displayName;
    private boolean addRandomNumbers = false;
    private boolean replaceLettersWithLeetSpeak = true;

    public TutorialIslandOptions(
            AccountType accountType,
            String displayName,
            boolean addRandomNumbers,
            boolean replaceLettersWithLeetSpeak
    ) {
        this.accountType = accountType;
        this.displayName = displayName;
        this.addRandomNumbers = addRandomNumbers;
        this.replaceLettersWithLeetSpeak = replaceLettersWithLeetSpeak;
    }




}
