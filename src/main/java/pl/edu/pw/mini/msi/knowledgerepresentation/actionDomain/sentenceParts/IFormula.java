package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomek on 2015-08-29.
 */
public interface IFormula {
    //ArrayList<ArrayList<String>> getPositiveAndNegativeEvaluates(short fluentCount);
    //ArrayList<String> getNegativeEvaluates(short fluentCount);

    //void calculateIDs();
    char evaluateForValues(String evaluatee);
    ArrayList<String> getFluents();
    ArrayList<Byte> getFluentsIDs();
    void fillFluentsIDs(ArrayList<String> fluents);
    String getFluentsMask(short fluentCount);

    //ArrayList<String> restrictEvaluates(ArrayList<String> evaluates, String restricting);

}
