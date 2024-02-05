package controller;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import model.Position;
import view.CircuitView;

public abstract class LevelController extends MouseInputAdapter {
	
	public static Position getTileAtClick(CircuitView cv, MouseEvent e) {
		Point click = e.getPoint();
		Point[][] pts = cv.getPoints();
		
		int min_dist = Integer.MAX_VALUE;
		int l = Integer.MAX_VALUE;
		int c = Integer.MAX_VALUE;
		
		for(int i = 0; i < pts.length; i++) {
			for(int j = 0; j < pts[i].length; j++) {
				Point center = pts[i][j];
				if(center != null) {
					int dist = (int) center.distance(click);
					if(dist < min_dist) {
						min_dist = dist;
						l = i;
						c = j;
					}
				}
			}
		}
		if(min_dist > cv.getShapeSideLength()) return null;
		
		return new Position(l, c);
	}
}