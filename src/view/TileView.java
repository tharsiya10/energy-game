package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import model.Component;
import model.Tile;
import model.TileShape;

public class TileView {
	
	private final Tile model;
	
	private static Map<String, BufferedImage> loadedRes = null;
	
	private final String isPowered;
	
	private String componentFilename;
	private ArrayList<String> cableFilenames = new ArrayList<>();
	private ArrayList<Integer> angles = new ArrayList<>();
	
	private BufferedImage borderImage;
	private BufferedImage componentImage;
	private ArrayList<BufferedImage> cableImages = new ArrayList<>();
	
	// Info about shape dimension on initial image (120 * 120 for square and
    // 120*104 for hexagons)
	static final int SQUARE_IMAGE_SHAPE_WIDTH = 120;
	static final int SQUARE_IMAGE_SHAPE_HEIGHT = 120;
	static final int HEXAGON_IMAGE_SHAPE_WIDTH = 120;
	static final int HEXAGON_IMAGE_SHAPE_HEIGHT = 104;
	
	private static final String TILE_IMAGES_FOLDER = "tuiles_png/";
	private static final String SQUARE_IMAGES_FOLDER = "square/";
	private static final String HEXAGON_IMAGES_FOLDER = "hexagon/";
	
	public TileView(Tile t) {
		this.model = t;
		
		if(loadedRes == null) {
			try {
				TileView.loadResources();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(model.getIsPowered()) {
			isPowered = "on";
		}
		else {
			isPowered = "off";
		}
		// init filenames attributes
		initFilenames();
	}
	
	public void initFilenames() {
		this.cableFilenames.clear();
		this.angles.clear();
		
		Component component = model.getComponent();
		TileShape shape = model.getShape();
		
		String shapeStr = shape.toString().toLowerCase();
		String componentStr = component.toString().toLowerCase();
		
		String shapeFolder;
		if(shape == TileShape.SQUARE) {
			shapeFolder = SQUARE_IMAGES_FOLDER;
		}
		else {
			shapeFolder = HEXAGON_IMAGES_FOLDER;
		}
		
		/* outline file : square_on / square_off / hexagon_on / hexagon_off */
		String outlineFile;
		outlineFile = shapeFolder + shapeStr + "_" + isPowered + ".png";
		
		/* component file name */
		if(component != Component.EMPTY && component != Component.SOURCE) {
			this.componentFilename = shapeFolder + shapeStr + "_" + componentStr + "_" + isPowered + ".png";
		}
		// inutile car Component.source toujours on il suffit de nommer le fichier que avec on
		else if(component == Component.SOURCE) {
			componentFilename = shapeFolder + shapeStr + "_" + componentStr + ".png";
		}
		
		/* tile has component or empty with one connected side */
		int nb_sides = model.sides();
		if(component != Component.EMPTY || model.connectedSides() == 1) {
			String cableFilename;
			if(model.getShape() == TileShape.SQUARE) {
				cableFilename = "square_link1_";
			}
			
			else {
				cableFilename = "hexagon_link1_";
			}
			
			for (int i = 0; i < nb_sides; i++) {
				if(model.connectedBorder(i)) {
					cableFilenames.add(shapeFolder + cableFilename + isPowered + ".png" );
					angles.add(i * (360 / nb_sides));
				}
			}
		}
		
		else {
			ArrayList<Integer> connected = new ArrayList<>();
			for (int dist = 1; dist < nb_sides; dist ++) {
				ArrayList<Integer> newConnected = new ArrayList<>();
				
				for (int i = 0; i < nb_sides; i++) {
					if(model.connectedBorder(i) && model.connectedBorder((i+dist) % nb_sides) && 
							(!connected.contains((i+dist)%nb_sides) || !connected.contains(i))) {
						newConnected.add(i);
						newConnected.add((i+dist) % nb_sides);
						
						if(model.getShape() == TileShape.SQUARE) {
							angles.add(i * (360 / nb_sides));			
							
							if(dist == 1 || dist == 3) {
								cableFilenames.add(shapeFolder + "square_link2_" + isPowered + ".png");
							}
							if(dist == 2) {
								cableFilenames.add(shapeFolder + "square_link3" + isPowered + ".png");
							}
						}
						
						else {
							angles.add(i * (360 / nb_sides));
							if(dist == 1 || dist == 5) {
								cableFilenames.add(shapeFolder + "hexagon_link2_" + isPowered + ".png");
							}
							if(dist == 2 || dist == 4) {
								cableFilenames.add(shapeFolder + "hexagon_link3_" + isPowered + ".png");
							}
							if(dist == 3) {
								cableFilenames.add(shapeFolder + "hexagon_link4_" + isPowered + ".png");
							}
							
						}
						
					}
					connected.addAll(newConnected);
				}
				
				
			}
			
		}
		this.borderImage = TileView.loadedRes.get(outlineFile);
		// if(this.borderImage == null) { System.out.println("borderImage is NULL"); }
		// else { System.out.println("borderImage IS NOT NULL"); }
		componentImage = TileView.loadedRes.get(this.componentFilename);
		for(String cableFilename : cableFilenames) {
			BufferedImage cableImage = TileView.loadedRes.get(cableFilename);
			cableImages.add(cableImage);
		}
		
		
	}
	
	public BufferedImage getBorderImage() {
		return borderImage;
	}
	
	public ArrayList<BufferedImage> getCableImages(){
		return this.cableImages;
	}
	
	public ArrayList<Integer> getAngles(){
		return this.angles;
	}
	
	public BufferedImage getComponentImage(){
		return this.componentImage;
	}
	
	public static void loadResources() throws URISyntaxException, IOException {
		loadedRes = new HashMap<>();
		
		URL folderSquareURL = TileView.class.getClassLoader().getResource(TILE_IMAGES_FOLDER + SQUARE_IMAGES_FOLDER);
		File folderSquareFiles = new File(folderSquareURL.toURI());
		String[] squareFileNames = folderSquareFiles.list();
		
		URL folderHexagonURL = TileView.class.getClassLoader().getResource(TILE_IMAGES_FOLDER + HEXAGON_IMAGES_FOLDER);
		File folderHexagonFiles = new File(folderHexagonURL.toURI());
		String[] hexagonFilenames = folderHexagonFiles.list();
		
		for(String s : squareFileNames) {
			TileView.loadResource(SQUARE_IMAGES_FOLDER + s);
			
		}
		for(String s : hexagonFilenames) {
			TileView.loadResource(HEXAGON_IMAGES_FOLDER + s);
		}
		
	}
	
	public static void loadResource(String filename) throws URISyntaxException, IOException {
		URL resource = TileView.class.getClassLoader().getResource(TILE_IMAGES_FOLDER + filename);
		File f = new File(resource.toURI());
		
		BufferedImage img = ImageIO.read(f);
		TileView.loadedRes.put(filename, img);
	}

}
