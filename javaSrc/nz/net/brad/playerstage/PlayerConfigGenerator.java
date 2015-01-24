package nz.net.brad.playerstage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple class for generating Player configuration files. 
 * @author Brad
 *
 */
public class PlayerConfigGenerator {
	
	/**
	 * Main method for generating Player configuration files. 
	 * @param outputFileName the name of the player configuration file to generate
	 * @param NUM_ROBOTS the number of robots to provide control to
	 * @param stageWorldFileName the Stage file this configuration connects to
	 * @param mapFileName the environment map this configuration connects to
	 * @throws IOException
	 */
	public void generateConfigFile(final String outputFileName, final int NUM_ROBOTS, final String stageWorldFileName, final String mapFileName) throws IOException {
		
		BufferedWriter playerConfigFile = new BufferedWriter(new FileWriter(outputFileName));
		
		playerConfigFile.write("# Auto-generated Stage Configuration File" + System.lineSeparator());
		playerConfigFile.newLine();
		playerConfigFile.write("# load the Stage plugin simulation driver" + System.lineSeparator());
		playerConfigFile.write("driver" + System.lineSeparator());
		playerConfigFile.write("(" + System.lineSeparator());
		playerConfigFile.write("  name \"stage\"" + System.lineSeparator());
		playerConfigFile.write("  provides [ \"simulation:0\" ]" + System.lineSeparator());
		playerConfigFile.write("  plugin \"stageplugin\"" + System.lineSeparator());
		playerConfigFile.write("  # load the named file into the simulator" + System.lineSeparator());
		playerConfigFile.write("  worldfile \"" + stageWorldFileName + "\"" + System.lineSeparator());
		playerConfigFile.write(")" + System.lineSeparator());
		playerConfigFile.newLine();
		
		// configure the robots
		for (int i = 0; i < NUM_ROBOTS; i++) {
			int port = 6665 + i;
			playerConfigFile.write("# Create a Stage driver and attach position2d and laser interfaces " + System.lineSeparator());
			playerConfigFile.write("# to the model \"r" + i + "\" " + System.lineSeparator());
			playerConfigFile.write("driver" + System.lineSeparator());
			playerConfigFile.write("(" + System.lineSeparator());
			playerConfigFile.write("  name \"stage\"" + System.lineSeparator());
			playerConfigFile.write("  provides [ \"" + port + ":position2d:0\" \"" + port + ":ranger:0\" \"" + port + ":ranger:1\" ]" + System.lineSeparator());
			playerConfigFile.write("  model \"r" + i + "\" " + System.lineSeparator());
			playerConfigFile.write(")" + System.lineSeparator());
			playerConfigFile.newLine();
			playerConfigFile.write("driver" + System.lineSeparator());
			playerConfigFile.write("(" + System.lineSeparator());
			playerConfigFile.write("  name \"mapfile\"" + System.lineSeparator());
			playerConfigFile.write("  provides [\"" + port + ":map:0\"]" + System.lineSeparator());
			playerConfigFile.write("  filename \"" + mapFileName + "\"" + System.lineSeparator());
			playerConfigFile.write("  resolution 0.05" + System.lineSeparator());
			playerConfigFile.write(")" + System.lineSeparator());
			playerConfigFile.newLine();
			playerConfigFile.write("driver" + System.lineSeparator());
			playerConfigFile.write("(" + System.lineSeparator());
			playerConfigFile.write("  name \"rangertolaser\"" + System.lineSeparator());
			playerConfigFile.write("  requires [\"" + port + ":ranger:1\"]" + System.lineSeparator());
			playerConfigFile.write("  provides [\"" + port + ":laser:0\"]" + System.lineSeparator());
			playerConfigFile.write("  alwayson 1" + System.lineSeparator());
			playerConfigFile.write(")" + System.lineSeparator());
			playerConfigFile.newLine();
			playerConfigFile.write("driver" + System.lineSeparator());
			playerConfigFile.write("(" + System.lineSeparator());
			playerConfigFile.write("  name \"snd\"" + System.lineSeparator());
			playerConfigFile.write("  requires [\"input::" + port + ":position2d:0\" \"output::" + port + ":position2d:0\" \"" + port + ":laser:0\"]" + System.lineSeparator());
			playerConfigFile.write("  provides [\"" + port + ":position2d:1\"]" + System.lineSeparator());
			playerConfigFile.write("  robot_radius 0.24" + System.lineSeparator());
			playerConfigFile.write("  min_gap_width 0.35" + System.lineSeparator());
			playerConfigFile.write("  obstacle_avoid_dist 0.5" + System.lineSeparator());
			playerConfigFile.write(")" + System.lineSeparator());
			playerConfigFile.newLine();
			playerConfigFile.write("driver" + System.lineSeparator());
			playerConfigFile.write("(" + System.lineSeparator());
			playerConfigFile.write("  name \"fakelocalize\"" + System.lineSeparator());
			playerConfigFile.write("  requires [\"6665:simulation:0\"]" + System.lineSeparator());
			playerConfigFile.write("  provides [\"" + port + ":localize:0\"]" + System.lineSeparator());
			playerConfigFile.write("  # a model (probably position) declared in \"stage.world\" " + System.lineSeparator());
			playerConfigFile.write("  model \"r" + i + "\"" + System.lineSeparator());
			playerConfigFile.write(")" + System.lineSeparator());
			playerConfigFile.newLine();
			playerConfigFile.write("driver" + System.lineSeparator());
			playerConfigFile.write("(" + System.lineSeparator());
			playerConfigFile.write("  name \"wavefront\"" + System.lineSeparator());
			playerConfigFile.write("  requires [\"output::" + port + ":position2d:1\" \"input::" + port + ":position2d:0\" \"" + port + ":map:0\"]" + System.lineSeparator());
			playerConfigFile.write("  provides [\"" + port + ":planner:0\"]" + System.lineSeparator());
			playerConfigFile.write("  safety_dist 0.15" + System.lineSeparator());
			playerConfigFile.write("  distance_epsilon 0.5" + System.lineSeparator());
			playerConfigFile.write("  angle_epsilon 40" + System.lineSeparator());
			playerConfigFile.write(")" + System.lineSeparator());
			playerConfigFile.newLine();
		}
		
		playerConfigFile.close();
	}

}
