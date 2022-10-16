import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    private static final List<String> filterFileName = new ArrayList<>();

    public static void main(String[] args) {

        File file = new File(path(new String[]{
                "Users",
                "zhaojian",
                "Documents",
                "library"
        }));

        //deleteFile();
        filterFileName.clear();
        filterFileName.add("build");
        filterFileName.add("bin");
        mainDeleteDirs(file);

        filterFileName.add("百度云SVIP长期免费使用.url");
        filterFileName.add("本教程由我爱学it提供.url");
        filterFileName.add("高清电子书籍.url");
        filterFileName.add("更多精品教程.url");
        filterFileName.add("下载必看.txt");
        //deleteFiles(file);
    }

    private static void mainDeleteDirs(File file) {
        if (file == null || !file.exists()) {
            System.out.println("文件不存在:" + file.getAbsolutePath());
            return;
        }
        if (file.isDirectory()) {
            for (File listFile : Objects.requireNonNull(file.listFiles())) {
                if (filterFileName.contains(listFile.getName())) {
                    deleteDirs(listFile);
                } else if(listFile.isDirectory()) {
                    mainDeleteDirs(listFile);
                }
            }
        }
    }

    private static void deleteDirs(File path) {
        if (path == null || !path.exists()) {
            System.out.println("文件不存在:" + path.getAbsolutePath());
            return;
        }
        if (path.isFile()) {
            path.deleteOnExit();
            System.out.println("delete " + path.getAbsolutePath());
        } else if (path.isDirectory()) {
            for (File file : Objects.requireNonNull(path.listFiles())) {
                deleteDirs(file);
            }
            path.delete();
            path.deleteOnExit();
        }
    }

    private static String path(String[] ps) {
        StringBuilder sb = new StringBuilder();
        for (String p : ps) {
            sb.append(File.separator).append(p);
        }
        return sb.toString();
    }

    private static void deleteFiles(File path) {
        if (path == null || !path.exists()) {
            System.out.println("文件不存在:" + path.getAbsolutePath());
            return;
        }
        String name = path.getName();
        //System.out.println("文件名称:" + name);
        if (path.isFile() && filterFileName.contains(name)) {
            path.deleteOnExit();
            System.out.println("delete " + path.getAbsolutePath());
        } else if (path.isDirectory()) {
            for (File file : Objects.requireNonNull(path.listFiles())) {
                deleteFiles(file);
            }
        }
    }


}
