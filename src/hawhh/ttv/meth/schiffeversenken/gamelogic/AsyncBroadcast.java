package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;

/**
 * Thread to broadcast and shoot asynchron.
 * @author Timo Haeckel
 *
 */
public class AsyncBroadcast implements Runnable {

	/**
	 * Data Storage for thread.
	 */
	private Chord chord;
	private ID target;
	private boolean hit;
	private Battleship battleship;
	private Logger logger;
	
	public AsyncBroadcast(Chord chord, ID target, boolean hit, Battleship battleship, Logger logger) {
		this.chord = chord;
		this.target = target;
		this.hit = hit;
		this.battleship = battleship;
		this.logger = logger;
	}

	@Override
	public void run() {
		
		//check if there was a target.
		//if null it is the first run and we start the game
		if(target != null){
			//its not the first run so broadcast where and if we were hit.
			chord.broadcast(target, hit);
		}
		
		//get the next target id to shoot
		ID target = battleship.getNextTarget();
		
		try {
			//shoot!
			chord.retrieve(target);
		} catch (ServiceException e) {
			logger.fatal(e);
		}

	}

}
