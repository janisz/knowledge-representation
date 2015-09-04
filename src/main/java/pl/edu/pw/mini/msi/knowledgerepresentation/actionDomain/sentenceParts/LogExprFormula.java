package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-09-04.
 */
public class LogExprFormula implements IFormula {

    private ActionLanguageParser.LogicalExpressionContext logExpr = null;
    private ArrayList<String> fluentsMapping = null;

    //initially ((night || day) => sleep)
    //log.debug("" + ctx.fluent()); //==null ; ctx.fluent().getText()
    //log.debug("" + ctx.logicalOperator()); //
    //log.debug("" + ctx.logicalExpression().size()); //
    //log.debug("" + ctx.logicalExpression(1).fluent().getText()); //

    public LogExprFormula(ActionLanguageParser.LogicalExpressionContext logExpr) {
        this.logExpr = logExpr;
    }

    @Override
    public char evaluateForValues(String evaluatee) {
        return 0;
    }

    @Override
    public ArrayList<String> getFluents() {
        ArrayList<String> fluentsAL = new ArrayList<String>();

        getFluentsFromLogExpr(logExpr, fluentsAL);

        return fluentsAL;
    }

    private void getFluentsFromLogExpr(ActionLanguageParser.LogicalExpressionContext logExpr,
                                                    ArrayList<String> fluentsAL) {
        if (logExpr.fluent() != null) {
            fluentsAL.add( logExpr.fluent().getText() );
        }
        else {
            getFluentsFromLogExpr(logExpr.logicalExpression(0), fluentsAL);
            getFluentsFromLogExpr(logExpr.logicalExpression(1), fluentsAL);
        }
    }

    @Override
    public ArrayList<Byte> getFluentsIDs() {
        ArrayList<String> fluentsAL = getFluents();
        ArrayList<Byte> fluentsIDsAL = new ArrayList<>();

        for (String fluent : fluentsAL) {
            Byte id = ArrayListOfStringUtils.getIndexOfString(fluentsAL, fluent);
            fluentsIDsAL.add(id);
        }

        return fluentsIDsAL;
    }

    @Override
    public void fillFluentsIDs(ArrayList<String> fluents) {
        this.fluentsMapping = fluents;
    }

    @Override
    public String getFluentsMask(short fluentCount) {
        StringBuilder resultSB = new StringBuilder(fluentCount);
        for (short index = 0; index < fluentCount; index++) {
            resultSB.append("-");
        }
        ArrayList<Byte> fluentIDs = getFluentsIDs();
        for (byte fluentID : fluentIDs) {
            resultSB.replace(fluentID, fluentID + 1, "+");
        }
        return resultSB.toString();
    }

    @Override
    public String toString() {
        //return logExpr.toString(); //not perfect, returns only one fluent TODO TOMEKL
        String result = "";

        result = getStringFromLogExpr(logExpr, result);

        return result;
    }

    private String getStringFromLogExpr(ActionLanguageParser.LogicalExpressionContext logExpr,
                                       String oldString) {
        if (logExpr.fluent() != null) {
            return (oldString + " " + logExpr.fluent().getText() );
        }
        else {
            oldString += getStringFromLogExpr(logExpr.logicalExpression(0), oldString);
            oldString += " " + logExpr.logicalOperator().getText() + " ";
            oldString += getStringFromLogExpr(logExpr.logicalExpression(1), oldString);
            return oldString;
        }
    }

    /*
    @Override
    public String toString(){
        ArrayList<ActionLanguageParser.LogicalExpressionContext> logExprs = new ArrayList<>();
        String result = "";
        logExprs.add(this.logExpr);
        while (logExprs.size() > 0) {
            ActionLanguageParser.LogicalExpressionContext logExpr = logExprs.remove(0);
            if (logExpr.fluent() != null) {
                result += " " + logExpr.fluent().getText();
            }
            else {

            }
            //TODO TOMEKL
        }
        return result;
    }
    */
}
