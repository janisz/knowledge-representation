package pl.edu.pw.mini.msi.knowledgerepresentation;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageBaseListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser;
import pl.edu.pw.mini.msi.knowledgerepresentation.grammar.ActionLanguageParser.ScenarioIdContext;

public class ActionLanguageListener extends ActionLanguageBaseListener {

    private static final Logger log = LoggerFactory.getLogger(ActionLanguageListener.class);

    public ActionLanguageListener() {
    }

    @Override
    public void exitInitialization(ActionLanguageParser.InitializationContext ctx) {
    }

    @Override
    public void exitCauses(ActionLanguageParser.CausesContext ctx) {
    }

    @Override
    public void exitInvokes(ActionLanguageParser.InvokesContext ctx) {
    }

    @Override
    public void exitReleases(ActionLanguageParser.ReleasesContext ctx) {
    }

    @Override
    public void exitTriggers(ActionLanguageParser.TriggersContext ctx) {
    }

    @Override
    public void exitOccurs(ActionLanguageParser.OccursContext ctx) {

    }

    @Override
    public void exitImpossible(ActionLanguageParser.ImpossibleContext ctx) {
    }

    @Override
    public void exitAlways(ActionLanguageParser.AlwaysContext ctx) {
    }

    @Override
    public void exitState(ActionLanguageParser.StateContext ctx) {
    }

    @Override
    public void exitPerformed(ActionLanguageParser.PerformedContext ctx) {
    }

    @Override
    public void exitInvolved(ActionLanguageParser.InvolvedContext ctx) {
    }

    @Override
    public void enterQuery(ActionLanguageParser.QueryContext ctx) {

    }

    @Override
    public void exitQuestion(ActionLanguageParser.QuestionContext ctx) {
    }

    @Override
    public void exitBasicQuestion(ActionLanguageParser.BasicQuestionContext ctx) {

    }

    @Override
    public void enterUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {

    }

    @Override
    public void exitUnderCondition(ActionLanguageParser.UnderConditionContext ctx) {

    }

    @Override
    public void enterActor(ActionLanguageParser.ActorContext ctx) {

    }

    @Override
    public void enterTask(ActionLanguageParser.TaskContext ctx) {

    }

    @Override
    public void exitAction(ActionLanguageParser.ActionContext ctx) {

    }

    @Override
    public void enterFluent(ActionLanguageParser.FluentContext ctx) {

    }

    @Override
    public void enterInstruction(ActionLanguageParser.InstructionContext ctx) {

    }

    @Override
    public void enterEntry(ActionLanguageParser.EntryContext ctx) {

    }

    @Override
    public void enterTime(ActionLanguageParser.TimeContext ctx) {

    }

    @Override
    public void enterFluentsList(ActionLanguageParser.FluentsListContext ctx) {

    }

    @Override
    public void exitScenario(ActionLanguageParser.ScenarioContext ctx) {

    }

    @Override
    public void exitEvent(ActionLanguageParser.EventContext ctx) {

    }

    @Override
    public void exitObservation(ActionLanguageParser.ObservationContext ctx) {

    }

    @Override
    public void visitErrorNode(ErrorNode node) {

    }

    @Override
    public void exitScenarioId(ScenarioIdContext ctx) {

    }
}
