package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.core.GigaSpace;

import de.haw.tt2p.trafficcoordination.topology.Roxel;

/**
 * The driver is controlling his specific car and drives it in the world.
 */
public class Driver extends Thread {

	// Car refreshing speed in ms
	private final int speed = 1000;
	private final GigaSpace gigaSpace;
	private final Car car;

	public Driver(GigaSpace gigaSpace, Car car) {
		this.gigaSpace = gigaSpace;
		this.car = car;
	}

	@Override
	public void run() {
		init();
		while (!interrupted()) {
			Roxel myRoxelTemplate = new Roxel();
			myRoxelTemplate.setCar(car);

			Roxel readMyRoxel = gigaSpace.read(myRoxelTemplate);
			if (readMyRoxel != null) {
				Roxel findNextTemplate = new Roxel();
				findNextTemplate.setId(readMyRoxel.getNextRoxelId());

				Roxel possibleNextRoxel = gigaSpace.read(findNextTemplate);
				if (possibleNextRoxel != null && !possibleNextRoxel.hasCar()) {
					Roxel myRoxel = gigaSpace.take(myRoxelTemplate);
					Roxel nextRoxel = gigaSpace.take(findNextTemplate);
					if (myRoxel != null) {
						if (nextRoxel != null && !nextRoxel.hasCar()) {
							nextRoxel.setCar(car);
							myRoxel.removeCar();
							log(nextRoxel);
							gigaSpace.write(nextRoxel);
						} else {
							log("Nächstes Roxel ist nicht frei!");
						}
						gigaSpace.write(myRoxel);
					} else {
						log("Konnte mein Roxel nicht erreichen!");
					}
				}
			}
			try {
				// make a pause
				Thread.sleep(speed + speed / 10 * (Math.random() < 0.5 ? 1 : -1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Find a free roxel and start driving.
	 */
	private void init() {
		Roxel template = new Roxel();
		template.setType(Roxel.Type.Street);
		Roxel roxel = gigaSpace.take(template);
		if (!roxel.hasCar()) {
			roxel.setCar(car);
			gigaSpace.write(roxel);
			log("init auf " + roxel + " " + roxel.getType());
			log("finished initialize");
		} else {
			gigaSpace.write(roxel);
			init();
		}
	}

	private void log(Object msg) {
		System.out.println(car + " => " + msg);
	}
}
