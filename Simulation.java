
public class Simulation {

	public static void main(String[] args) {
			/*
			 * removingall
			 * testcases
			 * add dupl* -
			 * dupl+no -
			 * no* -
			 * remo emp
			 * dup+no
			 * dup*
			 * no*
			 * costexced team teamnumber
			 */
			Team model = new Team();
			GUI view = new GUI(model.getAllplayers(),model.getselectedplayers(),model.getTeamName(),model.getTotalcost());
			IPL controller = new IPL(model,view);
			view.addListener(controller);
			model.addPropertyChangeListener(controller);
		
	}

}
