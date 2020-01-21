import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public abstract class SearchProblem {
	String[] operators; //an array of string operators which are going to be used in a certain search problem 
	State initialState; // the initial state of the SearchProblem
	Set<String> repeatedStates; //a hash set which carries the repeated states
	Comparator<SearchTreeNode> costComparator = new Comparator<SearchTreeNode>() { // a comparator which adds to the priority queue according to the cheap path cost first
		@Override
		public int compare(SearchTreeNode o1, SearchTreeNode o2) {
			// TODO Auto-generated method stub
			return o1.pathCost - o2.pathCost;
		}
	};
	Comparator<SearchTreeNode> GS1Comparator = new Comparator<SearchTreeNode>() {// a comparator which adds to the priority queue according to the cheap heuristic cost first
		@Override
		public int compare(SearchTreeNode o1, SearchTreeNode o2) {
			// TODO Auto-generated method stub
			return o1.heuristic - o2.heuristic;
		}
	};

	Comparator<SearchTreeNode> AstarComparator = new Comparator<SearchTreeNode>() {// a comparator which adds to the priority queue according to the cheap heuristic and path costs first
		@Override
		public int compare(SearchTreeNode o1, SearchTreeNode o2) {
			// TODO Auto-generated method stub
			return (o1.pathCost + o1.heuristic) - (o2.pathCost + o2.heuristic);
		}
	};

	public SearchProblem(String[] operators, State initialState) {
		this.operators = operators;
		this.initialState = initialState;
		this.repeatedStates = new HashSet<>();

	}

	public String GeneralSearch(SearchProblem sp, String strategy, boolean visualize) {

		boolean isExpanded = false; // a boolean variable to check if a node is expanded or not
		Queue<SearchTreeNode> nodes = new LinkedList<>(); // a queue used for the BFS  
		Stack<SearchTreeNode> stackNodes = new Stack<SearchTreeNode>(); // A stack used by IDS and DFS
		PriorityQueue<SearchTreeNode> pq = new PriorityQueue<SearchTreeNode>(costComparator); // A pq used by UCS
		PriorityQueue<SearchTreeNode> Astarpq = new PriorityQueue<SearchTreeNode>(AstarComparator); //A priority queue used by A star
		PriorityQueue<SearchTreeNode> gpq = new PriorityQueue<SearchTreeNode>(GS1Comparator);//A priority queue used by Greedy
		SearchProblem eg = sp;

		SearchTreeNode newStn; // declare a SearchTreeNode
		//M
		if (strategy.equals("GR1") || strategy.equals("AS1")) {
			newStn = new SearchTreeNode(eg.initialState, null, null, 0, 0, 0, 1); //the initial state is the same the SearchProblem's, the parent null since it is the root, operator null since this is the root, depth is 0 and the path cost is 0, the heuristic cost is 0, and the type of the heuristic is 1
		} else if (strategy.equals("GR2") || strategy.equals("AS2")) {
			newStn = new SearchTreeNode(eg.initialState, null, null, 0, 0, 0, 2);//the initial state is the same the SearchProblem's, the parent null since it is the root, operator null since this is the root, depth is 0 and the path cost is 0, the heuristic cost is 0, and the type of the heuristic is 2
		} else {
			newStn = new SearchTreeNode(eg.initialState, null, null, 0, 0, 0, 0);//the initial state is the same the SearchProblem's, the parent null since it is the root, operator null since this is the root, depth is 0 and the path cost is 0, the heuristic cost is 0, and the type of the heuristic is 0
		}
//the heuristic cost is set to every node with a value in the transition, we start by( the number of stones in the transition function*2)+4.
		int nodesNumber = 0;//this will carry the number of expanded nodes

		boolean flag = true; //set a flag to true
		State endgamestate; //declare endgamestate
		switch (strategy) { //check the strategy

		case "BF": //if it is breadth-first

			nodes.add(newStn); //add the SearchTreeNode which we have created to the queue
			endgamestate = sp.initialState; // set to the initial state of the search problem

			while (flag) { //keep looping
				isExpanded = false; //
				if (nodes.isEmpty()) { //if the queue is empty
					System.out.println("No solution"); //print no solution
					return null;//and return null
				} else {

					SearchTreeNode node = nodes.remove(); //remove the first node off the queue

					if (GoalTest(node.state)) { //does it pass the goal test?
						String str = PathCost(node).substring(5); //fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) { //if the user wants to visualize the grid steps
							PathCostVisualize(node);//call the PathCostVisualize function to display the sequence of grids from the root to the goal
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber; //String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from the while loop
						this.repeatedStates.clear();//clear the repeated states hash set
						return s; //return string s
					} else {

						for (int y = 0; y < sp.operators.length; y++) { //loop on all the operators of a search problem

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) { //if the node returned is null, do nothing

							} else {
								isExpanded = true; //else this means a node has been expanded so set it to true
								nodes.add(searchtreenode); //add the node created to the nodes queue
							}
						}
						if (isExpanded) //if the node which the transition was applied on  has children
							nodesNumber++; //increment number of nodes
					}
				}
			}
			break;

		case "DF":

			stackNodes.push(newStn); //push the initial SearchTreeNode on top of the stack
			endgamestate = sp.initialState;// set to the initial state of the search problem

			while (flag) { //keep looping 
				if (stackNodes.isEmpty()) { //if the stack is empty
					System.out.println("no answer");
					return null; //return null
				} else {
					SearchTreeNode node = stackNodes.pop(); //take the first SearchTreeNode off the queue
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5); //fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps
							PathCostVisualize(node); //call the PathCostVisualize function to display the sequence of grids from the root to the goal
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from loop
						this.repeatedStates.clear();//clear the repeated states hash set

						return s;//return string s
					} else {

						for (int y = 0; y < sp.operators.length; y++) { //loop on all the operators of a search problem

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) { //if it is null do nothing

							} else { 

								isExpanded = true; //else this means a node has been expanded so set it to true
								stackNodes.push(searchtreenode); //push the child on top of the stack

							}
						}
						if (isExpanded) {//if the node which the transition was applied on  has children
							nodesNumber++; //increment number of expanded nodes
						}

					}
				}
			}
			break;
		case "UC":

			pq.add(newStn); //add the initial SearchTreeNode to the priority queue
			while (flag) {

				isExpanded = false; 
				if (pq.isEmpty()) { //is the priority queue is empty
					System.out.println("No Solution");
					return null; //return null
				} else {
					SearchTreeNode node = pq.remove(); //remove the first element in the priority queue
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps

							PathCostVisualize(node); //call PathCostVisualize 

						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from loop
						this.repeatedStates.clear();//clear the repeated states hash set

						return s; //return the string s
					} else {
						for (int y = 0; y < sp.operators.length; y++) {//loop on all the operators of a search problem

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) {//if it is null do nothing

							} else {
								isExpanded = true;//else this means a node has been expanded so set it to true

								pq.add(searchtreenode); //add the child to the priority queue
							}
						}
						if (isExpanded == true) {//if the node which the transition was applied on  has children
							nodesNumber++; //increment number of expanded nodes
						}
					}
				}
			}

			break;
		case "ID"://push the initial SearchTreeNode to the stack
			int maxDepth = 0; //Carries the value of the maximum depth 
			for (int i = 0; i < maxDepth + 1; i++) { //loop from 0 till maxDepth
				this.repeatedStates.clear(); //make sure that nothing is in the repeated states hash set
				stackNodes.push(newStn); //push the intial SearchTreeNode to the queue
				while (flag) {//keep looping
					isExpanded = false;
					if (stackNodes.isEmpty()) {//if the stack is empty
						maxDepth++;//increment the depth, maybe you will find a goal in a deeper place
						break; //break from the while loop and go back to the for loop
					} else { // if the stack is not empty
						SearchTreeNode node = stackNodes.pop(); //pop the first element off the stack
						if (GoalTest(node.state)) { //if it passes the goal test
							String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
							str = str.substring(0, str.length() - 1);
							if (visualize) {//if the user wants to visualize the grid steps

								PathCostVisualize(node);//call PathCostVisualize 

							}
							String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
							flag = false;//break from loop
							this.repeatedStates.clear();//clear the repeated states hash set
							return s;//return the string s
						} else { //if the goal node is not found
							int childD = node.depth + 1; 
							if (childD <= maxDepth) {//check if the children's depth is less than or equal the maximum depth 
								for (int y = 0; y < sp.operators.length; y++) { // expand only if children at acceptable
																				// level

									SearchTreeNode egs = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
									if (egs == null) { //if the node reterived from the transition is null do nothing

									} else { //else 
										stackNodes.push(egs); //push the child node on top of the stack
										isExpanded = true; //this means a node has been expanded so set it to true

									}

								}
								if (isExpanded) {//if the node which the transition was applied on  has children
									nodesNumber++;//increment number of expanded nodes
								}
							}

						}
					}
				}
			}

			break;

		case "GR1":
			gpq.add(newStn); //add the initial SearchTreeNode to the priority queue
			while (flag) { //keep looping
				isExpanded = false;
				if (gpq.isEmpty()) { //if the pq is empty
					System.out.println("No Solution");
					return null; //return null
				} else {
					SearchTreeNode node = gpq.remove(); //remove the first element off the pq
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps

							PathCostVisualize(node);//call PathCostVisualize 

						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber; //String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false;//break from loop
						this.repeatedStates.clear(); //clear the hash set
						return s; //return the string s

					} else {
						for (int y = 0; y < sp.operators.length; y++) {//loop on all the operators of a search problem

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) { //if the SearchTreeNode is null do nothing

							} else {
								isExpanded = true;//this means a node has been expanded so set it to true
								gpq.add(searchtreenode); //add the child node to the pq
							}
						}
						if (isExpanded) {//if the node which the transition was applied on  has children
							nodesNumber++;//increment number of expanded nodes
						}
					}
				}
			}

			break;

		case "GR2": //IT HAS THE SAME EXPLANATION AS THE ONE ABOVE IT EXACTLY GR1
			gpq.add(newStn);
			while (flag) {
				isExpanded = false;
				if (gpq.isEmpty()) {
					System.out.println("No Solution");
					return null;
				} else {
					SearchTreeNode node = gpq.remove();
					if (GoalTest(node.state)) {
						String str = PathCost(node).substring(5);
						str = str.substring(0, str.length() - 1);
						if (visualize) {

							PathCostVisualize(node);

						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;
						flag = false;
						this.repeatedStates.clear();
						return s;
					} else {
						for (int y = 0; y < sp.operators.length; y++) {

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); 
							if (searchtreenode == null) {

							} else {
								isExpanded = true; 
								gpq.add(searchtreenode);
							}
						}
						if (isExpanded) {
							nodesNumber++;
						}
					}
				}
			}

			break;

		case "AS1":
			Astarpq.add(newStn); //Add the initial SearchTreeNode to the pq 
			while (flag) {
				isExpanded = false;
				if (Astarpq.isEmpty()) { //if the pq is empty
					System.out.println("No Solution");
					return null; //return null
				} else {
					SearchTreeNode node = Astarpq.remove(); //remove the first SearchTreeNode off the pq
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps
							PathCostVisualize(node); //call PathCostVisualize
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from loop
						this.repeatedStates.clear(); //clear the hashset
						return s;//return the String s
					} else {
						for (int y = 0; y < sp.operators.length; y++) {//loop on all the operators of a search problem

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) { //if the SearchTreeNode is null do nothing

							} else {
								isExpanded = true;//this means a node has been expanded so set it to true
								Astarpq.add(searchtreenode); //add the child node to the pq
							}
						}
						if (isExpanded) {//if the node which the transition was applied on  has children
							nodesNumber++;//increment number of expanded nodes
						}
					}
				}
			}

			break;

		case "AS2"://IT HAS THE SAME EXPLANATION AS THE ONE ABOVE IT EXACTLY AS1
			Astarpq.add(newStn);
			while (flag) {
				isExpanded = false;
				if (Astarpq.isEmpty()) {
					System.out.println("No Solution");
					return null;
				} else {
					SearchTreeNode node = Astarpq.remove();
					if (GoalTest(node.state)) {
						String str = PathCost(node).substring(5);
						str = str.substring(0, str.length() - 1);
						if (visualize) {

							PathCostVisualize(node);

						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;
						flag = false;
						this.repeatedStates.clear();
						return s;
					} else {
						for (int y = 0; y < sp.operators.length; y++) {

							SearchTreeNode searchtreenode = sp.transition(sp.operators[y], node); 
							if (searchtreenode == null) {

							} else {
								isExpanded = true;
								Astarpq.add(searchtreenode);
							}
						}
						if (isExpanded) {
							nodesNumber++;
						}
					}
				}
			}

			break;

		}

		return "No Solution";

	}

	public abstract String PathCost(SearchTreeNode node); // and abstract method to return the sequence of operators applied to reach SearchTreeNode node

	public abstract void PathCostVisualize(SearchTreeNode node);// and abstract method to print the sequence of grids to reach SearchTreeNode node

	public abstract boolean GoalTest(State state); //Is used to test whether State state is the goal state or not

	public abstract SearchTreeNode transition(String op, SearchTreeNode stn); //Apply an operator op on the SearchTreeNode stn to get a new SearchTreeNode
}
