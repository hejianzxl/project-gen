package com.yiyi.tang.projectgen.service;

import java.util.List;

/**
 * @author tangmingjian 2018-11-30 下午10:08
 **/

public class ParamsHelper {
    private static final ThreadLocal<String> ARTIFACT_ID = new ThreadLocal<>();            // artifactId
    private static final ThreadLocal<String> GROUP_ID = new ThreadLocal<>();               // groupId
    private static final ThreadLocal<List<String>> MODULES_SELECTED = new ThreadLocal<>(); // modules selected
    private static final ThreadLocal<List<String>> MODULES = new ThreadLocal<>();          // all modules

    public static void artifactId(String artifactId) {
        ARTIFACT_ID.set(artifactId);
    }

    public static String artifactId() {
        return ARTIFACT_ID.get();
    }


    public static void groupId(String groupId) {
        GROUP_ID.set(groupId);
    }

    public static String groupId() {
        return GROUP_ID.get();
    }


    public static void modulesSelected(List<String> modulesSelected) {
        MODULES_SELECTED.set(modulesSelected);
    }

    public static List<String> modulesSelected() {
        return MODULES_SELECTED.get();
    }

    public static void modules(List<String> modules) {
        MODULES.set(modules);
    }

    public static List<String> modules() {
        return MODULES.get();
    }
}
