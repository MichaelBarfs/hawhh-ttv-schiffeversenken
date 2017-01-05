package hawhh.ttv.meth.schiffeversenken;

import org.apache.log4j.Logger;



/**
 * Application class to start up.
 * @author Timo Haeckel
 *
 */
public class App{
	private static Logger log = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		log.warn("Starting ...");

		//load chord properties
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		
		//launch gui
		GUI gui = new GUI();
		
	}
	
}
