package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-08-29.
 */
public class Fluent {
    public String fluent;
    public byte fluentID = -1;

    public Fluent(String fluent){
        this.fluent = fluent;
    }

    public void fillFluentID(ArrayList<String> fluentsMapping) {
        fluentID = (byte)ArrayListOfStringUtils.getIndexOfString(fluentsMapping, fluent);
    }

    @Override
    public String toString() {
        return fluent;
    }

}
