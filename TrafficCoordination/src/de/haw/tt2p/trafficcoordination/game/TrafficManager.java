package de.haw.tt2p.trafficcoordination.game;

import java.util.Set;

import org.openspaces.core.GigaSpace;

import com.google.common.collect.Sets;

/**
 * Initializes the traffic flow.
 */
public class TrafficManager {

	Set<Driver> drivers = Sets.newHashSet();
	private boolean isRunning = false;

	public TrafficManager(GigaSpace gigaSpace, int amountOfCars) {
		for (int i = 0; i < amountOfCars; i++ ) {
			drivers.add(new Driver(gigaSpace, i));
		}
//		Car car1 = new Car(1);
//		gigaSpace.write(car1);
//		Car car2 = new Car(2);
//		gigaSpace.write(car2);
//		drivers.add(new TestDriver(gigaSpace, car1, 0, 2));
//		drivers.add(new TestDriver(gigaSpace, car2, 2, 4));
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
