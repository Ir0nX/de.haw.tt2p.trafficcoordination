package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.events.adapter.SpaceDataEvent;

import de.haw.tt2p.trafficcoordination.topology.Roxel;

/**
 * soll ein DataProcessor sein, welcher die Keuzungen wieder auf befahrbar
 * setzt, nachdem ein Auto darüber gefahren ist
 *
 */
// @EventDriven
// @Notify // NotifyContainer
public class TrafficLight {

  @SpaceDataEvent
  public Roxel eventListener(Roxel r) {
    // r.setScheduling(null);
    // TODO setze Ampel wieder auf null
    return r;
  }
}