import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;


public class GUI {
	private JFrame view;
	private JPanel toppanel;
	private JScrollPane  bottompanel;
	private JPanel centerpanel;
	private JPanel rightpanel;
	private JPanel leftpanel;	
	private JPanel playertablepanel;
	private JLabel teamnamelabel;
	private JLabel costlabel;
	
	private JTable displaytable;
	private JTable playertable;
	private JButton reset;
	private JButton remove;
	private JButton add;
	private JButton submit;
	private JTextField teamname; 
	private DefaultTableModel dtm;
	/*
	 * 
	 * "id", "playername", "playertype", "playerprice", "select" // table format
	 */
	
	GUI(Object allplayers[][],Object selectedplayers[][],String currentteamname,double cost) {
		debug("starting GUI");
		view = new JFrame(); 

		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	        g.setBackground(Color.WHITE);
		view.setLayout(new GridLayout(1, 2));
		view.getContentPane().setPreferredSize(new Dimension(650, 550));

		initialize(allplayers, selectedplayers, currentteamname,cost);

		view.getContentPane().add(leftpanel, "Left");
		view.getContentPane().add(rightpanel,"Right");     

		view.pack();
		view.setLocationRelativeTo(null);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void debug(String msg) {
		System.out.println(msg);
	}	
	void showmessage(String msg) {
		JOptionPane.showMessageDialog(view, msg);
	}
	void addListener(ActionListener a) {
		  
		  teamname.addActionListener(a);
	      teamname.setActionCommand("SUBMIT");
	
	      reset.addActionListener(a);
	      reset.setActionCommand("RESET");

	      remove.addActionListener(a);
	      remove.setActionCommand("REMOVE");
	      
	      add.addActionListener(a);
	      add.setActionCommand("ADD");
	    
		 
	      submit.addActionListener(a);
	      submit.setActionCommand("SUBMIT");
	      
	      view.addWindowListener((WindowAdapter) a);
	      debug("actionlisteners added Controller-view connection done!");
	      
	}
	 
	void initialize(Object allplayers[][],Object selectedplayers[][],String currentteamname,double cost) {

		//left table data
		String fields[] = { "playerid", "playername", "playertype" } ;
		dtm = new DefaultTableModel(selectedplayers, fields);
		displaytable = new JTable(dtm);
		displaytable.getTableHeader().setReorderingAllowed(false);
		
		
		leftpanel = new JPanel(new BorderLayout());
		leftpanel.setBackground(Color.WHITE);
		//leftpanel.add(displaytable.getTableHeader(), BorderLayout.CENTER);
		leftpanel.add(new JScrollPane(displaytable),BorderLayout.CENTER);
		teamnamelabel = new JLabel(currentteamname,SwingConstants.CENTER);
		teamnamelabel.setFont(new Font("Serif", Font.ROMAN_BASELINE, 16));
		Border border1 = BorderFactory.createEtchedBorder(Color.darkGray, Color.BLUE);
		teamnamelabel.setBorder(border1);
		
		costlabel = new JLabel("Current Team Cost(in millions): "+cost,SwingConstants.CENTER);
		costlabel.setFont(new Font("Serif", Font.ROMAN_BASELINE, 16));
		
		Border border = BorderFactory.createEtchedBorder(Color.darkGray, Color.BLUE);
		costlabel.setBorder(border);
		
		leftpanel.add(teamnamelabel,BorderLayout.NORTH);
		leftpanel.add(costlabel,BorderLayout.SOUTH);
		
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

		
		//fonts border settooltiptext paddings focus colorpane/lookfeel

		//right panel prerequisites
		//toppanel code
		toppanel = new JPanel();
		toppanel.setBackground(Color.WHITE);
		reset = new JButton("CLEAR");toppanel.add(reset);
		add = new JButton("ADD");toppanel.add(add);
		remove = new JButton("REMOVE");toppanel.add(remove);

		//centerpanel code
		centerpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerpanel.setBackground(Color.WHITE);
		teamname = new JTextField("Enter TeamName",25);centerpanel.add(teamname);
		//reqyest focus on jtextfield
		view.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				teamname.requestFocusInWindow();
			}
		});
		submit = new JButton("SUBMIT");centerpanel.add(submit);

		//bottompanel data 
		Object[] columnNames = {"id", "playername", "playertype", "playerprice", "select"};
		
		DefaultTableModel model = new DefaultTableModel(allplayers, columnNames);
		playertable = new JTable(model) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				default:
					return Boolean.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int col) {
				//Note that the data/cell address is constant,
				//no matter where the cell appears onscreen.
				if (col < 4) {
					return false;
				} else {
					return true;
				}
			}
		};     

		//spacing for column width
		playertable.getColumnModel().getColumn(0).setMaxWidth(50);
		playertable.getColumnModel().getColumn(1).setMaxWidth(100);
		playertable.getColumnModel().getColumn(2).setMaxWidth(50);
		playertable.getColumnModel().getColumn(3).setMaxWidth(60);
		playertable.getColumnModel().getColumn(4).setMaxWidth(70);
		playertable.setPreferredScrollableViewportSize(new Dimension(350, 350));
		playertable.setFillsViewportHeight(true);
		playertable.getTableHeader().setReorderingAllowed(false);
		//cell spacing
		playertable.setRowHeight(20);

		//bottomlayerpanel
		playertablepanel = new JPanel(new BorderLayout());
		playertablepanel.setBackground(Color.WHITE);
		playertablepanel.add(playertable.getTableHeader(), BorderLayout.NORTH);
		playertablepanel.add(playertable, BorderLayout.CENTER);

		bottompanel = new JScrollPane(playertablepanel,v,h);

		//constrainst for adding panels in rightpanel
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		gbc1.anchor = GridBagConstraints.CENTER;
		gbc1.insets = new Insets(0, 45, 0, 45);// top, left, bottom, right
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.gridwidth = 250;
		gbc1.ipadx = 250;
		gbc1.weighty = 1;//for expanding extrapace
		gbc1.weightx = 1;

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.RELATIVE;
		gbc2.anchor = GridBagConstraints.NORTH;
		gbc2.insets = new Insets(2, 0, 0, 0);
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 250;
		gbc2.gridheight = 50;
		gbc2.ipadx = 250;
		gbc2.ipady = 18;
		gbc2.weighty = 1;
		gbc2.weightx = 1;

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.fill = GridBagConstraints.RELATIVE;
		gbc3.insets = new Insets(0, 0, 0, 0);
		gbc3.anchor = GridBagConstraints.CENTER;
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		gbc3.gridwidth = 350;
		gbc3.gridheight = 200;
		gbc3.ipadx = 350;
		gbc3.ipady = 250;
		gbc3.weighty = 1;
		gbc3.weightx = 1;

		//right panel code  
		rightpanel = new JPanel(new GridBagLayout());
		rightpanel.setBackground(Color.WHITE);        
		rightpanel.add(toppanel,gbc1);
		rightpanel.add(centerpanel,gbc2);
		rightpanel.add(bottompanel,gbc3);
		
	}

	HashSet<String> getcheckedplayers() {
		debug("gui is returning selected players");
		HashSet<String> checkedplayers = new HashSet<String>();   
		for (int i = 0; i < playertable.getRowCount() ;i++) {
			//debug(i+""+playertable.getValueAt(i, 4)+playertable.getValueAt(i, 0));
			if(playertable.getValueAt(i, 4).equals(true)) {
				checkedplayers.add((String) playertable.getValueAt(i,0));
			}
		}
		return checkedplayers;
	}
	
	void removechecks(){
		debug("removechecked ");
		for (int i = 0; i < playertable.getRowCount(); i++) {
			playertable.setValueAt(false, i, 4);
		}
	}
	
	String getnewteamname() {
		return teamname.getText();
	}
	void setteamname(String tname) {
		
		teamnamelabel.setText(tname);
		teamname.setText("");
		debug("view updated teamname");
	}

	
	void removerow(HashSet<String> rejectedplayers) {
		for (int i = 0; i < displaytable.getRowCount(); i++) {
			if(rejectedplayers.contains(displaytable.getValueAt(i, 0)) ){
				dtm.removeRow(i);
				i--;
			}
		}
		debug("view removed rows in teamtable");
		
	}
	void removeallrows() {
		dtm.setRowCount(0);//or iterate
		debug("view removed all rows from teamtable");
	}
	void addrow(Object[][] newplayers) {
		debug("view added rows from teamtable");
		for(Object[] r: newplayers) {
			dtm.addRow(r);
		}
	}
		
	void setcostlabel(String cost){
		debug("view updated cost"+cost);
		costlabel.setText("Current Team Cost(in millions): "+cost);
	}

}
