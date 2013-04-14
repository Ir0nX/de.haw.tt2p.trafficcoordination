package de.haw.tt2p.trafficcoordination.game;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Representation class of a car in the tupel space.
 */
public class Car{

	private Integer id;

	/**
	 * Necessary Default constructor.
	 */
	public Car() {
	}

	public Car(Integer id) {
		this.id = id;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("Car(%s)", id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Car other = (Car) obj;
		if (id == null) {
			return other.id == null;
		}
		return id.equals(other.id);
	}

}
