package scripts;

import scripts.api.script.ScriptConfiguration;
import scripts.api.script.ScriptExtension;

import java.util.function.UnaryOperator;

/* Written by IvanEOD 6/24/2022, at 7:09 AM */
public class GildedAltar extends ScriptExtension {


    @Override
    protected boolean onScriptStart(String args) {
        return false;
    }

    @Override
    protected void onMainLoop() {

    }

    @Override
    protected void onScriptShutdown() {

    }

    @Override
    protected UnaryOperator<ScriptConfiguration> updateScriptConfiguration() {
        return config -> config;
    }

}
