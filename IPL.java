import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

import javax.swing.JOptionPane;


public class IPL extends WindowAdapter implements PropertyChangeListener, ActionListener {

	Team model;
	GUI view;
	
	public IPL(Team model, GUI view) {
		super();
		this.model = model;
		this.view = view;
		debug("Starting Controller IPL Begins!!!");

	}
	
	void debug(String msg) {
		System.out.println(msg);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String actionCommand = e.getActionCommand();
		debug("Controller Listened" + actionCommand  + "sending updates to model");
	    
		if(actionCommand.equals("SUBMIT")) {
			if (view.getnewteamname().isEmpty())
				view.showmessage("TeamName Empty");
			else
				model.setTeamName(view.getnewteamname());
	    } else if (actionCommand.equals("ADD")) {
	    		model.CheckTeamformation(view.getcheckedplayers());
	    	
	    	
	    	/*if(view.getcheckedplayers().isEmpty()){
	    		view.showmessage("No Selection");
	    		view.removechecks();
	    	}
	    	else {
	    		String msg = model.CheckTeamformation(view.getcheckedplayers());
	    		if(msg.equals("ok")) {
//	    			model.addplayers(view);
	    			view.removechecks();
	    		} else {
	    			view.showmessage(msg);
	    			view.removechecks();
	    		}  		
	    	}*/
	    } else if (actionCommand.equals("REMOVE")) {
	    	model.removeplayers(view.getcheckedplayers());
	    	/*if(view.getcheckedplayers().isEmpty()){
	    		view.showmessage("No Selection");
	    		view.removechecks();
	    	} else {
	    		model.removeplayers(view.getcheckedplayers());
	    		view.removechecks();

	    	}*/
	    } else if (actionCommand.equals("RESET")) {
	    	model.removeallplayers();
//	    	view.setcostlabel();
	    } else {
	    	System.out.println("UNDEFINED ACTIONCOMMAND");
	    }
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		debug("Controller Listened" + propName + "sending updates to views");
        view.removechecks();
		if("teamnamelabel".equalsIgnoreCase(propName)){
            view.setteamname((String)evt.getNewValue());
            
        } else if("totalcostlabel".equalsIgnoreCase(propName)) {
        	view.setcostlabel(String.valueOf(evt.getNewValue()));
        	
        } else if ("addplayers".equalsIgnoreCase(propName)) {
        	view.addrow((Object[][]) evt.getNewValue());
        	
        } else if("removeplayers".equalsIgnoreCase(propName)) {
        	view.removerow((HashSet<String>) evt.getNewValue());
        	
        } else if("reset".equalsIgnoreCase(propName)) {
        	view.removeallrows();
        	
        }  else if("tableempty".equalsIgnoreCase(propName)) {
        	view.showmessage("Table Empty");
        	
        } else if("noselections".equalsIgnoreCase(propName)) {
        	view.showmessage("No Selections");
        	
        } else if("duplicate".equalsIgnoreCase(propName)) {
        	view.showmessage("Duplicates Found "+ String.valueOf(evt.getNewValue()));
        	
        } else if("exceedcost".equalsIgnoreCase(propName)) {
        	view.showmessage("Cost Limit Exceeded "+String.valueOf(evt.getNewValue()));
        	
        } else if("nplayerexceed".equalsIgnoreCase(propName)) {
        	view.showmessage("Player limit exceeded "+String.valueOf(evt.getNewValue()));
        	
        } else if("wrongteamformation".equalsIgnoreCase(propName)) {
        	view.showmessage((String)evt.getNewValue());
        	
        } else {
        	debug("UNKNOWN PROPERTYNAME");
        }	
	}
	
	public void windowClosing(WindowEvent e){
		 debug("Controller Listened"  + " sending closing to model");
         model.savedata();
     }
	
}
