package com.zhqydot.framework.easyrouter.compiler;

import java.io.File;
import java.io.IOException;

import javax.annotation.processing.Filer;

public class ModuleUtils {

    private static final String APT_DIR = File.separator + "build" + File.separator + "generated" + File.separator + "source" + File.separator;
    private static final String APT = "apt";
    private static final String KAPT = "kapt";

    public static String getModuleName(Filer filer) {
        if (null == filer) {
            return null;
        }

        String moduleFilePath = null;
        try {
            moduleFilePath = filer.createSourceFile("module_docker").toUri().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != moduleFilePath) {
            String dir = APT_DIR + (moduleFilePath.contains(KAPT) ? KAPT : APT);
            int endIndex = moduleFilePath.indexOf(dir);
            if (endIndex > 0) {
                int startIndex = moduleFilePath.lastIndexOf(File.separator, endIndex - 1) + 1;
                if (startIndex > 0 && startIndex < endIndex) {
                    return moduleFilePath.substring(startIndex, endIndex);
                }
            }
        }


        return null;
    }
}