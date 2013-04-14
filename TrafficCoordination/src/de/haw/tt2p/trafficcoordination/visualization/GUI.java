package de.haw.tt2p.trafficcoordination.visualization;

import org.openspaces.core.GigaSpace;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import de.haw.tt2p.trafficcoordination.topology.Roxel;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Type;
import de.haw.tt2p.trafficcoordination.topology.RoxelStructure;

public class GUI extends GameGrid {

	private static final long serialVersionUID = -2041869033867768645L;
	private final GigaSpace gigaSpace;

	public GUI(GigaSpace gigaSpace) {
		super(gigaSpace.read(new RoxelStructure()).getX(), gigaSpace.read(new RoxelStructure()).getY(),
			gigaSpace.read(new RoxelStructure()).getSize());
		this.gigaSpace = gigaSpace;
		initMap();
		show();
	}

	private void initMap() {
		for (Roxel roxel : gigaSpace.readMultiple(new Roxel())) {
			Actor actor;
			if (roxel.getType() == Type.House) {
				actor = new HouseActor();
			} else {
				actor = new StreetActor();
			}
			addActor(actor, new Location(roxel.getX(), roxel.getY()));
		}
	}

	@Override
	public void act() {
		this.removeActors(CarActor.class);
		Roxel template = new Roxel();
		template.setType(Type.Street);
		for (Roxel roxel : gigaSpace.readMultiple(template)) {
			if (roxel.hasCar()) {
				CarActor car = new CarActor(roxel.getCar());
				addActor(car, new Location(roxel.getX(), roxel.getY()));
			}
		}

	}
}
