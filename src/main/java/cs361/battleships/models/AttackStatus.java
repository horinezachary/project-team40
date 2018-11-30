package cs361.battleships.models;

public enum AttackStatus {

	/**
	 * The result if an attack results in a miss.
	 */
	MISS,

	/**
	 * The result if an attack results in a hit on an enemy ship.
	 */
	HIT,

	/**
	 * THe result if an attack sinks the enemy ship
	 */
	SUNK,

	/**
	 * The results if an attack results in the defeat of the opponent (a
	 * surrender).
	 */
	SURRENDER,
	
	/**
	 * The result if the coordinates given are invalid.
	 */
	INVALID,

	/**
	 * The result if a sonar pulse reveals an empty square
	 */
	SONAR_EMPTY,

	/**
	 * The result if a sonar pulse reveals an occupied source
	 */
	SONAR_OCCUPIED

	/**
	 * The result if it's a surface attack
	 */


}
