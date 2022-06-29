package scripts.accountStartup.classes;

/* Written by IvanEOD 6/24/2022, at 7:51 AM */

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tribot.script.sdk.Options;
import scripts.appApi.classes.Directory;
import scripts.appApi.classes.StorableClass;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountSetupOptions extends StorableClass<AccountSetupOptions> {

    public boolean taskDoTutorial = true;
    public boolean taskAdjustSettings = true;
    public boolean taskBuyBond = true;

    private String displayName = "";
    private boolean addRandomNumbers = false;
    private boolean replaceLettersWithLeetSpeak = false;
    private AccountType accountType = AccountType.NORMAL;

    private boolean muteMusic = true;
    private boolean muteSoundEffects = true;
    private boolean muteAmbientSound = true;
    private boolean disableProfanityFilter = true;
    private boolean splitPrivateChat = true;
    private boolean hideRoofs = true;
    private boolean removeAcceptTradeDelay = true;
    private boolean adjustScreenDisplay = true;
    private Options.ResizableType resizableType = Options.ResizableType.FIXED;

    private boolean waitForTradeFromMule = false;
    private boolean onlyTradeListedAccounts = true;
    private boolean sellItemsForBond = false;
    private boolean logoutIfCannotAfford = true;
    private boolean logoutWhenFinished = true;
    private boolean usePriceModifier = false;
    private int maxAmount = 0;
    private List<String> trustedAccounts = new ArrayList<>();

    public AccountSetupOptions(){
        super(Directory.Script, "account-setup-options", "default");

    }
    public AccountSetupOptions(String saveFileName) {
        super(Directory.Script, "account-setup-options", saveFileName);
    }

    public AccountSetupOptions(String saveFileName, boolean load) {
        super(Directory.Script, "account-setup-options", saveFileName, load);
    }

    @Override
    public void copy(AccountSetupOptions other) {
        this.displayName = other.displayName;
        this.addRandomNumbers = other.addRandomNumbers;
        this.replaceLettersWithLeetSpeak = other.replaceLettersWithLeetSpeak;
        this.accountType = other.accountType;
        this.muteMusic = other.muteMusic;
        this.muteSoundEffects = other.muteSoundEffects;
        this.muteAmbientSound = other.muteAmbientSound;
        this.disableProfanityFilter = other.disableProfanityFilter;
        this.splitPrivateChat = other.splitPrivateChat;
        this.hideRoofs = other.hideRoofs;
        this.removeAcceptTradeDelay = other.removeAcceptTradeDelay;
        this.adjustScreenDisplay = other.adjustScreenDisplay;
        this.resizableType = other.resizableType;
        this.waitForTradeFromMule = other.waitForTradeFromMule;
        this.onlyTradeListedAccounts = other.onlyTradeListedAccounts;
        this.sellItemsForBond = other.sellItemsForBond;
        this.logoutIfCannotAfford = other.logoutIfCannotAfford;
        this.logoutWhenFinished = other.logoutWhenFinished;
        this.usePriceModifier = other.usePriceModifier;
        this.trustedAccounts = other.trustedAccounts;
    }

}
