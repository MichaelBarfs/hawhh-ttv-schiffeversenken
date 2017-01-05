package hawhh.ttv.meth.schiffeversenken;

import java.net.MalformedURLException;

import javax.swing.JOptionPane;

import hawhh.ttv.meth.schiffeversenken.gamelogic.CoapLEDHelper;
import hawhh.ttv.meth.schiffeversenken.gamelogic.CoapLEDHelper.Color;
import hawhh.ttv.meth.schiffeversenken.gamelogic.StartUp;
import hawhh.ttv.meth.schiffeversenken.gamelogic.UserInterface;

import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;


public class App{
	private static final String LOCAL_ADDRESS = "localhost";
	private static final String LOCAL_PORT = "9001";
	private static final String SERVER_ADDRESS = "localhost";
	private static final String SERVER_PORT = "9000";
	private static final int NODE_COUNT = 0;
	private static final boolean IS_SERVER = true;
	private static Logger log = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		log.warn("Starting ...");

		//load chord properties
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		
		GUI gui = new GUI();
		
	}
	
}
