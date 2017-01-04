package hawhh.ttv.meth.schiffeversenken.gamelogic;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * MyPlayer manages all data of our ship states. It holds the ship positions and checks if we were hit or not.
 * @author abf902
 *
 */
public class MyPlayer extends Player {

	public MyPlayer(ID startId, ID endId) {
		super(startId, endId);
		fillShips();
	}

	/**
	 * fill the ship list SHIP_COUNT ships at random ship positions.
	 */
	private void fillShips() {
		for (int i = 0; i < SHIP_COUNT; i++) {
			//get Random position
			int random = 0;
			do {
				random = (int) (Math.random() * INTERVAL_LENGTH);
			} while (sectors.get(random).isShip());
			sectors.get(random).setShip(true);			
		}
	}

	/**
	 * check if a ship has been hit at target id.
	 * @param target	the id that was shot at.
	 * @return
	 */
	public boolean checkHit(ID target) {
		boolean ret = false;
		Sector sector = getContainingSector(target);
		if(sector == null){
			return false;
		}
		if(sector.isAlive()){
			shipCount--;
			ret = true;
		}
		sector.setHit(true);
		return ret;
	}

}
