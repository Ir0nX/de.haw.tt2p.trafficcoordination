package de.haw.tt2p.trafficcoordination.visualization;

import ch.aplu.jgamegrid.Actor;
import de.haw.tt2p.trafficcoordination.game.Car;

public class CarActor extends Actor {
	public CarActor(Car car) {
		super(getSprite(car.getId()));
	}

	/**
	 * Returns the sprite path for the given car id.
	 *
	 * @param id if of the car (identifies the sprite)
	 * @return the sprite path
	 */
	private static String getSprite(int id) {
		return String.format("Sprites/car%s.png", id % 5);
	}
}
