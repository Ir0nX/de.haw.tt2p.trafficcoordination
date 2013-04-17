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
		for (int i = 0; i < amountOfCars; i++ ) {
			Car car = new Car(i);
			gigaSpace.write(car);
			drivers.add(new Driver(gigaSpace, car));
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
