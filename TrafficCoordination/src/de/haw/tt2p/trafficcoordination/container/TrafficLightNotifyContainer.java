package de.haw.tt2p.trafficcoordination.container;

import java.util.Timer;
import java.util.TimerTask;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.Notify;
import org.openspaces.events.notify.NotifyType;

import de.haw.tt2p.trafficcoordination.topology.Roxel;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Direction;

@EventDriven
@Notify
@NotifyType
public class TrafficLightNotifyContainer {

	private final GigaSpace gigaSpace;
	private final Timer directionTimer;
	private Direction currentDirection = Direction.EAST;

	public TrafficLightNotifyContainer(GigaSpace gigaSpace) {
		this.gigaSpace = gigaSpace;
		directionTimer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				switch(currentDirection) {
					case EAST:
						currentDirection = Direction.SOUTH;
						break;
					default:
						currentDirection = Direction.EAST;
						break;
				}
				setNewDirection();
			}

		};
		directionTimer.schedule(task, 5000, 5000);
	}

	@EventTemplate
    Roxel unprocessedData() {
        Roxel template = new Roxel();
        template.setCurrentDirection(Direction.TODECIDE);
        return template;
    }

    @SpaceDataEvent
    public Roxel eventListener(Roxel crossroad) {
    	crossroad.setCurrentDirection(currentDirection);
        return crossroad;
    }

    /**
     * Updates all crossroad roxels in the space with the new direction. If the crossroad is currently
     * occupied by a car it will be ignored since it will be given to the traffic light service anyway with
     * current direction {@link Direction#TODECIDE}.
     */
    private void setNewDirection() {
    	Roxel template = new Roxel();
    	template.setCrossroad(true);
    	Roxel[] crossroads = gigaSpace.takeMultiple(template, Integer.MAX_VALUE);
    	for (Roxel crossroad : crossroads) {
    		if (!crossroad.hasCar()) {
    			crossroad.setCurrentDirection(currentDirection);
    		}
    		gigaSpace.write(crossroad);
    	}
    }

	public Direction getCurrentDirection() {
		return currentDirection;
	}

}
