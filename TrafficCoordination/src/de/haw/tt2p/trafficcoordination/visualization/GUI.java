package de.haw.tt2p.trafficcoordination.visualization;

import java.util.Set;

import org.openspaces.core.GigaSpace;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import com.google.common.collect.Sets;
import com.j_spaces.core.client.SQLQuery;

import de.haw.tt2p.trafficcoordination.game.TrafficManager;
import de.haw.tt2p.trafficcoordination.topology.Roxel;
import de.haw.tt2p.trafficcoordination.topology.Roxel.Type;
import de.haw.tt2p.trafficcoordination.topology.RoxelStructure;
import de.haw.tt2p.trafficcoordination.visualization.StreetActor.Direction;

public class GUI extends GameGrid {

	private static final long serialVersionUID = -3381742157631617426L;

	private final GigaSpace gigaSpace;
	private final TrafficManager trafficManager;

	public GUI(GigaSpace gigaSpace, TrafficManager trafficManager) {
		super(gigaSpace.read(new RoxelStructure()).getX(), gigaSpace.read(new RoxelStructure()).getY(), gigaSpace.read(
			new RoxelStructure()).getSize());
		this.gigaSpace = gigaSpace;
		this.trafficManager = trafficManager;
		initMap();
		show();
	}

	private void initMap() {
		for (Roxel roxel : gigaSpace.readMultiple(new Roxel())) {
			Actor actor;
			if (roxel.getType() == Type.House) {
				actor = new HouseActor();
			} else {
				Set<Direction> possibleDirections = Sets.newHashSet();
				int x = roxel.getX();
				// only allow down at every second street, only allow up for others
				if (x % 6 == 0) {
					possibleDirections.add(Direction.SOUTH);
				} else if (x % 3 == 0) {
					possibleDirections.add(Direction.NORTH);
				}
				int y = roxel.getY();
				// only allow right at every second street, only allow left for others
				if (y % 8 == 0) {
					possibleDirections.add(Direction.EAST);
				} else if (y % 4 == 0) {
					possibleDirections.add(Direction.WEST);
				}
				actor = new StreetActor(possibleDirections);
			}
			addActor(actor, new Location(roxel.getX(), roxel.getY()));
		}
	}

	@Override
	public void act() {
		trafficManager.startDrivers();
		removeActors(CarActor.class);
		Roxel template = new Roxel();
		template.setType(Type.Street);
		SQLQuery<Roxel> query = new SQLQuery<Roxel>(Roxel.class, "carId > -1");
		for (Roxel roxel : gigaSpace.readMultiple(query)) {
			CarActor car = new CarActor(roxel.getCarId());
			addActor(car, new Location(roxel.getX(), roxel.getY()));
		}
	}

}
