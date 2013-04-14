package de.haw.tt2p.trafficcoordination.visualization;

import ch.aplu.jgamegrid.Actor;

public class HouseActor extends Actor {
	public HouseActor() {
		super(getSprite());
	}

	/**
	 * Returns the sprite path of a building.
	 *
	 * @return the sprite path of the building (1 of 2 versions currently available)
	 */
	private static String getSprite() {
		long version = Math.round(Math.random()) + 1;
		return String.format("Sprites/building%s.png", version);
	}
}
