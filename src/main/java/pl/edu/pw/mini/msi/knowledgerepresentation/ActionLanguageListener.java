package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;

public class ActionLanguageListener extends pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageBaseListener {

    Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);

    @Override
    public void enterProgramm(ActionLanguageParser.ProgrammContext ctx) {
        super.enterProgramm(ctx);
        log.debug("Enter program");
    }


    @Override
    public void enterInstruction(ActionLanguageParser.InstructionContext ctx) {
        super.enterInstruction(ctx);
        log.debug("Enter program");
    }


    @Override
    public void enterEntry(ActionLanguageParser.EntryContext ctx) {
        super.enterEntry(ctx);
    }


    @Override
    public void enterInitiallisation(ActionLanguageParser.InitiallisationContext ctx) {
        super.enterInitiallisation(ctx);
    }


    @Override
    public void enterCauses(ActionLanguageParser.CausesContext ctx) {
        super.enterCauses(ctx);
    }


    @Override
    public void enterInvokes(ActionLanguageParser.InvokesContext ctx) {
        super.enterInvokes(ctx);
    }


    @Override
    public void enterReleases(ActionLanguageParser.ReleasesContext ctx) {
        super.enterReleases(ctx);
    }


    @Override
    public void enterTriggers(ActionLanguageParser.TriggersContext ctx) {
        super.enterTriggers(ctx);
    }


    @Override
    public void enterOccurs(ActionLanguageParser.OccursContext ctx) {
        super.enterOccurs(ctx);
    }


    @Override
    public void enterImpossible(ActionLanguageParser.ImpossibleContext ctx) {
        super.enterImpossible(ctx);
    }


    @Override
    public void enterAlways(ActionLanguageParser.AlwaysContext ctx) {
        super.enterAlways(ctx);
    }


    @Override
    public void enterUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {
        super.enterUnderCondition(ctx);
    }


    @Override
    public void enterAfterTime(ActionLanguageParser.AfterTimeContext ctx) {
        super.enterAfterTime(ctx);
    }


    @Override
    public void enterAction(ActionLanguageParser.ActionContext ctx) {
        super.enterAction(ctx);
    }


    @Override
    public void enterFluentsList(ActionLanguageParser.FluentsListContext ctx) {
        super.enterFluentsList(ctx);
    }


    @Override
    public void enterFluents(ActionLanguageParser.FluentsContext ctx) {
        super.enterFluents(ctx);
    }


    @Override
    public void enterScenariosList(ActionLanguageParser.ScenariosListContext ctx) {
        super.enterScenariosList(ctx);
    }


    @Override
    public void enterScenario(ActionLanguageParser.ScenarioContext ctx) {
        super.enterScenario(ctx);
    }


    @Override
    public void enterActions(ActionLanguageParser.ActionsContext ctx) {
        super.enterActions(ctx);
    }


    @Override
    public void enterEventsList(ActionLanguageParser.EventsListContext ctx) {
        super.enterEventsList(ctx);
    }


    @Override
    public void enterEvent(ActionLanguageParser.EventContext ctx) {
        super.enterEvent(ctx);
    }


    @Override
    public void enterObservations(ActionLanguageParser.ObservationsContext ctx) {
        super.enterObservations(ctx);
    }


    @Override
    public void enterObservationsList(ActionLanguageParser.ObservationsListContext ctx) {
        super.enterObservationsList(ctx);
    }


    @Override
    public void enterObservation(ActionLanguageParser.ObservationContext ctx) {
        super.enterObservation(ctx);
    }


    @Override
    public void enterQuery(ActionLanguageParser.QueryContext ctx) {
        super.enterQuery(ctx);
    }


    @Override
    public void enterQuestion(ActionLanguageParser.QuestionContext ctx) {
        super.enterQuestion(ctx);
    }


    @Override
    public void enterBasicQuestion(ActionLanguageParser.BasicQuestionContext ctx) {
        super.enterBasicQuestion(ctx);
    }


    @Override
    public void enterActorsList(ActionLanguageParser.ActorsListContext ctx) {
        super.enterActorsList(ctx);
    }


    @Override
    public void enterActors(ActionLanguageParser.ActorsContext ctx) {
        super.enterActors(ctx);
    }


    @Override
    public void enterFluent(ActionLanguageParser.FluentContext ctx) {
        super.enterFluent(ctx);
    }


    @Override
    public void enterActor(ActionLanguageParser.ActorContext ctx) {
        super.enterActor(ctx);
    }


    @Override
    public void enterTask(ActionLanguageParser.TaskContext ctx) {
        super.enterTask(ctx);
    }


    @Override
    public void enterTime(ActionLanguageParser.TimeContext ctx) {
        super.enterTime(ctx);
    }


    @Override
    public void enterScenarioId(ActionLanguageParser.ScenarioIdContext ctx) {
        super.enterScenarioId(ctx);
    }


    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        log.debug(String.format("Entered %s..%s (%s)", ctx.getStart(), ctx.getStop(), ctx.getText()));
        super.enterEveryRule(ctx);
    }


    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
