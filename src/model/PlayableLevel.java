package model;

import view.Observable;

public interface PlayableLevel extends Observable {
	
	public boolean rotateTileAt(Position position);
	public void randomRotations();
	void propagateElectricity();
	void blackout();
}