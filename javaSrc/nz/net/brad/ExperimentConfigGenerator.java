package nz.net.brad;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderException;

import nz.net.brad.mapgen.MapGenerator;
import nz.net.brad.playerstage.PlayerConfigGenerator;
import nz.net.brad.playerstage.StageConfigGenerator;

/**
 * Entry class for generating configuration files for Player/Stage
 * @author Brad
 *
 */
public class ExperimentConfigGenerator {
	
	// TODO: in future this can be taken in as an argument
	private static final int NUM_ROOM_HIGH = 4;
	private static final int NUM_ROOM_WIDE = 4;

	/**
	 * Entry point for generating configuration files for Player/Stage
	 * @param args playerConfig.cfg stageConfig.world numberOfRobots outputMap.png optionalMinimumNumberOfDoorsOpen
	 */
	public static void main(String[] args) {
		
		if (args.length < 4) {
			System.out.println("USAGE: java nz.net.brad.ExperimentConfigGenerator playerConfig.cfg stageConfig.world numberOfRobots outputMap.png optionalMinimumNumberOfDoorsOpen");
			return;
		}
		
		final String playerConfigFileName = args[0];
		final String stageConfigFilename = args[1];
		
		final int NUM_OF_ROBOTS = Integer.valueOf(args[2]);
		
		final String mapFileName = args[3];
		
		int minimumDoorsToOpen = NUM_ROOM_HIGH * NUM_ROOM_WIDE - 1;
		if (args.length > 4) {
			minimumDoorsToOpen = new Integer(args[4]);
		}
		
		Random r = new Random();	// TODO: this can be seeded to recreate identical maps/files for testing.
		
		// Generate a map
		try {
			MapGenerator map = new MapGenerator(mapFileName,minimumDoorsToOpen);
			int numberOfDoorsOpened = map.generateMap(r);
			System.out.println("Map with " + numberOfDoorsOpened + " doors opened written to: " + mapFileName);
		} catch (TranscoderException | IOException e) {
			e.printStackTrace();
			System.out.println("Error converting SVG to PNG");
			return;
		}	
		
		// read the png file back as an occupancy grid. 
		Boolean[][] occupancyGrid;
		try {
			occupancyGrid = readOccupancyGridPNG(mapFileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error converting map PNG file to occupancy grid");
			return;
		}
		
		// Generate a stage world file. 
		try {
			StageConfigGenerator stageConfig = new StageConfigGenerator(r);
			stageConfig.generateConfigFile(stageConfigFilename,NUM_OF_ROBOTS,occupancyGrid,mapFileName);
			System.out.println("Writen Stage Configuration File with " + NUM_OF_ROBOTS + " Robots to: " + stageConfigFilename);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing Stage Configuration File");
			return;
		}
		
		// Generate a player config file. 
		try {
			PlayerConfigGenerator playerConfig = new PlayerConfigGenerator();
			playerConfig.generateConfigFile(playerConfigFileName, NUM_OF_ROBOTS, stageConfigFilename, mapFileName);
			System.out.println("Writen Player Configuration File with " + NUM_OF_ROBOTS + " Robots to: " + playerConfigFileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing Player Configuration File");
			return;
		}

	}

	/**
	 * Method to read a PNG file into a 2D boolean array. 
	 * @param mapFileName the name of the file to read
	 * @return returns a 2D array occupancy grid
	 * @throws IOException
	 */
	private static Boolean[][] readOccupancyGridPNG(String mapFileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(mapFileName));
		Raster imgData = image.getData(); // convert the image into something useful
		int[] prealloc = null;
		int[] singlePixel = imgData.getPixels(0, 0, imgData.getWidth(),
				imgData.getHeight(), prealloc); // get each of the pixels

		// convert to a boolean map
		Boolean[][] occupancyGrid = new Boolean[imgData.getHeight()][imgData.getWidth()];
		for (int i = 0; i < singlePixel.length; i += singlePixel.length / (imgData.getWidth() * imgData.getHeight())) {
			if (singlePixel[i] == 0) {
				occupancyGrid[ (i / (singlePixel.length / (imgData.getWidth() * imgData.getHeight()))) / imgData.getWidth()][ (i / (singlePixel.length / (imgData.getWidth() * imgData.getHeight())) ) % imgData.getWidth()] = true;
			} else {
				occupancyGrid[ (i / (singlePixel.length / (imgData.getWidth() * imgData.getHeight()))) / imgData.getWidth()][ (i / (singlePixel.length / (imgData.getWidth() * imgData.getHeight()))) % imgData.getWidth()] = false;
			}
		}
		return occupancyGrid;
	}

}
