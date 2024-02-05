package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Position {
	
	private final int i;
	private final int j;
	
	private Position() {
		this.i = 0;
		this.j = 0;
	}
	
	public Position(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public static Position origin() {
		return new Position();
	}
	
	public int getNbLine() {
		return i;
	}
	
	public int getNbColumn() {
		return j;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(this.getClass() != other.getClass()) return false;
		Position pos = (Position) other;
		return this.i == pos.i && this.j == pos.j;
	}
	
	public static List<Position> voisinPositions(Position pos, TileShape shape){
		List<Position> res = new ArrayList<>();
		int[] is = new int[shape.sides()];
		int[] js = new int[shape.sides()];
		int i = pos.getNbLine();
		int j = pos.getNbColumn();
		if(shape == TileShape.SQUARE) {
			is[0] = i-1; /*TOP*/ js[0] = j;
			is[1] = i; // right
            js[1] = j+1;
            is[2] = i+1; // bottom
            js[2] = j;
            is[3] = i; // left
            js[3] = j-1;
        } else if (shape == TileShape.HEXAGON) {
                is[0] = i-1; // top
                js[0] = j;
                is[3] = i+1; // bottom
                js[3] = j;
            if (j % 2 == 1) {
                is[1] = i; // right 1
                js[1] = j+1;
                is[2] = i+1; // right 2
                js[2] = j+1;
                is[4] = i+1; // left 2
                js[4] = j-1;
                is[5] = i; // left 1
                js[5] = j-1;
            } else {
                is[1] = i-1; // right 1
                js[1] = j+1;
                is[2] = i; // right 2
                js[2] = j+1;
                is[4] = i; // left 2
                js[4] = j-1;
                is[5] = i-1; // left 1
                js[5] = j-1;                
            }
        } 
        else {
        	return res;
        }
		for (int k = 0; k < shape.sides(); k++) {
            if (is[k] >= 0 && js[k] >= 0) {
                res.add(new Position(is[k], js[k]));
            }
        }
        return res;
	}
	
	// Returns true if the given Position is a neighbor of this Position
    // according to shape
    public boolean isNeighbor(Position pos, TileShape shape) {
        return touchingSide(pos, shape) != -1;
    }

    // Returns the index of the side at which this Position and the given
    // Position touches according to shape. If they don't touch, returns -1
    public int touchingSide(Position pos, TileShape shape) {
        Objects.requireNonNull(pos);
        Objects.requireNonNull(shape);
        if (shape == TileShape.SQUARE) {
            if (pos.i == i-1 && pos.j == j) return 0;
            if (pos.i == i && pos.j == j+1) return 1;
            if (pos.i == i+1 && pos.j == j) return 2;
            if (pos.i == i && pos.j == j-1) return 3;
            return -1;
        } else if (shape == TileShape.HEXAGON) {
            if (pos.i == i-1 && pos.j == j) return 0;
            if (pos.i == i+1 && pos.j == j) return 3;

            if (j % 2 == 1) {
                if (pos.i == i && pos.j == j+1) return 1;
                if (pos.i == i+1 && pos.j == j+1) return 2;
                if (pos.i == i+1 && pos.j == j-1) return 4;
                if (pos.i == i && pos.j == j-1) return 5;
            } else {
                if (pos.i == i-1 && pos.j == j+1) return 1;
                if (pos.i == i && pos.j == j+1) return 2;
                if (pos.i == i && pos.j == j-1) return 4;
                if (pos.i == i-1 && pos.j == j-1) return 5;
            }
            return -1;
        } else {
            return -1;
        }
    }
	

}