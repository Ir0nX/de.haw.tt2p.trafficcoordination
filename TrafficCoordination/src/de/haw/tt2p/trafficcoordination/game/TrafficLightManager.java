package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;

import de.haw.tt2p.trafficcoordination.topology.Roxel;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Type;

/**
 * initialisiert alle polling container
 *
 * @see http://www.gigaspaces.com/wiki/display/XAP7/Polling+Container
 */
public class TrafficLightManager {

  private final GigaSpace space;

  public TrafficLightManager(GigaSpace space) {
    this.space = space;
    initTrafficLights();
  }

  public void initTrafficLights() {
    for (Roxel roxel : space.readMultiple(new Roxel())) {
      // TODO kein polling container, sondern ein NotifyContainer
      // funktioniert nicht, da er die Roxel zu stark in beanspruchung nimmt,
      // müsste eher event getriggert werden
      if (roxel.getType().equals(Type.Street)) {
        new SimplePollingContainerConfigurer(space).template(roxel).idleTaskExecutionLimit(2000)
            .eventListenerAnnotation(new TrafficLight()).pollingContainer();
      }
    }
  }

}
