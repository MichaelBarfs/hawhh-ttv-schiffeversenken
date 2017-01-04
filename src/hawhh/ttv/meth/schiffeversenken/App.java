package hawhh.ttv.meth.schiffeversenken;

import java.net.MalformedURLException;

import hawhh.ttv.meth.schiffeversenken.gamelogic.StartUp;
import hawhh.ttv.meth.schiffeversenken.gamelogic.UserInterface;

import org.apache.log4j.Logger;


public class App{
	private static final String LOCAL_ADDRESS = "localhost";
	private static final String LOCAL_PORT = "9000";
	private static final String SERVER_ADDRESS = "localhost";
	private static final String SERVER_PORT = "9000";
	private static final int NODE_COUNT = 5;
	private static final boolean IS_SERVER = true;
	private static Logger log = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		log.info("Starting ...");

		//load chord properties
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		
		try {
			StartUp starter = new StartUp(LOCAL_ADDRESS, LOCAL_PORT, SERVER_ADDRESS, SERVER_PORT, NODE_COUNT, IS_SERVER);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
