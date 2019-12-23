package org.ivy.tools;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Createdate    2017年9月5日 下午2:10:21
 * @Classname BatchCompiler
 * @Description 批量编译
 */
public class BatchCompiler {

    public static void main(String[] args) {
        LinkedList<String> folderList = new LinkedList<String>();
        folderList.add(System.getProperty("user.dir"));
        while (folderList.size() > 0) {
            File file = new File(folderList.poll());
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    folderList.add(files[i].getPath());
                } else {
                    if (files[i].getName().contains(".")
                            && (files[i].getName().substring(files[i].getName().lastIndexOf(".")).equals(".c")
                            || files[i].getName().substring(2).substring(files[i].getName().lastIndexOf("."))
                            .equals(".cpp"))) {
                        try {
                            Runtime.getRuntime()
                                    .exec(new String[]{"gcc", files[i].getAbsoluteFile().toString(), "-o",
                                            files[i].getAbsoluteFile().toString().substring(0,
                                                    files[i].getAbsoluteFile().toString().lastIndexOf("."))});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}