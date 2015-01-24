package nz.net.brad.playerstage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;

/**
 * Simple class for generating Stage configuration files. 
 * @author Brad
 *
 */
public class StageConfigGenerator {
	
	private final Random r;

	/**
	 * Construction for generating a Stage Configuration File
	 * @param r Requires an instance of Java Random
	 */
	public StageConfigGenerator(Random r) {
		this.r = r;
	}

	/**
	 * Main method for writing a Stage Configuration File. 
	 * @param outputFileName the name of the Stage configuration file to write to
	 * @param NUM_ROBOTS the number of robots to simulate
	 * @param occupancyGrid the occupancy grid representing the map and robot locations
	 * @param mapFileName the name of the PNG map file this simulation will use
	 * @throws IOException
	 */
	public void generateConfigFile(final String outputFileName, final int NUM_ROBOTS, Boolean[][] occupancyGrid, final String mapFileName) throws IOException {
		
		String relativeDirectory = "";	// TODO: pass an optional argument that is a relative directory for includes. 
		
		BufferedWriter stageConfigFile = new BufferedWriter(new FileWriter(outputFileName));
		
		stageConfigFile.write("# Auto-generated Stage Configuration File" + System.lineSeparator());
		stageConfigFile.newLine();
		stageConfigFile.write("# Includes" + System.lineSeparator());
		stageConfigFile.write("include \"" + relativeDirectory + "pioneer.inc\"" + System.lineSeparator());
		stageConfigFile.write("include \"" + relativeDirectory + "map.inc\"" + System.lineSeparator());
		stageConfigFile.write("include \"" + relativeDirectory + "sick.inc\"" + System.lineSeparator());
		stageConfigFile.newLine();
		stageConfigFile.write("# Global Settings" + System.lineSeparator());
		stageConfigFile.write("paused 1" + System.lineSeparator());
		stageConfigFile.write("resolution 0.02" + System.lineSeparator());
		stageConfigFile.write("threads 2" + System.lineSeparator());
		stageConfigFile.write("speedup 1 " + System.lineSeparator());
		stageConfigFile.newLine();
		stageConfigFile.write("# Configure the GUI window" + System.lineSeparator());
		stageConfigFile.write("window");
		stageConfigFile.write("(" + System.lineSeparator());
		stageConfigFile.write("  size [ 1100.000 1100.000 ] # in pixels" + System.lineSeparator());
		stageConfigFile.write("  scale 40   # pixels per meter" + System.lineSeparator());
		stageConfigFile.write("  center [ 0  0 ]" + System.lineSeparator());
		stageConfigFile.write("  rotate [ 0  0 ]" + System.lineSeparator());
		stageConfigFile.write("  show_data 1              # 1=on 0=off" + System.lineSeparator());
		stageConfigFile.write(")" + System.lineSeparator());
		stageConfigFile.newLine();
		stageConfigFile.write("# load a map" + System.lineSeparator());
		stageConfigFile.write("floorplan");
		stageConfigFile.write("(" + System.lineSeparator());
		stageConfigFile.write("  name \"grid\"" + System.lineSeparator());
		stageConfigFile.write("  size [26.000 26.000 2.500]" + System.lineSeparator());
		stageConfigFile.write("  pose [0 0 0 0]" + System.lineSeparator());
		stageConfigFile.write("  bitmap \"" + mapFileName + "\"" + System.lineSeparator());
		stageConfigFile.write(")" + System.lineSeparator());
		stageConfigFile.newLine();
		
		// configure the robots
		for (int i = 0; i < NUM_ROBOTS; i++) {
			stageConfigFile.write("fancypioneer2dx");
			stageConfigFile.write("(" + System.lineSeparator());
			stageConfigFile.write("  # can refer to the robot by this name" + System.lineSeparator());
			stageConfigFile.write("  name \"r" + i + "\"" + System.lineSeparator());
			stageConfigFile.write("  pose [ " + generateRobotPose(occupancyGrid) + " ] " + System.lineSeparator());
			stageConfigFile.write("  fancysicklaser( )" + System.lineSeparator());
			stageConfigFile.write(")" + System.lineSeparator());
			if (i < NUM_ROBOTS - 1) stageConfigFile.newLine();
		}
		
		stageConfigFile.close();

	}

	/**
	 * Randomly generate the location of a robot.
	 * Adds this location to the occupancy grid to ensure robots are not placed on top of each other. 
	 * Also ensures robots are offset from walls. 
	 * @param occupancyGrid The occupancy grid will be modified. 
	 * @return Returns a string to be written into a stage configuration file giving the robot's pose. 
	 */
	private String generateRobotPose(Boolean[][] occupancyGrid) {
		
		int x = r.nextInt(490);	// width of the image/occupancy grid?
		// need to check that this value of x is not in a wall or very close to a wall, if it is regenerate x. 
		while ( (x > 10 && x < 40) || (x > 40 && (x - 40) % 110 > 80) ) x = r.nextInt(490);
		x += 10;	// offset for the left wall. 
		
		int y = r.nextInt(500);	// width of the image/occupancy grid?
		// need to check that this value of y is not in a wall or very close to a wall, if it is regenerate y. 
		while ( (y > 10 && y < 40) || (y > 40 && (y - 40) % 110 > 80) ) y = r.nextInt(490);
		y += 10;// offset for the top wall.
		
		// Need to check we are not placing on top of another robot. 
		if (occupancyGrid[y][x]) {
			return generateRobotPose(occupancyGrid);	// restart pose generation. 
		}
		
		// Add robot pose to occupancy grid... 
		// make sure that the area surrounding this new robot is now occupied. 
		for (int i = x - 3; i <= x + 3; i++) {
			for (int j = y - 3; j <= y + 3; j++) {
				occupancyGrid[j][i] = true;
			}
		}

	    int z = r.nextInt(360);
	    
	    StringWriter out = new StringWriter();
	    out.append(String.format("%3.3f %3.3f 0 %d.000", (x - 255) * 0.05, (y - 255) * 0.05, z - 180));
		
		return out.toString();
	}

}
