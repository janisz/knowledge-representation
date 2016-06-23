package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Fluents;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ByteUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.CharUtils;

import java.util.ArrayList;

/**
 * Created by Tomek on 2015-09-04.
 */
public class LogExprFormula implements IFormula {

    private static final Logger log = LoggerFactory.getLogger(LogExprFormula.class);

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
        char result =  getEvaluateFromLogExpr(this.logExpr, evaluatee, fluentsMapping);;
        //DEBUGINFO
        //log.debug("Evaluating formula: [" + getStringFromLogExpr(this.logExpr) + "] with values [" +
        //        Fluents.evaluationToString(evaluatee, fluentsMapping) + "] == [" + result + "]" );
        return result;
    }

    private char getEvaluateFromLogExpr(ActionLanguageParser.LogicalExpressionContext logExpr, String evaluatee,
                                          ArrayList<String> fluentsMapping) {
        if (logExpr.fluent() != null) {
            byte fluentID = ArrayListOfStringUtils.getIndexOfString(fluentsMapping, logExpr.fluent().IDENTIFIER().getText());
            char result = evaluatee.charAt(fluentID);
            if (logExpr.fluent().NOT() != null) {
                result = CharUtils.switchZeroAndOne(result);
            }
            return result;
        }
        else {
            char ch1 = getEvaluateFromLogExpr(logExpr.logicalExpression(0), evaluatee, fluentsMapping);
            String operator = logExpr.logicalOperator().getText();
            char ch2 = getEvaluateFromLogExpr(logExpr.logicalExpression(1), evaluatee, fluentsMapping);

            if (ch1 == '?' || ch2 == '?') {
                return '?';
            }
            byte b1 = (byte) FormulaUtils.getShortForChar(ch1);
            byte b2 = (byte) FormulaUtils.getShortForChar(ch2);

            switch (operator) {
                case "&&": {
                    byte result = (byte) Math.min(b1, b2);
                    return ByteUtils.toZeroOrOneChar(result);
                }
                case "||": {
                    byte result = (byte) Math.max(b1, b2);
                    return ByteUtils.toZeroOrOneChar(result);
                }
                case "=>":
                    if (b1 == 1 && b2 == 0) {
                        return '0';
                    } else {
                        return '1';
                    }
                default:
                    return '?';
            }
        }
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
            String fluent = logExpr.fluent().IDENTIFIER().getText();
            if (ArrayListOfStringUtils.getIndexOfString(fluentsAL, fluent) == -1) {
                fluentsAL.add(fluent);
            }
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
            Byte id = ArrayListOfStringUtils.getIndexOfString(fluentsMapping, fluent);
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
        //String result = "";

        String result = getStringFromLogExpr(logExpr);

        return result;
    }

    private String getStringFromLogExpr(ActionLanguageParser.LogicalExpressionContext logExpr) {
        if (logExpr.fluent() != null) {
            return (" " + logExpr.fluent().getText() + " ");
        }
        else {
            String str = "(";
            str += getStringFromLogExpr(logExpr.logicalExpression(0));
            str += " " + logExpr.logicalOperator().getText() + " ";
            str += getStringFromLogExpr(logExpr.logicalExpression(1));
            str+= ")";
            return str;
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
