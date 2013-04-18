package de.haw.tt2p.trafficcoordination.game;

import java.util.Set;

import org.openspaces.core.GigaSpace;

import com.google.common.collect.Sets;

/**
 * Initializes the traffic flow.
 */
public class TrafficManager {

	private boolean isRunning = false;
	private final Set<Driver> drivers = Sets.newHashSet();

	public TrafficManager(GigaSpace gigaSpace, int amountOfCars) {
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

}
