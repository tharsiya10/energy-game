package model;

public enum TileShape {
	
	SQUARE(4, "S"),
	HEXAGON(6, "H");
	
	private final int sides;
	private final String shapeCode;
	
	TileShape(int sides, String shapeCode) {
		this.sides = sides;
		this.shapeCode = shapeCode;
	}
	
	public int sides() {
		return sides;
	}
	
	public String shapeCode() {
		return this.shapeCode;
	}
}