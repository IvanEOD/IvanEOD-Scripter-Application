package scripts.appApi.script;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import scripts.appApi.classes.Utility;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

/* Written by IvanEOD 6/24/2022, at 7:11 AM */
public abstract class ScriptExtension implements TribotScript {

    @Getter
    protected ScriptConfiguration scriptConfiguration;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean startButtonPressed = new AtomicBoolean(false);

    @Getter
    protected ScriptGui gui;
    @Getter
    protected ScriptGuiController controller;

    protected abstract boolean onScriptStart(String args);
    protected abstract void onMainLoop();
    protected abstract void onScriptShutdown();
    protected abstract UnaryOperator<ScriptConfiguration> updateScriptConfiguration();

    private void onScriptShutdownInternal() {
        if (gui != null && gui.isShowing()) gui.close();
        onScriptShutdown();
    }

    private boolean onScriptStartInternal(String args) {

        return onScriptStart(args);
    }

    private void onMainLoopInternal() {
        onMainLoop();
        Waiting.wait(100);
    }

    @Override
    public void configure(@NotNull ScriptConfig config) {
        scriptConfiguration = new ScriptConfiguration();
        updateScriptConfiguration().apply(scriptConfiguration);
        config.setBreakHandlerEnabled(scriptConfiguration.isBreakHandlerEnabled());
        config.setRandomsAndLoginHandlerEnabled(scriptConfiguration.isRandomsAndLoginHandlerEnabled());

    }

    @Override
    public void execute(@NotNull String args) {
        if (!onScriptStartInternal(args)) {
            Log.error("Script failed startup checks, aborting.");
            return;
        }
        running.set(true);

        if (scriptConfiguration.isWaitUntilLoggedInToShowGui()) handleWaitForLogin();
        loadGui();

        if (gui != null) handleGuiSettings();

        while (running.get()) onMainLoopInternal();


        onScriptShutdownInternal();


        Log.info("Script Completed.");
    }

    private void handleGuiSettings() {

    }

    public void onGuiOpened() {  }
    public void onGuiClosed() {  }
    public void onGuiStartButton() {  }

    private void handleWaitForLogin() {
        if (scriptConfiguration.isOpenGuiAtScriptStart() && scriptConfiguration.isWaitUntilLoggedInToShowGui()) {
            if (Utility.waitUntilLoggedIn()) Log.trace("Finished logging in.");
        }
    }

    private void loadGui() {
        if (!scriptConfiguration.isUsingGui()) return;
        Log.trace("Loading GUI...");
        gui = new ScriptGui(this);
        Log.trace("GUI Loaded.");
        controller = gui.getController();
        Log.trace("GUI Controller Loaded: " + (controller != null));
//        if (controller == null) {
//            if (!Waiting.waitUntil(4000, () -> {
//                controller = gui.getController();
//                return controller != null;
//            })) throw new RuntimeException("Failed to load GUI.");
//        }

        if (scriptConfiguration.isOpenGuiAtScriptStart()) {
            gui.show();
            Waiting.waitUntil(2000, gui::isShowing);
            while(gui.isShowing() && scriptConfiguration.isStartButtonStartsScript() && !startButtonPressed.get()) {
                if (!running.get()) Log.warn("Script quit from Gui");
                Waiting.wait(100);
            }
        }




    }


    public void quit() {
        running.set(false);
    }
}
