package pl.edu.pw.mini.msi.knowledgerepresentation.controller;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.pw.mini.msi.knowledgerepresentation.ActionLanguageListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.ErrorListener;
import pl.edu.pw.mini.msi.knowledgerepresentation.Interpreter;
import pl.edu.pw.mini.msi.knowledgerepresentation.Main;
import pl.edu.pw.mini.msi.knowledgerepresentation.controller.event.EventHandler;
import pl.edu.pw.mini.msi.knowledgerepresentation.controller.event.LineEnteredEvent;
import pl.edu.pw.mini.msi.knowledgerepresentation.controller.event.LoadFileEvent;
import pl.edu.pw.mini.msi.knowledgerepresentation.engine.Knowledge;
import pl.edu.pw.mini.msi.knowledgerepresentation.util.FileLoader;

import com.google.common.base.Joiner;
import com.google.common.eventbus.EventBus;
/**
 * 
 * @author maq
 *
 */
public class KnowledgeRepresentationController {
	
	private static final Logger log = LoggerFactory.getLogger(KnowledgeRepresentationController.class);
	
	protected EventBus eventBus;
    protected ANTLRErrorListener errorListener = new ErrorListener();
    protected Knowledge knowledge = new Knowledge();
    protected ParseTreeListener parseTreeListener;
    protected Interpreter interpreter = new Interpreter(errorListener, parseTreeListener);
    
	public KnowledgeRepresentationController(EventBus eventBus) {
		this.eventBus = eventBus;
		addEventBusHandlers();
		this.parseTreeListener = new ActionLanguageListener(knowledge, eventBus);
	}
	
	private void addEventBusHandlers(){
		
		eventBus.register(new EventHandler<LoadFileEvent>() {
			
			public void handle(LoadFileEvent event) {
				filePathEntered(event.getFilePath());
			}
			
		});
		
		eventBus.register(new EventHandler<LineEnteredEvent>(){

			public void handle(LineEnteredEvent event) {
				lineEntered(event.getLine());
			}
			
		});
		
	}
	
	private void filePathEntered(String path){
		
		for(String parsedLine : FileLoader.loadFile(path)){
			lineEntered(parsedLine);
		}
		
	}
	
	private void lineEntered(String line) {
		
		try{
			log.info("Rule1: " + line);
	        List<Interpreter.Return> returns = interpreter.eval(line);
	        log.info(Joiner.on(", ").join(returns));
        }catch (RuntimeException e) {
        	log.error(e.getMessage(), e);
        }
		
	}
	
}
