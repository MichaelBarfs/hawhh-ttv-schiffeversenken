package hawhh.ttv.meth.schiffeversenken.gamelogic;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class StartUp {
	private static Logger log = Logger.getLogger(StartUp.class);
	private List<Game> nodes = new ArrayList<>();

	public static boolean go = false;
	
	
	public StartUp(String localAddress, String localPort, String serverAddress,
			String serverPort, int nodeCount, boolean isServer) throws MalformedURLException{
		if(isServer){
			//startup as server
			log.warn("Starting server node");
			Game server = new Game(serverAddress, serverPort);
			nodes.add(server);
		}
		log.warn("Starting additional " + nodeCount + " nodes.");
		for (int i = 0; i < nodeCount; i++) {
			int portNumber = Integer.parseInt(localPort) + i;
			String port = "" + portNumber;
			Game node = new Game(localAddress,port,serverAddress,serverPort,true);
			nodes.add(node);
		}
		log.warn("Startup finished.");
	}


	public List<String> getNodes() {
		List<String> ret = new ArrayList<String>();
		for (Game node : nodes) {
			ret.add(node.getChordId());
		}

		return ret;
	}
	
	public void startGame() {
		log.warn("starting Game in");

		try {
			//Count Down before game start.
			for (int i = 3; i > 0; i--) {
				log.warn(i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			log.fatal(e);
		}
		
		for (Game node : nodes) {
			node.startGame();
		}
	}
	
}
