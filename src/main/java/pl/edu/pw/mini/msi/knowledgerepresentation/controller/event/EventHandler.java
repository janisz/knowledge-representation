package pl.edu.pw.mini.msi.knowledgerepresentation.controller.event;

import com.google.common.eventbus.Subscribe;
/**
 * 
 * @author maq
 *
 * @param <T>
 */
public interface EventHandler<T extends Event>{
	
	@Subscribe
	public abstract void handle(T event);
	
}