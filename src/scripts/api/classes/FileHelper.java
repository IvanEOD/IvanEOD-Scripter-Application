package scripts.api.classes;

import com.google.gson.Gson;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Tribot;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Written by IvanEOD 6/18/2022, at 10:49 AM */
public class FileHelper {

    //<editor-fold desc="Public Static Constants">
    public static final Gson GSON = new Gson();
    //</editor-fold>

    //<editor-fold desc="Private Static Constants">
    private static final String ROOT_DIRECTORY_NAME = "IvanEOD-Scripts";
    private static final String GITHUB_URL = "https://raw.githubusercontent.com/IvanEOD/TribotScripterApplication/main/";
    private static final String FILE_REGEX = "[^a-zA-Z\\d\\-_.]+";
    private static final String FILE_EXTENSION_REGEX = "\\.([\\da-z]+)(?=[?#])|(\\.)\\w+$";
    private static final String FILE_DUPLICATE_NUMBER_REGEX = "-*(\\d+)$";
    //</editor-fold>


    public static Image getImage(String imageName) {
        var imageFile = getGitFile("images", imageName + ".png");
        return loadImageFromFile(imageFile);
    }

    public static File getCss(String cssName) {
        return getGitFile("css",cssName + ".css");
    }

    public static File getFxml(String fxmlName) {
        return getGitFile("fxml", fxmlName + ".fxml");
    }

    private static File getGitFile(String folder, String fileName) {
        var directory = Directory.Root;
        var shouldDownload = !directory.fileExists("github", folder, fileName)
                || directory.getLastModified("github", folder, fileName).isBefore(LocalDateTime.now().minusDays(3));
        if (shouldDownload) directory.downloadFile(GITHUB_URL + fileName, "github", folder, fileName);
        return directory.getFile("github", folder, fileName);
    }

