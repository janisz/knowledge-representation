package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.AtSentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.OccursAtSentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Query;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.Sentence;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.ArrayListOfStringUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Usage:
 *      1. create
 *      2. getQueriesAnswers
 * Created by Tomek on 2015-08-31.
 */
public class Hoents {

    private static final Logger log = LoggerFactory.getLogger(Hoents.class);

    public ArrayList<Sentence> sentences;
    public ArrayList<Query> queries;
    public byte tMax;
    public byte fluentsCount;
    public ArrayList<String> actions;

    public ArrayList<Hoent> structures;
    public ArrayList<Hoent> oMinimalStructures;
    public ArrayList<Hoent> modelsOfTypeOne; //fluents that were changed are in the set O(A,t)
    public ArrayList<Hoent> modelsOfTypeTwo; //maximization of typicalities

    public Hoents( ArrayList<Sentence> sentences, ArrayList<Query> queries, byte tMax, byte fluentsCount,
                   ArrayList<String> actions) {
        this.sentences = sentences;
        this.queries = queries;
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;
        this.actions = actions;

        structures = new ArrayList<Hoent>();
        oMinimalStructures= new ArrayList<Hoent>();
        modelsOfTypeOne = new ArrayList<Hoent>();
        modelsOfTypeTwo = new ArrayList<Hoent>();

        Hoent firstHoent = new Hoent(tMax, fluentsCount);
        structures.add(firstHoent);
    }

    public ArrayList<Boolean> getQueriesAnswers()
            throws Exception {
        calculateStructures();
        calculateOMinimalStructures();
        calculateModelsOfTypeOne();
        calculateModelsOfTypeTwo();

        if (modelsOfTypeOne.size() == 0) {
            throw new Exception("Zero models of type one.");
        }
        if (modelsOfTypeTwo.size() == 0) {
            throw new Exception("Zero models of type two.");
        }

        ArrayList<Boolean> results = new ArrayList<Boolean>();
        for (Query query : this.queries) {
            results.add( query.getAnswer(modelsOfTypeOne, modelsOfTypeTwo, actions) );
        }
        return results;
    }

    private void calculateStructures() throws Exception {
        boolean isCurrentlyProcessingTypicalSentences = false; //Yes, I know it's a long name.
        ArrayList<Sentence> certainSentences = getSentences(isCurrentlyProcessingTypicalSentences);
        isCurrentlyProcessingTypicalSentences = true;
        ArrayList<Sentence> typicallySentences = getSentences(isCurrentlyProcessingTypicalSentences);

        //1. proceed atSentence and occursAt sentences=================================================================
        for (Sentence sentence : certainSentences) {
            if ( (sentence instanceof AtSentence)
                    || (sentence instanceof OccursAtSentence) ){
                structures = sentence.applyCertainSentence(structures, fluentsCount, (byte)-1);
            }
        }

        //3. proceed occursAt "typically" sentences=================================================================
        for (Sentence sentence : typicallySentences) {
            if ( (sentence instanceof AtSentence)
                    || (sentence instanceof OccursAtSentence) ){
                structures = sentence.applyTypicalSentence(structures, fluentsCount, (byte) -1, false);
            }
        }

        //2. proceed certain sentences other than atSentence and occursAtSentence=======================================
        //Boolean hasChanged = true;
        for (byte timeID = 0; timeID < tMax; timeID++) {
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    structures = sentence.applyCertainSentence(structures, fluentsCount, timeID);
                }
            }
        //}

