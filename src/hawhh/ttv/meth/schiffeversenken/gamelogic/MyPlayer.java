package hawhh.ttv.meth.schiffeversenken.gamelogic;

import de.uniba.wiai.lspi.chord.data.ID;

public class MyPlayer extends Player {

	public MyPlayer(ID startId, ID endId) {
		super(startId, endId);
		fillShips();
	}

	private void fillShips() {
		for (int i = 0; i < SHIP_COUNT; i++) {
			//get Random position
			int random = 0;
			do {
				random = (int) (Math.random() * INTERVAL_LENGTH);
			} while (!sectors.get(random).isShip());
			sectors.get(random).setShip(true);			
		}
	}

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
