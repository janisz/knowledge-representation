package pl.edu.pw.mini.msi.knowledgerepresentation.actionDomain.sentenceParts;

/**
 * Created by Tomek on 2015-08-30.
 */
public enum EQueryType {
    always ("always"),
    ever ("ever"),
    typically ("typically");

    private final String string;

    EQueryType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    }
