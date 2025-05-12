package com.BaseReq;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationUtil {
    private static ResourceBundle bundle;

    public static void loadLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    public static String getMessage(String key) {
        return bundle.getString(key);
    }

    public static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
            System.out.println("Deleted file: " + filePath);
        } catch (Exception e) {
            System.err.println("Error deleting file: " + filePath);
            e.printStackTrace();
        }
    }
    public static void deleteFilesAndFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    deleteFilesAndFolder(file.getPath()); // Recursively delete subfolders
                }
                file.delete(); // Delete files and empty subfolders
            }
            folder.delete(); // Finally, delete the now-empty main folder
            System.out.println("Deleted folder: " + folderPath);
        } else {
            System.out.println("Folder does not exist: " + folderPath);
        }
    }
}
