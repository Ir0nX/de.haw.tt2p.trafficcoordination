package de.haw.tt2p.trafficcoordination.visualization;

import java.util.Set;

import ch.aplu.jgamegrid.Actor;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Direction;

public class StreetActor extends Actor {

	public StreetActor(Set<Direction> possibleDirections) {
		super(getSprite(possibleDirections));
	}

	/**
	 * Returns the sprite path of a street element.
	 *
	 * @return the sprite path of the street element (depends on the possible directions)
	 */
	private static String getSprite(Set<Direction> possibleDirections) {
		String path = "Sprites/street.jpg";
		if (possibleDirections.contains(Direction.NORTH)) {
			if (possibleDirections.contains(Direction.EAST)) {
				path = "Sprites/northEast.jpg";
			} else if (possibleDirections.contains(Direction.WEST)) {
				path = "Sprites/northWest.jpg";
			}
		} else if (possibleDirections.contains(Direction.SOUTH)) {
			if (possibleDirections.contains(Direction.EAST)) {
				path = "Sprites/southEast.jpg";
			} else if (possibleDirections.contains(Direction.WEST)) {
				path = "Sprites/southWest.jpg";
			}
		}
		return path;
	}

}
