package de.haw.tt2p.trafficcoordination.container;

import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.openspaces.events.polling.ReceiveHandler;
import org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler;
import org.openspaces.events.polling.receive.ReceiveOperationHandler;

import de.haw.tt2p.trafficcoordination.visualization.IGUIUpdater;
import de.haw.tt2p.trafficcoordination.visualization.RoxelUpdate;

@EventDriven
@Polling
public class RoxelUpdatePollingContainer {

	private final IGUIUpdater updater;

	public RoxelUpdatePollingContainer(IGUIUpdater updater) {
		this.updater = updater;
	}

	@ReceiveHandler
    ReceiveOperationHandler receiveHandler() {
		MultiTakeReceiveOperationHandler receiveHandler = new MultiTakeReceiveOperationHandler();
        return receiveHandler;
    }

    @EventTemplate
    RoxelUpdate unprocessedData() {
    	RoxelUpdate template = new RoxelUpdate();
        return template;
    }

    @SpaceDataEvent
    public RoxelUpdate eventListener(RoxelUpdate event) {
    	updater.performUpdate(event);
    	// Return nothing since we don't want to write something back into the space -> this consumes the RoxelUpdate
        return null;
    }

}
