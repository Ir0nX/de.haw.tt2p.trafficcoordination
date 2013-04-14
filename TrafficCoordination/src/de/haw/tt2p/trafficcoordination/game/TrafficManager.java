package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.core.GigaSpace;

import de.haw.tt2p.trafficcoordination.game.carId.CarIdGenerator;
import de.haw.tt2p.trafficcoordination.game.carId.CarIdGeneratorFlag;

/**
 * Initializes the traffic flow.
 */
public class TrafficManager {

	private final GigaSpace gigaSpace;

	public TrafficManager(GigaSpace gigaSpace, int amountOfCars) {
		this.gigaSpace = gigaSpace;
		for (int i = 0; i < amountOfCars; i++ ) {
			Car car = new Car(getNextCarId());
			gigaSpace.write(car);
			new Driver(gigaSpace, car).start();
		}

	}

	/**
	 * Always generates a unique car id.
	 *
	 * @return a tupel space unique car id
	 */
	private Integer getNextCarId() {
		CarIdGeneratorFlag flagTemplate = new CarIdGeneratorFlag();
		// only reading access for the flag, so that this tupel is always available
		CarIdGeneratorFlag carIdGeneratorFlag = gigaSpace.read(flagTemplate);
		if (carIdGeneratorFlag == null) {
			// if no car id generator tupel exists, then build one
			gigaSpace.write(new CarIdGeneratorFlag(0));
			gigaSpace.write(new CarIdGenerator(0));
			System.out.println("intiated carIdGenerator");
			return getNextCarId();
		}
		CarIdGenerator generatorTemplate = new CarIdGenerator();
		CarIdGenerator carIdGenerator = gigaSpace.take(generatorTemplate);
		if (carIdGenerator == null) {
			// another manager is using the generator
			try {
				// make a pause
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// an retry again
			return getNextCarId();
		}

		// raise given id and use the new one
		carIdGenerator.raiseId();
		Integer id = carIdGenerator.getId();
		gigaSpace.write(carIdGenerator);
		System.out.println("raised car id " + carIdGenerator);
		return id;
	}
}
