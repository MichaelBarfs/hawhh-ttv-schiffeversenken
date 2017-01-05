package hawhh.ttv.meth.schiffeversenken.gamelogic;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * class contains events for shooting
 * @author Timo Haeckel
 *
 */
public class GameEvent {
	
	/**
	 * Enum for eventtypes
	 * @author Timo Haeckel
	 *
	 */
	public enum EventType {
		BROADCAST
	}

	/**
	 * Event data
	 */
	public final EventType eventType;
	public final ID source;
	public final ID target; 
	public final Boolean hit;
	public final int transactionID;
	
	public GameEvent(EventType eventType, ID source, ID target, Boolean hit, int transactionID) {
		this.eventType = eventType;
		this.source = source;
		this.target = target;
		this.hit = hit;
		this.transactionID = transactionID;
	}
	
	@Override
	public String toString() {
		String ret = "[";
		ret += eventType.name() + ": " + transactionID + " ";
		ret += "source " + source.toDecimalString() + " ";
		if(hit){
			ret += "hit ";
		} else {
			ret+= "not hit ";
		}
		ret += "at " + target.toDecimalString() + "]";
		return ret;
	}
	
}
