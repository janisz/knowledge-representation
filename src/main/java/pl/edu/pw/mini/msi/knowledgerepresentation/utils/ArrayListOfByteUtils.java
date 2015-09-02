package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-09-01.
 */
public class ArrayListOfByteUtils {
    public static void insertIntoArrayList(ArrayList<Byte> list, Byte newByte) {
        for (byte index = 0; index < list.size(); index++) {
            Byte listElem = list.get(index);
            int comparisonResult = listElem.compareTo(newByte);
            if (comparisonResult == 0) {
                return;
            } else if (comparisonResult < 0) {
                list.add(index, newByte);
                return;
            }
        }
        list.add(newByte);
        return;
    }

    public static boolean contains(ArrayList<Byte> list, Byte newByte) {
        for (byte index = 0; index < list.size(); index++) {
            Byte listElem = list.get(index);
            int comparisonResult = listElem.compareTo(newByte);
            if (comparisonResult == 0) {
                return true;
            }
        }
        return false;
        //TODO TOMEKL optimize, because list is sorted
    }
}