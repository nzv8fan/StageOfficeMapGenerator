package nz.net.brad.mapgen;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A hallway can connect to a number of rooms and doors.
 * @author Brad
 *
 */
public class Hallway implements ConnectedRoom {
	
	ArrayList<Door> connectedDoors;
	private HashSet<Room> accessibleRooms;
	
	public Hallway() {
		connectedDoors = new ArrayList<Door>();
		accessibleRooms = new HashSet<Room>();
	}

	@Override
	public HashSet<Room> getAccessibleRooms() {
		return accessibleRooms;
	}

	public void addConnectedDoor(Door door) {
		connectedDoors.add(door);
		
	}

	@Override
	public void addAccessibleRooms(HashSet<Room> accessibleRooms) {
		this.accessibleRooms = accessibleRooms;
	}

}
