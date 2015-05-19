
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;
import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;
/**
 * Created by rwyka on 5/11/15.
 */
public class StepResult {
    public boolean fork = false;
    public boolean end = false;
    public Engine engineL;
    public Engine engineR;


    public StepResult(Engine e){
        engineL = engineR = e;
    }

    public StepResult(Engine nL, Engine nR){
        fork = true;
        engineL = nL;
        engineR = nR;
    }

    public StepResult(Engine e, boolean endflag){
        engineL = engineR = e;
        end = endflag;
    }

    public static StepResult Normal(Engine e){
        return  new StepResult(e);
    }

    public static StepResult Fork(Engine nL, Engine nR){
        return new StepResult(nL, nR);
    }

    public static StepResult End(Engine e){
        return new StepResult(e, true);
    }
}
