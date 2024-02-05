package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Circuit;
import model.Tile;

public class CircuitView extends JPanel {
	
	private Circuit model;
	private Point[][] centerPts; // center of the tiles
	
	public CircuitView() {
		this.model = null;
		this.centerPts = null;
		this.setLayout(null);
		this.setBackground(Color.black);
	}
	
	public void setModel(Circuit model) {
		this.model = model;
	}
	
	public Circuit getModel() {
		return this.model;
	}
	
	public double getShapeSideLength() {
		int height = model.getLines();
		int width = model.getColumns();
		Dimension cirDim = new Dimension(height, width);
		
		if(model.areSquaredTiles()) {
			return Math.min(this.getSize().width / width, this.getSize().height / height);
		}
		else if(model.areHexagonTiles()) {
			if(this.getSize().width / width < this.getSize().height / height) {
				double halfW = width / 2.0;
				return (this.getSize().width) / (Math.floor(halfW) + 2.0 * Math.ceil(halfW));
			}
			else {
				return this.getSize().height / height / Math.sqrt(3.0);
			}
		}
		return 0;
	}
	
	private void loadShapeCenters() {
		int height = model.getLines();
		int width = model.getColumns();
		Dimension cirDim = new Dimension(height, width);
		
		centerPts = new Point[height][width];
		
		int shapeSideLength = (int) this.getShapeSideLength();
		int x1; int y1;
		for(int i = 0; i < centerPts.length; i++) {
			for(int j = 0; j < centerPts[i].length; j++) {
				if(model.areSquaredTiles()) {
					x1 = (int) shapeSideLength / 2;
					y1 = (int) shapeSideLength / 2;
					centerPts[i][j] = new Point(x1 + (j * shapeSideLength), y1 + (i * shapeSideLength));
				}
				else if(model.areHexagonTiles()){
					int horizDist = (int) (3.0 / 2.0 * shapeSideLength);
					int vertDist = (int) (Math.sqrt(3.0) * shapeSideLength);
					
					x1 = shapeSideLength;
					y1 = vertDist / 2;
					if(j%2 == 0) {
						centerPts[i][j] = new Point(x1 + (j * horizDist), y1 + (i * vertDist));
					}
					else {
						if(i == centerPts.length - 1) { /* skip the unwanted hexagon tile */
							continue ;
						}
						centerPts[i][j] = new Point(x1 + (j * horizDist), 2 * y1 + (i * vertDist));
					}
				}
 			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		loadShapeCenters();
		Graphics2D g2d = (Graphics2D) g;
		int sideLength = (int) getShapeSideLength();
		int tileH = 0, tileW = 0, posX = 0, posY = 0;
		ArrayList<Tile> tiles = model.getTiles();
		
		boolean square = model.areSquaredTiles();
		for(Tile t : tiles) {
			
			int line = t.getPosition().getNbLine();
			int column = t.getPosition().getNbColumn();
			Point point = centerPts[line][column];
			TileView tview = new TileView(t);
			if(point != null) {
				if(square) {
					tileW = sideLength;
					tileH = sideLength;
					posX = (int) (point.getX() - tileW / 2);
					posY = (int) (point.getY() - tileH / 2);
				}
				else {
					tileW = (int) (2*sideLength);
			 		tileH = (int) (2*sideLength);  /*BufferedImage height*/
		 			posX = (int) (point.getX() - tileW / 2);
	 				posY = (int) (point.getY()
                                  - (TileView.HEXAGON_IMAGE_SHAPE_HEIGHT * tileH
                                     / TileView.HEXAGON_IMAGE_SHAPE_WIDTH) / 2);
				}
				
				Image tmp = tview.getBorderImage().getScaledInstance(tileW, tileH, Image.SCALE_SMOOTH);
				g2d.drawImage(tmp, posX, posY, null);
				int i = 0;
				ArrayList<BufferedImage> cableImages = tview.getCableImages();
				for(BufferedImage img : cableImages) {
					tmp = img.getScaledInstance(tileW, tileH, Image.SCALE_SMOOTH);
					BufferedImage dimg = new BufferedImage(tileW, tileH, BufferedImage.TYPE_INT_ARGB);
					Graphics2D gBF = dimg.createGraphics();
					gBF.drawImage(tmp, 0, 0, null);
					gBF.dispose();
					
					/*rotate*/
					BufferedImage rotatedImage = new BufferedImage(tileW, tileH, dimg.getType());
					double angle = Math.toRadians(tview.getAngles().get(i));
					AffineTransform at = new AffineTransform();
					if(model.areSquaredTiles()) {
						at.rotate(angle, tileW / 2.0, tileH/2.0);
					}
					else {
						int h = TileView.HEXAGON_IMAGE_SHAPE_HEIGHT * tileH / TileView.HEXAGON_IMAGE_SHAPE_WIDTH;
						at.rotate(angle, tileW / 2.0, h / 2.0);
					}
					Graphics2D gIm = rotatedImage.createGraphics();
					gIm.setTransform(at);
					gIm.drawImage(dimg, 0, 0, null);
					gIm.dispose();
					g2d.drawImage(rotatedImage, posX, posY, null);
					i++;
				}
				/*draw component*/
				BufferedImage componentImage = tview.getComponentImage();
				if(componentImage != null) {
					tmp = componentImage.getScaledInstance(tileW, tileH, Image.SCALE_SMOOTH);
					g2d.drawImage(tmp, posX, posY, null);
				}
			}
		}
	}
	
	public Point[][] getPoints() {
        int h = model.getLines();
        int w = model.getColumns();
        Point[][] cpy = new Point[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (this.centerPts[i][j] != null)
                    cpy[i][j] = this.centerPts[i][j].getLocation();
            }
        }
        return cpy;
    }

}
