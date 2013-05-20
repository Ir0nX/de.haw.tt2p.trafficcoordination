package de.haw.tt2p.trafficcoordination.topology;

import java.util.Set;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.notify.SimpleNotifyContainerConfigurer;

import com.google.common.collect.Sets;

import de.haw.tt2p.trafficcoordination.container.TrafficLightNotifyContainer;
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
				Set<Direction> possibleDirections = getPossibleDirections(x, y);
				Roxel.Type type = possibleDirections.isEmpty() ? Roxel.Type.HOUSE : Roxel.Type.STREET;
				Roxel roxel = new Roxel(id, x, y, type, possibleDirections);
				if (roxel.isCrossroad()) {
					new SimpleNotifyContainerConfigurer(gigaSpace)
					.eventListenerAnnotation(new TrafficLightNotifyContainer(gigaSpace, id)).notifyContainer();
				}
				gigaSpace.write(roxel);
			}
		}
	}

	private Set<Direction> getPossibleDirections(int x, int y) {
		Set<Direction> possibleDirections = Sets.newHashSet();

		// vertical (only south direction)
		if (x % 3 == 0) {
			possibleDirections.add(Direction.SOUTH);
		}

		// horizontal (only east direction)
		if (y % 4 == 0) {
			possibleDirections.add(Direction.EAST);
		}

		return possibleDirections;
	}

}
