import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CopyDirectory {

    // пути для Version №1
    private static final String PATH_SRC_DIR = "/Users/maks/test-app";
    private static final String PATH_DEST_DIR = "/Users/maks/CopyTo/test-app";

    // пути для Version №2
    private static final String PATH_SRC_DIR_ONE = "/Users/maks/Documents/Try/srcDir/One";
    private static final String PATH_DEST_DIR_ONE = "/Users/maks/Documents/Try/destDir";

    // пути для Version №3
    private static final String PATH_SRC_DIR_TWO = "/Volumes/Transcend/CopyDir/srcDir";
    private static final String PATH_DEST_DIR_TWO = "/Volumes/Transcend/Copy/srcDir";


    public static void main(String[] args) throws IOException {

        File srcDir = new File(PATH_SRC_DIR);
        File destDir = new File(PATH_DEST_DIR);

        File srcDirOne = new File(PATH_SRC_DIR_ONE);
        File destDirOne = new File(PATH_DEST_DIR_ONE);


        //Version №1
        System.out.println("\nVersion №1 (use java.io)");
        long beging = System.currentTimeMillis();
        try {
            copy(srcDir, destDir);
        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Copying lasted " + (end - beging) + " ms"); // печататем время, за которое выполнилось копирование


        //Version №2
        System.out.println("\nVersion №2 (use commons-io)");
        long begingOne = System.currentTimeMillis();
        try {
            FileUtils.copyDirectoryToDirectory(srcDirOne, destDirOne);
//            FileUtils.copyDirectory(srcDir, destCrtDir);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        long endOne = System.currentTimeMillis();
        System.out.println("Copying lasted " + (endOne - begingOne) + " ms"); // печататем время, за которое выполнилось копирование



        //Version №3
        System.out.println("\nVersion №3 (use java.nio.file.Files)");

        long begingTwo = System.currentTimeMillis();
        Path from = Paths.get(PATH_SRC_DIR_TWO);
        Path target = Paths.get(PATH_DEST_DIR_TWO);
        try {
            Files.walk(from).forEach(source -> {
            try {
                Path destination = target.resolve(from.relativize(source));
                if (Files.isDirectory(source)) {
                    if (Files.notExists(destination)) {
                        System.err.println("create dir: " + destination);
                        Files.createDirectory(destination);
                    }
                } else {
                    System.out.println("copy file: " + source);
                    Files.copy(source, destination, REPLACE_EXISTING, COPY_ATTRIBUTES);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            });
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
        }
        long endTwo = System.currentTimeMillis();
        System.out.println("Directory copying completed, time copying " + (endTwo - begingTwo) + " ms");
    }



    // методы для Version №2
    private static void copy(File src, File dest) throws IOException { // основной метод копирования, в котором распределяется что будем копировать
        if (!src.exists()) {
            throw new FileNotFoundException("File or directory not exist");
        }
        if (src.isDirectory()) {
            copyDirectory(src, dest);
        } else {
            copyFile(src, dest);
        }
    }

    private static void copyDirectory(File src, File dest) throws IOException { // метод копирования папок
        if (!dest.exists()) dest.mkdirs();
        System.err.println("create dir: " + dest);
        for (File folder : src.listFiles(File::exists)) {
            File newFolder = new File(dest, folder.getName());
            copy(folder, newFolder);
            System.out.println("copy file: " + newFolder);
        }
        }


    private static void copyFile(File source, File target) throws IOException { // метод копирования файлов
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)
        ) {
            byte[] buf = new byte[in.available()];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

