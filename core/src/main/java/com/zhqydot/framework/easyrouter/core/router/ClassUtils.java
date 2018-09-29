package com.zhqydot.framework.easyrouter.core.router;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class ClassUtils {
    public static List<String> getClasses(Context context, String startWith) {
        List<String> classes = new ArrayList<>();
        String parentDir = new File(context.getPackageCodePath()).getParent();
        String[] files = new File(new File(context.getPackageCodePath()).getParent()).list();
        for (String file : files) {
            if (file.endsWith(".apk")) {
                String apkFile = parentDir + "/" + file;
                try {
                    DexFile df = new DexFile(apkFile);
                    Enumeration<String> enumeration = df.entries();
                    while (enumeration.hasMoreElements()) {
                        String className = enumeration.nextElement();
                        if (className.startsWith(startWith)) {
                            classes.add(className);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return classes;
    }
}
