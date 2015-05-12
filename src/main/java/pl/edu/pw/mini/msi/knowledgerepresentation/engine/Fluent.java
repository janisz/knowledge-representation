package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

public class Fluent {
    String _Name;
    boolean _Value;

    public Fluent(String Name, boolean Value) {
        _Name = Name;
        _Value = Value;
    }


    public void SetVal(boolean Value) {
        _Value = Value;
    }

    public boolean GetVal() {
        return _Value;
    }

    public boolean SameName(Fluent F) {
        return _Name.equals(F._Name);
    }

    public String toString() {
        return "[" + _Name + " " + _Value + "]";
    }

    public Fluent clone() {
        return new Fluent(_Name, _Value);
    }
}
