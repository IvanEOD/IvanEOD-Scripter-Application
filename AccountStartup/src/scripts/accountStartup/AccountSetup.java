package scripts.accountStartup;

import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.script.TribotScriptManifest;
import scripts.accountStartup.nodes.PurchasingBond;
import scripts.appApi.script.ScriptConfiguration;
import scripts.appApi.script.ScriptExtension;

import java.util.function.UnaryOperator;

/* Written by IvanEOD 6/24/2022, at 6:55 AM */
@TribotScriptManifest(name = "Account Setup", author = "IvanEOD", category = "All-Reworked")
public class AccountSetup extends ScriptExtension {

    @Override
    protected boolean onScriptStart(String args) {
        return true;
    }

    @Override
    protected void onMainLoop() {

        var purchasingBond = new PurchasingBond().getSequence();

        var success = purchasingBond.tick();

        Log.info("Success = " + success);


        quit();
    }

    @Override
    protected void onScriptShutdown() {

    }

    @Override
    protected UnaryOperator<ScriptConfiguration> updateScriptConfiguration() {
//        return config -> config.cssName("NewCss").fxmlName("GrandExchangeBuyOfferGui");
        return config -> config;
    }


}
