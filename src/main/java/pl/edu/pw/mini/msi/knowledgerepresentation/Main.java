package pl.edu.pw.mini.msi.knowledgerepresentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pw.mini.msi.knowledgerepresentation.controller.KnowledgeRepresentationController;
import pl.edu.pw.mini.msi.knowledgerepresentation.controller.event.LineEnteredEvent;
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge;

import com.google.common.base.Joiner;
import com.google.common.eventbus.EventBus;

public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
    public static void main(String[] args) throws IOException {
    	
    	EventBus eventBus = new EventBus();
        KnowledgeRepresentationController knowledgeRepresentationController = new KnowledgeRepresentationController(eventBus);
        
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
    	    eventBus.post(new LineEnteredEvent(bufferRead.readLine()));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }
}