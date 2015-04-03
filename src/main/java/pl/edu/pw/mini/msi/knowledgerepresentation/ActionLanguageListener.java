package pl.edu.pw.mini.msi.knowledgerepresentation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
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

    private static final Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);

    private final Context context;
    private final Collection<Event> events = Lists.newArrayList();
    private final Multimap<Time, Fluent> observations = LinkedHashMultimap.create();

    private Action lastAction;
    private Actor lastActor;
    private Time lastTime;
    private Task lastTask;
    private Fluent lastFluent;

    public ActionLanguageListener(Context context) {
        this.context = context;
    }

    @Override
    public void enterActor(ActionLanguageParser.ActorContext ctx) {
        Actor actor = new Actor(ctx.IDENTIFIER().getText());
        lastActor = actor;
        context.actors.add(actor);
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
        context.fluents.add(new Fluent(ctx.IDENTIFIER().getText(), true));
    }

    @Override
    public void enterTime(ActionLanguageParser.TimeContext ctx) {
        lastTime = new Time(Integer.parseInt(ctx.DecimalConstant().getText()));
    }

    @Override
    public void enterScenario(ActionLanguageParser.ScenarioContext ctx) {
        log.debug("Clean last events and observations");
        events.clear();
        observations.clear();
    }

    @Override
    public void exitScenario(ActionLanguageParser.ScenarioContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        Map<Time, Action> actions = events.stream().collect(Collectors.toMap(Event::getTime, Event::getAction));
        Scenario scenario = new Scenario(name, ImmutableMultimap.copyOf(observations), actions);
        context.scenarios.put(name, scenario);
    }

    @Override
    public void exitEvent(ActionLanguageParser.EventContext ctx) {
        events.add(new Event(lastAction, lastTime));
    }

    @Override
    public void exitObservation(ActionLanguageParser.ObservationContext ctx) {
        observations.put(lastTime, lastFluent);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        log.debug("Enter " + ctx.getText());
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        log.debug("Visit terminal: " + node.getText());
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        log.error("Visit error node: " + node.getText());
    }
}
