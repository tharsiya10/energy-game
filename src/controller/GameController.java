package controller;

import java.awt.event.MouseEvent;


import model.PlayableLevel;
import model.Position;
import view.CircuitView;

public class GameController extends LevelController {
	
	private PlayableLevel model;
	
	public GameController(PlayableLevel model) {
		this.model = model;
		this.model.randomRotations();
		this.updateModel();
	}
	
	public void updateModel() {
		model.blackout();
		model.propagateElectricity();
		model.notifyObservers();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Object src = e.getSource();
		if(!(src instanceof CircuitView)) return;
		Position tile_pos = LevelController.getTileAtClick((CircuitView) src, e);
		System.out.println("i : "+ tile_pos.getNbLine() + " j :" + tile_pos.getNbColumn());
		if(model.rotateTileAt(tile_pos)) {
			System.out.println("model rotateTile");
			this.updateModel();
		}
	}

}
