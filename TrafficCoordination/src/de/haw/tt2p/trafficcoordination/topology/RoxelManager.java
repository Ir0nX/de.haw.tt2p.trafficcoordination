package de.haw.tt2p.trafficcoordination.topology;

import java.util.Set;

import org.openspaces.core.GigaSpace;

import com.google.common.collect.Sets;

import de.haw.tt2p.trafficcoordination.topology.Roxel.Direction;

/**
 * Creates a manager which manages the roxel structure and all roxels.
 * The manager must be initialized before it can be used.
 * @see #init()
 */
public class RoxelManager {

	private final GigaSpace gigaSpace;

	public RoxelManager(GigaSpace gigaSpace) {
		this.gigaSpace = gigaSpace;
	}

	/**
	 * Initializes the topology map.
	 */
	public void init() {
		removeOld();
		RoxelStructure template = new RoxelStructure();
		RoxelStructure roxelStructure = gigaSpace.read(template);
		if (roxelStructure == null) {
			roxelStructure = new RoxelStructure(1, 10, 10, 64);
			gigaSpace.write(roxelStructure);
		}
		init(roxelStructure.getWidth(), roxelStructure.getHeight());
	}

	/**
	 * Removes existing Roxels and RoxelStructure from the tupel space
	 */
	private void removeOld() {
		System.out.println("Anzahl vorhandener Roxel im Space: " + gigaSpace.count(new Roxel()));
		System.out.println("Lösche alte Tupel...");
		gigaSpace.takeMultiple(new Roxel());
		System.out.println("Anzahl vorhandener Roxel im Space: " + gigaSpace.count(new Roxel()));
		System.out.println("Lösche alte Roxel-Struktur...");
		gigaSpace.take(new RoxelStructure());
		System.out.println("Roxel-Struktur gelöscht!");
	}

	/**
	 * Initializes the topology map as a grid.
	 *
	 * @param height amount of rows
	 * @param width amount of columns
	 */
	private void init(int height, int width) {
		int[][] grid = new int[width][height];

		// init ids
		int id = 0;
		for (int x = 0; x < width; x++ ) {
			for (int y = 0; y < height; y++ ) {
				grid[x][y] = id++ ;
			}
		}

		// init followers
		for (int x = 0; x < width; x++ ) {
			for (int y = 0; y < height; y++ ) {
				id = grid[x][y];
				Set<Direction> possibleDirections = Sets.newHashSet();

				// vertical
				// only allow down at every second street, only allow up for others
				if (x % 6 == 0) {
					possibleDirections.add(Direction.SOUTH);
				} else if (x % 3 == 0) {
					possibleDirections.add(Direction.NORTH);
				}

				// horizontal
				// only allow right at every second street, only allow left for others
				if (y % 8 == 0) {
					possibleDirections.add(Direction.EAST);
				} else if (y % 4 == 0) {
					possibleDirections.add(Direction.WEST);
				}

				Roxel.Type type = possibleDirections.isEmpty() ? Roxel.Type.HOUSE : Roxel.Type.STREET;
				Roxel roxel = new Roxel(id, x, y, type, possibleDirections);
				gigaSpace.write(roxel);
			}
		}
	}

}
