import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.event.SwingPropertyChangeSupport;

public class Team {
	
	static String AllPlayerfile = "players.txt";
	static String SelectedPlayerfile = "team.txt"; 
	String TeamName;
	double totalcost;
	double newcost;
	static double MaxTeamLimit = 100;
	static int MaxNplayers = 8;
	HashMap<String,Object[]> allplayers;
	HashSet<String> selectedplayers;
	SwingPropertyChangeSupport spcs;
	/*
	 * add no of players and nbowler,nbatsmen,nwk,nallr to gui
	 * 
	 */
	Team() {
		debug("model is intializing");
		spcs = new SwingPropertyChangeSupport(this);
		loaddata();
	}
	
	void debug(String msg) {
		System.out.println(msg);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
        spcs.addPropertyChangeListener(listener);
        debug("actionlisteners added Controller-model connection done!");
    }

	public String getTeamName() {
		return TeamName;
	}
	public void setTeamName(String teamName) {
		debug("model changed teamname");
		TeamName = teamName;
		spcs.firePropertyChange("teamnamelabel", null, TeamName);
	}
	
	public int getNplayers() {
		return selectedplayers.size();
	}
	
	public double getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(double totalcost) {
		debug("model updated cost");
		this.totalcost = totalcost;
		debug(""+totalcost);
		spcs.firePropertyChange("totalcostlabel", null, totalcost);
		
	}
	      
    Object[][] getAllplayers() {
    	Object[][] table = new Object[allplayers.size()][];
    	int i = 0;
    	for (Map.Entry<String,Object[]> o: allplayers.entrySet()) {
    		table[i] = o.getValue();
    		i++;
    	}
    	return table;
    }
    Object[][] getselectedplayers() {
    	Object[][] table = new Object[selectedplayers.size()][];
    	int i = 0;
    	for (String id:selectedplayers) {
    		Object[] temp = {allplayers.get(id)[0],allplayers.get(id)[1],allplayers.get(id)[2]}; 
    		table[i] = temp;
    		i++;
    	}
    	return table;
    }
    
    void addplayers(HashSet<String> newplayers) {
    	
    	debug("model added some players " + newplayers.size());
    	
    	Object[][] table = new Object[newplayers.size()][];
    	int i = 0;
    	for (String id:newplayers) {
    		selectedplayers.add(id);
    		Object[] temp = {allplayers.get(id)[0],allplayers.get(id)[1],allplayers.get(id)[2]}; 
    		table[i] = temp;
    		i++;
    	}
    	
    	setTotalcost(totalcost+newcost);
    	spcs.firePropertyChange("addplayers", null, table);
    	
    }
    void removeplayers(HashSet<String> rejectedplayers) {
//    	debug("model removed players");
    	
    	if(selectedplayers.isEmpty()) {
    		spcs.firePropertyChange("tableempty", null, null);
    		return;
    	}
    		
    	double subcost = 0,i=0;
    	for (String id:rejectedplayers) { 
    		if (selectedplayers.contains(id)) {
    			selectedplayers.remove(id);
    			subcost += Double.parseDouble((String)allplayers.get(id)[3]);i++;
    		}
    	}
    	debug("model players removed "+i);
    	setTotalcost(totalcost-subcost);
    	
    	spcs.firePropertyChange("removeplayers", null, rejectedplayers);
    }
    
    void removeallplayers() {
    	debug("model removed allplayers");
    	if (selectedplayers.isEmpty()) {    		
    		spcs.firePropertyChange("tableempty", null,null);
    		return;
    	}
    	selectedplayers.clear();
    	setTotalcost(0);
    	spcs.firePropertyChange("reset", null,null);	
    }
    
