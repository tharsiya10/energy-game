package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class RoundButton extends JButton implements MouseListener {
	
	private Color background, foreground, border;
	private Color hoverBackground, hoverForeground, hoverBorder;
	private int radius;
	private boolean hover = false;
	private String label;
	
	public RoundButton(String label) {
		super(label);
		this.label = label;
		setBorder(null);
		setContentAreaFilled(false);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setFont(new Font("Arial", Font.PLAIN, 18));
		this.addMouseListener(this);

	}
	
	public void setInitColor(Color background, Color foreground, Color border) {
		this.background = background;
		this.foreground = foreground;
		this.border = border;
		setInitColor();
	}
	
	public void setHoverColor(Color hoverbg, Color hoverfg, Color hoverborder){
		this.hoverBackground = hoverbg;
		this.hoverForeground = hoverfg;
		this.hoverBorder = hoverborder;
	}
	

	public void setInitColor() {
		
		this.setBackground(background);
		this.setForeground(foreground);
		this.setBorderColor(border);
	}
	
	public void setHoverColor() {

		this.setBackground(hoverBackground);
		this.setForeground(hoverForeground); 
		this.setBorderColor(hoverBorder);
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
	}

	public void setBorderColor(Color b) {
		if(!hover) this.border = b;
		else this.hoverBorder = b;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
				
		if(!hover && border != null) {
			g.setColor(border);
		}
		if(hover && hoverBorder != null) {
			g.setColor(hoverBorder);
		}

		g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
		if(!hover && background != null) {
			g.setColor(background);
		}
		if(hover && hoverBackground != null) {
			g.setColor(hoverBackground);
		}
		g.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, radius, radius);

		Font font = new Font(Font.SERIF, Font.PLAIN,  18);
		// Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = 0 + (getWidth() - metrics.stringWidth(label)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y =((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    if(!hover && foreground != null) {
	    	g.setColor(foreground);
	    }
	    if(hover && hoverForeground != null) {
	    	g.setColor(hoverForeground);
	    }
		g.drawString(label, x, y);
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		hover = true;
		
		this.setHoverColor();
		
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		hover = false;
		
		this.setInitColor();
		
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	
}