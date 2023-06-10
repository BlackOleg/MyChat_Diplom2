package olegivanov.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Log {
    private static Log INSTANCE = null;

    public String getLevel() {
        return level;
    }

    private String level;

    private Log(String level) {
        this.level = level;
    }

    public static Log getInstance(String level) {
        if (INSTANCE == null) {
            synchronized (Log.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Log(level);
                }
            }

        }
        return INSTANCE;
    }

    public void logInsert(String who, String msg) {
        StringBuilder out = new StringBuilder();
        out.append("[");
        out.append(LocalDateTime.now());
        out.append("]");
        out.append(",");
        out.append(level);
        out.append(",");
        out.append(who);
        out.append(",");
        out.append(msg);
        out.append("\r\n");
        System.out.println(out);
        toFile(String.valueOf(out));
    }

    private void toFile(String line) {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Path currentRelativePath = Paths.get("");
        String currentPath = currentRelativePath.toAbsolutePath().toString();
        StringBuilder path = new StringBuilder();
        path.append(currentPath);
        path.append("\\");
        path.append(this.level);
        path.append("\\");
        path.append("file.log");
        try (FileWriter writer = new FileWriter(String.valueOf(path), true)) {
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(line);
            bufferWriter.flush();
        } catch (IOException ex) {
            System.out.println("Что-то пошло не так " + ex.getMessage());
        }
    }

}

