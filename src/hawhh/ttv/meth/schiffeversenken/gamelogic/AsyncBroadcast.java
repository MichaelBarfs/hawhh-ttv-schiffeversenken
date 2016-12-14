package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;

public class AsyncBroadcast implements Runnable {

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
		
		if(target != null){
			chord.broadcast(target, hit);
		}
		
		ID t = battleship.getNextTarget();
		
		try {
			chord.retrieve(t);
		} catch (ServiceException e) {
			logger.fatal(e);
		}

	}

}