        //4. proceed "typically" sentences other than occursAtSentence=======================================
        //Boolean hasChanged = true;
        //for (byte timeID = 0; timeID < tMax; timeID++) {
            for (Sentence sentence : typicallySentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    structures = sentence.applyTypicalSentence(structures, fluentsCount, timeID, false);
                }
            }
        //}

        //20150906
        //2. proceed certain sentences other than atSentence and occursAtSentence=======================================
        //Boolean hasChanged = true;
        //for (byte timeID = 0; timeID < tMax; timeID++) {
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    structures = sentence.applyCertainSentence(structures, fluentsCount, timeID);
                }
            }
        //}

        //4. proceed "typically" sentences other than occursAtSentence=======================================
        //Boolean hasChanged = true;
        //for (byte timeID = 0; timeID < tMax; timeID++) {
            for (Sentence sentence : typicallySentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    structures = sentence.applyTypicalSentence(structures, fluentsCount, timeID, false);
                }
            }
        }



    }

    /**
     * FAPR96.pdf site 8 ABOVE Definition 2
     * H=H', N=N' forall (timeid, actionid), fluents are in (strictly in) fluents'
     */
    private void calculateOMinimalStructures() {
        oMinimalStructures = (ArrayList<Hoent>)structures.clone();//new ArrayList<Hoent>();
        //boolean isStrictlyIn = false;
        //int deletionsOfSameStructuresCounter = 0;
        int deletionsOfNotOMinimalStructuresCounter = 0;

        for (int index1 = 0; index1 < structures.size(); index1++) {
            for (int index2 = index1 + 1; index2 < structures.size(); index2++) {
                Hoent firstHoent = structures.get(index1);
                Hoent secondHoent = structures.get(index2);

                if (firstHoent.hAreSysElemHsTheSame(secondHoent.sysElemH) == false) {
                    continue;
                }
                if (firstHoent.nAreSysElemNsTheSame(secondHoent.sysElemN) == false) {
                    continue;
                }
                if (firstHoent.oIsIn(secondHoent.sysElemO)) {
                    oMinimalStructures.remove(secondHoent);
                    deletionsOfNotOMinimalStructuresCounter++;
                }
                else if (secondHoent.oIsIn(firstHoent.sysElemO)) {
                    oMinimalStructures.remove(firstHoent);
                    deletionsOfNotOMinimalStructuresCounter++;
                }
                //TODO TOMEKL throws?
            }
        }
        log.debug("deletionsOfNotOMinimalStructuresCounter:" + String.valueOf(deletionsOfNotOMinimalStructuresCounter));
    }

    /**
     * Fluents that changed are in the set O(A,t)
     * FAPR96.pdf site 8 Definition 2
     */
    private void calculateModelsOfTypeOne() throws Exception {
        modelsOfTypeOne = new ArrayList<Hoent>();

        //time == 0
        for (Hoent oMinimalStructure : oMinimalStructures) {
            String hAtTimeZero = oMinimalStructure.sysElemH.get(0);
            if (Fluents.countQuestionMarks(hAtTimeZero) == 0) {
                modelsOfTypeOne.add(oMinimalStructure);
            } else {
                ArrayList<String> hElems = Fluents.expandQuestionMarks(hAtTimeZero);
                for (String hElem : hElems) {
                    Hoent copyOfOMinStr = oMinimalStructure.copy();
                    copyOfOMinStr.sysElemH.remove(0);
                    copyOfOMinStr.sysElemH.add(0, hElem);
                    modelsOfTypeOne.add(copyOfOMinStr);
                }
            }
        }

        //time > 0
        for (byte timeIndex = 1; timeIndex < tMax; timeIndex++) {
            ArrayList<Hoent> newModelsOfTypeOne = new ArrayList<Hoent>();
            for (Hoent modelOfTypeOne : modelsOfTypeOne) {
                ArrayList<HashMap<Byte, String>> sysElemO = modelOfTypeOne.sysElemO;
                HashMap<Byte, String> sysElemOAtTime = sysElemO.get(timeIndex - 1); // "- 1" important
                //ArrayList<String> posEvalsFromAtSentences = getPosEvalsFromAtSentences(sentences, timeIndex);
                if (sysElemOAtTime.keySet().size() == 0){
                    //20150906
                    Hoent newModelOfTypeOne = modelOfTypeOne.copy();
                    newModelOfTypeOne.hPreserveFluentsAtTime(timeIndex);
                    //if (newModelOfTypeOne.hCheckCompatibility(modelOfTypeOne.sysElemH.get(timeIndex), timeIndex) == false) {
                        //continue;
                    //}
                    if (Fluents.checkCompatibilityUsingMask(modelOfTypeOne.sysElemH.get(timeIndex - 1),
                            modelOfTypeOne.sysElemH.get(timeIndex), null, null) == false ) {
                        continue;
                    }
                    newModelsOfTypeOne.add( newModelOfTypeOne );
                }
                else {
                    String fluentsInO = sysElemOAtTime.entrySet().iterator().next().getValue();
                    ////20150906
                    //-------------------------------------------------------------------------------------------------
                    //ArrayList<ArrayList<String>> posAndNegEvaluates =
                    //        FormulaUtils.getPositiveAndNegativeEvaluates(this.conditionFormula, fluentsCount);
                    //ArrayList<String> posEvaluates = posAndNegEvaluates.get(0); //e.g., ?100? [fluentIDs: 2,3,4; negations: 0,1,1; fluentCount: 5]

                    //-------------------------------------------------------------------------------------------------
                    ArrayList<String> newHsFromOcclusion =
                            Fluents.expandQuestionMarksWithMask(modelOfTypeOne.sysElemH.get(timeIndex - 1),
                                    modelOfTypeOne.sysElemH.get(timeIndex), fluentsInO);
                   for( String newH : newHsFromOcclusion) {
                        Hoent newModelOfTypeOne = modelOfTypeOne.copy();
                        newModelOfTypeOne.sysElemH.remove(timeIndex);
                        newModelOfTypeOne.sysElemH.add(timeIndex, newH);
                        //if (newModelOfTypeOne.hCheckCompatibility(modelOfTypeOne.sysElemH.get(timeIndex), timeIndex) == false) {
                        //   continue;
                        //}
                        if (Fluents.checkCompatibilityUsingMask(modelOfTypeOne.sysElemH.get(timeIndex - 1),
                               modelOfTypeOne.sysElemH.get(timeIndex), fluentsInO, null) == false) {
                            continue;
                        }

                        newModelsOfTypeOne.add(newModelOfTypeOne);
                    }
                }
            }
            modelsOfTypeOne = newModelsOfTypeOne;
        }

        //20150906
        //after filling '?' with values (using occlusion) check if new HOENT's are compatible with sentences
        //2. proceed certain sentences other than atSentence and occursAtSentence=======================================
        //Boolean hasChanged = true;
        ArrayList<Sentence> certainSentences = getSentences(false);
        for (byte timeID = 0; timeID < tMax; timeID++) {
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    modelsOfTypeOne = sentence.applyCertainSentence(modelsOfTypeOne, fluentsCount, timeID);
                }
            }
        }

        //20150906
        //minimal set of occured actions
        int deletionsOfEMinimalModelsCounter = 0;

        ArrayList<Hoent> newModelsOfTypeOne = (ArrayList<Hoent>)modelsOfTypeOne.clone();
        for (int index1 = 0; index1 < modelsOfTypeOne.size(); index1++) {
            for (int index2 = index1 + 1; index2 < modelsOfTypeOne.size(); index2++) {
                Hoent firstHoent = modelsOfTypeOne.get(index1);
                Hoent secondHoent = modelsOfTypeOne.get(index2);

                if (firstHoent.hAreSysElemHsTheSame(secondHoent.sysElemH) == false) {
                    continue;
                }
                if (firstHoent.oAreSysElemOsTheSame(secondHoent.sysElemO) == false) {
                    continue;
                }
                if (firstHoent.nAreSysElemNsTheSame(secondHoent.sysElemN) == false) {
                    continue;
                }
                if (firstHoent.eIsIn(secondHoent.sysElemE)) {
                    newModelsOfTypeOne.remove(secondHoent);
                    deletionsOfEMinimalModelsCounter++;
                }
                else if (secondHoent.eIsIn(firstHoent.sysElemE)) {
                    newModelsOfTypeOne.remove(firstHoent);
                    deletionsOfEMinimalModelsCounter++;
                }
            }
        }
        modelsOfTypeOne = newModelsOfTypeOne;
        log.debug("deletionsOfEMinimalModelsCounter: " + String.valueOf(deletionsOfEMinimalModelsCounter));

    }

    private ArrayList<String> getPosEvalsFromAtSentences(ArrayList<Sentence> sentences, byte timeIndex) {
        ArrayList<String> posEvaluates = null;
        for (Sentence sentence : sentences) {
            if (sentence instanceof AtSentence) {
                AtSentence atSentence = (AtSentence)sentence;
                if (atSentence.time.timeID != timeIndex) {
                    continue;
                }
                ArrayList<ArrayList<String>> posAndNegEvaluatesForSentence =
                        FormulaUtils.getPositiveAndNegativeEvaluates(atSentence.formula, fluentsCount);
                ArrayList<String> posEvaluatesForSentence = posAndNegEvaluatesForSentence.get(0);
                if (posEvaluates == null) {
                    posEvaluates = posEvaluatesForSentence;
                }
                else {
                    posEvaluates = Fluents.getFluentsConjunction(posEvaluates, posEvaluatesForSentence);
                }
            }
        }
        return posEvaluates;
    }

    /**
     * Maximization of typicalities.
     * FAPR96.pdf site 9 Definition 3
     */
    private void calculateModelsOfTypeTwo() {
        modelsOfTypeTwo = (ArrayList<Hoent>)modelsOfTypeOne.clone();//new ArrayList<Hoent>();
        //boolean isStrictlyIn = false;
        //int deletionsOfSameStructuresCounter = 0;
        int deletionsOfGMDPreferredModelsCounter = 0;

        for (int index1 = 0; index1 < modelsOfTypeOne.size(); index1++) {
            for (int index2 = index1 + 1; index2 < modelsOfTypeOne.size(); index2++) {
                Hoent firstHoent = modelsOfTypeOne.get(index1);
                Hoent secondHoent = modelsOfTypeOne.get(index2);

                if (firstHoent.nIsIn(secondHoent.sysElemN)) {
                    modelsOfTypeTwo.remove(firstHoent);
                    deletionsOfGMDPreferredModelsCounter++;
                }
                else if (secondHoent.nIsIn(firstHoent.sysElemN)) {
                    modelsOfTypeTwo.remove(secondHoent);
                    deletionsOfGMDPreferredModelsCounter++;
                }
            }
        }
        log.debug("deletionsOfGMDPreferredModelsCounter: " + String.valueOf(deletionsOfGMDPreferredModelsCounter));

        //preserve hoents with max number of ns===============================================
        int deletionsOfGMDPreferredModelsCounter_2 = 0;
        ArrayList<Hoent> newModelsOfTypeTwo = (ArrayList<Hoent>)modelsOfTypeTwo.clone();//new ArrayList<Hoent>();
        int maxNs = Integer.MIN_VALUE;
        for (Hoent modelOfTypeTwo : modelsOfTypeTwo) {
            int ns = modelOfTypeTwo.nCountNs();
            if (ns >= maxNs) {
                maxNs = ns;
            }
        }
        for (Hoent modelOfTypeTwo : modelsOfTypeTwo) {
            if (modelOfTypeTwo.nCountNs() < maxNs) {
                newModelsOfTypeTwo.remove(modelOfTypeTwo);
                deletionsOfGMDPreferredModelsCounter_2++;
            }
        }

        modelsOfTypeTwo = newModelsOfTypeTwo;
        log.debug("preserve hoents with max number of ns deletions: " + String.valueOf(deletionsOfGMDPreferredModelsCounter_2));

        //remove duplicate HOENTs===============================================
        int deletionsOfGMDPreferredModelsCounter_3 = 0;
        newModelsOfTypeTwo = (ArrayList<Hoent>)modelsOfTypeTwo.clone();//new ArrayList<Hoent>();
        for (int firstIndex = 0; firstIndex < modelsOfTypeTwo.size(); firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < modelsOfTypeTwo.size(); secondIndex++) {
                Hoent firstHoent = modelsOfTypeTwo.get(firstIndex);
                Hoent secondHoent = modelsOfTypeTwo.get(secondIndex);
                if (firstHoent.hAreSysElemHsTheSame(secondHoent.sysElemH) &&
                        firstHoent.oAreSysElemOsTheSame(secondHoent.sysElemO) &&
                        firstHoent.eAreSysElemEsTheSame(secondHoent.sysElemE) &&
                        firstHoent.nAreSysElemNsTheSame(secondHoent.sysElemN)) {
                    newModelsOfTypeTwo.remove(secondHoent);
                    deletionsOfGMDPreferredModelsCounter_3++;
                }
            }
        }

        modelsOfTypeTwo = newModelsOfTypeTwo;
        log.debug("remove duplicate HOENTs deletions: " + String.valueOf(deletionsOfGMDPreferredModelsCounter_3));
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
