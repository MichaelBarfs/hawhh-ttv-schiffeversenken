package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Class for enemy players
 * @author Timo Haeckel
 *
 */
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
		//apply event to enemy data
		sector.setShip(event.hit);
		sector.setHit(true);
		//if hit count down the ships for this enemy
		if(event.hit){
			shipCount--;
			logger.info("Enemy " + getEndId().toHexString() + " hit " + (SHIP_COUNT - shipCount) + " recognized at " + event.target.toHexString() + ".");
		}
	}

	/**
	 * check all sectors and return target id to shoot next.
	 * @return target id. Null if no target left.
	 */
	public ID getNextTargetID() {
		//check if player still have ships left
		if(shipCount <= 0){
			return null;
		}
		//search for a sector which not has been hit yet
		for (Sector sector : sectors) {
			if(!sector.isHit()){
				return sector.getEndId();
			}
		}
		return null;
	}

}
