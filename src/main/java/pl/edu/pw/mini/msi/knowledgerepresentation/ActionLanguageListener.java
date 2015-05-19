package pl.edu.pw.mini.msi.knowledgerepresentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.FlexibleHashMap;
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
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.EngineManager;
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageBaseListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class ActionLanguageListener extends ActionLanguageBaseListener {

    public enum QueryType {
        GENERALLY, ALWAYS, EVER, NONE
    }
    
    private static final Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);
    
    private final Knowledge knowledge;
    private final HashMap<String, Scenario> scenarios = Maps.newLinkedHashMap();
    private EngineManager engineManager;
    
    private Collection<Event> events = Lists.newArrayList();
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
    
    public ActionLanguageListener(Knowledge knowledge) {
        this.knowledge = knowledge;
    }
    
    public Knowledge getKnowledge() {
        return knowledge;
    }

    public HashMap<String, Scenario> getScenarios() {
        return scenarios;
    }

    @Override
    public void exitInitiallisation(ActionLanguageParser.InitiallisationContext ctx) {
        log.debug("Set initial state: %s", lastFluentsList);
        for (Fluent fluent : lastFluentsList) {
            //knowledge._Initially.add(fluent);
        }
    }
    
    @Override
    public void exitCauses(ActionLanguageParser.CausesContext ctx) {
        log.debug(String.format("(Typically=%s) %s CAUSES %s after %s IF %s", typically, lastAction, lastFluentsList, lastTime, underConditionFluentList));
        knowledge.addEffectCause(typically, lastAction, new ArrayList<Fluent>(lastFluentsList), new ArrayList<Fluent>(underConditionFluentList));
    }
    
    @Override
    public void exitInvokes(ActionLanguageParser.InvokesContext ctx) { 
    	log.debug(String.format("(Typically=%s) %s INVOKES %s after %s IF %s", typically, previousLastAction, lastAction, lastTime, underConditionFluentList));
    	knowledge.addEffectInvokes(typically, previousLastAction, lastAction, lastTime.getTime(), new ArrayList<Fluent>(underConditionFluentList));
    }
    
    @Override
    public void exitReleases(ActionLanguageParser.ReleasesContext ctx) { 
    	log.debug(String.format("(Typically=%s) %s RELEASES %s AFTER %s IF %s", typically, lastAction, lastFluentsList, lastTime, underConditionFluentList));
    	knowledge.releases(typically, lastAction, lastFluentsList, lastTime.getTime(), underConditionFluentList);
    }

    @Override
    public void exitTriggers(ActionLanguageParser.TriggersContext ctx) {
    	log.debug(String.format("(Typically=%s) %s TRIGGERS %s", typically, lastAction, underConditionFluentList));
    	knowledge.addEffectTriggers(typically, lastAction, new ArrayList<Fluent>(underConditionFluentList));
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
    	log.debug(String.format("ALWAYS %s", lastFluent));
    	knowledge.addAlways(new ArrayList<>(lastFluentsList));
    }
    
    @Override
    public void exitState(ActionLanguageParser.StateContext ctx) {
    	//log.debug(String.format("ALWAYS %s ", lastFluent));

    	//engineManager.conditionAt(lastQueryType, lastFluentsList, lastTime, ctx.scenarioId().IDENTIFIER().getText());
    }

    @Override
    public void exitPerformed(ActionLanguageParser.PerformedContext ctx) {
    	//log.debug(String.format("ALWAYS %s PERFORMED", lastFluent));
    	//engineManager.performed(lastQueryType, lastAction, lastTime, ctx.scenarioId().IDENTIFIER().getText());
    }

    @Override
    public void exitInvolved(ActionLanguageParser.InvolvedContext ctx) {
    	//log.debug(String.format("%s INVOLVED %s WHEN %s", lastQueryType, lastActorsList, scenario));
        Scenario scenario = scenarios.get(ctx.scenarioId().IDENTIFIER().getText());
        //engineManager.involved(lastQueryType, lastActorsList, ctx.scenarioId().IDENTIFIER().getText());
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
        
        if(!lastActorsList.contains(actor)){
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
        scenarios.put(name, scenario);
        //engineManager = new EngineManager(knowledge, scenario, 20);
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
