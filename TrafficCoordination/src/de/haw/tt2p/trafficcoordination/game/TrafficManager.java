package de.haw.tt2p.trafficcoordination.game;

import java.util.Set;

import org.openspaces.core.GigaSpace;

import com.google.common.collect.Sets;

/**
 * Initializes the traffic flow.
 */
public class TrafficManager {

	Set<Driver> drivers = Sets.newHashSet();

	public TrafficManager(GigaSpace gigaSpace, int amountOfCars) {
		for (int i = 0; i < amountOfCars; i++ ) {
			Car car = new Car(i);
			gigaSpace.write(car);
			drivers.add(new Driver(gigaSpace, car));
		}
	}

	public void startDrivers() {
		for (Driver driver : drivers) {
			driver.start();
		}
	}

}
