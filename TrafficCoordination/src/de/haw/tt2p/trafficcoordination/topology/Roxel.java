package de.haw.tt2p.trafficcoordination.topology;

import java.util.Set;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Roxels are the grid components of the map. They have specific types/functionality and may contain
 * a car.
 */
public class Roxel {

	public enum Type {
		STREET, HOUSE;
	}

	public enum Direction {
		EAST, SOUTH, TODECIDE;
	}

	private Integer id;
	private Integer x;
	private Integer y;
	private Integer currentCarId;
	private Type type;
	private Set<Direction> possibleDirections;
	private Direction currentDirection;
	private Boolean isCrossroad;

	/**
	 * Necessary Default constructor.
	 */
	public Roxel() {
		// nothing to do
	}

	public Roxel(Integer id, Integer x, Integer y, Type type, Set<Direction> possibleDirections) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
		this.possibleDirections = possibleDirections;
		resetCurrentDirection();
		isCrossroad = possibleDirections.size() > 1;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getCurrentCarId() {
		return currentCarId;
	}

	public void setCurrentCarId(Integer currentCarId) {
		this.currentCarId = currentCarId;
	}

	public Boolean hasCar() {
		return currentCarId != null;
	}

	public void removeCar() {
		currentCarId = null;
	}

	public Set<Direction> getPossibleDirections() {
		return possibleDirections;
	}

	public void setPossibleDirections(Set<Direction> possibleDirections) {
		this.possibleDirections = possibleDirections;
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	public Boolean isCrossroad() {
		return isCrossroad;
	}

	public void setCrossroad(Boolean isCrossroad) {
		this.isCrossroad = isCrossroad;
	}

	/**
	 * Resets the current direction of this roxel to TODECIDE if it is a crossroad,
	 * otherwise it will set the direction from the possible directions (which should only be one).
	 */
	public void resetCurrentDirection() {
		if (possibleDirections.size() == 1) {
			currentDirection = possibleDirections.iterator().next();
		} else if (possibleDirections.size() > 1){
			currentDirection = Direction.TODECIDE;
		}
	}

	@Override
	public String toString() {
		return String.format("Roxel(%s->%s)", id, currentDirection);
	}

}
