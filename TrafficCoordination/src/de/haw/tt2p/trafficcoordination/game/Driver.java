package de.haw.tt2p.trafficcoordination.game;

import org.openspaces.core.GigaSpace;

import com.j_spaces.core.client.SQLQuery;

import de.haw.tt2p.trafficcoordination.topology.Roxel;

/**
 * The driver is controlling his specific car and drives it in the world.
 */
public class Driver extends Thread {

	// Car refreshing speed in ms
	private final int speed = 1000;
	private final GigaSpace gigaSpace;
	private final Integer id;
	private Integer currentRoxelId;

	public Driver(GigaSpace gigaSpace, Integer id) {
		this.gigaSpace = gigaSpace;
		this.id = id;
	}

//	@Override
//	public void run() {
//		init();
//		while (!interrupted()) {
//			Roxel myRoxelTemplate = new Roxel();
//			myRoxelTemplate.setCar(car);
//
//			Roxel readMyRoxel = gigaSpace.read(myRoxelTemplate);
//			if (readMyRoxel != null) {
//				Roxel findNextTemplate = new Roxel();
//				findNextTemplate.setId(readMyRoxel.getNextRoxelId());
//
//				Roxel possibleNextRoxel = gigaSpace.read(findNextTemplate);
//				if (possibleNextRoxel != null && !possibleNextRoxel.hasCar()) {
//					Roxel myRoxel = gigaSpace.take(myRoxelTemplate);
//					Roxel nextRoxel = gigaSpace.take(findNextTemplate);
//					if (myRoxel != null) {
//						if (nextRoxel != null && !nextRoxel.hasCar()) {
//							nextRoxel.setCar(car);
//							myRoxel.removeCar();
//							log(nextRoxel);
//							gigaSpace.write(nextRoxel);
//						} else {
//							log("Nächstes Roxel ist nicht frei!");
//						}
//						gigaSpace.write(myRoxel);
//					} else {
//						log("Konnte mein Roxel nicht erreichen!");
//					}
//				}
//			}
//			try {
//				// make a pause
//				Thread.sleep(speed + speed / 10 * (Math.random() < 0.5 ? 1 : -1));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	public void run() {
		init();
		while (!interrupted()) {
			SQLQuery<Roxel> myRoxelQuery = new SQLQuery<Roxel>(Roxel.class, "id = " + currentRoxelId);
			Roxel myRoxel = gigaSpace.take(myRoxelQuery);

			Integer nextRoxelId = myRoxel.getNextRoxelId();
			SQLQuery<Roxel> nextRoxelQuery = new SQLQuery<Roxel>(Roxel.class, "carId = -1 AND id = " + nextRoxelId);
			Roxel nextRoxel = gigaSpace.take(nextRoxelQuery, Integer.MAX_VALUE);
			nextRoxel.setCarId(id);
			myRoxel.setCarId(-1);
			currentRoxelId = nextRoxelId;

			gigaSpace.write(nextRoxel);
			gigaSpace.write(myRoxel);

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
	protected void init() {
		Roxel template = new Roxel();
		template.setType(Roxel.Type.Street);
		Roxel myRoxel = gigaSpace.take(template, Integer.MAX_VALUE);
		currentRoxelId = myRoxel.getId();
		myRoxel.setCarId(id);
		gigaSpace.write(myRoxel);
	}

}
