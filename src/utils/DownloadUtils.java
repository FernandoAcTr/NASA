package utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadUtils {

    public static final String temporalPath = "tmp/";

    public static File downloadFile(String name, String extension, String url) {
        createTmpDirectory();

        File saveFile = null;

        try {
            saveFile = File.createTempFile(name, extension, new File(temporalPath));

            int readBytes;

            BufferedInputStream is = new BufferedInputStream(new URL(url).openStream());
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(saveFile));

            while ((readBytes = is.read()) != -1)
                os.write(readBytes);

            is.close();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        saveFile.deleteOnExit();
        return saveFile;
    }

    private static void createTmpDirectory() {
        File tempFile = new File(temporalPath);
        if (!tempFile.exists())
            tempFile.mkdirs();
    }

}
