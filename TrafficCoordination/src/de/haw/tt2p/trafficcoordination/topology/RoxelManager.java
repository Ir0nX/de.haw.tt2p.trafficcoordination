package de.haw.tt2p.trafficcoordination.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspaces.core.GigaSpace;

/**
 * Initializes the map in the tupel space.
 */
public class RoxelManager {

	private final GigaSpace gigaSpace;

	public RoxelManager(GigaSpace gigaSpace) {
		this.gigaSpace = gigaSpace;
		removeOld();
		init();
	}

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
	 * Initializes the topology map.
	 */
	private void init() {
		RoxelStructure template = new RoxelStructure();
		RoxelStructure roxelStructure = gigaSpace.read(template);
		if (roxelStructure == null) {
			roxelStructure = new RoxelStructure(1, 15, 15, 64);
			gigaSpace.write(roxelStructure);
		}
		init(roxelStructure.getX(), roxelStructure.getY());
	}

	/**
	 * Initializes the topology map as a grid.
	 *
	 * @param height amount of rows
	 * @param width amount of columns
	 */
	private void init(int height, int width) {
		int[][] grid = new int[width][height];
		Map<Integer, List<Integer>> forward = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> backward = new HashMap<Integer, List<Integer>>();

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
				if (!forward.containsKey(id)) {
					forward.put(id, new ArrayList<Integer>());
				}

				// vertical
				if (x % 6 == 0) {
					forward.get(id).add(grid[x][(y + 1) % height]);
				} else if (x % 3 == 0) {
					forward.get(id).add(grid[x][(y == 0 ? height - 1 : y - 1) % height]);
				}

				// horizontal
				if (y % 8 == 0) {
					forward.get(id).add(grid[(x + 1) % width][y]);
				} else if (y % 4 == 0) {
					forward.get(id).add(grid[x == 0 ? width - 1 : (x - 1) % width][y]);
				}
			}
		}

		for (Integer currentRoxelId : forward.keySet()) {
			for (Integer followingRoxelId : forward.get(currentRoxelId)) {
				if (!backward.containsKey(followingRoxelId)) {
					backward.put(followingRoxelId, new ArrayList<Integer>());
				}
				backward.get(followingRoxelId).add(currentRoxelId);
			}
		}

		// init roxels
		for (int x = 0; x < width; x++ ) {
			for (int y = 0; y < height; y++ ) {
				id = grid[x][y];
				Roxel.Type type = forward.get(id).isEmpty() ? Roxel.Type.House : Roxel.Type.Street;
				Roxel roxel = new Roxel(id, x, y, type, null, forward.get(id),
					backward.get(id) == null ? new ArrayList<Integer>() : backward.get(id));
				gigaSpace.write(roxel);
				System.out.print(roxel);
			}
			System.out.println();
		}
		System.out.println("Roxel grid initiated");
	}

}
