package hawhh.ttv.meth.schiffeversenken.gamelogic;

import java.math.BigInteger;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

/**
 * Gameclass contains gamelogic
 * @author Timo Haeckel
 *
 */
public class Game implements NotifyCallback {

	private Logger logger = Logger.getLogger(Game.class);
	/**
	 * Chordnode to communicate
	 */
	Chord chord = new ChordImpl();

	/**
	 * Chord properties 
	 */
	private String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
	private String localAddress;
	private String localPort;
	private URL localURL;
	private String serverAddress;
	private String serverPort;
	private URL serverURL;

	private boolean isClient;

	private boolean isStarted = false;
	
	/**
	 * Playermanagement
	 */
	private Battleship battleship;
	
	/**
	 * Our player
	 */
	private MyPlayer myPlayer;
	
	// Server Constructor
	public Game(String localAddress, String localPort)
			throws MalformedURLException {
		this(localAddress, localPort, localAddress, localPort, false);
	}

	public Game(String localAddress, String localPort, String serverAddress,
			String serverPort, boolean isClient) throws MalformedURLException {

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
			// join DHT
			try {
				chord.join(localURL, serverURL);
			} catch (ServiceException e) {
				throw new RuntimeException(
						"Game as Client: Could not join the DHT !", e);
			}
			logger.warn("Node as Client: init done");
		} else {
			// create DHT
			try {
				chord.create(serverURL);
			} catch (ServiceException e) {
				throw new RuntimeException(
						"Game as Server: Could not create the DHT !", e);
			}
			logger.warn("Node as Server: init done");
		}
	}

	@Override
	public void retrieved(ID target) {
		if(!isStarted) {
			//if game not startet, start
			isStarted = startGame();
		}
		boolean hit = false;
		if(target != null){
			//check if we were hit
			hit = myPlayer.checkHit(target);
		}
		
		//start aychron broadcast to shoot
		new Thread(new AsyncBroadcast(chord,target,hit, battleship, logger)).start();
		
		//print our player 
		myPlayer.print();
		
		//print all enemies
		for (EnemyPlayer e: battleship.getEnemies()) {
			e.print();
		}
		
	}

	/**
	 * starting a new game
	 * @return true
	 */
	public boolean startGame() {
		//check if already started
		if(isStarted){
			return true;
		}
		//get start id (predecessor +1)
		ID start = ID.valueOf(chord.getPredecessorID().toBigInteger()
				.add(BigInteger.valueOf(1)));
		logger.warn("start id: " + start.toHexString());
		//get end id
		ID end = chord.getID();
		logger.warn("end id: " + end.toHexString());
		
		//create our player
		myPlayer = new MyPlayer(start, end);
		logger.warn(myPlayer);
		
		//create playermanagement
		battleship = new Battleship(myPlayer);
		isStarted = true;
		
		//if we are the max ID shoot
		if (myPlayer.hasMaxID()) {
			logger.warn(chord.getID() + " has 2^160-1");
			JOptionPane.showInputDialog("START!");
			retrieved(null);
		}
		
		return true;
	}

	/**
	 * get chordid 
	 * @return
	 */
	public String getChordId() {
		return chord.getID().toString();
	}
	
	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		// save this to history!
		battleship.notify(GameEvent.EventType.BROADCAST, source, target, hit, TransactionHelper.transactionNumber);
		if (hit) {
			logger.warn(String.format("%s hit at %s", source.toDecimalString(),
					target.toDecimalString()));
		}
	}

}