    public static Path createDirectories(Path path) {
        if (path.toFile().exists()) return path;
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for path: " + path, e);
        }
    }

    public static void createSubDirectories(Path path) {
        var parentDirectory = path.getParent();
        createDirectories(parentDirectory);
    }

    public static String loadFileAsString(File file) {
        return new String(loadFile(file));
    }
    public static byte[] loadFile(File file) {
        if (!file.exists()) {
            Log.warn("Tried to load a file that does not exist: " + file.getAbsolutePath());
            return new byte[0];
        }
        InputStream inputStream = null;
        long length = file.length();
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        try {
            inputStream = new FileInputStream(file);
            while (offset < bytes.length && (numRead = inputStream
                    .read(bytes, offset, bytes.length - offset)) >= 0)
                offset += numRead;
            if (offset < bytes.length) {
                Log.warn("Could not completely read file " + file.getName());
                throw new IOException("Could not completely read file " + file.getName());
            }
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    public static Path createTRiBotPath(String... chunks) {
        var rootPath = Tribot.getDirectory().toPath().resolve(ROOT_DIRECTORY_NAME);
        return createDirectories(Paths.get(rootPath.toString(), formatFileName(chunks)));
    }


    public static <ClassType> boolean save(Path path, ClassType classType) {
        var json = GSON.toJson(classType);
        if (!path.toString().endsWith(".json")) path = Paths.get(path + ".json");
        var saveFile = writeFile(path, json);
        return saveFile != null;
    }
    public static <ClassType> Optional<ClassType> load(Path path, Class<ClassType> classTypeClass) {
        if (!path.toString().endsWith(".json")) path = Paths.get(path + ".json");
        if (!path.toFile().exists()) return Optional.empty();;
        var fileContent = loadFileAsString(path.toFile());
        if (fileContent.isEmpty()) return Optional.empty();;
        return Optional.of(GSON.fromJson(fileContent, classTypeClass));
    }

    public static File writeFile(Path filePath, String fileText) {
        return createFile(filePath, fileText);
    }
    private static File writeFile(Directory directory, String fileName, String fileText) {
        return createFile(directory.getPath().resolve(fileName), fileText);
    }
    private static File createFile(Directory directory, String fileName, String fileText) {
        return createFile(directory.getPath().resolve(fileName), fileText);
    }
    private static File createFile(Path path, String fileText) {
        createSubDirectories(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            writer.write(fileText);
        } catch (IOException e) {
            Log.warn("Error: Unable to write to directory.");
            return null;
        }
        return path.toFile();
    }

    public static File createFile(Path filePath, byte[] fileContents) {
        return createFile(filePath, new String(fileContents));
    }

    public static File downloadFile(String urlString, Directory directory, String... pathChunks) {
        Path filePath = Paths.get(directory.getPathString(), pathChunks);
        File downloadedFile = filePath.toFile();
        try {
            FileUtils.copyURLToFile(new URL(urlString), downloadedFile);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return downloadedFile;
    }

    public static boolean copyURLtoFile(String urlString, File file) {
        try {
            URL url = new URL(urlString);
            InputStream input = url.openStream();
            if (file.exists()) {
                if (file.isDirectory())
                    throw new IOException("File '" + file + "' is a directory");
                if (!file.canWrite())
                    throw new IOException("File '" + file + "' cannot be written");
            } else {
                File parent = file.getParentFile();
                if ((parent != null) && (!parent.exists()) && (!parent.mkdirs()))
                    throw new IOException("File '" + file + "' could not be created");
            }
            FileOutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = input.read(buffer)))
                output.write(buffer, 0, n);
            input.close();
            output.close();
            Log.trace("File '" + file + "' downloaded successfully!");
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
            return false;
        }
        return true;
    }

    public static Image loadImageFromFile(File file) {
        Image getImage = null;
        try {
            getImage = SwingFXUtils.toFXImage(ImageIO.read(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getImage == null) {
            Log.warn("Failed to load image...");
            return null;
        }
        return getImage;
    }
    public static Image loadImageFromURL(String urlString) {
        Image getImage = null;
        try {
            getImage = SwingFXUtils.toFXImage(ImageIO.read(new URL(urlString)), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getImage == null) {
            Log.warn("Failed to load image...");
            return null;
        }
        return getImage;
    }

    public static boolean deleteFilesOlderThan(long duration, TimeUnit unit, Path path, boolean recursive) {
        var directoryFile = path.toFile();
        var filter = new AgeFileFilter(TimeUnit.MILLISECONDS.convert(duration, unit));
        var files = directoryFile.listFiles((FileFilter) filter);
        if (files == null) return true;
        for (File file : files) {
            if (file.isDirectory()) {
                if (recursive) deleteFilesOlderThan(duration, unit, file.toPath(), true);
                var childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) file.delete();
            } else {
                file.delete();
            }
        }
        return true;
    }

    private static String[] formatFileName(String... chunks) {
        var cleanedChunks = Arrays.stream(chunks)
                .map(string -> string.replaceAll("\\\\", "/"))
                .flatMap(string -> {
                    String[] splitArray = new String[]{ string };
                    if (string.contains("/")) splitArray = string.split("/");
                    return Arrays.stream(splitArray);
                }).map(string -> {
                    final String subst = "-";
                    Pattern pattern = Pattern.compile(FILE_REGEX);
                    Matcher matcher = pattern.matcher(string);
                    var cleaned = matcher.replaceAll(subst).toLowerCase();
                    if (cleaned.length() > 31) return cleaned.substring(0, 31);
                    else return cleaned;
                }).toArray(String[]::new);
        List<String> combined = new ArrayList<>();
        StringBuilder combinedString = new StringBuilder();
        for (int index = 0; index < cleanedChunks.length; index++) {
            combinedString.append(cleanedChunks[index]);
            if (combinedString.toString().endsWith(".")) {
                if (index + 1 >= cleanedChunks.length) {
                    var fileName = combinedString.toString();
                    combined.add(fileName.substring(0, fileName.length() -1));
                    combinedString = new StringBuilder();
                }
            } else {
                combined.add(combinedString.toString());
                combinedString = new StringBuilder();
            }
        }
        return combined.toArray(String[]::new);
    }

    public static Font loadFontFromFile(File file) {
        try {
            return Font.createFont(Font.PLAIN, file);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private static String etagRequest(String fileName) {
        String etag = null;
        String urlString = GITHUB_URL + fileName;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            etag = connection.getHeaderField("ETag");

        } catch (IOException ioException) {
            Log.error("Error checking file from GitHub", ioException);
        }
        return etag;
    }

}
