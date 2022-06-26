package scripts;

import org.tribot.script.sdk.script.TribotScriptManifest;
import scripts.api.script.ScriptConfiguration;
import scripts.api.script.ScriptExtension;

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

    }

    @Override
    protected void onScriptShutdown() {

    }

    @Override
    protected UnaryOperator<ScriptConfiguration> updateScriptConfiguration() {
        return config -> config.disableWaitUntilLoggedInToShowGui().cssName("NewCss").fxmlName("Template");
    }


}
