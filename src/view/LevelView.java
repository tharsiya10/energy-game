package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Level;

public class LevelView extends JPanel implements Observer {
	
	private Level model;
	private CircuitView circuitView;
	private ScreenSwitch switcher;
	
	public LevelView(int id, ScreenSwitch switcher) {
		model = null;
		circuitView = new CircuitView();
		this.switcher = switcher;
		setBackground(Color.RED);
		this.setLayout(new BorderLayout());
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JLabel levelIDLabel = new JLabel("#" + id);
		levelIDLabel.setForeground(Color.white);

		RoundButton backButton = new RoundButton("back");
		backBtnUI(backButton);
		backButton.addActionListener(e -> {
			this.switcher.back();
		});
		p1.setBackground(Color.black);
		p2.setBackground(Color.black);
		p1.add(levelIDLabel);
		p2.add(backButton);
		this.add(p1, BorderLayout.PAGE_START);
		this.add(p2, BorderLayout.PAGE_END);
		this.add(circuitView, BorderLayout.CENTER);
	}
	
	private void backBtnUI(RoundButton btn) {
		btn.setInitColor(Color.black, Color.white, Color.BLACK);
		btn.setHoverColor(Color.black, Color.cyan, Color.black);
	}

	@Override
	public void update(Observable obs) {
		if(obs instanceof Level) {
			this.model = (Level) obs;
			circuitView.setModel(model.getCircuit());
			circuitView.repaint();
			displayEndGameDialog();
		}
		
	}
	
	public void displayEndGameDialog() {
		if(model.endGame()) {
			if(askNextGameConfirmation()) {
				switcher.next(model.getId() + 1, true);
			}
			else {
				switcher.back();
			}
		}
	}
	
	private boolean askConfirmation(String msg, String title) {
		int res = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
		return res == JOptionPane.YES_OPTION;
	}
	
	private boolean askNextGameConfirmation() {
        return this.askConfirmation("Play next ?", "You won !");
    }
	
	public CircuitView getCircuitView() {
		return this.circuitView;
	}

}