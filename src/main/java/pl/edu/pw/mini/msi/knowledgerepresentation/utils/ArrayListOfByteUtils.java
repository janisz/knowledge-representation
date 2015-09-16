package pl.edu.pw.mini.msi.knowledgerepresentation.utils;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
    }

    public static boolean contains(ArrayList<Byte> list, Byte newByte) {
        for (Byte listElem : list) {
            int comparisonResult = listElem.compareTo(newByte);
            if (comparisonResult == 0) {
                return true;
            }
        }
        return false;
        //TODO TOMEKL optimize, because list is sorted
    }

    public static ArrayList<Byte> copy(ArrayList<Byte> oldList) {
        return oldList.stream().map(Byte::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean areSame(ArrayList<Byte> list1, ArrayList<Byte> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (Byte elem1 : list1) {
            if (!ArrayListOfByteUtils.contains(list2, elem1)) {
                return false;
            }
        }
        return true;
    }

    public static String myToString(ArrayList<Byte> list) {
        return "[" + Joiner.on(",").join(list) + "]";
    }
}