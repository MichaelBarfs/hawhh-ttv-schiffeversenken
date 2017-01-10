package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;
import hawhh.ttv.meth.schiffeversenken.gamelogic.CoapLEDHelper.Color;

/**
 * MyPlayer manages all data of our ship states. It holds the ship positions and checks if we were hit or not.
 * @author abf902
 *
 */
public class MyPlayer extends Player {

	private Logger logger = Logger.getLogger(MyPlayer.class);
	
	public MyPlayer(ID startId, ID endId) {
		super(startId, endId);
		//set number of availabe ships
		setShipCount(SHIP_COUNT);
		//place ships in sectors
		fillShips();
	}

	/**
	 * set shipcount and paint LED 
	 * @param shipCount
	 */
	private void setShipCount(int shipCount) {
		this.shipCount = shipCount;
		if(shipCount == SHIP_COUNT){
			//LED green
			CoapLEDHelper.getInstance().setLED(Color.GREEN);
		} else if (shipCount < 10 && shipCount > 5){
			//LED blau
			CoapLEDHelper.getInstance().setLED(Color.BLUE);
		} else if (shipCount <= 5 && shipCount > 0){
			//LED pink
			CoapLEDHelper.getInstance().setLED(Color.PINK);
		} else {
			//LED red
			CoapLEDHelper.getInstance().setLED(Color.RED);
		}
	}

	/**
	 * fill the ship list SHIP_COUNT ships at random ship positions.
	 */
	private void fillShips() {
		sectors.get(0).setShip(true);
		sectors.get(INTERVAL_LENGTH-1).setShip(true);
		sectors.get(INTERVAL_LENGTH/2).setShip(true);
		for (int i = 0; i < SHIP_COUNT-3; i++) {
			//get Random position
			int random = 0;
			do {
				//do until a sector has no ship
				random = (int) (Math.random() * INTERVAL_LENGTH);
			} while (sectors.get(random).isShip());
			//place ship in sector
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
		//check contain the target id
		Sector sector = getContainingSector(target);
		if(sector == null){
			return false;
		}

		
		if(!isAlive()){
			logger.info(getEndId().toHexString() + ": I am already dead :(");
		}
		
		//check if sector has a ship
		if(sector.isAlive()){
			setShipCount(getShipCount()-1);;
			if(!isAlive()){
				logger.info(getEndId().toHexString() + ": I am dead :(");
			}
			ret = true;
		}
		//set sector has been hit
		sector.setHit(true);
		logger.warn(this.toString());
		return ret;
	}

}
