package hawhh.ttv.meth.schiffeversenken.gamelogic;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;

public abstract class Player implements Comparable<Player> {

	// my ids start and end of interval
	private final ID startId;
	private final ID endId;

	// interval size and number of ships
	public final int INTERVAL_LENGTH = 100;
	public final int SHIP_COUNT = 10;

	// maximum ids in chord chain
	public final BigInteger chordMax = BigInteger.valueOf(2).pow(160)
			.subtract(BigInteger.ONE);

	// gamefield
	protected ArrayList<Sector> sectors = new ArrayList<>();

	protected int shipCount = SHIP_COUNT;

	public Player(ID startId, ID endId) {
		this.startId = startId;
		this.endId = endId;

		initSectors();
	}

	public void print() {
		Logger.getLogger(Player.class).debug("Player: " + this.toString());
	}

	public ID getStartId() {
		return startId;
	}

	public ID getEndId() {
		return endId;
	}

	public boolean hasShips() {
		return shipCount > 0;
	}

	public int getShipCount() {
		return shipCount;
	}
	
	public boolean containsID(ID id) {
		ID s = ID.valueOf(getStartId().toBigInteger().subtract(BigInteger.valueOf(1)));
		ID e = ID.valueOf(getEndId().toBigInteger().add(BigInteger.valueOf(1)));
		return id.isInInterval(s, e);
	}
	
	protected Sector getContainingSector(ID id) {
		for (Sector sector : sectors) {
			ID s = ID.valueOf(sector.getStartId().toBigInteger().subtract(
					BigInteger.valueOf(1)));
			ID e = ID.valueOf(sector.getEndId().toBigInteger()
					.add(BigInteger.valueOf(1)));
			if (id.isInInterval(s, e)) {
				return sector;
			}
		}
		return null;
	}
	
	// Check if this Player should start.
	public boolean hasMaxID() {
		return getContainingSector(ID.valueOf(chordMax)) != null;
	}
	
	protected BigInteger getIntervalSize() {
		return getEndId().toBigInteger().add(getStartId().toBigInteger().negate())
				.mod(chordMax);
	}

	@Override
	public int compareTo(Player o) {
		return startId.compareTo(o.startId);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Player)) {
			return false;
		}

		Player other = (Player) obj;

		return this.getStartId().equals(other.getStartId())
				&& this.getEndId().equals(other.getEndId());
	}
	
	@Override
	public String toString() {
		String ret = "{ |";
		for (int i = 0; i < INTERVAL_LENGTH; i++) {
			ret += sectors.get(i).toString() + "|";
		}
		ret += " }";
		return ret;
	}

	/**
	 * To be implemented by enemies and own player, initializes ship locations
	 * in the sectors list.
	 */
	private void initSectors() {
		// get total size
		BigInteger totalSize = getIntervalSize();
		BigInteger sectorSize = totalSize.divide(BigInteger.valueOf(INTERVAL_LENGTH));
		
		for(int i = 0; i < INTERVAL_LENGTH; i++){
			BigInteger offset = sectorSize.multiply(BigInteger.valueOf(i));
			ID start = ID.valueOf(getStartId().toBigInteger().add(offset));
			ID end = ID.valueOf(start.toBigInteger().add(sectorSize).subtract(BigInteger.valueOf(1)));
			sectors.add(new Sector(start,end));
		}
		
	}
}
