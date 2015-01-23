package nz.net.brad.mapgen;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 * This is the main class of the map generator. 
 * @author Brad
 */
public class MapGenerator {
	
	// Constants for defining the size of the office and drawing it as an svg and png.
	private static final int NUM_ROOM_HIGH = 4;
	private static final int NUM_ROOM_WIDE = 4;
	
	private static final int OUTSIDE_WIDTH = 30;
	private static final int WALL_WIDTH = 10;
	private static final int WALL_LENGTH = 40;
	private static final int DOOR_WIDTH = 20;
	
	// Class fields
	private final String mapFile;
	private final int minimumDoorsToOpen;

	/**
	 * Constructor for the Map Generator Class
	 * @param mapFileName the file name for the map to output, should be .png
	 * @param minimumDoorsToOpen the minimum number of doors in the map to be opened. 
	 */
	public MapGenerator(String mapFileName, int minimumDoorsToOpen) {
		mapFile = mapFileName;
		this.minimumDoorsToOpen = minimumDoorsToOpen;
	}

	/**
	 * Method for generating a PNG Map file
	 * @param r required a random number generator
	 * @return returns the number of doors opened int the map
	 * @throws FileNotFoundException
	 * @throws TranscoderException
	 * @throws IOException
	 */
	public int generateMap(Random r) throws FileNotFoundException, TranscoderException, IOException {
		
		// Container to store the rooms in
		ArrayList<ArrayList<Room>> rooms = new ArrayList<ArrayList<Room>>();
		
		// A hallway connects to some of the rooms. 
		Hallway hallway = new Hallway();

		// Generate an office with all doors closed. 
		for (int i = 0; i < NUM_ROOM_HIGH; i++) {
			rooms.add(new ArrayList<Room>());	// create a container to store a row of rooms. 
			for (int j = 0; j < NUM_ROOM_WIDE; j++) {
				
				// get the reference for the north door, it is either the hallway or another room. 
				Door north;
				if (i == 0) {
					north = new Door(hallway,false);
				} else {
					north = rooms.get(i - 1).get(j).getSouthDoor();
				}
				
				// get the reference for the west door
				Door west;
				if (j == 0) {
					west = new Door(hallway,false);
				} else {
					west = rooms.get(i).get(j - 1).getEastDoor();
				}
				
				// n.b. the south and east doors are always new as this is how the loop progresses. 
				Door south;
				if (i == NUM_ROOM_HIGH - 1) {
					south = new Door(hallway,true);
				} else {
					south = new Door();
				}
				
				Door east;
				if (j == NUM_ROOM_WIDE - 1) {
					east = new Door(hallway,true);
				} else {
					east = new Door();
				}
				
				// create the room and add it to the collection of rooms. 
				rooms.get(i).add(new Room(north,south,east,west));
			}
		}
		
		int numberOfDoorsOpened = 0;
		
		// loop while the rooms are not all fully connected, and the number of doors opened is less than the minimum. 
		while (rooms.get(0).get(0).isFullyConnected(16) == false || numberOfDoorsOpened < minimumDoorsToOpen) {
			
			// randomly chose a room and a door to open. 
			int roomNum = r.nextInt(16);
			
			Room roomToOpen = rooms.get(roomNum / 4).get(roomNum % 4);
			int doorNum = r.nextInt(4);
			
			if (doorNum == 0 && roomToOpen.isNorthDoorOpen() == false) {
				roomToOpen.openNorthDoor();
				numberOfDoorsOpened++;
			} else if (doorNum == 1 && roomToOpen.isSouthDoorOpen() == false) {
				roomToOpen.openSouthDoor();
				numberOfDoorsOpened++;
			} else if (doorNum == 2 && roomToOpen.isEastDoorOpen() == false) {
				roomToOpen.openEastDoor();
				numberOfDoorsOpened++;
			} else if (doorNum == 3 && roomToOpen.isWestDoorOpen() == false) {
				roomToOpen.openWestDoor();
				numberOfDoorsOpened++;
			}
			
		}
				
		// Create the SVG File in a string writer
		StringWriter out = generateSvg(rooms);
		
		// Convert the SVG to a PNG file and save it. 
		convertSvgToPng(mapFile, new StringReader(out.toString()));

		return numberOfDoorsOpened;
	}

