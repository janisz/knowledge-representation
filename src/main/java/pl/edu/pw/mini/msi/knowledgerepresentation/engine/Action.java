package pl.edu.pw.mini.msi.knowledgerepresentation.engine;


public class Action {
    String _Actor;
    String _Task;

    public Action(String Actor, String Task) {
        _Actor = Actor;
        _Task = Task;
    }

    public boolean Compare(Action X) {
        return X._Task.equals(_Task) && X._Actor.equals(_Actor);
    }

    public Action Clone() {
        return new Action(_Actor, _Task);
    }

    public String toString() {
        return "(" + _Actor + " " + _Task + ")";
    }
}
