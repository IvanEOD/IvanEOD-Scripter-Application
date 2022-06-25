package scripts.api.classes;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.tribot.script.sdk.Log;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* Written by IvanEOD 6/25/2022, at 2:26 PM */
public class GitHub implements Directory {

    private static final String GITHUB_URL = "https://raw.githubusercontent.com/IvanEOD/IvanEOD-Scripter-Application/master/src/scripts/api/resources/";
    private static GitCache downloadCache;


    private static Path getGithubPath() {
        return getInstance().getDirectoryPath();
    }

    public static File getFont(String fontName) { return getGitFile("fonts", fontName + ".ttf"); }
    public static File getJson(String jsonName) {
        return getGitFile("json", jsonName + ".json");
    }
    public static File getFxml(String fxmlName) {
        return getGitFile("fxml", fxmlName + ".fxml");
    }
    public static File getCss(String cssName) {
        return getGitFile("css", cssName + ".css");
    }
    public static Image getImage(String imageName) { return FileHelper.loadImageFromFile(getImageFile(imageName)); }
    private static File getImageFile(String imageName) { return getGitFile("images", imageName + ".png"); }

    private static boolean gitHubFileExistsLocally(String folder, String fileName) {
        return getFullPath(folder, fileName).toFile().exists();
    }

    private static Path getFullPath(String folder, String etag) {
        return getInstance().getPath(folder, etag.replaceAll("\"", ""));
    }

    private static File getGitFile(String folder, String fileName) {
        String currentTag = getGitCache().getEtag(fileName);
        String checkTag = getETag(folder, fileName);
        boolean needsUpdate = currentTag == null || !Objects.equals(currentTag, checkTag);
        Path previousFile = null;
        Path requiredFile = null;
        if (currentTag != null) previousFile = getFullPath(folder, currentTag);
        if (checkTag != null) requiredFile = getFullPath(folder, checkTag);
        try {
            if (previousFile != null && Files.exists(previousFile) && needsUpdate) Files.delete(previousFile);
            if (requiredFile != null && (Files.notExists(requiredFile) || needsUpdate)) {
                if (Files.notExists(requiredFile)) Files.createFile(requiredFile);
                downloadGitFile(requiredFile.toFile(), folder, fileName);
            }
        } catch (IOException exception) {
            Log.error("ERROR GETTING GIT FILE", exception);
        }
        if (requiredFile == null) {
            Log.error("GIT FILE IS NULL: " + fileName);
            return null;
        }
        return requiredFile.toFile();
    }

    private static void downloadGitFile(File file, String folderName, String fileName) {
        String urlString = GITHUB_URL + folderName + "/" + fileName;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            String etag = connection.getHeaderField("ETag");
            if (etag != null) etag = etag.replaceAll("\"", "");
            InputStream inStream = connection.getInputStream();
            FileOutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int index = 0;
            while (-1 != (index = inStream.read(buffer))) output.write(buffer, 0, index);
            inStream.close();
            output.close();
            var cache = getGitCache();
            cache.setEtag(fileName, etag);
            getInstance().save(cache, "cache");
            System.out.println("Downloaded file from GitHub successfully.");
        } catch (IOException ioException) {
            Log.error("Error downloading file from GitHub", ioException);
        }
    }

    private static Image downloadGitImage(String imageName) {
        String urlString = GITHUB_URL + "images/" + imageName + ".png";
        Image getImage = null;

        try {
            URL url = new URL(urlString);
            getImage = SwingFXUtils.toFXImage(ImageIO.read(url), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getImage == null) {
            Log.warn("Failed to load image...");
            return null;
        }
        return getImage;
    }

    public static String getETag(String folder, String fileName) {
        String etag = null;
        String urlString = GITHUB_URL + folder + "/" + fileName;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            etag = connection.getHeaderField("ETag");
        } catch (IOException ioException) {
            Log.error("Error checking file from GitHub", ioException);
        }
        return etag != null ? etag.replaceAll("\"", "") : null;
    }
    
    @Override
    public Path getDirectoryPath() {
        return FileHelper.createTRiBotPath("github");
    }
        
    private GitHub() {  }

    private static GitCache getGitCache() {
        if (downloadCache == null) downloadCache = getInstance().load(GitCache.class, "cache").orElse(new GitCache());
        return downloadCache;
    }

    public static GitHub getInstance() { return GitHubInstance.INSTANCE; } 
    private static class GitHubInstance {
        private static final GitHub INSTANCE = new GitHub();
    }
    
    private static class GitCache {
        private Map<String, String> cache = new HashMap<>();
        public GitCache() {}
        public boolean etagExists(String fileName) { return cache.containsKey(fileName); }
        public String getEtag(String fileName) { return cache.get(fileName); }
        public void setEtag(String fileName, String etag) { cache.put(fileName, etag); }
    }
    
}
