package com.Entity;

import javax.persistence.*;

@Entity
@Table(name="vehiclePictures")
public class VehiclePictures {
	@EmbeddedId
	private VehiclePicturesId id;
	
	public VehiclePictures() {
		super();
	}

	public VehiclePictures(VehiclePicturesId id) {
		super();
		this.id = id;
	}

	public VehiclePicturesId getId() {
		return id;
	}

	public void setId(VehiclePicturesId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "VehiclePictures [id=" + id + "]";
	}
	
}
