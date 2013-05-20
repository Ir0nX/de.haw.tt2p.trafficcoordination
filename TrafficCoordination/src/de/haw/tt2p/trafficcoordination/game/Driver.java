package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.core.GigaSpace;

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
	private Point preferredPosition;

	public Driver(GigaSpace gigaSpace, Integer carId, Point preferredPosition) {
		this.gigaSpace = gigaSpace;
		this.carId = carId;
		this.preferredPosition = preferredPosition;
	}

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
				Point nextRoxelPosition = getNextRoxelPosition(myRoxel);
				Roxel nextRoxel = getNextRoxel(nextRoxelPosition, myRoxel.getCurrentDirection());
				if (!nextRoxel.hasCar()) {
					myRoxel.removeCar();
					myRoxel.resetCurrentDirection();
					nextRoxel.setCurrentCarId(carId);
					// write roxel update for the gui
					gigaSpace.write(new RoxelUpdate(carId, myRoxel.getX(), myRoxel.getY(),
						nextRoxel.getX(), nextRoxel.getY(), nextRoxel.getCurrentDirection()));
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
		Direction currentDirection = roxel.getCurrentDirection();

		Point nextRoxelPosition;
		RoxelStructure roxelStructure = gigaSpace.read(new RoxelStructure());
		int width = roxelStructure.getWidth();
		int height = roxelStructure.getHeight();

		if (currentDirection == Direction.EAST) {
			int eastX = x + 1;
			nextRoxelPosition = new Point(eastX > width - 1 ? 0 : eastX, y);
		} else {
			int southY = y + 1;
			nextRoxelPosition = new Point(x, southY > height - 1 ? 0 : southY);
		}

		return nextRoxelPosition;
	}

	private Roxel getNextRoxel(Point position, Direction direction) {
		Roxel findNextTemplate = new Roxel();
		findNextTemplate.setX(position.getX());
		findNextTemplate.setY(position.getY());
		findNextTemplate.setCurrentDirection(direction);
		Roxel nextRoxel = gigaSpace.take(findNextTemplate, Integer.MAX_VALUE);

		return nextRoxel;
	}

	/**
	 * Find a free roxel and start driving.
	 */
	private void init() {
		Roxel template = new Roxel();
		template.setType(Roxel.Type.STREET);
		template.setCrossroad(false);
		if (preferredPosition != null) {
			template.setX(preferredPosition.getX());
			template.setY(preferredPosition.getY());
		}
		Roxel roxel = gigaSpace.take(template);
		if (!roxel.hasCar()) {
			roxel.setCurrentCarId(carId);
			gigaSpace.write(roxel);
			gigaSpace.write(new RoxelUpdate(carId, -1, -1,
				roxel.getX(), roxel.getY(), roxel.getCurrentDirection()));
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
