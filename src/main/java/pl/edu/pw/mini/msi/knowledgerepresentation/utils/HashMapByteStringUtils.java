package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

import java.util.HashMap;

/**
 * Created by Tomek on 2015-09-02.
 */
public class HashMapByteStringUtils {
    public static HashMap<Byte, String> copy(HashMap<Byte, String> oldHashMap) {
        HashMap<Byte, String> newHashMap = new HashMap<Byte, String>();

        for (Byte b : oldHashMap.keySet()) {
            String str = oldHashMap.get(b);
            newHashMap.put(b, str);
        }

        return newHashMap;
    }
}
