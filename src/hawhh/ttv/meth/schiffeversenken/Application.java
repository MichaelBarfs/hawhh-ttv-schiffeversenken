package hawhh.ttv.meth.schiffeversenken;

import hawhh.ttv.meth.schiffeversenken.gamelogic.UserInterface;

import org.apache.log4j.Logger;


public class Application {
	private static Logger log = Logger.getLogger(Application.class);
	
	public static void main(String[] args) {
		log.info("Starting ...");

		//load chord properties
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		
		UserInterface ui = new UserInterface();
	}
}
