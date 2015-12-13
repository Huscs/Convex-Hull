package Bruteforce;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import QuickHull.QuickHullAlgorithm;

public class Gui extends JFrame implements Observer
{
	
	private static final long serialVersionUID = 1L;
	long startTime;
	long endTime;


	private State state;
	
	private DrawArea drawArea;
	
	public Gui(State state)
	{
		this.state = state;
		
		state.addObserver(this);
		
		setTitle("Convex Hull Algorithm by Hussam & Abdulrahman");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(100, 100, screenSize.width, screenSize.height);
		
		Container frame = getContentPane();
		
		SpringLayout layout = new SpringLayout();
		frame.setLayout(layout);
		
		drawArea = new DrawArea();
		frame.add(drawArea);
		
		layout.putConstraint(SpringLayout.NORTH, drawArea, 5, SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST, drawArea, 5, SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.EAST, drawArea, -5, SpringLayout.EAST, frame);
		
		ButtonPanel bp = new ButtonPanel();
		frame.add(bp);
		
		layout.putConstraint(SpringLayout.SOUTH, drawArea, -5, SpringLayout.NORTH, bp);
		layout.putConstraint(SpringLayout.WEST, bp, 0, SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.EAST, bp, 0, SpringLayout.EAST, frame);
		layout.putConstraint(SpringLayout.SOUTH, bp, -5, SpringLayout.SOUTH, frame);
		
		setVisible(true);
	}
	
	private class DrawArea extends JPanel implements MouseListener, MouseMotionListener
	{
		
		private static final long serialVersionUID = 1L;

		private static final int pointRadius = 5;
		
		private boolean mouseActive;
		private int mouseX, mouseY;
		
		public DrawArea()
		{
			setBackground(Color.lightGray);
			
			mouseActive = false;
			mouseX = mouseY = 0;
			
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		@Override
		protected void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			for (Point p : state.getPoints())
			{
				g2.setColor(Color.GREEN);
				g2.fillOval(p.x - pointRadius, p.y - pointRadius, 2 * pointRadius, 2 * pointRadius);
			}
			
			Polygon hull = new Polygon();
			
			for (Point p : state.getConvexHull())
			{
				hull.addPoint(p.x, p.y);
			}
			
			if (hull.npoints > 0)
			{
				g2.setColor(Color.RED);
				g2.drawPolygon(hull);
			}
			
			if (mouseActive)
			{
				g2.setColor(Color.GRAY);
				g2.fillOval(mouseX - pointRadius, mouseY - pointRadius, 2 * pointRadius, 2 * pointRadius);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) 
		{
			state.addPoint(e.getX(), e.getY());
		}

		@Override
		public void mouseMoved(MouseEvent e) 
		{
			mouseX = e.getX();
			mouseY = e.getY();
			
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) 
		{ 
			mouseActive = true; 
		}

		@Override
		public void mouseExited(MouseEvent e) 
		{ 
			mouseActive = false; 
			repaint(); 
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseDragged(MouseEvent e) {}
	}
	
	private class ButtonPanel extends JPanel
	{
		
		private static final long serialVersionUID = 1L;
		

		public ButtonPanel()
		{
			setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			final Component frame = null;
			
			JButton hull = new JButton("Brute force");
			add(hull);
			
			hull.addActionListener(new ActionListener() {
				
			public void actionPerformed(ActionEvent arg0) {
				
					if(!(state.getPoints().isEmpty()))
					{
						startTime = System.nanoTime();
						state.computeConvexHull(new ConvexHullAlgorithm());
						endTime = System.nanoTime();
						
						JOptionPane.showMessageDialog(frame, "It takes " + 
						(endTime - startTime) + " ns to run brute force techniqe");
						
					
					}
					else {
						
						JOptionPane.showMessageDialog(frame, "There are no points to calculate");
					
					}
						
				}
			});
			
			JButton quickHullBtn = new JButton("Quick Hull");
			add(quickHullBtn);
			
			quickHullBtn.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {				
					if(!(state.getPoints().isEmpty())){
						long dcAlgStart = System.nanoTime();
						state.applyQuickHull(new QuickHullAlgorithm());
						long dcAlgEnd = System.nanoTime();
						JOptionPane.showMessageDialog(frame, "It takes " + 
						(dcAlgEnd - dcAlgStart) + " ns to run Quick Hull techniqe");
						try (BufferedWriter pointsFileBufWriter = new BufferedWriter(new FileWriter("DCProfiling.txt", true));){	
							pointsFileBufWriter.write("Profiling for Convex Hull by Divide and Conquer:");
							pointsFileBufWriter.newLine();
							
							pointsFileBufWriter.write("\t\t\tSet Size: " + String.valueOf(state.getPoints().size()));
							pointsFileBufWriter.newLine();
							
							
							pointsFileBufWriter.write("\t\t\tSet Elements: " + setToString(state.getPoints()));
							pointsFileBufWriter.newLine();
							
							pointsFileBufWriter.write("\t\t\tTime Taken: " + String.valueOf(dcAlgEnd - dcAlgStart) + "ns");
							pointsFileBufWriter.newLine();
							pointsFileBufWriter.newLine();
							pointsFileBufWriter.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}					
					else {
						Component frame = null;
						JOptionPane.showMessageDialog(frame, "There are no points to calculate");
					
					}
						
				}
			});
			
			JButton clear = new JButton("Clear");
			add(clear);
			
			clear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!(state.getPoints().isEmpty()))
						state.removeAllPoints();
						
						else {
							Component frame = null;
							JOptionPane.showMessageDialog(frame, "There are no points to clear");
						
						}
					
				}
			});
			
			String randomSelStr[] = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
			final JComboBox<String> randmomSelComboBox = new JComboBox<>(randomSelStr);
			add(randmomSelComboBox);
			
			JButton loadRandomBtn = new JButton("Load Random Data");
			add(loadRandomBtn);
			loadRandomBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(!(state.getPoints().isEmpty()))
						state.removeAllPoints();
					loadRandomDataFile((String) randmomSelComboBox.getSelectedItem());
				}
			});
			
			JButton exit = new JButton("Exit");
			add(exit);
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Component frame = null;
					JOptionPane.showMessageDialog(frame, "Thanks for using our application");
					state.closeWindow();
				}
			});
		}
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		drawArea.repaint();
	}
	
	private void loadRandomDataFile(String countStr){
		String path = Paths.get("").toAbsolutePath().toString();
		try (BufferedReader pointsBufReader = new BufferedReader(new FileReader(path + "/src/" + countStr + "Points.txt"));){
			String lineRead = null;
			lineRead = pointsBufReader.readLine();
			while (lineRead != null){
				String x, y;
				if(!lineRead.equals("")){
					int indexOfSpace = lineRead.indexOf(" ");
					x = lineRead.substring(0, indexOfSpace);
					y = lineRead.substring(indexOfSpace + 1);
					state.addPoint(Integer.parseInt(x), Integer.parseInt(y));
					lineRead = pointsBufReader.readLine();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		drawArea.repaint();
	}
	
	private String setToString(ArrayList<Point> set){
		String setValues = "";
		if (set.size() > 0){
			setValues = "{";
			for (int i = 0 ; i< set.size(); i++){
				setValues += "(" + set.get(i).x + "," + set.get(i).y + ")  ";
			}
			setValues += "}";
		}
		return setValues; 
	}
	
	public static void main(String[] args) 
	{
		State NewState = new State();
		
		new Gui(NewState);
	}
}
