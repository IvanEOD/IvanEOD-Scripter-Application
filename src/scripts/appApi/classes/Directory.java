package scripts.appApi.classes;

import javafx.scene.image.Image;
import org.tribot.script.sdk.Tribot;
import org.tribot.script.sdk.script.ScriptRuntimeInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/* Written by IvanEOD 6/18/2022, at 10:51 AM */
public interface Directory {


    Directory TRiBot = () -> Tribot.getDirectory().toPath();
    Directory Root = FileHelper::createTRiBotPath;
    Directory Script = () -> FileHelper.createTRiBotPath("scripts", ScriptRuntimeInfo.getScriptName());
    scripts.appApi.classes.GitHub GitHub = scripts.appApi.classes.GitHub.getInstance();
    Directory Account = () -> FileHelper.createTRiBotPath("tribot-account", Tribot.getUsername());

    default Path getLocksDirectory() {
        return getDirectoryPath().resolve("locks");
    }

    Path getDirectoryPath();

    default String getPathString() {
        return getDirectoryPath().toString();
    }

    default File downloadFile(String url, String... chunks) {
        return FileHelper.downloadFile(url, this, chunks);
    }

    default boolean fileExists(String... chunks) {
        return getFile(chunks).exists();
    }

    default byte[] loadFile(String... chunks) {
        return FileHelper.loadFile(getFile(chunks));
    }

    default File getFile(String... chunks) {
        return getPath(chunks).toFile();
    }

    default File[] getFilesInDirectory(String... chunks) {
        var file = getFile(chunks);
        if (!file.exists()) FileHelper.createDirectories(getPath(chunks));
        file = getFile(chunks);
        if (!file.isDirectory()) return new File[0];
        return file.listFiles();
    }

    default Path getPath(String... pathChunks) {
        var path = Paths.get(getDirectoryPath().toString(), pathChunks);
        FileHelper.createSubDirectories(path);
        return path;
    }

    default String loadFileAsString(String... chunks) {
        return FileHelper.loadFileAsString(getFile(chunks));
    }

    default <ClassType> boolean save(ClassType saveClass, String... chunks) {
        return FileHelper.save(getPath(chunks), saveClass);
    }

    default <ClassType> Optional<ClassType> load(Class<ClassType> classTypeClass, String... chunks) {
        return FileHelper.load(getPath(chunks), classTypeClass);
    }

    default boolean copyURLtoFile(String urlString, String... chunks) {
        return FileHelper.copyURLtoFile(urlString, getFile(chunks));
    }

    default Image loadImageFromFile(String... chunks) {
        return FileHelper.loadImageFromFile(getFile(chunks));
    }

    default void writeToFile(String content, String... chunks) {
        FileHelper.writeFile(getPath(chunks), content);
    }
    
    default void clean() {
        FileHelper.deleteFilesOlderThan(5, TimeUnit.DAYS, getDirectoryPath(), true);
    }

    default LocalDateTime getLastModified(String... chunks) {
        var file = getFile(chunks);
        if (!file.exists()) return LocalDateTime.MIN;
        var lastModified = file.lastModified();
        return Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
