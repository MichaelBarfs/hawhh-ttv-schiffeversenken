package hawhh.ttv.meth.schiffeversenken.gamelogic;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Sector data class
 * @author Timo Haeckel
 *
 */
public class Sector {

	/**
	 * sector contains ship
	 */
	private boolean ship = false;
	
	/**
	 * sector has been hit
	 */
	private boolean hit = false;
	
	/**
	 * Sector interval
	 */
	private ID startId;
	private ID endId;

	public Sector(ID startId, ID endId) {
		this.startId = startId;
		this.endId = endId;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean isShip() {
		return ship;
	}

	public void setShip(boolean ship) {
		this.ship = ship;
	}
	
	public boolean isAlive(){
		return isShip() && !isHit(); 
	}

	public ID getStartId() {
		return startId;
	}

	public void setStartId(ID startId) {
		this.startId = startId;
	}

	public ID getEndId() {
		return endId;
	}

	public void setEndId(ID endId) {
		this.endId = endId;
	}
	
	@Override
	public String toString() {
		String ret = "";
		if(!hit && ship){
			ret += "S";
		} else if (hit && ship) {
			ret += "T";
		} else if (hit && !ship) {
			ret += "X";
		} else {
			ret += "-";
		}
		return ret;
	}
	
}
