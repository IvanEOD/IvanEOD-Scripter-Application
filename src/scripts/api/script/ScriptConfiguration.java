package scripts.api.script;

import org.tribot.script.sdk.Log;

/* Written by IvanEOD 6/24/2022, at 7:12 AM */
public class ScriptConfiguration {



    private boolean isBreakHandlerEnabled = true;
    private boolean isRandomsAndLoginHandlerEnabled = true;

    private String fxmlName = null;
    private String cssName = null;

    private boolean openGuiAtScriptStart = true;
    private boolean startButtonStartsScript = true;
    private boolean stopScriptOnEarlyGuiClose = true;
    private boolean waitUntilLoggedInToShowGui = true;

    public boolean isUsingGui() { Log.trace("FXML Name: "+ fxmlName); return fxmlName != null; }
    public boolean isStartButtonStartsScript() {
        return startButtonStartsScript;
    }
    public boolean isStopScriptOnEarlyGuiClose() {
        return stopScriptOnEarlyGuiClose;
    }
    public boolean isWaitUntilLoggedInToShowGui() {
        return waitUntilLoggedInToShowGui;
    }
    public boolean isBreakHandlerEnabled() { return isBreakHandlerEnabled; }
    public boolean isRandomsAndLoginHandlerEnabled() { return isRandomsAndLoginHandlerEnabled; }

    public String getFxmlName() { return fxmlName; }
    public String getCssName() { return cssName; }
    public boolean isOpenGuiAtScriptStart() { return openGuiAtScriptStart; }

    public ScriptConfiguration fxmlName(String fxmlName) {
        this.fxmlName = fxmlName;
        return this;
    }
    public ScriptConfiguration cssName(String cssName) {
        this.cssName = cssName;
        return this;
    }
    public ScriptConfiguration defaultCss() {
        this.cssName = "IvanEODStyle";
        return this;
    }
    public ScriptConfiguration disableLoadGuiAtScriptStart() {
        this.openGuiAtScriptStart = true;
        return this;
    }
    public ScriptConfiguration disableWaitUntilLoggedInToShowGui() {
        waitUntilLoggedInToShowGui = false;
        return this;
    }
    public ScriptConfiguration disableStartButtonStartsScript() {
        startButtonStartsScript = false;
        return this;
    }
    public ScriptConfiguration disableStopScriptOnEarlyGuiClose() {
        stopScriptOnEarlyGuiClose = false;
        return this;
    }
    public ScriptConfiguration disableBreakHandler() {
        isBreakHandlerEnabled = false;
        return this;
    }
    public ScriptConfiguration disableRandomsAndLoginHandler() {
        isRandomsAndLoginHandlerEnabled = false;
        return this;
    }


}