	/**
	 * Method to convert an SVG file and save it as a PNG file
	 * @param fileName
	 * @param out
	 * @throws FileNotFoundException
	 * @throws TranscoderException
	 * @throws IOException
	 */
	private static void convertSvgToPng(String fileName, StringReader reader) throws FileNotFoundException, TranscoderException, IOException {
		TranscoderInput input_svg_image = new TranscoderInput(reader); 
		OutputStream png_ostream = new FileOutputStream(fileName);
		TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);   
		PNGTranscoder my_converter = new PNGTranscoder();        
		my_converter.transcode(input_svg_image, output_png_image);
        png_ostream.flush();
        png_ostream.close();
	}

	/**
	 * Method to generate an SVG file of the map. Closed doors overlap walls to ensure that there are no gaps. 
	 * @param rooms
	 * @return
	 */
	private static StringWriter generateSvg(ArrayList<ArrayList<Room>> rooms) {
		StringWriter out = new StringWriter();
		
		// Set up the file structure. 
		out.append("<?xml version=\"1.0\" standalone=\"no\" ?>");
		out.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		out.append("<svg width=\"510\" height=\"510\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
		out.append("<rect width=\"510\" height=\"510\" style=\"fill:rgb(255,255,255);\"/>");

		// Draw a border around the image. 
		out.append("<line x1=\"0\" y1=\"0\" x2=\"510\" y2=\"0\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>");
		out.append("<line x1=\"0\" y1=\"510\" x2=\"510\" y2=\"510\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>");
		out.append("<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"510\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>");
		out.append("<line x1=\"510\" y1=\"0\" x2=\"510\" y2=\"510\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>");

		// Print out the horizontal walls.
		for (int i = 0; i < NUM_ROOM_HIGH; i++) {
			for (int j = 0; j < NUM_ROOM_WIDE; j++) {
				int startOffsetX = OUTSIDE_WIDTH + j * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH);
				int startOffsetY = OUTSIDE_WIDTH + i * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH) + 5;
				out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY, startOffsetX + WALL_WIDTH + WALL_LENGTH, startOffsetY));
				if (rooms.get(i).get(j).isNorthDoorOpen() == false) {
					out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX + WALL_WIDTH + WALL_LENGTH - 1, startOffsetY, startOffsetX + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH + 2, startOffsetY));
				}
				out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetY, startOffsetX + WALL_WIDTH + DOOR_WIDTH + 2 * WALL_LENGTH + 1, startOffsetY));
				if ( i == NUM_ROOM_HIGH - 1) {
					startOffsetY = OUTSIDE_WIDTH + (i+1) * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH) + 5;
					out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY, startOffsetX + WALL_WIDTH + WALL_LENGTH, startOffsetY));
					if (rooms.get(i).get(j).isSouthDoorOpen() == false) {
						out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX + WALL_WIDTH + WALL_LENGTH - 1, startOffsetY, startOffsetX + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH + 2, startOffsetY));
					}
					out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetY, startOffsetX + WALL_WIDTH + DOOR_WIDTH + 2 * WALL_LENGTH + 1, startOffsetY));
				}
				
	      
			}
		}

		// Print out the vertical walls.
		for (int i = 0; i < NUM_ROOM_HIGH; i++) {
			for (int j = 0; j < NUM_ROOM_WIDE; j++) {
				int startOffsetX = OUTSIDE_WIDTH + j * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH) + 5;
				int startOffsetY = OUTSIDE_WIDTH + i * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH);
			      
				out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY, startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH));
				if (rooms.get(i).get(j).isWestDoorOpen() == false) {
					out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH - 1, startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH + 2));
				}
				out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetX, startOffsetY + 2 * WALL_WIDTH + DOOR_WIDTH + 2 * WALL_LENGTH));
				if ( j == NUM_ROOM_WIDE - 1) {
					startOffsetX = OUTSIDE_WIDTH + (j+1) * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH) + 5;
					out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY, startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH));
					if (rooms.get(i).get(j).isEastDoorOpen() == false) {
						out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH - 1, startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH + 2));
					}
					out.append(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetX, startOffsetY + 2 * WALL_WIDTH + DOOR_WIDTH + 2 * WALL_LENGTH));
				}
			}
		}
			  
		out.append("</svg>");
		return out;
	}

}
