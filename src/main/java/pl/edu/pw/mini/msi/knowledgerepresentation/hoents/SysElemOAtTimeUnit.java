package pl.edu.pw.mini.msi.knowledgerepresentation.hoents;

/**
 * Created by Tomek on 2015-09-02.
 */
public class SysElemOAtTimeUnit {
    public byte actionID;
    public String fluents;

    public SysElemOAtTimeUnit copy() {
        SysElemOAtTimeUnit newO = new SysElemOAtTimeUnit();
        newO.actionID = this.actionID;
        newO.fluents = new String(fluents);

        return newO;
    }
}
