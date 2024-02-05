package view;

import java.awt.CardLayout;

import javax.swing.JPanel;

public interface ScreenSwitch {
	
	void next(int id, boolean flag) ;
	void back();
	
	void nextCard(CardLayout cardLayout, JPanel pane);
	void prevCard(CardLayout cardLayout, JPanel pane);

}