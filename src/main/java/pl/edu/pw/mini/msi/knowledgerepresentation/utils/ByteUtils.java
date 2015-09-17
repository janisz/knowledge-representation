package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

/**
 * Created by Tomek on 2015-09-05.
 */
public class ByteUtils {
    public static char toZeroOrOneChar(byte b) {
        switch (b) {
            case 0:
                return '0';
            case 1:
                return '1';
            default:
                return '?';
        }
    }
}
