package de.haw.tt2p.trafficcoordination.game;

import java.util.Set;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.notify.SimpleNotifyContainerConfigurer;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;

import com.google.common.collect.Sets;

import de.haw.tt2p.trafficcoordination.container.TrafficLightNotifyContainer;

/**
 * Initializes the traffic flow.
 */
public class TrafficManager {

	private boolean isRunning = false;
	private final Set<Driver> drivers = Sets.newHashSet();
	private final SimpleNotifyEventListenerContainer trafficLightNotificationContainer;

	public TrafficManager(GigaSpace gigaSpace, int amountOfCars) {
		// add listener which handles the traffic lights
		trafficLightNotificationContainer = new SimpleNotifyContainerConfigurer(gigaSpace)
			.eventListenerAnnotation(new TrafficLightNotifyContainer(gigaSpace)).notifyContainer();

		for (int id = 0; id < amountOfCars; id++ ) {
			drivers.add(new Driver(gigaSpace, id));
		}
	}

	public void startDrivers() {
		if (!isRunning) {
			for (Driver driver : drivers) {
				driver.start();
			}
			isRunning = true;
		}
	}

	public void dispose() {
		trafficLightNotificationContainer.destroy();
		System.out.println("NotificationContainer for traffic lights removed");
	}

}
