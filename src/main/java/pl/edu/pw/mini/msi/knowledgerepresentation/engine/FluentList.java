
package pl.edu.pw.mini.msi.knowledgerepresentation.engine;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.mini.msi.knowledgerepresentation.data.*;
/**
 * Created by rwyka on 5/10/15.
 */
public class FluentList {

    List<Fluent> list = new ArrayList<Fluent>();
    public FluentList(){

    }

    public FluentList(Fluent a){
        Add(a);
    }

    public FluentList(Fluent a, Fluent b){
        Add(a);
        Add(b);
    }

    public FluentList(Fluent a, Fluent b, Fluent c){
        Add(a);
        Add(b);
        Add(c);
    }

    public void Add(Fluent F){
        list.add(F);
    }



    public FluentList clone(){
        FluentList fl = new FluentList();
        for(Fluent f : list){
            fl.Add(f.clone());
        }
        return fl;
    }
}
