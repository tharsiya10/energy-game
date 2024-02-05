package energy;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.GameController;
import model.Component;
import model.Level;
import model.LevelConfig;
import model.PlayableLevel;
import model.Tile;
import model.TileShape;
import view.LevelSelectionPane;
import view.LevelView;
import view.ScreenSwitch;
import view.TileView;

public class App extends JFrame implements ScreenSwitch {

	private JPanel currentScreen = new LevelSelectionPane(this);
	public static final String INSTALL_DIR = System.getProperty("user.home") + "/energy";
	public static final String playableLevelsPath = App.INSTALL_DIR + "/" + LevelConfig.PLAYABLE_LEVEL_PATH_PREFIX;

	public App() {
		super("Energy");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.back();
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void next(int id, boolean flag) {
		// TODO Auto-generated method stub
		String path = App.playableLevelsPath + "/" + LevelConfig.LEVEL_FILE_BASE_NAME + id + LevelConfig.FILE_FORMAT;
		
		LevelConfig lc = LevelConfig.extractFromFile(path);
		if(lc == null) {
			this.back();
			return;
		}
		
		LevelView lview = new LevelView(id, this);
		PlayableLevel model = Level.extractFromConfig(lc);
		model.addObserver(lview);
		GameController gc = new GameController(model);
		lview.getCircuitView().addMouseListener(gc);
		this.changeScreen(lview);
		
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		changeScreen(new LevelSelectionPane(this));
	}
	
	public void changeScreen(JPanel pane) {
		Container contentPane = this.getContentPane();
		contentPane.remove(currentScreen);
		currentScreen = pane;
		contentPane.add(currentScreen);
		this.revalidate();
		this.repaint();
	}
	
	private static void copyContent(Path src, Path dst) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(src)) {
            for (Path entry : stream) {
                Path entryCopy = Paths.get(
                        dst.toString() + "/" + entry.getFileName().toString()
                );
                Files.copy(entry,
                           entryCopy,
                           StandardCopyOption.COPY_ATTRIBUTES
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Path nrgpath = Paths.get(App.INSTALL_DIR);
		
		if(Files.notExists(nrgpath, LinkOption.NOFOLLOW_LINKS)) {
			Path playPath = Paths.get(App.playableLevelsPath);
			try {
				Files.createDirectories(playPath);
				
				ClassLoader loader = App.class.getClassLoader();
				
				Path playSrc = new File(loader.getResource(LevelConfig.PLAYABLE_LEVEL_PATH_PREFIX + "/").toURI()).toPath();
				copyContent(playSrc, playPath);
				
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		SwingUtilities.invokeLater(App::new);
		
	}

	@Override
	public void nextCard(CardLayout cardLayout, JPanel pane) {
		// TODO Auto-generated method stub
		cardLayout.next(pane);
	}

	@Override
	public void prevCard(CardLayout cardLayout, JPanel pane) {
		// TODO Auto-generated method stub
		cardLayout.previous(pane);
	}

}