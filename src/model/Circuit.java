package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Circuit {

	private final ArrayList<Tile> tiles;
	private int lines = 0;
	private int columns = 0;
	
	public Circuit() {
		this.tiles = new ArrayList<Tile>();
	}
	
	public Circuit(int lines, int columns) {
		this.lines = lines;
		this.columns = columns;
		this.tiles = new ArrayList<Tile>();
	}
	
	public static Circuit empty() {
		return new Circuit();
	}
	
	public ArrayList<Tile> getTiles(){
		return this.tiles;
	}
	
	public int getLines() {
		return lines;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public Tile getTileAt(Position position) {
		for(Tile tile : this.tiles) {
			if(tile.getPosition().equals(position)) {
				return tile;
			}
		}
		return null;
	}
	
	public boolean connectSideOfTileAt(Position position, int border) {
		Tile t = getTileAt(position);
		if(!this.tiles.contains(t)) return false;
		return t.connect(border);
		
	}
	
	public boolean disconnectSideOfTileAt(Position position, int border) {
		Tile t = getTileAt(position);
		if(!this.tiles.contains(t)) return false;
		return t.disconnect(border);
	}
	
	public boolean setTileComponent(Position position, Component c) {
		Tile t = getTileAt(position);
		if(!this.tiles.contains(t)) return false;
		t.setComponent(c);
		return true;
		
	}
	
	public boolean addTile(Tile t) {
		if(this.tiles.contains(t)) return false;
		return tiles.add(t);
	}
	
	public boolean removeTile(Position position) {
		Tile t = getTileAt(position);
		if(!this.tiles.contains(t) || (position.equals(Position.origin()))) {
			return false;
		}
		return tiles.remove(t);
	}
	
	public List<Tile> getSources(){
		List<Tile> res = new ArrayList<>();
		for(Tile tile : this.tiles) {
			if(tile.getComponent() == Component.SOURCE) {
				res.add(tile);
			}
		}
		return res;
	}
	
	public List<Tile> getWifi() {
        List<Tile> res = new ArrayList<>();
        for (Tile tile: this.tiles) {
            if (tile.getComponent() == Component.WIFI) {
                res.add(tile);
            }
        }
        return res;
    }
	
	public List<Tile> getVoisins(Tile tile){
		if(!tiles.contains(tile)) { System.out.println("TILE NOT IN CIRCUIT"); return null ;} // throw exception
		List<Tile> res = new ArrayList<>();
		List<Position> voisinPos = Position.voisinPositions(tile.getPosition(), tile.getShape());
		
		List<Tile> voisins = new ArrayList<>();
		for(Position position : voisinPos) {
			Tile t = this.getTileAt(position);
			if(t != null && !voisins.contains(t)) {
				voisins.add(t);
			}
		}
		
		for(Tile t: voisins) {
			if(tile.isLinkedTo(t)) {
				res.add(t);
			}
		}
		
		if(tile.getComponent() == Component.WIFI) {
			List<Tile> bornes = this.getWifi();
			for(Tile borne : bornes) {
				if(!res.contains(borne)) {
					res.add(borne);
				}
			}
			res.remove(tile);
		}
		return res;
	}
	
	public void blackout() {
        for (Tile tile: this.tiles) {
            tile.setPower(false);
        }
    }
	
	public void propagateElectricity() {
		Deque<Tile> toExplore = new ArrayDeque<>(this.getSources());
		while(!toExplore.isEmpty()) {
			Tile curr = toExplore.pop();
			curr.setPower(true);
			List<Tile> voisins = this.getVoisins(curr); 
			
			for(Tile tile : voisins) {
				if(!tile.getIsPowered() && !toExplore.contains(tile)) {
					toExplore.push(tile);
				}
			}
		}
	}
	
	public boolean rotateTileAt(Position position) {
		for(Tile tile: this.tiles) {
			if(tile.getPosition().equals(position) && tile.canRotate()) {
				tile.rotate();
				return true;
			}
		}
		return false;
	}
	
	public boolean containsLamp() {
		for(Tile tile : tiles) {
			if(tile.getComponent() == Component.LAMP) return true;
		}
		return false;
	}
	
	public boolean allLampsAreLit() {
		if(tiles.isEmpty()) return false;
		for(Tile tile : tiles) {
			if(tile.getComponent() == Component.LAMP && !tile.getIsPowered()) {
				return false;
			}
		}
		return true;
	}
	
	public void randomRotation() {
		Random random = new Random();
		for(Tile tile : tiles) {
			if(tile.canRotate()) {
				int m = tile.sides();
				int nbRotations = 0;
				nbRotations = random.nextInt(m);
				for(int i = 0; i < nbRotations; i++) {
					tile.rotate();
				}
			}
		}
	}
	
	public boolean areShapedTiles(TileShape shape) {
		for(Tile tile : tiles) {
			if(tile.getShape() != shape) {
				return false;
			}
		}
		return true;
	}
	
	public boolean areSquaredTiles() {
		return areShapedTiles(TileShape.SQUARE);
	}
	
	public boolean areHexagonTiles() {
		return areShapedTiles(TileShape.HEXAGON);
	}
	
}