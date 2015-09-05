package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

/**
 * Created by Tomek on 2015-09-05.
 */
public class CharUtils {
    public static byte toZeroOrOneByte(char ch) {
        if (ch == '0') {
            return 0;
        }
        else if (ch == '1') {
            return 1;
        }
        else {
            return -1;
        }
    }

    public static char switchZeroAndOne(char ch) {
        if (ch == '0') {
            return '1';
        }
        else if (ch == '1') {
            return '0';
        }
        else {
            return ch;
        }
    }
}
