package de.haw.tt2p.trafficcoordination.game.carId;

import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * semaphore to generate car ids
 *
 */
public class CarIdGenerator {
  private Integer id;

  /**
   * Necessary Default constructor
   */
  public CarIdGenerator() {
  }

  public CarIdGenerator(Integer id) {
    this.id = id;
  }

  @SpaceRouting
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void raiseId() {
    this.id++;
  }

  @Override
  public String toString() {
    return String.format("CarIdGenerator(%s)", id);
  }

}
