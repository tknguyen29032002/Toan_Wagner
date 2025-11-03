package clueGame;

/**
 * Enum representing the direction a door faces on the board
 * Doors can face UP (^), DOWN (v), LEFT (<), RIGHT (>) or NONE (not a door)
 * The direction indicates which room the door provides access to
 */
public enum DoorDirection {
	/** Door facing upward - room is above the door */
	UP, 
	/** Door facing downward - room is below the door */
	DOWN, 
	/** Door facing left - room is to the left of the door */
	LEFT, 
	/** Door facing right - room is to the right of the door */
	RIGHT, 
	/** Not a doorway */
	NONE
}

