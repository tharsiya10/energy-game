package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;

import energy.App;
import model.LevelConfig;

public class LevelSelectionPane extends JPanel {
	
	private ScreenSwitch switcher;
	private JPanel levelButtonsPanel = new JPanel();
	private List<String> levelNames;
	
	private RoundButton prev = new RoundButton("<");
	private RoundButton next = new RoundButton(">");
	
	
	private JPanel cards;
	private JPanel control;
	
	private static final int LEVEL_SELECTION_PANE_WIDTH = 700;
    private static final int LEVEL_SELECTION_PANE_HEIGHT = 700;
	 
	public LevelSelectionPane(ScreenSwitch switcher) {
		this.switcher = switcher;
		this.setBackground(new Color(25,25,25));
		cards = new JPanel(new CardLayout());
//		ActionListener a1 = createLoadButtonActionListener(true, this); 
		
		levelNames = new ArrayList<>();
		this.loadPlayableLevelNames();
		
		levelButtonsPanel.setLayout(new CardLayout());
		Dimension levelButtonsPanelDim = new Dimension(LEVEL_SELECTION_PANE_WIDTH,(int)(0.9 * LEVEL_SELECTION_PANE_HEIGHT));
		levelButtonsPanel.setPreferredSize(levelButtonsPanelDim);
		
		cardUI();

		handleControlBtn();
		
		controlUI();
	
		this.setLayout(new BorderLayout());
		this.add(control, BorderLayout.PAGE_END);
		this.add(levelButtonsPanel, BorderLayout.CENTER);
	}
	
	private void cardUI() {
		int i = 0;
		int len = levelNames.size();
		JPanel p = new JPanel();
		p.setBackground(getBackground());
		p.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = GridBagConstraints.REMAINDER;
		g.anchor = GridBagConstraints.CENTER;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.insets = new Insets(5,5,5,5);
		for(String name : levelNames) {
			RoundButton btn = createLevelButton(name);
			levelBtnUI(btn);
			
			
			p.add(btn, g);
			
			i++;
			if(i % 3 == 0 || i == len) {
				this.levelButtonsPanel.add(p, "Niveaux ");
				
				p = new JPanel();
				p.setBackground(getBackground());
				p.setLayout(new GridBagLayout());
			}
			
			
		}
	}
	
	private void controlUI() {
		control = new JPanel();
		
		controlBtnUI(prev);
		controlBtnUI(next);
		
		control.setBackground(new Color(25,25,25));
		control.add(prev);
		control.add(next); 
		
	}
	
	private void controlBtnUI(RoundButton btn) {
		Dimension dim = new Dimension(40,40);
		btnUI(btn, dim, new Color(25,25,25), Color.white, new Color(25,25,25), new Color(25,25,25), Color.cyan, new Color(25,25,25));
	}
	
	private void levelBtnUI(RoundButton btn) {
		Dimension dim = new Dimension(300,100);
		btnUI(btn, dim, Color.white, Color.black, Color.black, Color.black, Color.white, Color.LIGHT_GRAY);
	}
	
	private void btnUI(RoundButton btn, Dimension dim, Color bg, Color fg, Color border, Color hoverBg, Color hoverFg, Color hoverBorder) {
		btn.setPreferredSize(dim);
		btn.setPreferredSize(dim);
		btn.setMaximumSize(dim);
		btn.setRadius(50);
		btn.setInitColor(bg, fg, border);
		btn.setHoverColor(hoverBg, hoverFg, hoverBorder);
	}
	
	private void handleControlBtn() {
		prev.addActionListener(
				e -> switcher.prevCard((CardLayout) levelButtonsPanel.getLayout(), levelButtonsPanel));
		
		next.addActionListener(
				e -> switcher.nextCard((CardLayout) levelButtonsPanel.getLayout(), levelButtonsPanel));
		
	}
	
	private RoundButton createLevelButton(String name) {
        RoundButton b = new RoundButton(name);
        String idAsString = name.substring(5, name.length() - 4);
        int id = Integer.parseInt(idAsString);
        b.addActionListener(
          e -> switcher.next(id, true)
        );
        return b;
    }
	
	private int extractIntFromStr(String s) {
		String nb = s.replaceAll("\\D","");
		return nb.isEmpty() ? 0 : Integer.parseInt(nb);
	}
	
	private List<String> loadFileNamesInDirectory(String dirName) {
		// System.out.println("LOAD FILE NAMES IN DIRECTORY");
        List<String> names = new ArrayList<>();
        Path dir;
        try {
            dir = Paths.get(App.INSTALL_DIR + "/" + dirName);
        } catch (Exception e) {
            return new ArrayList<>();
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                names.add(file.getFileName().toString());
            }
            // sort name by numerical order
            Collections.sort(names, new Comparator<String>() {
            	public int compare(String s1, String s2) {
            		return extractIntFromStr(s1) - extractIntFromStr(s2);
            	}
            });
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return names;
    }
	
	private void loadPlayableLevelNames() {
		
        this.levelNames = this.loadFileNamesInDirectory(
            LevelConfig.PLAYABLE_LEVEL_PATH_PREFIX + "/"
        );
    }
	
}
