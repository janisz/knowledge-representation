package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.FormulaUtils;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.*;
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
    public HoentsSettings hoentsSettings;

    public ArrayList<Hoent> structures;
    public ArrayList<Hoent> oMinimalStructures;
    public ArrayList<Hoent> modelsOfTypeOne; //fluents that were changed are in the set O(A,t)
    public ArrayList<Hoent> modelsOfTypeTwo; //maximization of typicalities

    public Hoents( ArrayList<Sentence> sentences, ArrayList<Query> queries, byte tMax, byte fluentsCount,
                   ArrayList<String> actions, HoentsSettings hoentsSettings) {
        this.sentences = sentences;
        this.queries = queries;
        this.tMax = tMax;
        this.fluentsCount = fluentsCount;
        this.actions = actions;
        this.hoentsSettings = hoentsSettings;

        structures = new ArrayList<Hoent>();
        oMinimalStructures= new ArrayList<Hoent>();
        modelsOfTypeOne = new ArrayList<Hoent>();
        modelsOfTypeTwo = new ArrayList<Hoent>();

        Hoent firstHoent = new Hoent(tMax, fluentsCount);
        structures.add(firstHoent);
    }

    public ArrayList<Boolean> getQueriesAnswers()
            throws Exception {
        calculateStructures1();
        calculateOMinimalStructures1();
        calculateModelsOfTypeOne1();
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

    private void calculateStructures1() throws Exception {
        boolean isCurrentlyProcessingTypicalSentences = false; //Yes, I know it's a long name.
        ArrayList<Sentence> certainSentences = getSentences(isCurrentlyProcessingTypicalSentences);
        isCurrentlyProcessingTypicalSentences = true;
        ArrayList<Sentence> typicallySentences = getSentences(isCurrentlyProcessingTypicalSentences);

        //1. proceed atSentence and occursAt sentences=================================================================
        for (Sentence sentence : certainSentences) {
            if ( (sentence instanceof AtSentence)
                    || (sentence instanceof OccursAtSentence) ){
                structures = sentence.applyCertainSentence(structures, fluentsCount, (byte)-1, false, hoentsSettings);
            }
        }

        //3. proceed occursAt "typically" sentences=================================================================
        for (Sentence sentence : typicallySentences) {
            if ( (sentence instanceof AtSentence)
                    || (sentence instanceof OccursAtSentence) ){
                structures = sentence.applyTypicalSentence(structures, fluentsCount, (byte) -1, false, hoentsSettings);
            }
        }

        //4. proceed certain sentences other than atSentence and occursAtSentence=======================================
        //Boolean hasChanged = true;
        for (byte timeID = 0; timeID < tMax; timeID++) {

            structures = calculateStructures1Subcall(structures, timeID);

            //proceed triggers certain sentences
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false
                        && (sentence instanceof TriggersSentence) == true) {
                    structures = sentence.applyCertainSentence(structures, fluentsCount, timeID, false, hoentsSettings);
                }
            }

            //Boolean hasChanged = true;
            //proceed triggers typical sentences
            for (Sentence sentence : typicallySentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false
                        && (sentence instanceof TriggersSentence) == true) {
                    structures = sentence.applyTypicalSentence(structures, fluentsCount, timeID, false, hoentsSettings);
                }
            }

            //Boolean hasChanged = true;
            //proceed other certain sentences (not at, not occurs at and not triggers)
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false
                        && (sentence instanceof TriggersSentence) == false) {
                    structures = sentence.applyCertainSentence(structures, fluentsCount, timeID, false, hoentsSettings);
                }
            }

            //Boolean hasChanged = true;
            //proceed other typical sentences (not at, not occurs at and not triggers)
            for (Sentence sentence : typicallySentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false
                        && (sentence instanceof TriggersSentence) == false) {
                    structures = sentence.applyTypicalSentence(structures, fluentsCount, timeID, false, hoentsSettings);
                }
            }

            //check if invoked actions (by "invoke" sentence) that couldn't be inserted are really erroneous
            //(if isActionTypicalAtTime is false at the time of action invoke
            ArrayList<Hoent> newStructures = (ArrayList<Hoent>) structures.clone();
            for (Hoent structure : structures) {
                byte erroneousTime = structure.getErrorInsertingActionAtTime();
                if (erroneousTime == timeID) {
                    if (erroneousTime != -1) {
                        if (hoentsSettings.isDoThrow() == true &&
                                structure.isActionTypicalAtTime(erroneousTime) == false) {
                            String message = "Error in structure - error inserting action at time [" + Integer.toString(erroneousTime) + "]."; //20150906
                            log.debug(message);
                            throw new Exception(message);
                        }
                        newStructures.remove(structure);
                    }
                }
            }
            structures = newStructures;
        }

    }

    /**
     * FAPR96.pdf site 8 ABOVE Definition 2
     * H=H', N=N' forall (timeid, actionid), fluents are in (strictly in) fluents'
     */
    private void calculateOMinimalStructures1() {
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

   private ArrayList<Hoent> calculateStructures1Subcall(ArrayList<Hoent> structuresArg, byte timeID) throws Exception {
       ArrayList<Hoent> newStructures = new ArrayList<Hoent>();

       if (timeID == 0) {
           //time == 0
           for (Hoent structure : structuresArg) {
               String hAtTimeZero = structure.sysElemH.get(0);
               if (Fluents.countQuestionMarks(hAtTimeZero) == 0) {
                   newStructures.add(structure);
               } else {
                   ArrayList<String> hElems = Fluents.expandQuestionMarks(hAtTimeZero);
                   for (String hElem : hElems) {
                       Hoent copyOfOMinStr = structure.copy();
                       copyOfOMinStr.sysElemH.remove(0);
                       copyOfOMinStr.sysElemH.add(0, hElem);
                       newStructures.add(copyOfOMinStr);
                   }
               }
           }
       } else {
           //time > 0
           int deletionsBecauseOfOcclusionCounter = 0;
           //for (byte timeIndex = 1; timeIndex < tMax; timeIndex++) {
           byte timeIndex = timeID;
               ArrayList<Hoent> newModelsOfTypeOne = new ArrayList<Hoent>();
               for (Hoent modelOfTypeOne : structuresArg) {
                   ArrayList<HashMap<Byte, String>> sysElemO = modelOfTypeOne.sysElemO;
                   HashMap<Byte, String> sysElemOAtTime = sysElemO.get(timeIndex - 1); // "- 1" important
                   //ArrayList<String> posEvalsFromAtSentences = getPosEvalsFromAtSentences(sentences, timeIndex);
                   if (sysElemOAtTime.keySet().size() == 0) {
                       //20150906
                       Hoent newModelOfTypeOne = modelOfTypeOne.copy();
                       newModelOfTypeOne.hPreserveFluentsAtTime(timeIndex);
                       //if (newModelOfTypeOne.hCheckCompatibility(modelOfTypeOne.sysElemH.get(timeIndex), timeIndex) == false) {
                       //continue;
                       //}
                       if (!Fluents.checkCompatibilityUsingMask(modelOfTypeOne.sysElemH.get(timeIndex - 1),
                               modelOfTypeOne.sysElemH.get(timeIndex), null, null)) {
                           deletionsBecauseOfOcclusionCounter++;
                           continue;
                       }
                       newModelsOfTypeOne.add(newModelOfTypeOne);
                   } else {
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
                       for (String newH : newHsFromOcclusion) {
                           Hoent newModelOfTypeOne = modelOfTypeOne.copy();
                           newModelOfTypeOne.sysElemH.remove(timeIndex);
                           newModelOfTypeOne.sysElemH.add(timeIndex, newH);
                           //if (newModelOfTypeOne.hCheckCompatibility(modelOfTypeOne.sysElemH.get(timeIndex), timeIndex) == false) {
                           //   continue;
                           //}
                           if (!Fluents.checkCompatibilityUsingMask(modelOfTypeOne.sysElemH.get(timeIndex - 1),
                                   modelOfTypeOne.sysElemH.get(timeIndex), fluentsInO, null)) {
                               deletionsBecauseOfOcclusionCounter++;
                               continue;
                           }

                           newModelsOfTypeOne.add(newModelOfTypeOne);
                       }
                   }
               }
               structuresArg = newModelsOfTypeOne;
               newStructures = structuresArg;
           //}
           log.debug("deletionsBecauseOfOcclusionCounter: " + String.valueOf(deletionsBecauseOfOcclusionCounter));
       }
       return newStructures;
   }

   /**
    * Fluents that changed are in the set O(A,t)
    * FAPR96.pdf site 8 Definition 2
    */
   private void calculateModelsOfTypeOne1() throws Exception {
       modelsOfTypeOne = (ArrayList<Hoent>)oMinimalStructures.clone();

       //20150906 //20150911 commented
        //after filling '?' with values (using occlusion) check if new HOENT's are compatible with sentences
        //2. proceed certain sentences other than atSentence and occursAtSentence=======================================
        //Boolean hasChanged = true;
        ArrayList<Sentence> certainSentences = getSentences(false);
        for (byte timeIDindex = 0; timeIDindex < tMax; timeIDindex++) {
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof AtSentence) == false
                        && (sentence instanceof OccursAtSentence) == false) {
                    modelsOfTypeOne = sentence.applyCertainSentence(modelsOfTypeOne, fluentsCount, timeIDindex, true, hoentsSettings);
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

        for (byte timeID = 0; timeID < tMax - 1; timeID++) {
            for (Sentence sentence : certainSentences) {
                if ((sentence instanceof ReleasesSentence) == true) {
                    ((ReleasesSentence)sentence).isAtLeastOnePosAndNegEval(
                                    modelsOfTypeOne, fluentsCount, timeID, true, hoentsSettings);
                }
            }
        }

        if (hoentsSettings.isDoThrowIfExceededTimeLimit()) {
            for (Hoent modelofTypeOne : modelsOfTypeOne) {
                if (modelofTypeOne.hasExceededTimeLimit == true && modelofTypeOne.firstTypicalActionIndex == -1) {
                    throw new Exception("modelofTypeOne.hasExceededTimeLimit == true");
                }
            }
        }

//        if (hoentsSettings.isDoThrow()) {
//            for (Hoent modelofTypeOne : modelsOfTypeOne) {
//                if (modelofTypeOne.hasContradiction == true) {
//                    throw new Exception("modelofTypeOne.hasContradiction == true");
//                }
//            }
//        }


    }

    /**
     * Maximization of typicalities.
     * FAPR96.pdf site 9 Definition 3
     */
    private void calculateModelsOfTypeTwo() throws Exception {
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

        //preserve hoents with min number of As===============================================
        int deletionsOfGMDPreferredModelsCounter_2a = 0;
        newModelsOfTypeTwo = (ArrayList<Hoent>)modelsOfTypeTwo.clone();//new ArrayList<Hoent>();
        int minAs = Integer.MAX_VALUE;
        for (Hoent modelOfTypeTwo : modelsOfTypeTwo) {
            int as = modelOfTypeTwo.aCountAs();
            if (as <= minAs) {
                minAs = as;
            }
        }
        for (Hoent modelOfTypeTwo : modelsOfTypeTwo) {
            if (modelOfTypeTwo.aCountAs() > minAs) {
                newModelsOfTypeTwo.remove(modelOfTypeTwo);
                deletionsOfGMDPreferredModelsCounter_2a++;
            }
        }

        modelsOfTypeTwo = newModelsOfTypeTwo;
        log.debug("preserve hoents with min number of As deletions: " + String.valueOf(deletionsOfGMDPreferredModelsCounter_2a));

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
                        firstHoent.nAreSysElemNsTheSame(secondHoent.sysElemN) &&
                        firstHoent.aAreSysElemAsTheSame(secondHoent.sysElemA)) {
                    newModelsOfTypeTwo.remove(secondHoent);
                    deletionsOfGMDPreferredModelsCounter_3++;
                }
            }
        }

        modelsOfTypeTwo = newModelsOfTypeTwo;
        log.debug("remove duplicate HOENTs deletions: " + String.valueOf(deletionsOfGMDPreferredModelsCounter_3));

        if (hoentsSettings.isDoThrowIfExceededTimeLimit()) {
            for (Hoent modelofTypeTwo : modelsOfTypeTwo) {
                if (modelofTypeTwo.hasExceededTimeLimit == true) {
                    throw new Exception("modelofTypeTwo.hasExceededTimeLimit == true");
                }
            }
        }


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
