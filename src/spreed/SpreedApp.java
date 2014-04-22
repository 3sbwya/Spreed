
package spreed;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JSlider;
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * SpreedApp.java
 * Implements a basic java version of the recently-popular speed reading 
 * application which employs Rapid Serial Visual Presentation (RSVP).
 *  
 * @author Matthew Greene <mgreen85@uncc.edu>
 *
 */
public class SpreedApp {

	private JFrame frame;
	private JSlider slider;
	private JLabel outputLabel, btnStart;
	private JComboBox comboBox;
	private WordSource token;
	private Timer timer;
	private static String filename;
	private String word;
	private String alignedWord;
	private int position, centerPoint, speed;
	private final int length = 15;
	private boolean isRunning = false;
	private JLabel btnReset;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		filename = args[0];
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpreedApp window = new SpreedApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Private method which takes in the aligned word and colors the pivot
	 * letter red.
	 * 
	 * @param alignedWord
	 * @return
	 */
	private String colorPivot(String alignedWord){
		centerPoint = SpreedWord.getCenter(alignedWord);
		String pivot = "<font color='red'>"+alignedWord.charAt(centerPoint)+"</font>";
		String left = alignedWord.substring(0, centerPoint);
		String right = alignedWord.substring(centerPoint+1, alignedWord.length());
		return left+pivot+right;
	}	
	
	/**
	 * Sets the speed of the wpm based on the selection of the comboBox.
	 */
	private void setSpeed(){
		String item = (String)comboBox.getSelectedItem();
		float itemSelected = Float.parseFloat(item);
		speed = token.wpm(itemSelected);
	}

	/**
	 * Create the application.
	 * @throws FileNotFoundException 
	 */
	public SpreedApp() throws FileNotFoundException {
		token = new WordSource(filename);
		initialize();
		position = slider.getValue();
		
		//Create timer.
		timer = new Timer(500, new ActionListener(){
			/**
			 * Prints the aligned word with the pivot letter colored red and sets delay based on 
			 * next word length. If position equals index of last word, stop timer.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				position = slider.getValue();
				if(position == token.count()-1){
					timer.stop();
					isRunning = false;
					btnStart.setIcon(new ImageIcon(SpreedApp.class.getResource("/spreed/play.png")));
				}else{
					word = SpreedWord.getAlignedWord(token.getWord(position), length);
					if(position+1 != token.count()-1){
						timer.setDelay(speed * SpreedWord.getPauseLength(token.getWord(position+2)));
					}
					alignedWord =colorPivot(word);
					outputLabel.setText("<html><pre><font face=\"Consolas\">" + alignedWord + "</font></pre></html>");
					position++;
					slider.setValue(position);
				}
			}
			
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 203);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBackground(Color.WHITE);
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		layeredPane.setBounds(10, 11, 414, 119);
		frame.getContentPane().add(layeredPane);
		
		outputLabel = new JLabel("");
		outputLabel.setFont(new Font("Consolas", Font.BOLD, 36));
		outputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		outputLabel.setBounds(0, 23, 414, 71);
		layeredPane.add(outputLabel);
		
		JLabel lblNewLabel = new JLabel("|");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 414, 12);
		layeredPane.add(lblNewLabel);
		
		JLabel label = new JLabel("|");
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 105, 414, 14);
		layeredPane.add(label);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"250", "350", "500", "650", "800", "1000"}));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(362, 141, 62, 20);
		frame.getContentPane().add(comboBox);
		comboBox.addActionListener(new ActionListener(){
			/**
			 * Set speed based on comboBox selection. Calls setSpeed().
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				setSpeed();
			}
			
		});
		
		JLabel lblWpm = new JLabel("wpm:");
		lblWpm.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWpm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWpm.setBounds(320, 144, 39, 17);
		frame.getContentPane().add(lblWpm);
		
		slider = new JSlider();
		/**
		 * Prints the aligned, colored word based on slider location.
		 */
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				position = slider.getValue();
				String w = SpreedWord.getAlignedWord(token.getWord(position), length);
				w = colorPivot(w);
				outputLabel.setText("<html><pre><font face=\"Consolas\">"+w
						+"</font></pre></html>");
			}
		});
		slider.setValue(0);
		slider.setMaximum(token.count()-1);
		slider.setBounds(70, 141, 240, 26);
		frame.getContentPane().add(slider);
		
		btnStart = new JLabel("");
		btnStart.addMouseListener(new MouseAdapter() {
			
			/**
			 * If timer is running, stop timer and set btnStart's icon to "Play."
			 * Otherwise, start timer and set btnStart's icon to "Pause."
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(isRunning){
					timer.stop();
					isRunning = false;
					btnStart.setIcon(new ImageIcon(SpreedApp.class.getResource("/spreed/play.png")));
				}else{
					if(slider.getValue() == slider.getMaximum()){
						slider.setValue(slider.getMinimum());
					}
					isRunning = true;
					setSpeed();
					timer.setDelay(speed);
					timer.start();
					btnStart.setIcon(new ImageIcon(SpreedApp.class.getResource("/spreed/pause.png")));
				}
			}
		});
		btnStart.setIcon(new ImageIcon(SpreedApp.class.getResource("/spreed/play.png")));
		btnStart.setBounds(34, 144, 26, 23);
		frame.getContentPane().add(btnStart);
		
		btnReset = new JLabel("");
		
		/**
		 * Stops timer and sets all components back to starting position.
		 */
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				timer.stop();
				slider.setValue(0);
				isRunning = false;
				btnStart.setIcon(new ImageIcon(SpreedApp.class.getResource("/spreed/play.png")));
			}
		});
		btnReset.setIcon(new ImageIcon(SpreedApp.class.getResource("/spreed/first.png")));
		btnReset.setBounds(10, 141, 20, 26);
		frame.getContentPane().add(btnReset);
	}
}
