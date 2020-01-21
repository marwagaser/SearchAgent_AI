import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EndGame extends SearchProblem {

	public EndGame(String[] operators, State initialState) {
		super(operators, initialState);
		// TODO Auto-generated constructor stub
	}

	@Override

	public String PathCost(SearchTreeNode node) {
		// TODO Auto-generated method stub
		if (node == null) { // if the node is null return an empty string
			return "";
		} else {
			SearchTreeNode pn = node.parentNode; // get the parent node

			return PathCost(pn) + node.operator + ","; // return the parent operator + the current operator+ a comma

		}

	}

	public String PathCostVisualize1(SearchTreeNode node) {
		// TODO Auto-generated method stub
		if (node == null) {
			return "";
		} else {
			SearchTreeNode pn = node.parentNode;

			String[] split = ((EndGameState) node.state).grid.split(";");

			String thegrid = "";
			for (int i = 0; i < split.length - 2; i++) {
				thegrid += split[i];
				thegrid += ";";

			}

			return PathCostVisualize1(pn) + thegrid + "N"; // separate each grid and the other by the letter N
		}

	}

	@Override
	public void PathCostVisualize(SearchTreeNode node) {
		String h = PathCostVisualize1(node);

		String[] split = h.split("N");

		for (int i = 0; i < split.length; i++) {
			Main.visualize(split[i]); // calls the visualize from the Main method to display each grid at a time
			System.out.println("\n");
		}

	}

	public String generateEGS(int rows, int cols, int ironx, int irony, int tx, int ty, ArrayList<Point> stones,
			ArrayList<Point> warriors, int damage, boolean isThanosAlive) { // changes the variables and data structures
																			// which holds the Positions of the warriors
																			// and Stones into a string
		String str = "";
		str += rows + "," + cols + ";" + ironx + "," + irony + ";" + tx + "," + ty + ";";
		for (int i = 0; i < stones.size(); i++) {
			if (i < stones.size() - 1)
				str += (stones.get(i).x) + "," + (stones.get(i).y) + ",";
			else {
				str += (stones.get(i).x) + "," + (stones.get(i).y);
			}
		}
		str += ";";
		for (int i = 0; i < warriors.size(); i++) {
			if (i < warriors.size() - 1)
				str += (warriors.get(i).x) + "," + (warriors.get(i).y) + ",";
			else {
				str += (warriors.get(i).x) + "," + (warriors.get(i).y);
			}
		}
		str += ";";
		str += damage + ";" + isThanosAlive;
		return str;

	}

	@Override
	public boolean GoalTest(State state) {
		// TODO Auto-generated method stub
		EndGameState egs = ((EndGameState) state);
		String[] getLocations = splitGrid(egs.grid);
		if (Boolean.parseBoolean(getLocations[6]) == false) { // if Thanos is dead and this is set in the EndGame state
																// string, then you have reached the goal.
			return true;
		} else
			return false;
	}

	public boolean checkWarrior(int x, int y, ArrayList<Point> w) {

		if (w.contains(new Point(x, y))) { // check if iron man's position is the same as a position of the warrior
			return true; // if yes return true
		} else {
			return false; // otherwise return false
		}
	}

	public static String[] splitGrid(String str) {
		String[] stringArray = str.split(";"); // split the string upon semicolons and returning it in an array of
												// strings
		return stringArray;
	}

	public static String[] splitComma(String str) { // A function used to split a string upon finding a comma and
													// returning it in an array of Strings
		String[] stringArray = str.split(",");
		return stringArray;
	}

	public ArrayList<Point> killWarriors(int x, int y, ArrayList<Point> e) {
		// kill all warriors adjacent to it
		if (e.contains(new Point(x, y - 1))) {
			e.remove(new Point(x, y - 1));
		}
		if (e.contains(new Point(x, y + 1))) {
			e.remove(new Point(x, y + 1));
		}

		if (e.contains(new Point(x - 1, y))) {
			e.remove(new Point(x - 1, y));
		}
		if (e.contains(new Point(x + 1, y))) {
			e.remove(new Point(x + 1, y));
		}

		return e;
	}

	public int adjWarriors(int x, int y, ArrayList<Point> e) {
		int count = 0;
		// find the number of adjacent warriors and increment count by 1 if there is an
		// adjacent warrior.
		if (e.contains(new Point(x, y - 1))) {
			count++;
		}
		if (e.contains(new Point(x, y + 1))) {
			count++;
		}

		if (e.contains(new Point(x - 1, y))) {
			count++;
		}
		if (e.contains(new Point(x + 1, y))) {
			count++;
		}

		return count; // return the count of the adjacent warriors
	}

	public boolean adjThanos(int x, int y, int tx, int ty) {
		if ((tx == x && ty == (y - 1)) || (tx == x && ty == (y + 1)) || (tx == (x + 1) && (ty == y))
				|| (tx == (x - 1) && ty == y)) { // check if Thanos is adjacent to iron man
			return true; // if yes return true
		} else {
			return false; // otherwise return dalse
		}
	}

	@Override
	public SearchTreeNode transition(String op, SearchTreeNode stn) {

		// TODO Auto-generated method stub
		EndGameState egs = ((EndGameState) stn.state); // The search tree node takes a string
		String[] getLocations = splitGrid(egs.grid); // Take the string input and split upon semicolon.
		String gridDimensions = getLocations[0]; // Get the dimensions of the grid
		int rows = Integer.valueOf(splitComma(gridDimensions)[0]); // Get the x dimension
		int cols = Integer.valueOf(splitComma(gridDimensions)[1]); // Get the y dimension
		String ironManLocation = getLocations[1];// get Iron Man location (x,y)
		int ironx = Integer.valueOf(splitComma(ironManLocation)[0]); // get Iron Man x axis
		int irony = Integer.valueOf(splitComma(ironManLocation)[1]); // get Iron Man y axis

		String thanosLocation = getLocations[2]; // get Thanos location (x,y)
		int tx = Integer.valueOf(splitComma(thanosLocation)[0]); // get Thanos x axis
		int ty = Integer.valueOf(splitComma(thanosLocation)[1]); // get Thanos y axis

		ArrayList<Point> warriorsPositions = new ArrayList<Point>(); // Store warriors positions in this arraylist
		ArrayList<Point> stonesPositions = new ArrayList<Point>(); // Store warriors positions in this arraylist

		String stonesLocation = getLocations[3]; // get the stones locations (x,y)
		if (stonesLocation.length() == 0) {
			stonesPositions = new ArrayList<Point>(); // if there are no stones return an empty array list
		} else { // otherwise add each stone in the string grid in the arraylist of type Point
			String sLocation[] = stonesLocation.split(","); // get all the stones location in an array
			for (int i = 0; i < (sLocation.length); i = i + 2) {
				int j = i + 1;
				stonesPositions.add(new Point(Integer.valueOf(sLocation[i]), Integer.valueOf(sLocation[j]))); // add
																												// stones
				// to the
				// arraylist by creating a Point object from the x and y of each Point
			}
		}
		String warriorsLocation = getLocations[4]; // get the warriors locations
		if (warriorsLocation.length() == 0) {
			warriorsPositions = new ArrayList<Point>();
		} else {
			String wLocation[] = warriorsLocation.split(","); // get all the warriors location in an array
			for (int i = 0; i < (wLocation.length); i = i + 2) {
				int j = i + 1;
				warriorsPositions.add(new Point(Integer.valueOf(wLocation[i]), Integer.valueOf(wLocation[j]))); // add
																												// warriors
																												// to
																												// the
																												// arraylist
																												// by
																												// creating
																												// a
																												// Point
																												// object
																												// for
																												// each
																												// existent
																												// warrior
			}

		}

		int damage = Integer.valueOf(getLocations[5]); // Get the damage value of the state
		int newy; // a variable which will hold the new y value of Iron man in case he moves up or
					// down
		int newx; // a variable which will hold the new x value of Iron man in case he moves left
					// or right
		int hcost = 0; // set heursitic cost to 0
		if (stn.heuristictype == 1 || stn.heuristictype == 2) // if the node is of type heuristic 1 or 2s
			hcost = (stonesPositions.size() *2)+ 4; // set the starting heuristic cost by the number of the current existing
												// stones time 2 +4
		boolean isAlive = Boolean.parseBoolean(getLocations[6]); // get the boolean value of whether Thanos is Alive or
																	// not
		if (damage >= 100) { // if the damage in the state is greater than or equal to 100, it is of no use
								// to create a new SearchTreeNode as Iron Man is now dead. Therefore, return
								// null
			return null;
		}
		switch (op) { // check the operator to be applied on the SearchTreeNode

		case "collect": // if it is collect

			if (stonesPositions.size() > 0) { // check if there are still stones

				if (stonesPositions.contains(new Point(ironx, irony))) { // check if iron man is standing in a cell with
																			// a stone
					stonesPositions.remove(new Point(ironx, irony)); // remove the stone from the arraylist if iron man
																		// is in the same cell as the stone.

					if (adjThanos(ironx, irony, tx, ty) == true) { // if Thanos is adjacent to iron man while he is
																	// collecting, increment the damage by 5
						damage += 5;
						if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
							hcost += 1; // increment the heuristic cost of the SearchTreeNode which will be generated by
										// 1 as a result of being adjacent to Thanos
						}
					}

					int adjWarriors = adjWarriors(ironx, irony, warriorsPositions);
					damage += adjWarriors;
					if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
						hcost += adjWarriors; // increment the heuristic cost of the SearchTreeNode which will be
												// generated by the number of adjacent warriors as a result of being
												// adjacent to them
						hcost -=2; // decrement the heuristic cost by 2 as a result of being closer to the goal and
									// collecting a stone
					} else if (stn.heuristictype == 2) {// If the node is following an AS2 or GR2 search
						hcost -= 2; // decrement the heuristic cost by 2 as a result of being closer to the goal by
									// picking up a stone
					}
					damage += 3; // increment the damage by 3 as a result of collecting a stone

					String newEGS = generateEGS(rows, cols, ironx, irony, tx, ty, stonesPositions, warriorsPositions,
							damage, isAlive); // transform the variables and arraylists to a string which will be used
												// to create the new EndGameState
					String[] split = newEGS.split(";");
					String thegrid = "";
					for (int i = 0; i < split.length - 2; i++) { // remove the isAlive and damage from the state, just
																	// to check for repeated states
						thegrid += split[i];
						thegrid += ";";

					}
					if (this.repeatedStates.contains(thegrid)) { // if the state is repeated
						return null; // return null
					} else { // otherwise add the repeated state to the hashset
						this.repeatedStates.add(thegrid);
						int depthchild = stn.depth + 1; // increment depth by 1
						int type = stn.heuristictype; // set heuristic type to that of the parent
						EndGameState endgamestate = new EndGameState(newEGS); // create an EndGameState
						return new SearchTreeNode(endgamestate, stn, "collect", depthchild, damage, hcost, type); // Create
																													// the
																													// new
																													// SearchTreeNode
																													// as
																													// a
																													// result
																													// of
																													// the
																													// transformation
																													// and
																													// return
																													// it.
					}

				} else { // if iron man is not in a cell which has a stone you cannot perform the action
							// collect
					return null; // Therefore return null
				}
			} else { // if there are no stones you cannot collect
				return null; // So, return null
			}

		case "snap":

			if (tx == ironx && ty == irony & damage < 100) { // if iron man is in thanos cell and the damage is less
																// than 100
				if (stonesPositions.size() == 0) { // check if all stones are collect
					isAlive = false; // if stones collected then you can snap. So set isAlive to false

					String newEGS = generateEGS(rows, cols, ironx, irony, tx, ty, stonesPositions, warriorsPositions,
							damage, isAlive); // generate the new state string

					int depthchild = stn.depth + 1; // increment depth by 1 because it's a child node
					int type = stn.heuristictype; // set the heuristic type to the parent's
					EndGameState endgamestate = new EndGameState(newEGS); // create a new EndGameState from the string
																			// generated
					return new SearchTreeNode(endgamestate, stn, "snap", depthchild, damage, hcost, type); // return the
																											// new
																											// SearchTreeNode

				} else { // if not all stones collected. Then you cannot snap. Therefore, return null
					return null;
				}

			} else { // if you are at thanos and your damage is more than 100 then you cannot snap.
						// You cannot also snap if your not at thanos cell
				return null; // Therefore return null
			}
		case "left":
			newy = irony - 1; // set new y to the left position of iron man
			if (newy >= 0) { // as long as iron man is not trying to move not outside the grid then fine

				if ((tx == ironx && newy == ty && damage >= 100)
						|| (tx == ironx && newy == ty && stonesPositions.size() > 0)) { // If Iron man is to move left
																						// to a thanos cell, this should
																						// be impossible. Also, if he
																						// tries to move left without
																						// having all stones a null
																						// should be returned as this is
																						// an invalid action
					return null;
				} else {
					if (checkWarrior(ironx, newy, warriorsPositions)) { // check if the cell iron man wants to move to
																		// has warriors
						return null; // if thats the case return null, because its an invalid move
					} else {
						int adjWarriors = adjWarriors(ironx, newy, warriorsPositions);// Otherwise, count the number of
																						// adjacent warriors as a result
																						// of moving left

						damage += adjWarriors; // increment the damage by the number of adjacent warriors as a result of
												// moving left
						if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
							hcost += adjWarriors; // increment the heuristic cost by the number of warriors adjacent to
													// iron man
						}
						if (adjThanos(ironx, newy, tx, ty) == true) { // If iron man is adjacent to thanos as a result
																		// of moving left
							damage += 5; // increment damage by 5
							if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
								hcost += 1; // increment the heuristic cost by 1 as a result of being adjacent to thanos
							}
						}

						String newEGS = generateEGS(rows, cols, ironx, newy, tx, ty, stonesPositions, warriorsPositions,
								damage, isAlive); // create a string of the new state as a result of moving left
						String[] split = newEGS.split(";");
						String thegrid = "";
						for (int i = 0; i < split.length - 2; i++) { // remove the isAlive and damage from the state,
																		// just
																		// to check for repeated states
							thegrid += split[i];
							thegrid += ";";

						}
						if (this.repeatedStates.contains(thegrid)) { // if the state is repeated

							return null; // then eliminate it
						} else {
							this.repeatedStates.add(thegrid); // else if it is unique add it to the hash set of
																// repeatedStates
							int depthchild = stn.depth + 1; // set the SearchTreeNode depth to 1+the parent's depth
							int type = stn.heuristictype; // set the SearchTreeNode heuristic type
							EndGameState endgamestate = new EndGameState(newEGS); // create an EndGameState

							return new SearchTreeNode(endgamestate, stn, "left", depthchild, damage, hcost, type); // create
																													// and
																													// return
																													// the
																													// new
																													// SearchTreeNode
																													// after
																													// the
																													// transition
						}
					}

				}

			} else { // if Iron man is outside the grid return null because invalid move
				return null;
			}
		case "right":
			newy = irony + 1;// set new y to the right position of iron man
			if (newy <= (cols - 1)) {// as long as iron man is not trying to move not outside the grid then fine

				if ((tx == ironx && newy == ty && damage >= 100)
						|| (tx == ironx && newy == ty && stonesPositions.size() > 0)) { // If Iron man is to move right
																						// to a thanos cell, this should
																						// be impossible. Also, if he
																						// tries to move right without
																						// having all stones a null
																						// should be returned as this is
																						// an invalid action

					return null;
				} else {
					if (checkWarrior(ironx, newy, warriorsPositions)) { // check if the cell iron man wants to move to
																		// has warriors
						return null;
					} else {
						int adjWarriors = adjWarriors(ironx, newy, warriorsPositions); // count the number of adjacent
																						// warriors as a result of
																						// moving right
						damage += adjWarriors; // increment the damage by the number of existing adjacent warriors
						if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
							hcost += adjWarriors; // increment the heuristic cost by the number of adjacent warriors
						}
						if (adjThanos(ironx, newy, tx, ty) == true) { // If iron man adjacent to thanos as a result of
																		// moving right
							damage += 5; // increment damage by 5
							if (stn.heuristictype == 1) {// If the node is following an AS1 or GR1 search
								hcost += 1; // increment heuristic cost by 1 as a result of being adjacent to Thanos

							}
						}

						String newEGS = generateEGS(rows, cols, ironx, newy, tx, ty, stonesPositions, warriorsPositions,
								damage, isAlive); // create the new string which will be used to create the new
													// EndGameState
						String[] split = newEGS.split(";");
						String thegrid = "";
						for (int i = 0; i < split.length - 2; i++) { // remove the isAlive and damage from the state,
																		// just to check for repeated states
							thegrid += split[i];
							thegrid += ";";

						}

						if (this.repeatedStates.contains(thegrid)) { // if a repeated state
							return null; // ignore it
						} else {
							this.repeatedStates.add(thegrid); // else add it to the hash set
							int depthchild = stn.depth + 1; // Set the depth of the new SearchTreeNode by 1+the parent's
															// depth
							int type = stn.heuristictype; // set the heuristic type of the searchtreenode
							EndGameState endgamestate = new EndGameState(newEGS); // create a new EndGameState from the
																					// string

							return new SearchTreeNode(endgamestate, stn, "right", depthchild, damage, hcost, type); // return
																													// the
																													// new
																													// SearchTreeNode

						}

					}

				}

			} else { // if iron man tries to move outside grid this is an invalid action so return
						// null
				return null;
			}

		case "up":
			newx = ironx - 1; // set new x to the position above iron man
			if (newx >= 0) { // as long as iron man is not trying to move not outside the grid then fine

				if ((tx == newx && irony == ty && damage >= 100)
						|| (tx == newx && irony == ty && stonesPositions.size() > 0)) {// If Iron man is to move up
					// to a thanos cell, this should
					// be impossible. Also, if he
					// tries to move up without
					// having all stones a null
					// should be returned as this is
					// an invalid action
					return null;
				} else {
					if (checkWarrior(newx, irony, warriorsPositions)) {// check if the cell iron man wants to move to
																		// has warriors
						return null;
					} else {
						int adjWarriors = adjWarriors(newx, irony, warriorsPositions); // count the number of adjacent
																						// warriors as a result of
																						// moving up

						damage += adjWarriors; // increment damage by the number of warriors
						if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
							hcost += adjWarriors; // increment heuristic cost by the number of adjacent warriors
						}
						if (adjThanos(newx, irony, tx, ty) == true) { // if iron man adjacent to thanos as a result of
																		// moving up
							damage += 5; // increment damage by 5
							if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
								hcost += 1; // increment heuristic cost by 1 as a result of being adjacent to Thanos
							}
						}

						String newEGS = generateEGS(rows, cols, newx, irony, tx, ty, stonesPositions, warriorsPositions,
								damage, isAlive); // create the new string which will be used to create the new
													// EndGameState
						String[] split = newEGS.split(";");
						String thegrid = "";
						for (int i = 0; i < split.length - 2; i++) { // remove the isAlive and damage from the state,
																		// just to check for repeated states
							thegrid += split[i];
							thegrid += ";";

						}
						if (this.repeatedStates.contains(thegrid)) { // if there is a repeated state then this was a
																		// useless action and eliminate it
							return null;
						} else {
							this.repeatedStates.add(thegrid); // else add it to the hashset
							int depthchild = stn.depth + 1; // Set the depth of the new SearchTreeNode by 1+the parent's
															// depth
							int type = stn.heuristictype; // set the heuristic type of the searchtreenode
							EndGameState endgamestate = new EndGameState(newEGS);// create a new EndGameState from the
																					// string
							return new SearchTreeNode(endgamestate, stn, "up", depthchild, damage, hcost, type);// return
																												// the
																												// new
																												// SearchTreeNode
						}
					}

				}

			} else { // if iron man tries to move outside grid this is an invalid action so return
						// null
				return null;
			}
		case "down":
			newx = ironx + 1;// set new x to the position above iron man
			if (newx <= (rows - 1)) {// as long as iron man is not trying to move not outside the grid then fine

				if ((tx == newx && irony == ty && damage >= 100)
						|| (tx == newx && irony == ty && stonesPositions.size() > 0)) {
					// If Iron man is to move down
					// to a thanos cell, this should
					// be impossible. Also, if he
					// tries to move down without
					// having all stones a null
					// should be returned as this is
					// an invalid action
					return null;
				} else {
					if (checkWarrior(newx, irony, warriorsPositions)) { // check if the cell iron man wants to move to
																		// has warriors
						return null; // if yes then invalid action and he cannot move
					} else {
						int adjWarriors = adjWarriors(newx, irony, warriorsPositions); // count number of adjacent
																						// warriors as a result of
																						// moving down

						damage += adjWarriors;
						if (stn.heuristictype == 1) {// If the node is following an AS1 or GR1 search
							hcost += adjWarriors; // increment heuristic cost by the number of adjacent warriors
						}
						if (adjThanos(newx, irony, tx, ty) == true) {// if iron man adjacent to thanos as a result of
																		// moving down
							damage += 5; // increment the damage by 5
							if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
								hcost += 1; // increment the heuristic cost by 1 as a result of being adjacent to thanos
							}
						}

						String newEGS = generateEGS(rows, cols, newx, irony, tx, ty, stonesPositions, warriorsPositions,
								damage, isAlive); // create the new string which will be used to create the new
													// EndGameState
						String[] split = newEGS.split(";");
						String thegrid = "";
						for (int i = 0; i < split.length - 2; i++) { // remove the isAlive and damage from the state,
																		// just to check for repeated states
							thegrid += split[i];
							thegrid += ";";

						}
						if (this.repeatedStates.contains(thegrid)) {// if there is a repeated state then this was a
																	// useless action and eliminate it

							return null;
						} else {
							this.repeatedStates.add(thegrid); // otherwise add it to the hashset
							int depthchild = stn.depth + 1;// Set the depth of the new SearchTreeNode by 1+the parent's
															// depth
							int type = stn.heuristictype; // set the heuristic type of the SearchTreeNode
							EndGameState endgamestate = new EndGameState(newEGS);// create a new EndGameState from the
																					// string

							return new SearchTreeNode(endgamestate, stn, "down", depthchild, damage, hcost, type);// return
																													// the
																													// new
																													// SearchTreeNode
						}
					}

				}

			} else { // if iron man tries to move outside grid this is an invalid action so return
						// null
				return null;
			}

		case "kill":

			int adjWarriors = adjWarriors(ironx, irony, warriorsPositions); // count the number of adjacent warriors to
																			// iron man
			if (adjWarriors > 0) { // if there are warriors adjacent to iron man
				killWarriors(ironx, irony, warriorsPositions); // remove them from the arraylist of warriors

				damage += (adjWarriors * 2); // increment damage by 2* the number of warriors killed

				if (adjThanos(ironx, irony, tx, ty) == true) {// if iron man adjacent to thanos while killing
					damage += 5; // increment damage by 5
					if (stn.heuristictype == 1) { // If the node is following an AS1 or GR1 search
						hcost += 1; // increment the heuristic cost by 1 as a result of being adjacent to thanos
					}

				}

				String newEGS = generateEGS(rows, cols, ironx, irony, tx, ty, stonesPositions,
						killWarriors(ironx, irony, warriorsPositions), damage, isAlive);// create the new string which
																						// will be used to create the
																						// new EndGameState
				String[] split = newEGS.split(";");
				String thegrid = "";
				for (int i = 0; i < split.length - 2; i++) {// remove the isAlive and damage from the state, just to
															// check for repeated states
					thegrid += split[i];
					thegrid += ";";

				}
				if (this.repeatedStates.contains(thegrid)) { // if there is a repeated state then this was a useless
																// action and eliminate it
					return null;
				} else {
					this.repeatedStates.add(thegrid);// otherwise add it to the hashset
					int depthchild = stn.depth + 1;// Set the depth of the new SearchTreeNode by 1+the parent's depth
					int type = stn.heuristictype;// set the heuristic type of the SearchTreeNode
					EndGameState endgamestate = new EndGameState(newEGS);// create a new EndGameState from the string

					return new SearchTreeNode(endgamestate, stn, "kill", depthchild, damage, hcost, type);// return the
																											// new
																											// SearchTreeNode
				}

			} else { // if no adjacent warriors iron man cannot kill
				return null; // return null because of the invalid action
			}

		}
		return null;
	}

}