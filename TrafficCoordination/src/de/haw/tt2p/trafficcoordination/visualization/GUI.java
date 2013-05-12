package de.haw.tt2p.trafficcoordination.visualization;

import java.util.ArrayList;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGExitListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import de.haw.tt2p.trafficcoordination.container.RoxelUpdatePollingContainer;
import de.haw.tt2p.trafficcoordination.game.TrafficManager;
import de.haw.tt2p.trafficcoordination.topology.Roxel;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Type;
import de.haw.tt2p.trafficcoordination.topology.RoxelStructure;

public class GUI extends GameGrid implements IGUIUpdater {

	private static final long serialVersionUID = -3381742157631617426L;

	private final GigaSpace gigaSpace;
	private final TrafficManager trafficManager;

	private SimplePollingEventListenerContainer roxelPollingContainer;

	public GUI(GigaSpace gigaSpace, final UrlSpaceConfigurer urlSpaceConfigurer, TrafficManager trafficManager) {
		super(gigaSpace.read(new RoxelStructure()).getWidth(), gigaSpace.read(new RoxelStructure()).getHeight(), gigaSpace.read(
			new RoxelStructure()).getPixelSize());
		this.gigaSpace = gigaSpace;
		this.trafficManager = trafficManager;

		initMap();
		show();

		// add listener which removes the RoxelUpdatePollingContainer on exit
		addExitListener(new GGExitListener() {
			@Override
			public boolean notifyExit() {
				System.out.println("Cleanup...");
				if (roxelPollingContainer != null) {
					roxelPollingContainer.destroy();
					System.out.println("PollingContainer for Roxels removed");
				}
				if (urlSpaceConfigurer != null) {
					try {
						urlSpaceConfigurer.destroy();
						System.out.println("Destroyed thread and memory resources");
					} catch (Exception e) {
						System.out.println("WARNING: Could not destroy thread and memory resources!");
					}
				}
				return true;
			}
		});
		// add listener which polls the roxel updates for the gui
		roxelPollingContainer = new SimplePollingContainerConfigurer(gigaSpace)
	        .eventListenerAnnotation(new RoxelUpdatePollingContainer(this)).pollingContainer();
	}

	private void initMap() {
		for (Roxel roxel : gigaSpace.readMultiple(new Roxel())) {
			Actor actor;
			if (roxel.getType() == Type.HOUSE) {
				actor = new HouseActor();
			} else {
				actor = new StreetActor(roxel.getPossibleDirections());
			}
			addActor(actor, new Location(roxel.getX(), roxel.getY()));
		}
	}

	@Override
	public void act() {
		trafficManager.startDrivers();
		super.act();
	}

	@Override
	public void performUpdate(RoxelUpdate roxelUpdate) {
		Integer oldX = roxelUpdate.getOldX();
		Integer oldY = roxelUpdate.getOldY();
		Integer newX = roxelUpdate.getNewX();
		Integer newY = roxelUpdate.getNewY();
		Integer carId = roxelUpdate.getCarId();
		if (oldX == -1 || oldY == -1) {
			CarActor car = new CarActor(carId);
			addActor(car, new Location(newX, newY));
		} else {
			ArrayList<Actor> actorsAt = getActorsAt(new Location(oldX, oldY));
			for (Actor actor : actorsAt) {
				if (actor instanceof CarActor) {
					actor.setLocation(new Location(newX, newY));
				}
			}
		}
	}

}
