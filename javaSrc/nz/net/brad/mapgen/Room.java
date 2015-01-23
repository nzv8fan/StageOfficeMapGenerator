package nz.net.brad.mapgen;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Class representing a square 4-door room in the map. 
 * @author Brad
 *
 */
public class Room implements ConnectedRoom {

	private Door northDoor;
	private Door southDoor;
	private Door eastDoor;
	private Door westDoor;
	
	private HashSet<Room> accessibleRooms;
	
	/**
	 * Constructor to build a room with four doors. 
	 * @param n the north door
	 * @param s the south door
	 * @param e the east door
	 * @param w the west door
	 */
	public Room(Door n, Door s, Door e, Door w) {
		northDoor = n;
		southDoor = s;
		eastDoor = e;
		westDoor = w;
		
		northDoor.addRightRoom(this);
		southDoor.addLeftRoom(this);
		westDoor.addRightRoom(this);
		eastDoor.addLeftRoom(this);
		
		accessibleRooms = new HashSet<Room>();
		accessibleRooms.add(this);
	}

	public Door getSouthDoor() {
		return southDoor;
	}

	public Door getEastDoor() {
		return eastDoor;
	}

	@Override
	public HashSet<Room> getAccessibleRooms() {
		return accessibleRooms;
	}

	/**
	 * Returns true if this room is connected to all known rooms. 
	 * @param totalRooms the total number of rooms
	 * @return
	 */
	public boolean isFullyConnected(int totalRooms) {
		if (accessibleRooms.size() == totalRooms) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Opens the north door and connects to the rooms accessible via this door.
	 */
	public void openNorthDoor() {
		northDoor.open();
		ConnectedRoom connectedRoom = northDoor.getLeftRoom();
		updateAccessibleRooms(connectedRoom);
	}

	/**
	 * Ensures that when two rooms are connected, all other known connected rooms are also made aware of the new rooms.
	 * This is done by sharing one reference to a set of connected rooms across all rooms in the set. 
	 * @param connectedRoom 
	 */
	private void updateAccessibleRooms(ConnectedRoom connectedRoom) {
		accessibleRooms.addAll(connectedRoom.getAccessibleRooms());
		connectedRoom.addAccessibleRooms(accessibleRooms);
		Iterator<Room> it =  accessibleRooms.iterator();
		while(it.hasNext()) {
			Room r = it.next();
			if (r.getAccessibleRooms() != accessibleRooms) {
				r.addAccessibleRooms(accessibleRooms);
			}
		}
	}

	/**
	 * Opens the south door and connects to the rooms accessible via this door.
	 */
	public void openSouthDoor() {
		southDoor.open();
		ConnectedRoom connectedRoom = southDoor.getRightRoom();
		updateAccessibleRooms(connectedRoom);
	}

	/**
	 * Opens the east door and connects to the rooms accessible via this door.
	 */
	public void openEastDoor() {
		eastDoor.open();
		ConnectedRoom connectedRoom = eastDoor.getRightRoom();
		updateAccessibleRooms(connectedRoom);
	}

	/**
	 * Opens the west door and connects to the rooms accessible via this door.
	 */
	public void openWestDoor() {
		westDoor.open();
		ConnectedRoom connectedRoom = westDoor.getLeftRoom();
		updateAccessibleRooms(connectedRoom);
	}
	
	@Override
	public void addAccessibleRooms(HashSet<Room> accessibleRooms) {
		this.accessibleRooms = accessibleRooms;
	}

	public boolean isNorthDoorOpen() {
		return northDoor.isOpen();
	}
	
	public boolean isSouthDoorOpen() {
		return southDoor.isOpen();
	}
	
	public boolean isEastDoorOpen() {
		return eastDoor.isOpen();
	}
	
	public boolean isWestDoorOpen() {
		return westDoor.isOpen();
	}
}
