package nz.net.brad.mapgen;

/**
 * A door connects two classes that implement ConnectedRoom together. 
 * @author Brad
 *
 */
public class Door {
	
	private Boolean opened;
	
	private ConnectedRoom rightRoom;
	private ConnectedRoom leftRoom;
	
	/**
	 * Creates a door with no connected rooms.
	 */
	public Door() {
		opened = false;
		rightRoom = null;
		leftRoom = null;
	}
	
	/**
	 * Creates a door that connects to a hallway
	 * @param hallway the hallway
	 * @param isRightRoom is this hallway on the right side of the door?
	 */
	public Door(Hallway hallway, boolean isRightRoom) {
		this();	// call the other constructor to set the door closed. 
		if (isRightRoom) {
			rightRoom = hallway;
		} else {
			leftRoom = hallway;
		}
		hallway.addConnectedDoor(this);
	}

	public Boolean isOpen() {
		return opened;
	}

	public void addRightRoom(Room room) {
		rightRoom = room;
	}

	public void addLeftRoom(Room room) {
		leftRoom = room;
	}

	public ConnectedRoom getLeftRoom() {
		return leftRoom;
	}

	/**
	 * Opens a door. 
	 * A door only opens if two rooms are connected to the door. 
	 */
	public Boolean open() {
		if (leftRoom == null || rightRoom == null) return false;
		opened = true;
		return true;
		
	}

	public ConnectedRoom getRightRoom() {
		return rightRoom;
	}

}
