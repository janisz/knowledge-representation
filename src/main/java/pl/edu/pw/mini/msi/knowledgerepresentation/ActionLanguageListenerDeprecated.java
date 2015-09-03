package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.collect.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.ActionDomain;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageBaseListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser.ScenarioIdContext;
import pl.edu.pw.mini.msi.knowledgerepresentation.hoents.Hoents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.copyOf;

public class ActionLanguageListenerDeprecated{/*} extends ActionLanguageBaseListener {

    private static final Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);

    //===========================================================================================
    private ActionDomain actionDomain = new ActionDomain();

    //===========================================================================================
    //private final Knowledge knowledge;
    private final Map<String, Scenario> scenarios = Maps.newLinkedHashMap();
    private final List<Boolean> results = Lists.newArrayList();
    private EngineManager engineManager;
    private List<Event> events = Lists.newArrayList();
    private Multimap<Time, Fluent> observations = LinkedHashMultimap.create();
    private Collection<Fluent> lastFluentsList = Sets.newHashSet();
    private Collection<Fluent> underConditionFluentList = Sets.newHashSet();
    private Collection<Actor> lastActorsList = Sets.newHashSet();
    private Action previousLastAction;
    private Action lastAction;
    private Actor lastActor;
    private Time lastTime;
    private Task lastTask;
    private Fluent lastFluent;
    private String lastScenarioId;
    private QueryType lastQueryType;
    private boolean typically;
    private Environment environment;

    //===========================================================================================
    public ActionLanguageListenerDeprecated(ActionDomain actionDomain) {
        this.actionDomain = actionDomain;
        //environment = new Environment(knowledge);
    }
    //===========================================================================================

    //public Knowledge getKnowledge() {
    //    return knowledge;
    //}

    public Map<String, Scenario> getScenarios() {
        return scenarios;
    }

    //===========================================================================================
    public ActionDomain getActionDomain() { //List<Boolean>
        //actionDomain.baseSentences.add(...);
        //actionDomain.scenarios.add(...);
        //actionDomain.addQueries(...);
        actionDomain.calculateFullScenarios();
        actionDomain.calculateMappedQueries();
        actionDomain.fillFluentsAndActionsIDs();

        return actionDomain;
    }
    //===========================================================================================

    @Override
    public void exitInitiallisation(ActionLanguageParser.InitiallisationContext ctx) {
        log.debug("Set initial state: %s", lastFluentsList);
        knowledge._Initially.addAll(lastFluentsList);
    }

    @Override
    public void exitCauses(ActionLanguageParser.CausesContext ctx) {
        log.debug(String.format("(Typically=%s) %s CAUSES %s after %s IF %s", typically, lastAction, lastFluentsList, lastTime, underConditionFluentList));
        knowledge.addEffectCause(typically, lastAction, copyOf(lastFluentsList), copyOf(underConditionFluentList));
    }

    @Override
    public void exitInvokes(ActionLanguageParser.InvokesContext ctx) {
        log.debug(String.format("(Typically=%s) %s INVOKES %s after %s IF %s", typically, previousLastAction, lastAction, lastTime, underConditionFluentList));
        knowledge.addEffectInvokes(typically, previousLastAction, lastAction, lastTime.getTime(), copyOf(underConditionFluentList));
    }

    @Override
    public void exitReleases(ActionLanguageParser.ReleasesContext ctx) {
        log.debug(String.format("(Typically=%s) %s RELEASES %s AFTER %s IF %s", typically, lastAction, lastFluentsList, lastTime, underConditionFluentList));
        knowledge.releases(typically, lastAction, lastFluentsList, lastTime.getTime(), underConditionFluentList);
    }

    @Override
    public void exitTriggers(ActionLanguageParser.TriggersContext ctx) {
        log.debug(String.format("(Typically=%s) %s TRIGGERS %s", typically, lastAction, underConditionFluentList));
        knowledge.addEffectTriggers(typically, lastAction, copyOf(underConditionFluentList));
    }

    @Override
    public void exitOccurs(ActionLanguageParser.OccursContext ctx) {

    }

    @Override
    public void exitImpossible(ActionLanguageParser.ImpossibleContext ctx) {
        log.debug(String.format("IMPOSSIBLE %s AT %s IF %s", lastAction, lastTime, underConditionFluentList));
        knowledge.impossible(lastAction, lastTime.getTime(), underConditionFluentList);
    }

    @Override
    public void exitAlways(ActionLanguageParser.AlwaysContext ctx) {
        log.debug(String.format("ALWAYS %s", lastFluentsList));
        knowledge.addAlways(copyOf(lastFluentsList));
    }

    @Override
    public void exitState(ActionLanguageParser.StateContext ctx) {
        log.debug(String.format("ALWAYS %s ", lastFluent));
        //always/ever/typically [Fluent] at 1 when scenarioOne
        //lastQueryType, lastFluentsList, lastTime, lastScenarioId

        //engineManager.conditionAt(lastQueryType, lastFluentsList, lastTime, ctx.scenarioId().IDENTIFIER().getText());
        Scenario scenario = scenarios.get(ctx.scenarioId().IDENTIFIER().getText());
        List<Fluent> fluentList = new ArrayList<Fluent>();
        fluentList.addAll(lastFluentsList);

        Boolean result = null;
        switch (lastQueryType) {
            case ALWAYS:
                result = environment.QueryConditionAllways(fluentList, lastTime.getTime(), scenario);
                log.info("Answer: [" + result + "]");
                break;
            case EVER:
                result = environment.QueryConditionEver(fluentList, lastTime.getTime(), scenario);
                log.info("Answer: [" + result + "]");
                break;
            case GENERALLY:
                environment.QueryConditionTypically(fluentList, lastTime.getTime(), scenario);
                break;
            case NONE:

                break;
        }
        results.add(result);

    }

    @Override
    public void exitPerformed(ActionLanguageParser.PerformedContext ctx) {
        log.debug(String.format("ALWAYS %s PERFORMED", lastFluent));
        //always/ever/typically performed (Janek,action) at 1 when scenarioOne
        //lastQueryType,lastActorsList,lastTask,lastAction,lastTime,lastScenarioId

        //engineManager.performed(lastQueryType, lastAction, lastTime, ctx.scenarioId().IDENTIFIER().getText());
        Scenario scenario = scenarios.get(ctx.scenarioId().IDENTIFIER().getText());
        Boolean result = null;
        switch (lastQueryType) {
            case ALWAYS:
                result = environment.QueryActionAllwaysr(lastAction, lastTime.getTime(), scenario);
                log.info("Answer: [" + result + "]");
                break;
            case EVER:
                result = environment.QueryActionEver(lastAction, lastTime.getTime(), scenario);
                log.info("Answer: [" + result + "]");
                break;
            case GENERALLY:
                environment.QueryActionTypically(lastAction, lastTime.getTime(), scenario);
                break;
            case NONE:

                break;
        }
        results.add(result);

    }

    @Override
    public void exitInvolved(ActionLanguageParser.InvolvedContext ctx) {
        log.debug(String.format("%s INVOLVED %s WHEN %s", lastQueryType, lastActorsList, lastScenarioId));
        Scenario scenario = scenarios.get(ctx.scenarioId().IDENTIFIER().getText());

        Boolean result = null;
        switch (lastQueryType) {
            case ALWAYS:
                environment.QueryInvolvedAllways(lastFluent, 0, scenario);
                break;
            case EVER:
                environment.QueryInvolvedEver(lastFluent, 0, scenario);
                break;
        }
        results.add(result);
    }

    @Override
    public void enterQuery(ActionLanguageParser.QueryContext ctx) {
        lastQueryType = QueryType.NONE;
    }

    @Override
    public void exitQuestion(ActionLanguageParser.QuestionContext ctx) {
        if (ctx.TYPICALLY() != null) {
            lastQueryType = QueryType.GENERALLY;
        }
    }

    @Override
    public void exitBasicQuestion(ActionLanguageParser.BasicQuestionContext ctx) {
        if (ctx.ALWAYS() != null) {
            lastQueryType = QueryType.ALWAYS;
        } else if (ctx.EVER() != null) {
            lastQueryType = QueryType.EVER;
        }
    }

    @Override
    public void enterUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {
        underConditionFluentList = copyOf(lastFluentsList);
    }

    @Override
    public void exitUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {
        Collection<Fluent> fluents = copyOf(underConditionFluentList);
        underConditionFluentList = copyOf(lastFluentsList);
        lastFluentsList = fluents;
    }

    @Override
    public void enterActor(ActionLanguageParser.ActorContext ctx) {
        Actor actor = new Actor(ctx.IDENTIFIER().getText());
        lastActor = actor;

        if (!lastActorsList.contains(actor)) {
            lastActorsList.add(actor);
        }

    }

    @Override
    public void enterTask(ActionLanguageParser.TaskContext ctx) {
        lastTask = new Task(ctx.IDENTIFIER().getText());
    }

    @Override
    public void exitAction(ActionLanguageParser.ActionContext ctx) {
        previousLastAction = lastAction;
        lastAction = new Action(lastActor, lastTask);
    }

    @Override
    public void enterFluent(ActionLanguageParser.FluentContext ctx) {
        lastFluent = new Fluent(ctx.IDENTIFIER().getText(), ctx.NOT() == null);
        lastFluentsList.add(lastFluent);
    }

    @Override
    public void enterInstruction(ActionLanguageParser.InstructionContext ctx) {
        log.debug("Enter " + ctx.getText());
        log.debug("Clean previous instruction context");
        events.clear();
        observations.clear();
        lastFluentsList = Sets.newHashSet();
        lastActorsList.clear();
        lastTime = new Time(0);
    }

    @Override
    public void enterEntry(ActionLanguageParser.EntryContext ctx) {
        typically = ctx.TYPICALLY() != null;
    }

    @Override
    public void enterTime(ActionLanguageParser.TimeContext ctx) {
        lastTime = new Time(Integer.parseInt(ctx.DecimalConstant().getText()));
    }

    @Override
    public void enterFluentsList(ActionLanguageParser.FluentsListContext ctx) {
        log.debug("Clean previously parsed fluent list data");
        lastFluentsList = Sets.newHashSet();
    }

    @Override
    public void exitScenario(ActionLanguageParser.ScenarioContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        List<ScenarioACSPart> acs = events.stream().map(
                e -> new ScenarioACSPart(e.getAction(), e.getTime())
        ).collect(Collectors.toList());
        List<ScenarioOBSPart> obs = observations.asMap().entrySet().stream().map(
                e -> new ScenarioOBSPart(copyOf(e.getValue()), e.getKey().getTime())
        ).collect(Collectors.toList());

        Scenario scenario = new Scenario(acs, obs);
        log.debug("Create scenario: ", scenario);
        scenarios.put(name, scenario);
        environment.AddScenario(scenario, 10);
    }

    @Override
    public void exitEvent(ActionLanguageParser.EventContext ctx) {
        events.add(new Event(lastAction, lastTime));
    }

    @Override
    public void exitObservation(ActionLanguageParser.ObservationContext ctx) {
        for (Fluent fluent : lastFluentsList) {
            observations.put(lastTime, fluent);
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        log.error("Visit error node: " + node.getText());
    }

    @Override
    public void exitScenarioId(ScenarioIdContext ctx) {
        super.exitScenarioId(ctx);
        lastScenarioId = ctx.IDENTIFIER().getText();
    }

    public enum QueryType {
        GENERALLY, ALWAYS, EVER, NONE
    }
*/
}
