package nz.net.brad.mapgen;

import java.util.HashSet;

/**
 * Interface describing the functionality required to traverse a room or hallway.
 * @author Brad
 *
 */
public interface ConnectedRoom {
	
	/**
	 * Returns the rooms known to be accessible
	 * @return
	 */
	public HashSet<Room> getAccessibleRooms();

	/**
	 * Adds more rooms that are accessible to the set of known accessible rooms. 
	 * Note depending on implementation this may just copy the reference and not clone the collection. 
	 * @param accessibleRooms the set of newly accessible rooms.
	 */
	public void addAccessibleRooms(HashSet<Room> accessibleRooms);
}
