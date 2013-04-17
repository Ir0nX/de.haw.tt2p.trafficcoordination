package de.haw.tt2p.trafficcoordination.visualization;



public class RoxelUpdate {

	private Integer carId;
	private Integer oldX;
	private Integer oldY;
	private Integer newX;
	private Integer newY;

	/**
	 * Necessary Default constructor.
	 */
	public RoxelUpdate() {
	}

	public RoxelUpdate(Integer carId, Integer oldX, Integer oldY, Integer newX, Integer newY) {
		this.carId = carId;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Integer getOldX() {
		return oldX;
	}

	public void setOldX(Integer oldX) {
		this.oldX = oldX;
	}

	public Integer getOldY() {
		return oldY;
	}

	public void setOldY(Integer oldY) {
		this.oldY = oldY;
	}

	public Integer getNewX() {
		return newX;
	}

	public void setNewX(Integer newX) {
		this.newX = newX;
	}

	public Integer getNewY() {
		return newY;
	}

	public void setNewY(Integer newY) {
		this.newY = newY;
	}

}
