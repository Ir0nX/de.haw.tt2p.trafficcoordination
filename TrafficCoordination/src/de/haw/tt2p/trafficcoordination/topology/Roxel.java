package de.haw.tt2p.trafficcoordination.topology;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.gigaspaces.annotation.pojo.SpaceRouting;

import de.haw.tt2p.trafficcoordination.game.Car;

/**
 * roxels are the grid components of the map<br>
 * they have specific types/functionality and may contain a car
 *
 */
public class Roxel implements Serializable {
  private static final long serialVersionUID = -2645435700812910036L;

  /**
   * defines, what kind of roxel it is and though it's functionality
   *
   */
  public enum Type {
    Street, House;
  }

  public enum Scheduling {
    TBD, FREE;
  }

  private Integer id;
  private Integer x;
  private Integer y;
  private Car car;
  private Type type;
//  private Scheduling scheduling;
  private List<Integer> nextRoxels;
  private List<Integer> previosuRoxels;

  /**
   * Necessary Default constructor
   */
  public Roxel() {
  }

  public Roxel(Integer id, Integer x, Integer y, Type type, Car car, List<Integer> nextRoxels,
      List<Integer> previousRoxels) {
    this.id = id;
    this.x = x;
    this.y = y;
    setCar(car);
    setType(type);
    setNextRoxels(nextRoxels);
    setPreviosuRoxels(previousRoxels);
  }

  /**
   * The id of this message. We will use this attribute to route the message
   * objects when they are written to the space, defined in the Message.gs.xml
   * file.
   */
  @SpaceRouting
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

  public List<Integer> getNextRoxels() {
    return nextRoxels;
  }

  public void setNextRoxels(List<Integer> nextRoxels) {
    this.nextRoxels = nextRoxels;
  }

  public List<Integer> getPreviosuRoxels() {
    return previosuRoxels;
  }

  public void setPreviosuRoxels(List<Integer> previosuRoxels) {
    this.previosuRoxels = previosuRoxels;
  }

  @Override
  public String toString() {
    return String.format("Roxel(%s->%s->%s)", previosuRoxels, id, nextRoxels)
        + (hasCar() ? String.format("{%s}", car) : "");
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  /**
   *
   * @return any next roxel id
   */
  public Integer getRandomNextRoxelId() {

    return nextRoxels.get(new Random().nextInt(nextRoxels.size()));
  }

  public Car getCar() {
    return car;
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public boolean hasCar() {
    return car != null;
  }

  public void removeCar() {
    car = null;
  }

  public boolean isIntersection() {
    return nextRoxels.size() > 1;
  }

//  public Scheduling getScheduling() {
//    return scheduling;
//  }
//
//  public void setScheduling(Scheduling scheduling) {
//    this.scheduling = scheduling;
//  }
//
//  public void removeScheduling() {
//    this.scheduling = Scheduling.TBD;
//  }
}
