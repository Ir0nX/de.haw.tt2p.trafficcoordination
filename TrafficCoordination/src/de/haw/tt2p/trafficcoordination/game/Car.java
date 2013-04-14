package de.haw.tt2p.trafficcoordination.game;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * represantation class of a car in tupel space
 *
 */
public class Car implements Serializable {
  private static final long serialVersionUID = -4207930074533164738L;
  private Integer id;

  /**
   * Necessary Default constructor
   */
  public Car() {
  }

  public Car(Integer id) {
    this.id = id;
  }

  @SpaceRouting
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
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
      if (other.id != null) {
		return false;
	}
    } else if (!id.equals(other.id)) {
		return false;
	}
    return true;
  }

}
