package model;

import java.util.Objects;

public class Tile {
	
	private final TileShape shape;
	private Component component;
	private final Position position;
	private boolean[] border;
	private double angle;
	private boolean isPowered;
	
	public Tile(TileShape shape, Component component, Position position) {
		this.shape = shape;
		this.component = component;
		this.position = position;
		
		this.border = new boolean[shape.sides()];
		this.isPowered = (this.component == Component.SOURCE);
	}
	
	public int connectedSides() {
		int ret = 0;
		for(boolean b : border) {
			if(b) {
				ret ++;
			}
		}
		return ret;
	}
	
	public String toString() {
		return "Tile : "+shape.toString() + " " + component.toString() + " " + position.toString();
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean getIsPowered() {
		return isPowered;
	}
	
	public Component getComponent() {
		return this.component;
	}
	
	public TileShape getShape() {
		return this.shape;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	
	public int sides() {
		return shape.sides();
	}
	
	public boolean connectedBorder(int b) {
		// handle exception
		return border[b];
	}
	
	public boolean connectOrDisconnectAt(boolean connect, int i) {
		boolean prev = this.border[i];
		this.border[i] = connect;
		return (this.border[i] != prev);
	}
	
	public boolean connect(int i) {
		return connectOrDisconnectAt(true, i);
	}
	
	public void setComponent(Component c) {
		this.component = c;
	}
	
	public void setPower(boolean b) {
		this.isPowered = b;
	}
	
	public boolean disconnect(int i) {
		return connectOrDisconnectAt(false, i);
	}
	
	private boolean isConnectedTo(Tile tile, int i) {
        int len = border.length;
        return this.border[i] && tile.border[(i + len / 2) % len];
    }    
	
	public boolean isLinkedTo(Tile tile) {
		Objects.requireNonNull(tile);
		int ts = this.position.touchingSide(tile.getPosition(), this.shape);
		return this.position.isNeighbor(tile.position, this.shape) && this.isConnectedTo(tile, ts);
	}
	
	public boolean canRotate() {
		if(this.getComponent() == Component.SOURCE) return false;
		return true;
	}
	
	private void rotateBorder() {
		boolean tmp = border[border.length - 1];
		for(int i = border.length - 1; i > 0; i--) {
			border[i] = border[i-1];
		}
		border[0] = tmp;
	}
	
	public void rotate() {
		if(!canRotate()) return;
		rotateBorder();
		angle = (angle + (360 / sides()) ) % 360;
		
		
	}
}