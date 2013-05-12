package de.haw.tt2p.trafficcoordination.game;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openspaces.core.GigaSpace;

import com.google.common.collect.Lists;

import de.haw.tt2p.trafficcoordination.topology.Roxel;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Direction;
import de.haw.tt2p.trafficcoordination.topology.RoxelStructure;
import de.haw.tt2p.trafficcoordination.visualization.RoxelUpdate;

/**
 * The driver is controlling his specific car and drives it in the world.
 */
public class Driver extends Thread {

	class Point {
		private final int x;
		private final int y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	// Car refreshing speed in ms
	private final int speed = 1000;
	private final GigaSpace gigaSpace;
	private final Integer carId;

	public Driver(GigaSpace gigaSpace, Integer carId) {
		this.gigaSpace = gigaSpace;
		this.carId = carId;
	}

	@Override
	public void run() {
		init();
		while (!interrupted()) {
			Roxel myRoxelTemplate = new Roxel();
			myRoxelTemplate.setCurrentCarId(carId);
			Roxel myRoxel = gigaSpace.take(myRoxelTemplate);
			if (myRoxel != null) {
				Roxel findNextTemplate = new Roxel();
				Point nextRoxelPosition = getNextRoxelPosition(myRoxel);
				findNextTemplate.setX(nextRoxelPosition.getX());
				findNextTemplate.setY(nextRoxelPosition.getY());
				Roxel nextRoxel = gigaSpace.take(findNextTemplate, Integer.MAX_VALUE);
				if (!nextRoxel.hasCar()) {
					myRoxel.removeCar();
					nextRoxel.setCurrentCarId(carId);
					// write roxel update for the gui
					gigaSpace.write(new RoxelUpdate(carId, myRoxel.getX(), myRoxel.getY(), nextRoxel.getX(), nextRoxel.getY()));
				}
				gigaSpace.write(myRoxel);
				gigaSpace.write(nextRoxel);
			}

			try {
				// make a pause
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Point getNextRoxelPosition(Roxel roxel) {
		int x = roxel.getX();
		int y = roxel.getY();
		Set<Direction> possibleDirections = roxel.getPossibleDirections();
		List<Point> nextPossibleRoxelPositions = Lists.newArrayList();
		RoxelStructure roxelStructure = gigaSpace.read(new RoxelStructure());
		int width = roxelStructure.getWidth();
		int height = roxelStructure.getHeight();

		if (possibleDirections.contains(Direction.NORTH)) {
			int northY = y - 1;
			nextPossibleRoxelPositions.add(new Point(x, northY < 0 ? height - 1 : northY));
		}
		if (possibleDirections.contains(Direction.EAST)) {
			int eastX = x + 1;
			nextPossibleRoxelPositions.add(new Point(eastX > width - 1 ? 0 : eastX, y));
		}
		if (possibleDirections.contains(Direction.SOUTH)) {
			int southY = y + 1;
			nextPossibleRoxelPositions.add(new Point(x, southY > height - 1 ? 0 : southY));
		}
		if (possibleDirections.contains(Direction.WEST)) {
			int westX = x - 1;
			nextPossibleRoxelPositions.add(new Point(westX < 0 ? width - 1 : westX, y));
		}

		int random = new Random().nextInt(nextPossibleRoxelPositions.size());
		return nextPossibleRoxelPositions.get(random);
	}

	/**
	 * Find a free roxel and start driving.
	 */
	private void init() {
		Roxel template = new Roxel();
		template.setType(Roxel.Type.STREET);
		Roxel roxel = gigaSpace.take(template);
		if (!roxel.hasCar()) {
			roxel.setCurrentCarId(carId);
			gigaSpace.write(roxel);
			gigaSpace.write(new RoxelUpdate(carId, -1, -1, roxel.getX(), roxel.getY()));
			log("finished initialize");
		} else {
			gigaSpace.write(roxel);
			init();
		}
	}

	private void log(Object msg) {
		System.out.println("Car " + carId + " => " + msg);
	}
}
