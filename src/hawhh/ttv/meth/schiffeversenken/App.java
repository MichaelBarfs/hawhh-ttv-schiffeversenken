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
	private static final int NODE_COUNT = 5;
	private static final boolean IS_SERVER = true;
	private static Logger log = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		log.warn("Starting ...");

		//load chord properties
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		
		CoapLEDHelper coapHelper = new CoapLEDHelper("coap://0.0.0.0/0.0.0.0:5683/RGB-LED/");
		coapHelper.setLED(Color.RED);
		
		
		String locAddr = JOptionPane.showInputDialog("Enter local address: (nothing = default)");
		if(locAddr.equals("")){
			locAddr = LOCAL_ADDRESS;
		}
		
		String locPort = JOptionPane.showInputDialog("Enter local port: (nothing = default)");
		if(locPort.equals("")){
			locPort = LOCAL_PORT;
		}
		
		String serverAddr = JOptionPane.showInputDialog("Enter server address: (nothing = default)");
		if(serverAddr.equals("")){
			serverAddr = SERVER_ADDRESS;
		}
		
		String serverPort = JOptionPane.showInputDialog("Enter server port: (nothing = default)");
		if(serverPort.equals("")){
			serverPort = SERVER_PORT;
		}
		
		String nodeCountString = JOptionPane.showInputDialog("Enter node count: (nothing = default)");
		int nodeCount = 1;
		if(!nodeCountString.equals("")){
			nodeCount = Integer.parseInt(nodeCountString);
		}
		
		String isServerString = JOptionPane.showInputDialog("Enter true if this is the server: (nothing = default = false)");
		boolean isServer = false;
		if(isServerString.equals("true")){
			isServer = true;
		} else if (isServerString.equals("")){
			isServer = IS_SERVER;
		}
		
		try {
			StartUp starter = new StartUp(locAddr, locPort, serverAddr, serverPort, nodeCount, isServer);
			String go = "";
			while(!go.equals("go")){
				go = JOptionPane.showInputDialog("Type go to start");
			}
			coapHelper.setLED(Color.GREEN);
			starter.startGame();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
