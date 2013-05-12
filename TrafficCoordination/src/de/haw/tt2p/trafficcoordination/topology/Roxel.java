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
		NORTH, EAST, SOUTH, WEST;
	}

	private Integer id;
	private Integer x;
	private Integer y;
	private Integer currentCarId;
	private Type type;
	private Set<Direction> possibleDirections;

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

	public Boolean hasCar() {
		return currentCarId != null;
	}

	public void removeCar() {
		currentCarId = null;
	}

	public void setCurrentCarId(Integer currentCarId) {
		this.currentCarId = currentCarId;
	}

	public Set<Direction> getPossibleDirections() {
		return possibleDirections;
	}

	public void setPossibleDirections(Set<Direction> possibleDirections) {
		this.possibleDirections = possibleDirections;
	}

	@Override
	public String toString() {
		return String.format("Roxel(%s->%s->%s)", id, possibleDirections);
	}

}
