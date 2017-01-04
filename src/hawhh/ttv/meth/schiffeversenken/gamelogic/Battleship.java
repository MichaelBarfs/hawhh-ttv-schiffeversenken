package hawhh.ttv.meth.schiffeversenken.gamelogic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Battleship {
	
	private Logger log = Logger.getLogger(Battleship.class);
	
	private List<EnemyPlayer> enemies = new ArrayList<>();
	
	private List<GameEvent> gameEvents = new ArrayList<>();
	
	private Set<ID> enemyIDs = new HashSet<>();

	private MyPlayer myPlayer;

	private ID ownID; 
	
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
		EnemyPlayer weakest = enemies.get(0);
		for (EnemyPlayer enemyPlayer : enemies) {
			if(enemyPlayer.getShipCount() < weakest.getShipCount()){
				weakest = enemyPlayer;
			}
		}
		
		target = weakest.getNextTargetID();
		
		if(target == null){
			for (int i = 0; i < 10; i++)
				log.info("WINNER!!!!!" + weakest.getEndId() + " is dead!");
			target = weakest.getEndId();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return target;
	}

	private ID getRandomTarget() {
		Random rnd = new Random();
		ID id = null;
		do {
			//get random id in [ 0    2^160-1 ]
			id = ID.valueOf(new BigInteger(160, rnd));
		} while (myPlayer.containsID(id)); //check if random id is in our own area.
		return id;
	}

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
		
		GameEvent gameEvent = new GameEvent(event, source, target, hit, transactionNumber);
		gameEvents.add(gameEvent);
		addEnemy(source);
		log.info("New event: " + gameEvent);
		checkEnemiesAlive();
	}

	/**
	 * Check if all enemies alive.
	 */
	private void checkEnemiesAlive() {
		for (EnemyPlayer enemyPlayer : enemies) {
			if(enemyPlayer.isAlive()){
				for (int i = 0; i < 10; i++)
					log.info("##### " + enemyPlayer.getEndId() + " is dead!");
			}
		}
		
	}

	
	/**
	 * Add an enemy at the given id.
	 * @param source 	enemy id
	 */
	private void addEnemy(ID source) {
		if(!source.equals(ownID)) {
			enemyIDs.add(source);
			updateEnemies();
		}
		
	}

	/**
	 * Update enemies.
	 */
	private void updateEnemies() {
		List<ID> sortedIds = getEnemyIds();
		sortedIds.add(ownID);
		Collections.sort(sortedIds);
		enemies.clear();
		
		for (int i=0; i < sortedIds.size(); i++){
			ID startID = sortedIds.get((i - 1 + sortedIds.size()) % sortedIds.size()); // get predecessor
			
			startID = ID.valueOf(startID.toBigInteger().add(BigInteger.valueOf(1)));
			ID endID = sortedIds.get(i);
			if(!endID.equals(ownID)) {
				EnemyPlayer enemy = new EnemyPlayer(startID, endID);
				enemies.add(enemy);
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
