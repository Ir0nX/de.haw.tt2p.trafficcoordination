package de.haw.tt2p.trafficcoordination.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspaces.core.GigaSpace;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
		init(roxelStructure.getX(), roxelStructure.getY());
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
		Map<Integer, List<Integer>> forward = Maps.newHashMap();
		Map<Integer, List<Integer>> backward = Maps.newHashMap();

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

				List<Integer> forwardList = forward.get(id);
				// vertical
				// every vertical direction allowed
//				if (x % 3 == 0) {
//					List<Integer> forwardList = forward.get(id);
//					// up
//					forwardList.add(grid[x][y == 0 ? height - 1 : y - 1]);
//					// down
//					forwardList.add(grid[x][(y + 1) % height]);
//				}
				// only allow down at every second street, only allow up for others
				if (x % 6 == 0) {
					forwardList.add(grid[x][(y + 1) % height]);
				} else if (x % 3 == 0) {
					forwardList.add(grid[x][y == 0 ? height - 1 : y - 1]);
				}

				// horizontal
				// every horizontal direction allowed
//				if (y % 4 == 0) {
//					// right
//					forwardList.add(grid[(x + 1) % width][y]);
//					// left
//					forwardList.add(grid[x == 0 ? width - 1 : x - 1][y]);
//				}
				// only allow right at every second street, only allow left for others
				if (y % 8 == 0) {
					forward.get(id).add(grid[(x + 1) % width][y]);
				} else if (y % 4 == 0) {
					forward.get(id).add(grid[x == 0 ? width - 1 : x - 1][y]);
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
				List<Integer> backwardList = backward.get(id);
				Roxel roxel = new Roxel(id, x, y, type, forward.get(id),
					backwardList == null ? Lists.<Integer>newArrayList() : backwardList);
				gigaSpace.write(roxel);
				System.out.print(roxel);
			}
			System.out.println();
		}
		System.out.println("Roxel grid initiated");
	}

}
