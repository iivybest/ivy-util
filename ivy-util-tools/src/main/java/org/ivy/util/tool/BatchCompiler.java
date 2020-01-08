package org.ivy.util.tool;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 批量编译
 *
 * @author ivybest
 * @date 2017年9月5日 下午2:10:21
 */
public class BatchCompiler {

    public static void main(String[] args) {
        LinkedList<String> folderList = new LinkedList<String>();
        folderList.add(System.getProperty("user.dir"));
        while (folderList.size() > 0) {
            File file = new File(folderList.poll());
            File[] files = file.listFiles();

            String name;
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    folderList.add(files[i].getPath());
                } else {
                    name = files[i].getName();
                    if (files[i].getName().contains(".")
                            && (".c".equals(name.substring(name.lastIndexOf(".")))
                            || ".cpp".equals(name.substring(2).substring(name.lastIndexOf("."))))) {
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