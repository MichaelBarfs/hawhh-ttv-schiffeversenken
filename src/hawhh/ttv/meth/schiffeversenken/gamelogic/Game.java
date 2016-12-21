package hawhh.ttv.meth.schiffeversenken.gamelogic;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Game implements NotifyCallback {

	private Logger logger = Logger.getLogger(Game.class);
	Chord chord = new ChordImpl();

	private String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
	private String localAddress;
	private String localPort;
	private URL localURL;
	private String serverAddress;
	private String serverPort;
	private URL serverURL;

	private boolean isClient;

	private boolean isStarted;
	
	private History history = new History();
	
	private Battleship battleship = new Battleship();
	private MyPlayer myPlayer;
	
	// Server Constructor
	public Game(String localAddress, String localPort)
			throws MalformedURLException {
		this(localAddress, localPort, localAddress, localPort, false);
	}

	public Game(String localAddress, String localPort, String serverAddress,
			String serverPort, boolean isClient) throws MalformedURLException {
		PropertiesLoader.loadPropertyFile();

		// set local address
		this.localAddress = localAddress;
		this.localPort = localPort;
		localURL = new URL(protocol + "://" + localAddress + ":" + localPort
				+ "/");

		// set server address
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		serverURL = new URL(protocol + "://" + serverAddress + ":" + serverPort
				+ "/");

		// set callback
		chord.setCallback(this);

		this.isClient = isClient;

		if (this.isClient) {
			// join server
			try {
				chord.join(localURL, serverURL);
			} catch (ServiceException e) {
				throw new RuntimeException(
						"Game as Client: Could not join the DHT !", e);
			}
			logger.info("Node as Client: init done");
		} else {
			// create server
			try {
				chord.create(localURL);
			} catch (ServiceException e) {
				throw new RuntimeException(
						"Game as Server: Could not create the DHT !", e);
			}
			logger.info("Node as Server: init done");
		}
	}

	@Override
	public void retrieved(ID target) {
		if(!isStarted) {
			isStarted = startGame();
		}
		boolean hit = false;
		if(target != null){
			//check if we were hit
			hit = myPlayer.checkHit(target);
		}
		
		broadcastAndShoot(target, hit);
		
		myPlayer.print();
		
		for (EnemyPlayer e: battleship.getEnemies()) {
			e.print();
		}
		
	}

	private void broadcastAndShoot(ID target, boolean hit) {
		new Thread(new AsyncBroadcast(chord,target,hit, battleship, logger)).start();			
	}

	private boolean startGame() {
		battleship = new Battleship();
		myPlayer = new MyPlayer();
		
		return true;
	}

	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		// save this to history!
		history.addEvent(source, target, hit, TransactionHelper.transactionNumber);
		if (hit) {
			logger.info(String.format("############### %s hit by %s",
					target.toDecimalString(), source.toDecimalString()));
		}
	}

}