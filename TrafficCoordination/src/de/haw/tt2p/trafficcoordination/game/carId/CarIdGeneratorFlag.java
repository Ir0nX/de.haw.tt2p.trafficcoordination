package de.haw.tt2p.trafficcoordination.game.carId;

import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * indicates whtether or not a carIdGenerator exists in tupel space
 *
 */
public class CarIdGeneratorFlag {
  private Integer id;

  /**
   * Necessary Default constructor
   */
  public CarIdGeneratorFlag() {
  }

  public CarIdGeneratorFlag(Integer id) {
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
    return String.format("CarIdGeneratorFlag(%s)", id);
  }

}
