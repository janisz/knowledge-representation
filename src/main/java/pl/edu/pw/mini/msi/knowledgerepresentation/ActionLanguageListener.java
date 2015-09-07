package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts.*;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentences.*;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageBaseListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;

public class ActionLanguageListener extends ActionLanguageBaseListener {

    private static final Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);

    private Action lastAction = null;
    private Action preLastAction = null;
    private Actor lastActor = null;

    private EQueryType lastQueryType = EQueryType.always;
    private Fluent lastFluent = null;
    private IFormula lastFormula = null;
    private ScenarioName lastScenarioName = null;
    private Time lastTime = null;
    private boolean lastTypically = false;

    private boolean isInLogicalExpression = false;
    private IFormula lastUnderConditionFormula = null;
    private boolean isInUnderCondition = false;

    //===========================================================================================
    private ActionDomain actionDomain = new ActionDomain();

    public ActionLanguageListener() {
        log.debug("ActionLanguageListener()");
    }

    public ActionLanguageListener(ActionDomain actionDomain) {
        this.actionDomain = actionDomain;
        log.debug("ActionLanguageListener(ActionDomain actionDomain)");
    }

    public ActionDomain getActionDomain() {
        actionDomain.calculateFullScenarios();
        actionDomain.calculateMappedQueries();
        actionDomain.fillFluentsAndActionsIDs();

        return actionDomain;
    }
    //ActionLanguageParser.LogicalExpressionContext ctx;
    //===========================================================================================

    /**
     * {@inheritDoc}
     * <p/>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterProgramm(ActionLanguageParser.ProgrammContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitProgramm(ActionLanguageParser.ProgrammContext ctx) {
    }

    @Override
    public void enterInstruction(ActionLanguageParser.InstructionContext ctx) {
        lastAction = null;
        preLastAction = null;
        lastActor = null;

        lastQueryType = null;
        lastFluent = null;
        lastFormula = null;
        lastScenarioName = null;
        lastTime = null;

        lastTypically = false;
        isInLogicalExpression = false;
        lastUnderConditionFormula = null;
        isInUnderCondition = false;
    }

    @Override
    public void exitInstruction(ActionLanguageParser.InstructionContext ctx) {
        ;
    }


    @Override
    public void enterEntry(ActionLanguageParser.EntryContext ctx) {
        lastTypically = !(ctx.TYPICALLY() == null);
    }


    @Override
    public void exitEntry(ActionLanguageParser.EntryContext ctx) {
        ;
    }


    @Override
    public void enterInitialization(ActionLanguageParser.InitializationContext ctx) {
        ;
    }


    @Override
    public void exitInitialization(ActionLanguageParser.InitializationContext ctx) {
        AtSentence atSentence = new AtSentence(lastFormula, new Time("0"), this.actionDomain);
        this.actionDomain.addBaseSentence(atSentence);
    }


    @Override
    public void enterCauses(ActionLanguageParser.CausesContext ctx) {
        ;
    }


    @Override
    public void exitCauses(ActionLanguageParser.CausesContext ctx) {
        CausesSentence causesSentence;
        causesSentence = new CausesSentence(lastAction, lastFormula, lastUnderConditionFormula, actionDomain);
        actionDomain.addBaseSentence(causesSentence);
    }


    @Override
    public void enterInvokes(ActionLanguageParser.InvokesContext ctx) {
        ;
    }


    @Override
    public void exitInvokes(ActionLanguageParser.InvokesContext ctx) {
        //invokes: action INVOKES action afterTime? underCondition?;
        InvokesSentence invokesSentence = new InvokesSentence(
                lastTypically, preLastAction, lastAction, lastTime, lastUnderConditionFormula, actionDomain);
        actionDomain.addBaseSentence(invokesSentence);
    }


    @Override
    public void enterReleases(ActionLanguageParser.ReleasesContext ctx) {
        ;
    }


    @Override
    public void exitReleases(ActionLanguageParser.ReleasesContext ctx) {
        //releases: action RELEASES fluent underCondition?;
        ReleasesSentence releasesSentence = new ReleasesSentence(lastAction, lastFluent, lastUnderConditionFormula,
                actionDomain);
        actionDomain.addBaseSentence(releasesSentence);
    }


    @Override
    public void enterTriggers(ActionLanguageParser.TriggersContext ctx) {
        ;
    }


    @Override
    public void exitTriggers(ActionLanguageParser.TriggersContext ctx) {
        //triggers: logicalExpression TRIGGERS action;
        TriggersSentence triggersSentence = new TriggersSentence(lastTypically, lastFormula, lastAction, actionDomain);
        actionDomain.addBaseSentence(triggersSentence);
    }


    @Override
    public void enterOccurs(ActionLanguageParser.OccursContext ctx) {
        ;
    }


    @Override
    public void exitOccurs(ActionLanguageParser.OccursContext ctx) {
        //occurs: action OCCURS AT time;
        OccursAtSentence occursAtSentence = new OccursAtSentence(lastTypically, lastAction, lastTime, actionDomain);
        actionDomain.addBaseSentence(occursAtSentence);
    }


    @Override
    public void enterAtsentence(ActionLanguageParser.AtsentenceContext ctx) {
        ;
    }


    @Override
    public void exitAtsentence(ActionLanguageParser.AtsentenceContext ctx) {
        //atsentence: logicalExpression AT time;
        AtSentence atSentence = new AtSentence(lastFormula, lastTime, actionDomain);
        actionDomain.addBaseSentence(atSentence);
    }


    @Override
    public void enterUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {
        //lastUnderConditionFormula = new LogExprFormula( ctx.logicalExpression() );
        isInUnderCondition = true;
    }


    @Override
    public void exitUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {

        isInUnderCondition = false;
    }


    @Override
    public void enterAction(ActionLanguageParser.ActionContext ctx) {
        Actor foundActor = new Actor(ctx.actor().IDENTIFIER().getText());
        Task foundTask = new Task(ctx.task().NOT() != null, ctx.task().IDENTIFIER().getText());
        Action foundAction = new Action(foundActor, foundTask);
        if (lastAction == null) {
            lastAction = foundAction;
        } else {
            preLastAction = lastAction;
            lastAction = foundAction;
        }
    }


    @Override
    public void exitAction(ActionLanguageParser.ActionContext ctx) {
        ;
    }


    @Override
    public void enterScenario(ActionLanguageParser.ScenarioContext ctx) {
        lastScenarioName = new ScenarioName(ctx.IDENTIFIER().getText());
        actionDomain.addEmptyScenario(lastScenarioName.scenarioName); //20150906
    }

    @Override
    public void exitEvent(ActionLanguageParser.EventContext ctx) {
        OccursAtSentence occursAtSentence = new OccursAtSentence(false, lastAction, lastTime, actionDomain);
        actionDomain.addScenarioSentence(lastScenarioName.toString(), occursAtSentence);
    }

    @Override
    public void exitObservation(ActionLanguageParser.ObservationContext ctx) {
        AtSentence atSentence = new AtSentence(lastFormula, lastTime, actionDomain);
        actionDomain.addScenarioSentence(lastScenarioName.toString(), atSentence);
    }


    @Override
    public void enterLogicalExpression(ActionLanguageParser.LogicalExpressionContext ctx) {
        isInLogicalExpression = true;
        if (lastFormula == null) {
            lastFormula = new LogExprFormula(ctx);
        }
        if (isInUnderCondition) {
            if (lastUnderConditionFormula == null) {
                lastUnderConditionFormula = new LogExprFormula(ctx);
            }
        }
    }


    @Override
    public void exitLogicalExpression(ActionLanguageParser.LogicalExpressionContext ctx) {
        isInLogicalExpression = false;
    }

    @Override
    public void exitState(ActionLanguageParser.StateContext ctx) {
        //state: question logicalExpression AT time WHEN scenarioId;
        AtQuery atQuery = new AtQuery(lastQueryType, lastFormula, lastTime, lastScenarioName);
        actionDomain.addQuery(atQuery);
    }


    @Override
    public void enterPerformed(ActionLanguageParser.PerformedContext ctx) {
        ;
    }


    @Override
    public void exitPerformed(ActionLanguageParser.PerformedContext ctx) {
        //performed: question PERFORMED action AT time WHEN scenarioId;
        PerformedQuery performedQuery = new PerformedQuery(lastQueryType, lastAction, lastTime, lastScenarioName);
        actionDomain.addQuery(performedQuery);
    }

    @Override
    public void exitInvolved(ActionLanguageParser.InvolvedContext ctx) {
        //involved: basicQuestion INVOLVED actorsList WHEN scenarioId;
        InvolvedQuery involvedQuery = new InvolvedQuery(lastQueryType, lastActor, lastScenarioName);
        actionDomain.addQuery(involvedQuery);
    }


    @Override
    public void enterQuestion(ActionLanguageParser.QuestionContext ctx) {
        if (ctx.TYPICALLY() != null) {
            lastQueryType = EQueryType.typically;
        }
    }

    @Override
    public void enterBasicQuestion(ActionLanguageParser.BasicQuestionContext ctx) {
        if (ctx.ALWAYS() != null) {
            lastQueryType = EQueryType.always;
        }
        if (ctx.EVER() != null) {
            lastQueryType = EQueryType.ever;
        }
    }


    @Override
    public void enterFluent(ActionLanguageParser.FluentContext ctx) {
        if (!isInLogicalExpression) {
            lastFluent = new Fluent(ctx.IDENTIFIER().getText());
        }
    }

    @Override
    public void enterActor(ActionLanguageParser.ActorContext ctx) {
        lastActor = new Actor(ctx.IDENTIFIER().getText());
    }

    @Override
    public void enterTime(ActionLanguageParser.TimeContext ctx) {
        lastTime = new Time(ctx.DecimalConstant().getText());
    }


    @Override
    public void exitTime(ActionLanguageParser.TimeContext ctx) {
        ;
    }


    @Override
    public void enterScenarioId(ActionLanguageParser.ScenarioIdContext ctx) {
        lastScenarioName = new ScenarioName(ctx.IDENTIFIER().getText());
    }
}
