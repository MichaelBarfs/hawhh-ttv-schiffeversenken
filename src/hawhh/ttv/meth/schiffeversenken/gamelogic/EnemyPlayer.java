package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;


public class EnemyPlayer extends Player {
	private Logger logger = Logger.getLogger(EnemyPlayer.class);
	
	public EnemyPlayer(ID startId, ID endId) {
		super(startId, endId);
	}	
	
	/**
	 * Apply game event to enemyPlayer.
	 * @param event
	 */
	public void checkHit (GameEvent event){
		//check if target in sectors
		Sector sector = getContainingSector(event.target);
		if(sector == null){
			return;
		}
		//apply event
		sector.setShip(event.hit);
		sector.setHit(true);
		if(event.hit){
			shipCount--;
			logger.warn("Enemy " + getEndId().toHexString() + " hit " + (SHIP_COUNT - shipCount) + " recognized at " + event.target.toHexString() + ".");
		}
	}

	/**
	 * check all sectors and return target id to shoot next.
	 * @return target id. Null if no target left.
	 */
	public ID getNextTargetID() {
		if(shipCount <= 0){
			return null;
		}
		
		for (Sector sector : sectors) {
			if(!sector.isHit()){
				return sector.getEndId();
			}
		}
		return null;
	}

}
