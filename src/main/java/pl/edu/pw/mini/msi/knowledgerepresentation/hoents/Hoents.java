package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.AtSentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.OccursAtSentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Query;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Sentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;

/**
 * Usage:
 *      1. create
 *      2.
 * Created by Tomek on 2015-08-31.
 */
public class Hoents {

    private static final Logger log = LoggerFactory.getLogger(Hoents.class);

    public ArrayList<Sentence> sentences;
    public ArrayList<Query> queries;
    public byte tMax;
    public byte fluentsCount;

    public ArrayList<Hoent> structures;
    public ArrayList<Hoent> oMinimalStructures;
    public ArrayList<Hoent> modelsOfTypeOne; //fluents that were changed are in the set O(A,t)
    public ArrayList<Hoent> modelsOfTypeTwo; //maximization of typicalities

    public Hoents( ArrayList<Sentence> sentences, ArrayList<Query> queries, byte tMax, byte fluentsCount) {
        this.sentences = sentences;
        this.queries = queries;
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;

        structures = new ArrayList<Hoent>();
        oMinimalStructures= new ArrayList<Hoent>();
        modelsOfTypeOne = new ArrayList<Hoent>();
        modelsOfTypeTwo = new ArrayList<Hoent>();

        Hoent firstHoent = new Hoent(tMax, fluentsCount);
        structures.add(firstHoent);
    }

    public void getQueriesAnswers() {

    }

    private void calculateStructures() throws Exception {
        boolean isCurrentlyProcessingTypicalSentences = false; //Yes, I know it's a long name.
        ArrayList<Sentence> certainSentences = getSentences(isCurrentlyProcessingTypicalSentences);

        //1. proceed atSentence and occursAt sentences=================================================================
        for (Sentence sentence : certainSentences) {
            if ( (sentence instanceof AtSentence)
                    || (sentence instanceof OccursAtSentence) ){
                structures = sentence.applyCertainSentence(structures, fluentsCount, (byte)-1);
            }
        }
        //2. proceed certain sentences other than atSentence and occursAtSentence=======================================
        Boolean hasChanged = true;
        for (byte timeID = 0; timeID < tMax; timeID++) {
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    structures = sentence.applyCertainSentence(structures, fluentsCount, timeID);
                }
            }
        }

        for (short time = 0; time < this.tMax; time++) {
            for (Sentence sentence : certainSentences) {
                if ( sentence instanceof AtSentence) {

                }
            }
        }

        isCurrentlyProcessingTypicalSentences = true;

    }

    private void calculateOMinimalStructures() {

    }

    private void calculateModelsOfTypeOne() {

    }

    private void calculateModelsOfTypeTwo() {

    }

    //================================================================================================================
    private ArrayList<Sentence> getSentences(boolean isTypical) {
        ArrayList<Sentence> result = new ArrayList<Sentence>();

        for (Sentence sentence : this.sentences) {
            if (sentence.isTypical() == isTypical) {
                result.add(sentence);
            }
        }

        return result;
    }
}
