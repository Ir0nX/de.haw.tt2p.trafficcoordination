package de.haw.tt2p.trafficcoordination.topology;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Roxel structure describes the map topology.
 *
 */
public class RoxelStructure {

	private Integer id;
	private Integer width;
	private Integer height;
	private Integer pixelSize;

	/**
	 * Necessary Default constructor.
	 */
	public RoxelStructure() {
		// nothing to do
	}

	public RoxelStructure(Integer id, Integer width, Integer height, Integer pixelSize) {
		this.id = id;
		this.height = height;
		this.width = width;
		this.pixelSize = pixelSize;
	}

	@SpaceId
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
		return String.format("RoxelStructure(Dimension(%s, %s), Size(%s))", width, height, pixelSize);
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer newWidth) {
		this.width = newWidth;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer newHeight) {
		this.height = newHeight;
	}

	public Integer getPixelSize() {
		return pixelSize;
	}

	public void setPixelSize(Integer newPixelSize) {
		this.pixelSize = newPixelSize;
	}

}
