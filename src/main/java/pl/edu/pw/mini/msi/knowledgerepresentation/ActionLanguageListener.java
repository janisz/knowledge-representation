package pl.edu.pw.mini.msi.knowledgerepresentation;

import alice.tuprolog.Engine;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Action;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Actor;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Event;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Fluent;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Scenario;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Task;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.Time;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageBaseListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionLanguageListener extends ActionLanguageBaseListener {

    private enum QueryType {
        GENERALLY, ALWAYS, EVER, NONE
    }

    private static final Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);

    private final Context context;
    private final Prolog engine;

    private Collection<Event> events = Lists.newArrayList();
    private Multimap<Time, Fluent> observations = LinkedHashMultimap.create();
    private Collection<Fluent> lastFluentsList = Sets.newHashSet();
    private Collection<Fluent> underConditionFluentList = Sets.newHashSet();
    private Collection<Actor> lastActorsList = Sets.newHashSet();

    private Action lastAction;
    private Actor lastActor;
    private Time lastTime;
    private Task lastTask;
    private Fluent lastFluent;
    private String lastScenarioId;
    private QueryType lastQueryType;
    private boolean typically;

    public ActionLanguageListener(Context context, Prolog engine) {
        this.context = context;
        this.engine = engine;
    }

    @Override
    public void exitInitiallisation(ActionLanguageParser.InitiallisationContext ctx) {
        log.debug("Set initial state: %s", lastFluentsList);
        //TODO: Implement me
    }

    @Override
    public void exitCauses(ActionLanguageParser.CausesContext ctx) {
    /*TODO: Implement me*/
        log.debug(String.format("(Typically=%s) %s CAUSES %s after %s IF %s",
                typically, lastAction, lastFluentsList, lastTime, underConditionFluentList));
    }

    @Override
    public void exitInvokes(ActionLanguageParser.InvokesContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitReleases(ActionLanguageParser.ReleasesContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitTriggers(ActionLanguageParser.TriggersContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitOccurs(ActionLanguageParser.OccursContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitImpossible(ActionLanguageParser.ImpossibleContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitAlways(ActionLanguageParser.AlwaysContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitState(ActionLanguageParser.StateContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitPerformed(ActionLanguageParser.PerformedContext ctx) { /*TODO: Implement me*/ }

    @Override
    public void exitInvolved(ActionLanguageParser.InvolvedContext ctx) {
    /*TODO: Implement me*/
        Scenario scenario = context.scenarios.get(ctx.scenarioId().IDENTIFIER().getText());
        log.debug(String.format("%s INVOLVED %s WHEN %s",
                lastQueryType, lastActorsList, scenario));
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
        underConditionFluentList = ImmutableList.copyOf(lastFluentsList);
    }

    @Override
    public void exitUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {
        Collection<Fluent> fluents = ImmutableList.copyOf(underConditionFluentList);
        underConditionFluentList = ImmutableList.copyOf(lastFluentsList);
        lastFluentsList = fluents;
    }

    @Override
    public void enterActor(ActionLanguageParser.ActorContext ctx) {
        Actor actor = new Actor(ctx.IDENTIFIER().getText());
        lastActor = actor;
        lastActorsList.add(actor);
        context.actors.add(actor);
        addTheory(actor.theory());
    }

    private void addTheory(Theory theory) {
        try {
            engine.addTheory(theory);
        } catch (InvalidTheoryException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void enterTask(ActionLanguageParser.TaskContext ctx) {
        lastTask = new Task(ctx.IDENTIFIER().getText());
    }

    @Override
    public void exitAction(ActionLanguageParser.ActionContext ctx) {
        lastAction = new Action(lastActor, lastTask);
    }

    @Override
    public void enterFluent(ActionLanguageParser.FluentContext ctx) {
        lastFluent = new Fluent(ctx.IDENTIFIER().getText(), ctx.NOT() == null);
        lastFluentsList.add(lastFluent);
        context.fluents.add(new Fluent(ctx.IDENTIFIER().getText(), true));
        addTheory(lastFluent.theory());
    }

    @Override
    public void enterInstruction(ActionLanguageParser.InstructionContext ctx) {
        log.debug("Enter " + ctx.getText());
        log.debug("Clean previous instruction context");
        events.clear();
        observations.clear();
        lastFluentsList.clear();
        lastActorsList.clear();
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
        lastFluentsList.clear();
    }

    @Override
    public void exitScenario(ActionLanguageParser.ScenarioContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        Map<Time, Action> actions = events.stream().collect(Collectors.toMap(Event::getTime, Event::getAction));
        Scenario scenario = new Scenario(name, ImmutableMultimap.copyOf(observations), actions);
        log.debug("Create scenario: ", scenario);
        context.scenarios.put(name, scenario);
        addTheory(scenario.theory());
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
}
