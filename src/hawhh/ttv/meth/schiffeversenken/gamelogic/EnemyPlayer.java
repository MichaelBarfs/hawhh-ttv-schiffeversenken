package hawhh.ttv.meth.schiffeversenken.gamelogic;

import de.uniba.wiai.lspi.chord.data.ID;


public class EnemyPlayer extends Player {

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
