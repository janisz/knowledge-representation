package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

public class Main {

    public static void main(String[] args) {

        Engine e = new Engine(new Knowledge(), new Scenario(), 100);
        //e.Run();
        //e.StepRun();

        EngineManager em = new EngineManager(new Knowledge(), new Scenario(), 8);
        em.Run();

        System.out.println("XXX");
    }
}
