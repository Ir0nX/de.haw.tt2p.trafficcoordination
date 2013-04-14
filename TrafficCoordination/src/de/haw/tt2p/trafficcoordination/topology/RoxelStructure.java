package de.haw.tt2p.trafficcoordination.topology;

import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * Roxel structure describes the map topology.
 *
 */
public class RoxelStructure {
	private Integer id;
	private Integer x;
	private Integer y;
	private Integer size;

	/**
	 * Necessary Default constructor.
	 */
	public RoxelStructure() {
	}

	public RoxelStructure(Integer id, Integer x, Integer y, Integer size) {
		this.id = id;
		this.y = y;
		this.x = x;
		this.size = size;
	}

	@SpaceRouting
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void raiseId() {
		this.id++ ;
	}

	@Override
	public String toString() {
		return String.format("RoxelStructure(Dimension(%s, %s), Size(%s))", x, y, size);
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
