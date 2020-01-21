import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Main {
	public static String [] splitGrid(String str){
		String[] stringArray = str.split(";"); 
		return stringArray;
	}
	public static String [] splitComma(String str){ //A function used to split a string upon finding a comma
		String[] stringArray = str.split(","); 
		return stringArray;
	}

	public static String generateEGS(int rows, int cols, int ironx, int irony, int tx, int ty, ArrayList<Point> stones,
			ArrayList<Point> warriors, int damage, boolean isThanosAlive) {
		String str = ""; //intialize a srting
		str += rows + "," + cols + ";" + ironx + "," + irony + ";" + tx + "," + ty + ";"; //concatenate the rows to cols iron man x position, iron man y position, and Thanos position separated by commas
		for (int i = 0; i < stones.size(); i++) { //concatenate stones to the string each stone is separated from the other by a comma. 
			if(i<stones.size()-1)
			str += (stones.get(i).x) + "," + (stones.get(i).y)+",";
			else {
				str += (stones.get(i).x) + "," + (stones.get(i).y);
			}
		}
		str += ";"; //separate stones from the warriors by commas
		for (int i = 0; i < warriors.size(); i++) {
        	if(i<warriors.size()-1) {	
        		str += (warriors.get(i).x) + "," + (warriors.get(i).y)+",";
        	}
        	else {
        		str += (warriors.get(i).x) + "," + (warriors.get(i).y);
        	}
		}
		str += ";";
		str += damage + ";" + isThanosAlive;
		return str;

	}
	
	public static void visualize(String grid) { //The string grid is the string containing the grid
		String [] visualize= splitGrid(grid); //Take the string input and split upon semicolon.
		
		String gridDimensions=visualize[0]; //Get the dimensions of the grid
		int xaxis=Integer.valueOf(splitComma(gridDimensions)[0]); //Get the x dimension
		int yaxis=Integer.valueOf(splitComma(gridDimensions)[1]); //Get the y dimension
		String [] [] theGrid =  new String [xaxis][yaxis]; //create a 2d array with the dimensions provided
		
		String ironManLocation = visualize[1];//get Iron Man location (x,y)
		int ix = Integer.valueOf(splitComma(ironManLocation)[0]); //get Iron Man x axis
		int iy = Integer.valueOf(splitComma(ironManLocation)[1]); //get Iron Man y axis
		theGrid[ix][iy]= "I";
		String thanosLocation = visualize[2]; //get Thanos location (x,y)
		int tx = Integer.valueOf(splitComma(thanosLocation)[0]); //get Thanos x axis
		int ty = Integer.valueOf(splitComma(thanosLocation)[1]); //get Thanos y axis
		
		if((ix==tx)&&(iy==ty))
			theGrid[tx][ty]= "IT";
		else
			theGrid[tx][ty]= "T";
		
		if(visualize.length>3&&!(visualize[3].equals("")))
		{
		String stonesLocation = visualize[3]; //get the 6 stones locations (x,y)
		
		
		String  sLocation[] = stonesLocation.split(","); //get all the warriors location in an array
		
		for (int i=0;i<(sLocation.length);i=i+2) {
			int j=i+1;
			
			if((ix==Integer.valueOf(sLocation[i]))&&(iy==Integer.valueOf(sLocation[j])))
			{
			theGrid[Integer.valueOf(sLocation[i])][Integer.valueOf(sLocation[j])]= "Is"; //If IronMan is in the same cell as the stone then Is will be shown
			}
			else
				{theGrid[Integer.valueOf(sLocation[i])][Integer.valueOf(sLocation[j])]= "s";}
			//insert all the stones in the 2D array representing the grid	
		}
		}
		
		if(visualize.length>4&&!(visualize[4].equals(""))) {
		
		String warriorsLocation = visualize[4]; //get the warriors locations
	
		String  wLocation[] = warriorsLocation.split(","); //get all the warriors location in an array and concatenate them to string
		for (int i=0;i<(wLocation.length);i=i+2) {
			int j=i+1;
			theGrid[Integer.valueOf(wLocation[i])][Integer.valueOf(wLocation[j])]= "w"; //insert all warriros in the 2D array representing the grid	
		}
		}
		
		for (int i=0;i<theGrid.length;i++) {
			for (int j=0;j<theGrid[i].length;j++) {
				if(theGrid[i][j]==null)
				{
					System.out.print("-    ");
				}
				else
				System.out.print(theGrid[i][j]+"    ");
			}
			System.out.println("");
		}
		
	}
	
	public static String solve(String grid, String strategy, boolean visualize) {
		String [] getLocations= splitGrid(grid); //Take the string input and split upon semicolon.
		ArrayList<Point> warriorsPositions = new ArrayList<Point>(); // Store warriors positions in this arraylist
		ArrayList<Point> stonesPositions = new ArrayList<Point>(); // Store warriors positions in this arraylist
		String gridDimensions=getLocations[0]; //Get the dimensions of the grid
		int xaxis=Integer.valueOf(splitComma(gridDimensions)[0]); //Get the x dimension
		int yaxis=Integer.valueOf(splitComma(gridDimensions)[1]); //Get the y dimension
		
		String ironManLocation = getLocations[1];//get Iron Man location (x,y)
		int ix = Integer.valueOf(splitComma(ironManLocation)[0]); //get Iron Man x axis
		int iy = Integer.valueOf(splitComma(ironManLocation)[1]); //get Iron Man y axis
		
		String thanosLocation = getLocations[2]; //get Thanos location (x,y)
		int tx = Integer.valueOf(splitComma(thanosLocation)[0]); //get Thanos x axis
		int ty = Integer.valueOf(splitComma(thanosLocation)[1]); //get Thanos y axis

		String stonesLocation = getLocations[3]; //get the 6 stones locations (x,y)
		int s1x = Integer.valueOf(splitComma(stonesLocation)[0]); //get stone 1 x axis
		int s1y = Integer.valueOf(splitComma(stonesLocation)[1]); //get stone 1 y axis

		stonesPositions.add(new Point(s1x,s1y)); //add the stone to the arraylist
		
		int s2x = Integer.valueOf(splitComma(stonesLocation)[2]); //get stone 2 x axis
		int s2y = Integer.valueOf(splitComma(stonesLocation)[3]); //get stone 2 y axis

		stonesPositions.add(new Point(s2x,s2y)); //add the stone to the arraylist
		
		int s3x = Integer.valueOf(splitComma(stonesLocation)[4]); //get stone 3 x axis
		int s3y = Integer.valueOf(splitComma(stonesLocation)[5]); //get stone 3 y axis
		
		stonesPositions.add(new Point(s3x,s3y)); //add the stone to the arraylist
		
		int s4x = Integer.valueOf(splitComma(stonesLocation)[6]); //get stone 4 x axis
		int s4y = Integer.valueOf(splitComma(stonesLocation)[7]); //get stone 4 y axis
		stonesPositions.add(new Point(s4x,s4y)); //add the stone to the arraylist
		
		int s5x = Integer.valueOf(splitComma(stonesLocation)[8]); //get stone 5 x axis
		int s5y = Integer.valueOf(splitComma(stonesLocation)[9]); //get stone 5 y axis
		stonesPositions.add(new Point(s5x,s5y)); //add the stone to the arraylist
		
		int s6x = Integer.valueOf(splitComma(stonesLocation)[10]); //get stone 6 x axis
		int s6y = Integer.valueOf(splitComma(stonesLocation)[11]); //get stone 6 y axis
		stonesPositions.add(new Point(s6x,s6y)); //add the stone to the arraylist
		
		String warriorsLocation = getLocations[4]; //get the warriors locations
	
		String  wLocation[] = warriorsLocation.split(","); //get all the warriors location in an array
		for (int i=0;i<(wLocation.length);i=i+2) {
			int j=i+1;
			warriorsPositions.add( new Point(Integer.valueOf(wLocation[i]),Integer.valueOf(wLocation[j]))); // add warriors to the arraylist
	}
		//snap collect lef r d u kill
		String [] operators = {"collect","snap","left","right","up","down","kill"};// The array of operators used in this SearchProblem
		String state = generateEGS (xaxis,yaxis,ix,iy,tx,ty,stonesPositions,warriorsPositions,0,true);
		EndGameState endgamestate= new EndGameState(state); // the initial state that we are going to start with
		EndGame eg = new EndGame(operators,endgamestate); //create an instance of end game	
    	String x = eg.GeneralSearch(eg,strategy,visualize);
System.out.println(x);
		return x;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String grid5 = "5,5;2,2;4,2;4,0,1,2,3,0,2,1,4,1,2,4;3,2,0,0,3,4,4,3,4,4";
		String grid6 = "6,6;5,3;0,1;4,3,2,1,3,0,1,2,4,5,1,1;1,3,3,3,1,0,1,4,2,2";
		String grid7 = "7,7;5,4;0,1;5,0,5,6,3,1,4,3,1,2,6,3;2,5,2,6,1,0,5,5,6,5";
		String grid8 = "8,8;7,2;2,2;7,6,2,3,3,0,0,1,6,0,5,5;7,3,4,4,1,6,2,4,2,6";
		String grid9 = "9,9;2,5;3,3;6,2,5,1,3,0,2,8,8,3,0,5;5,4,5,5,1,6,6,3,4,8";
		String grid10 = "10,10;5,1;0,4;3,1,6,8,1,2,9,2,1,5,0,8;7,8,7,6,3,3,6,0,3,8";
		String grid11 = "11,11;9,5;7,1;9,0,8,8,9,1,8,4,2,3,9,10;2,0,0,10,6,3,10,6,6,2";
		String grid12 = "12,12;0,6;9,11;8,3,3,0,11,8,7,4,7,7,10,2;2,8,11,2,2,6,4,6,9,8";
		String grid13 = "13,13;4,2;2,4;6,1,1,10,8,4,9,2,2,8,9,4;6,4,3,4,3,11,1,12,1,9";
		String grid14 = "14,14;2,13;12,7;8,6,9,4,7,1,4,4,4,7,2,3;8,13,0,4,0,8,5,7,10,0";
		String grid15 = "15,15;12,13;5,7;7,0,9,14,14,8,5,8,8,9,8,4;6,6,4,3,10,2,7,4,3,11";
		
//		solve(grid5,"BF",false);
//		solve(grid6,"BF",true);
//		
//		solve(grid5,"DF",false);
//		solve(grid6,"DF",true);
//		
		solve(grid5,"UC",false);
		solve(grid6,"UC",false);
//		
//		solve(grid5,"ID",false);
//		solve(grid6,"ID",true);
//		
//		solve(grid5,"GR1",false);
//		solve(grid6,"GR1",true);
//		
//		solve(grid5,"GR2",false);
//		solve(grid6,"GR2",true);
//		
		solve(grid5,"AS1",false);
		solve(grid6,"AS1",false);
		
		solve(grid5,"AS2",false);
		solve(grid6,"AS2",false);


	}

}
