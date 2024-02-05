package model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import view.Observer;

public class Level implements PlayableLevel {
	
	private final int id;
	
	private final Circuit circuit;
	
	private List<Observer> observers = new ArrayList<>();
	
	private Level(int id, Circuit circuit) {
		this.id = id;
		this.circuit = circuit;
	}
	
	public static Level extractFromConfig(LevelConfig lc) {
		int height = lc.getHeight();
		int width = lc.getWidth();
		Circuit c = new Circuit(height, width);
		for(Tile t : lc.getTiles()) {
			c.addTile(t);
		}
		if(lc.getShape() == TileShape.HEXAGON) {
			Dimension dim = new Dimension(height, width);
			for(int j = 1; j < width; j +=2) {
				c.removeTile(new Position(height - 1, j));
			}
		}
		return new Level(lc.getId(), c);
	}
	
	public int getId() {
		return id;
	}
	
	public Circuit getCircuit() {
		return circuit;
	}
	 
	@Override
	public boolean rotateTileAt(Position position) {
		return circuit.rotateTileAt(position);
	}
	
	public boolean endGame() {
		return this.circuit.allLampsAreLit();
	}
	
	@Override
	public void randomRotations() {
		circuit.randomRotation();
	}
	
	@Override
	public void blackout() {
        this.circuit.blackout();
    }
	
	@Override
	public void propagateElectricity() {
		circuit.propagateElectricity();
	}
	
	
	public boolean connectSideOfTileAt(Position p, int index) {
		return circuit.connectSideOfTileAt(p, index);
	}
	
	
	public boolean disconnectSideOfTileAt(Position p, int index) {
		return circuit.disconnectSideOfTileAt(p, index);
	}
	
	
	public boolean setTileComponentTo(Position p, Component c) {
		return circuit.setTileComponent(p, c);
	}
	
	
	public boolean circuitContainsLamp() {
		return circuit.containsLamp();
	}

	@Override
	public void notifyObservers() {		
		for(Observer obs : observers) {
			obs.update(this);
		}
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

}