package hawhh.ttv.meth.schiffeversenken.gamelogic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Main Class to manage Players.
 * @author Timo Haeckel
 *
 */
public class Battleship {
	
	private Logger log = Logger.getLogger(Battleship.class);
	
	/**
	 * list of known enemies.
	 */
	private List<EnemyPlayer> enemies = new ArrayList<>();
	
	/**
	 * Event History.
	 */
	private List<GameEvent> gameEvents = new ArrayList<>();
	
	/**
	 * list of known enemy ids.
	 */
	private Set<ID> enemyIDs = new HashSet<>();

	/**
	 * My own player and ships.
	 */
	private MyPlayer myPlayer;

	/**
	 * id of our player.
	 */
	private ID ownID;

	/**
	 * last target that we shot at. to check if WE got a hit.
	 */
	private ID lastTarget; 
	
	public Battleship(MyPlayer myPlayer) {
		this.myPlayer = myPlayer;
		ownID = this.myPlayer.getEndId();
	}

	/**
	 * Get next target to shoot at.
	 * @return target id
	 */
	public ID getNextTarget() {

		ID target = null;
		
		if(enemies.isEmpty()){
			//if no enemies known we need a random target
			return getRandomTarget();
		}
		
		//detect weakest target
		EnemyPlayer weakest = enemies.get(0);
		for (EnemyPlayer enemyPlayer : enemies) {
			if(weakest.getShipCount() <= 0){
				log.warn("#####" + weakest.getEndId().toHexString() + " is allready dead!");
				weakest = enemyPlayer;
			} if(enemyPlayer.getShipCount() < weakest.getShipCount()){
				weakest = enemyPlayer;
			}
			
		}
	
		
		target = weakest.getNextTargetID();
	
		// if no target id given the enemy is allready dead.
		if(target == null){
			log.warn("#####" + weakest.getEndId().toHexString() + " is allready dead!");
			target = weakest.getEndId();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//save last target
		this.lastTarget = target;

		return target;
	}

	/**
	 * Calculate a random target that is not in my players range.
	 * @return
	 */
	private ID getRandomTarget() {
		Random rnd = new Random();
		ID id = null;
		do {
			//get random id in [ 0    2^160-1 ]
			id = ID.valueOf(new BigInteger(160, rnd));
		} while (myPlayer.containsID(id)); //check if random id is in our own area.
		return id;
	}

	//get all known enemies
	public List<EnemyPlayer> getEnemies() {
		return enemies;
	}

	/**
	 * Notification for incoming events.
	 * @param event 	the recognized event
	 * @param source	source id of the event
	 * @param target	target id
	 * @param hit		information if a ship was hit or not
	 * @param transactionNumber		transaction number of the event.
	 */
	public void notify(GameEvent.EventType event, ID source, ID target, Boolean hit,
			int transactionNumber) {
		
		//check winning condition!
		if(target.equals(lastTarget) && hit){
			//we got a hit
			log.warn("I GOT ONE!");

			for (EnemyPlayer enemyPlayer : enemies) {
				//find player we hit.
				boolean isTarget = enemyPlayer.containsID(lastTarget);
				if(isTarget){
					//check if he is dead now
					if(enemyPlayer.getShipCount() <= 1){
						Game.locked = true;
						//WE ARE THE WINNER YEEEAAAH
						log.warn("##### " + enemyPlayer.getEndId().toHexString() + " is dead! (" + transactionNumber + ")");
						JOptionPane.showInputDialog("WINNER!!! " + enemyPlayer.getEndId().toHexString() + " is dead! (" + transactionNumber + ")");
					}
				}
			}
		}
		
		//save game event to history
		GameEvent gameEvent = new GameEvent(event, source, target, hit, transactionNumber);
		gameEvents.add(gameEvent);
		//add the enemy id to our list and update the enemies
		addEnemy(source);
		//check if all enemies are still alive
		checkEnemiesAlive();
		if(hit){
			for (EnemyPlayer enemyPlayer : enemies) {
				boolean isTarget = enemyPlayer.containsID(lastTarget);
				if(isTarget){
					log.warn(enemyPlayer.toString());
				}
			}
		}
	}

	/**
	 * Check if all enemies alive.
	 */
	private void checkEnemiesAlive() {
		for (EnemyPlayer enemyPlayer : enemies) {
			if(!enemyPlayer.isAlive()){
				log.warn("##### " + enemyPlayer.getEndId().toHexString() + " is dead!");
			}
		}
	}

	
	/**
	 * Add an enemy at the given id.
	 * @param source 	enemy id
	 */
	private void addEnemy(ID source) {
		//add all enemies except our own id.
		if(!source.equals(ownID)) {
			enemyIDs.add(source);
			updateEnemies();
		}
		
	}

	/**
	 * Update enemies.
	 */
	private   void updateEnemies() {
		//get sorted ids
		List<ID> sortedIds = getEnemyIds();
		//add our own
		sortedIds.add(ownID);
		//sort it again
		Collections.sort(sortedIds);
		
		//clear alle enemies
		enemies.clear();
		
		//go threw the list 
		for (int i=0; i < sortedIds.size(); i++){
			//get start id (predecessor id + 1)
			ID startID = sortedIds.get((i - 1 + sortedIds.size()) % sortedIds.size()); // get predecessor
			startID = ID.valueOf(startID.toBigInteger().add(BigInteger.valueOf(1)));
			//get end id
			ID endID = sortedIds.get(i);
			//check if it is an enemy and not ourself
			if(!endID.equals(ownID)) {
				//create a new enemy
				EnemyPlayer enemy = new EnemyPlayer(startID, endID);
				enemies.add(enemy);
				//apply all game events to the new enemy
				for (GameEvent gameEvent : gameEvents) {
					enemy.checkHit(gameEvent);
				}
			}
			
		}
		
	}
	
	/**
	 * Get a sorted list of all known enemy ids.
	 */
	public List<ID> getEnemyIds() {
		List<ID> ret = new ArrayList<ID>();
		ret.addAll(enemyIDs);
		Collections.sort(ret);
		return ret;
	}
	
}
