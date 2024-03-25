package helpers;

import org.apache.commons.codec.language.bm.Languages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Initializations {
    public static ThreadLocal<Boolean> initialized = new ThreadLocal<>();
    public static List<String> testCaseIds = new ArrayList<>();
    public static ThreadLocal<File> screenshot = new ThreadLocal<>();
    public static ThreadLocal<Long> timeStart = new ThreadLocal<>();
    public static ThreadLocal<String> testCaseName = new ThreadLocal<>();
    public static ThreadLocal<Languages.LanguageSet> language = new ThreadLocal<Languages.LanguageSet>();
}
