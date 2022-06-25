package scripts.api.classes;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/* Written by IvanEOD 6/24/2022, at 10:27 AM */
public abstract class StorableClass<SaveClass extends StorableClass<SaveClass>> {

    protected final Directory directory;
    protected final String subdirectory;
    protected String saveFileName = "lastRun";

    public StorableClass() { this(Directory.Script, "stored-classes"); }
    public StorableClass(Directory directory, String subdirectory) {
        this.directory = directory;
        this.subdirectory = subdirectory;
    }
    public StorableClass(Directory directory, String subdirectory, String saveFileName) { this(directory, subdirectory, saveFileName, true); }
    public StorableClass(Directory directory, String subdirectory, String saveFileName, boolean load) {
        this.directory = directory;
        this.subdirectory = subdirectory;
        this.saveFileName = saveFileName;
        if (load) load();
    }

    public List<String> getSaveFileNames() {
        return Arrays.stream(directory.getFilesInDirectory(subdirectory)).map(File::getName).collect(Collectors.toList());
    }
    public abstract void copy(SaveClass other);

    public void save() {
        directory.save(this, subdirectory, saveFileName);
    }

    public void save(String saveFileName) {
        this.saveFileName = saveFileName;
        save();
    }

    public SaveClass load(String saveFileName) {
        this.saveFileName = saveFileName;
        return load();
    }

    public SaveClass load() {
        var temp = loadInternal((SaveClass) this, saveFileName);
        if (temp!= null) copy(temp);
        return (SaveClass) this;
    }

    @SuppressWarnings("unchecked")
    private SaveClass loadInternal(SaveClass defaultClass, String saveFileName) {
        var loaded = directory.load(defaultClass.getClass(), subdirectory, saveFileName).orElse(null);
        if (loaded != null) return (SaveClass) loaded;
        return (SaveClass) this;
    }

    private String getClassName() {
        return Utility.toTitleCase(getClass().getSimpleName());
    }

}