    void loaddata(){
//    	System.out.println(getClass().getResource("players.txt").getFile());
//    	System.out.println(getClass().getResource("players.txt").getPath());
    	
    	
    	File file = new File(getClass().getResource(AllPlayerfile).getFile());
    	Scanner scanner;
        try {
            scanner = new Scanner(file);//.useDelimiter(" ");
            allplayers = new HashMap<String, Object[]>();
            while (scanner.hasNext()) {
            	String id = scanner.next();
            	String firstname = scanner.next();
            	String lastname = scanner.next();
            	String name = firstname+lastname;
            	String playertype = scanner.next();
                String price = scanner.next();
                Object temp[] = {id,name,playertype,price,false};
                allplayers.put(id,temp);
                //debug(id + " " + name +  " " + playertype + " " + price);
            }

            scanner.close();
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        selectedplayers = new HashSet<String>();
        file = new File(getClass().getResource(SelectedPlayerfile).getFile());
        try {
            scanner = new Scanner(file);//.useDelimiter(" ");
            setTeamName( scanner.nextLine());
//            debug(TeamName);
            Double tc=0.0;
            while (scanner.hasNext()) {
                String id = scanner.next();
                selectedplayers.add(id);
                tc = tc + Double.parseDouble((String)allplayers.get(id)[3]);
                //debug(id);
            }
            setTotalcost(tc);
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        debug("model Loaded  data from files");
    }
    void savedata() {
    	try {
    		debug("model is storing data");
    		PrintWriter w = new PrintWriter(new File(getClass().getResource(SelectedPlayerfile).getFile()));
    		w.write(TeamName+"\n");
    		for(String s:selectedplayers)
    			w.write(s+"\n");
    		w.close();
    	} catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
    }
    
    public void CheckTeamformation (HashSet<String> newplayers)
    {
    	debug("model is checking teamformation");
    	
    	int N_batsmen = 0,N_bowler = 0,N_allrounder = 0,N_wicketkipper = 0;
    	newcost=0;
    	
    	if (newplayers.isEmpty()) {    		
    		spcs.firePropertyChange("noselections", null,null);
    		return;
    	}
    	//duplicate removal
    	
    	Iterator<String> it = newplayers.iterator();
    	while (it.hasNext())
 	   	{
 	      String item = it.next();
 	  	  if(selectedplayers.contains(item)) {
 	  		  	it.remove();
 	  			newcost++;
 	  		}  
 	   	}
    	
    	if (newplayers.isEmpty()) {
    		spcs.firePropertyChange("duplicate", null, newcost);
    		return;
    	}
    	newcost=0;
    	
    	//counting cost and noofolayers of each category
    	for (String id: newplayers) {
    		if(allplayers.get(id)[2].equals("Bowler"))N_bowler++;
    		if(allplayers.get(id)[2].equals("Batsman"))N_batsmen++;
    		if(allplayers.get(id)[2].equals("Allrounder"))N_allrounder++;
    		if(allplayers.get(id)[2].equals("Wicketkeeper"))N_wicketkipper++;
    		newcost = newcost + Double.parseDouble((String) allplayers.get(id)[3]) ;
    	}
    	
    	for (String id: selectedplayers) {
    		if(allplayers.get(id)[2].equals("Bowler"))N_bowler++;
    		if(allplayers.get(id)[2].equals("Batsman"))N_batsmen++;
    		if(allplayers.get(id)[2].equals("Allrounder"))N_allrounder++;
    		if(allplayers.get(id)[2].equals("Wicketkeeper"))N_wicketkipper++;
    		
    	}

    	int totalplayers = (N_batsmen+N_bowler+N_allrounder+N_wicketkipper);
    	
    	debug(totalplayers + " " + getNplayers() + " " + newplayers.size());
    	
    	if ( newcost + getTotalcost() > MaxTeamLimit) {
    		spcs.firePropertyChange("exceedcost", null,newcost);
    		return;
    	}
    	else if(totalplayers > MaxNplayers) { 	
    		spcs.firePropertyChange("nplayerexceed", null,newplayers.size());
    		return;
    	}
    	else if( N_wicketkipper > 1) {
    		spcs.firePropertyChange("wrongteamformation", null,"wicket keeper limit exceeded");
    		return;
    	} else if( (N_batsmen <= 3 && N_bowler <= 2 && N_allrounder <= 2) ) {
    		addplayers(newplayers);
    		debug("scheme1");
    		//spcs.firePropertyChange("tableempty", null,null);
    		return;
    	} else if(N_batsmen <= 3 && N_bowler <= 3 && N_allrounder <= 1) {
    		addplayers(newplayers);
    		//spcs.firePropertyChange("tableempty", null,null);
    		debug("scheme2");
    		return;
    	} else if (N_batsmen <= 4 && N_bowler <= 2 && N_allrounder <= 1) {
    		addplayers(newplayers);
    		debug("scheme3");
    		//spcs.firePropertyChange("tableempty", null,null);
    		return;
    	} else {
    		spcs.firePropertyChange("wrongteamformation", null,"no team scheme matched");
    		return;
    	}
    } 
   
}
