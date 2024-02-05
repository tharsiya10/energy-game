package model;

import java.io.File;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelConfig {
	
	public static final String PLAYABLE_LEVEL_PATH_PREFIX = "playable";
	public static final String LEVEL_FILE_BASE_NAME = "level";
	public static final String FILE_FORMAT = ".nrg";
	
	private int id;
	
	private int height;
	private int width;
	
	private TileShape shape;
	private ArrayList<Tile> tiles;
	
	private LevelConfig() {
		this.tiles = new ArrayList<>();
	}
	
	private LevelConfig(int id, int height, int width, TileShape shape, ArrayList<Tile> tiles) {
		this.id = id;
		this.height = height;
		this.width = width;
		this.tiles = tiles;
		
		
	}
	
	public int getId() {
		return id;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public TileShape getShape() {
		return shape;
	}
	
	public ArrayList<Tile> getTiles(){
		return tiles;
	}
	
	public static LevelConfig extractFromFile(String filepath) {
		LevelConfig ret = new LevelConfig();
		ArrayList<String> lines = new ArrayList<String>();
		try {
			File file = new File(filepath);
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				lines.add(sc.nextLine());
				
			}
			sc.close();
		}
		catch(Exception e) {
			return null;
		}
		try {
			ret.id = Integer.parseInt(filepath.replaceAll("[^0-9]" , ""));
			
			for(int nb_line = 0; nb_line < lines.size(); nb_line ++) {
				String curr_str = lines.get(nb_line);
				String[] tokens = curr_str.split(" ");
				
				if(nb_line == 0) {
					ret.height = Integer.parseInt(tokens[0]);
					ret.width = Integer.parseInt(tokens[1]);
					if(tokens[2].equals("S")) {
						ret.shape = TileShape.SQUARE;
					}
					if(tokens[2].equals("H")) {
						ret.shape = TileShape.HEXAGON;
					}
				}
				else {
					int column = 0;
					Component curr_comp = null;
					ArrayList<Integer> currentConnectedSides = new ArrayList<>();
					for(String token : tokens) {
						
						if(token.equals(".") || token.equals("L") || token.equals("S") || token.equals("W")) {
							if(curr_comp != null) {
								Tile t = new Tile(ret.shape, curr_comp, new Position(nb_line - 1, column) );
								for(int i : currentConnectedSides) {
									t.connect(i);
								}
								ret.tiles.add(t);
								column ++;
								curr_comp = Component.componentFromCode(token);
								currentConnectedSides = new ArrayList<>();
							}
							else {
								curr_comp = Component.componentFromCode(token);
							}
						}
						else {
							currentConnectedSides.add(Integer.valueOf(token));
						}
					}
					Tile t = new Tile(ret.shape, curr_comp, new Position(nb_line - 1, column));
					System.out.println(t);
					for(int i : currentConnectedSides) {
						t.connect(i);
						
					}
					ret.tiles.add(t);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(); 
			System.exit(1);
		}

		return ret;
	}

}