package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.core.GigaSpace;

import de.haw.tt2p.trafficcoordination.topology.Roxel;

/**
 * The driver is controlling his specific car and drives it in the world.
 */
public class Driver extends Thread {

	private final int speed = 1000;
	private final GigaSpace gigaSpace;
	private final Car car;

	public Driver(GigaSpace gigaSpace, Car car) {
		this.gigaSpace = gigaSpace;
		this.car = car;
		init();
	}

	@Override
	public void run() {
		while (!interrupted()) {
			Roxel myRoxelTemplate = new Roxel();
			myRoxelTemplate.setCar(car);

			Roxel readMyRoxel = gigaSpace.read(myRoxelTemplate);
			if (readMyRoxel != null) {
				Roxel findNextTemplate = new Roxel();
				findNextTemplate.setId(readMyRoxel.getRandomNextRoxelId());

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
							log("test: " + gigaSpace.read(myRoxelTemplate));
						} else {
							log("nächstes roxel ist nicht frei");
						}
						gigaSpace.write(myRoxel);
					} else {
						log("konnte mein roxel nicht erreichen");
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
	 * find a free roxel and start driving
	 */
	private void init() {
		Roxel template = new Roxel();
		template.setType(Roxel.Type.Street);
		Roxel read = gigaSpace.take(template);
		if (!read.hasCar()) {
			read.setCar(car);
			gigaSpace.write(read);
			log("init auf " + read + " " + read.getType());

		} else {
			gigaSpace.write(read);
			init();
		}
		log("finished initialize");
	}

	private void log(Object msg) {
		System.out.println(car + "=>" + msg);
	}
}
