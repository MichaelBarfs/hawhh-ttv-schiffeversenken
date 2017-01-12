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
		if(sector.isHit()){
			logger.info("Enemy " + getEndId().toHexString() + " hit twice at " + (SHIP_COUNT - shipCount) + " recognized at " + event.target.toHexString() + ".");
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
		for (int i = 0; i < 4; i++) {
			if(i==3){
				//search random sector that has not been hit yet.
				int random = 0;
				do {
					random = (int) (Math.random() * sectors.size());
				}while(sectors.get(random).isHit());
				
				return sectors.get(random).getEndId();
				
			} else if(!sectors.get(i).isHit()){
				return sectors.get(i).getEndId();
				
			} else if(!sectors.get(sectors.size()-(i+1)).isHit()){
				return sectors.get(sectors.size()-(i+1)).getEndId();
				
			} else if(!sectors.get(sectors.size()/2-(i)).isHit()){
				return sectors.get(sectors.size()/2-(i)).getEndId();
			} 
		}
		//shoot from first to last sector
		for (Sector sector : sectors) {
			if(!sector.isHit()){
				return sector.getEndId();
			}
		}
		return null;
	}

}
