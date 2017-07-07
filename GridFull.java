/**
 * Working modifiable and expandable grid
 * Allows user to add/remove cells to/from grid, change cell values, copy/paste rows/individual cells, save/open & more!
 * Quick note: I used "col" instead of "cell" in the code
 * Another note: this version creates empty cells in all spaces prior to a new cell e.g. a full grid (takes longer to load with large grid sizes)
 * This build has many known bugs, only the add command works outside the bounds of a grid, lots of repeated/excess code, wrong kind of loops used frequently. I'll fix all that eventually. Probably.
 */

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class GridFull {

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(System.in);
		String command = "";
		
		ArrayList<String> copyCol = new ArrayList<String>();//empty ArrayList for copying a single cell, would have used a String but String wasn't working
		ArrayList<String> copyRow = new ArrayList<String>();//empty ArrayList for copying a full row of cells
		ArrayList<ArrayList<String>> grid = new ArrayList<ArrayList<String>>();//initializes grid to size 0,0
		
		System.out.println("Welcome to the modifiable grid system! \nHere you can fill this empty grid with however many cells you want!(up to 9999 by 9999) \nThere are many different commands to use, to see them all type 'help'.");
		System.out.print(displayGrid(grid));//initially prints grid to console
		
		/*
		 * main grid loop starts
		 * repeats until "quit" is input
		 */
		do {
			
			System.out.println();
			System.out.print("Enter a command: ");
			command = in.nextLine();
			String[] commands = command.split(":");
			
			/*
			 * Processing commands for modifying grid elements
			 */
			if(commands[0].equalsIgnoreCase("add") || commands[0].equalsIgnoreCase("remove") || commands[0].equalsIgnoreCase("change") ||
					commands[0].equalsIgnoreCase("copy") || commands[0].equalsIgnoreCase("paste") || commands[0].equalsIgnoreCase("pasteover") ||
					commands[0].equalsIgnoreCase("save") || commands[0].equalsIgnoreCase("open") || commands[0].equalsIgnoreCase("clear")) {
				processCommand(grid, commands, copyCol, copyRow);
				System.out.print(displayGrid(grid));//prints grid to console
			} else if(commands[0].equalsIgnoreCase("get")) {
				System.out.print(grid.get(Integer.parseInt(commands[2]) - 1).get(Integer.parseInt(commands[3]) - 1));
			} else if(command.equalsIgnoreCase("quit")) {
				System.out.println("Quitting...");
			} else if (command.equalsIgnoreCase("help")) {
				System.out.println(showCommands());
				System.out.print(displayGrid(grid));
			} else {
				System.out.println("Not a recognized command.");
			}
			
		} while(!command.equalsIgnoreCase("quit"));
		
		in.close();
	}
	
	/*
	 * This mostly exists for when I forget how to use all of the commands.
	 * It's pretty useful for other users too, but I should really make the commands easier to use
	 */
	public static String showCommands() {
		String help = "";
		help += "Possible Commands:\n";
		help += "add:<row/col>:<row#>:(if col)<col#>:(if col)<col name>\n";
		help += "remove:<row/col>:<row#>:(if col)<col#>\n";
		help += "change:col:<row#>:<col#>:<col new name>\n";
		help += "get:col:<row#>:<col#>";
		help += "copy:<row/col>:<row#>:(if col)<col#>\n";
		help += "pasteover:<row/col>:<row#>:(if col)<col#>\n";
		help += "paste:<row/col>:<row#>:(if col)<col#>\n";
		help += "save:<file name>\n";
		help += "open:<file name+.filetype>\n";
		help += "clear\n";
		help += "quit\n";
		
		return help;
	}
	
	/*
	 * Displays the grid, complete with col and row numbering, shown as individual boxes and separated by single spaces
	 * empty spaces are displayed by empty spaces, no boxes
	 */
	public static String displayGrid(ArrayList<ArrayList<String>> grid) {
		String display = "";
		String space = "         ";
		int maxCols = 0;
		String numberSpacing = "    ";
		
		for(int i = 0 ; i < grid.size() ; i++) {//finds number of cols in longest row
			if(grid.get(i).size() > maxCols) maxCols = grid.get(i).size();
		}
		
		display += "    ";
		for(int c = 0 ; c < maxCols ; c++) {//displays col number
			int cLength = 1;
			if(c - 99 >= 0) cLength = 3;
			else if(c - 9 >= 0) cLength = 2;
			display += "|    " + (c + 1) + numberSpacing.substring(cLength) + " | ";
		}
		display += "\n";
		
		
		for(int i = 0; i < grid.size(); i++) {
			
			display += "__  ";
			
			for(int j = 0; j < grid.get(i).size(); j++) {//top line of each box
				if(!grid.get(i).get(j).equals("EMPTY")) display += " _________  ";
				else display += "            ";
			}
			display += "\n    ";
			
			for(int m = 0; m < grid.get(i).size(); m++) {//This loop and the next one used to be contained in one loop, but I separated them for some reason and don't feel like fixing them
				if(!grid.get(i).get(m).equals("EMPTY")) display += "|         | ";
				else display += "            ";
			}
			display += "\n    ";
			
			for(int m = 0; m < grid.get(i).size(); m++) {//Third line
				if(!grid.get(i).get(m).equals("EMPTY")) display += "|         | ";
				else display += "            ";
			}
			display += "\n" + (i + 1) + numberSpacing.substring(Integer.toString(i + 1).length()); //displays row number
			
			for(int n = 0; n < grid.get(i).size(); n++) {//mid label (fourth line)
				if(!grid.get(i).get(n).equals("EMPTY")) {
					if(grid.get(i).get(n).length() <= 9) {
						display += "|" + grid.get(i).get(n) + space.substring(grid.get(i).get(n).length()) + "| ";
					} else {
						display += "|" + grid.get(i).get(n).substring(0, 9) + "| ";
					}
				} else display += "            ";
			}
			display += "\n    ";
			
			for(int o = 0; o < grid.get(i).size(); o++) {//bottom mid (Fifth line)
				if(!grid.get(i).get(o).equals("EMPTY")) display += "|         | ";
				else display += "            ";
			}
			display += "\n__  ";
			
			for(int l = 0; l < grid.get(i).size(); l++) {//bottom (Sixth line)
				if(!grid.get(i).get(l).equals("EMPTY")) display += "|_________| ";
				else display += "            ";
			}
			display += "\n";
		}
		return display;
	}
	
	/*
	 * This method takes input from the commands array and parses the command
	 * This method got way too long, should have made separate methods for each command but oh well
	 */
	public static void processCommand(ArrayList<ArrayList<String>> grid, String[] commands, ArrayList<String> copyCol, ArrayList<String> copyRow) throws FileNotFoundException {//processes input command
		
		if(commands[0].equalsIgnoreCase("add")) { //adding elements
			if(commands[1].equalsIgnoreCase("row")) { //adding row(s)
				if (Integer.parseInt(commands[2]) - 1 <= grid.size()) { //add a row at specified index if index is <= total number of rows
					grid.add((Integer.parseInt(commands[2]) - 1), new ArrayList<String>());
				} else { //add a row at specified index, if index is > total rows in grid, fills in rows between with empty rows 
					int initRows = grid.size(); //gets total number of rows in the grid
					for(int i = 0 ; i < Integer.parseInt(commands[2]) - initRows - 1 ; i++) { //adds empty rows until the input index is reached
						grid.add(initRows, new ArrayList<String>());
					}
					grid.add(Integer.parseInt(commands[2]) - 1 , new ArrayList<String>()); //adds empty row at input index
				}
			} else if(commands[1].equalsIgnoreCase("col")) { //adding col(s)
				if ((Integer.parseInt(commands[2]) - 1) < grid.size()) { //adds col if the specified number of rows is <= current number in grid
					int initCols;
					if((Integer.parseInt(commands[3]) - 1) <= grid.get(Integer.parseInt(commands[2]) - 1).size()) { //add a column to a specific row if specified col index is <= current number in that row
						for(int i = 0 ; i < Integer.parseInt(commands[2]) - 1 ; i++) { //adds empty cells in each row before input index until each row is the same length
							initCols = grid.get(i).size(); //gets length of each row
							for(int j = 0 ; j < Integer.parseInt(commands[3]) ; j++) {
								grid.get(i).add(initCols, "EMPTY");
							}
						}
						for(int i = Integer.parseInt(commands[2]) ; i < grid.size() ; i++) { //adds empty cells in each row after input index until each row is the same length
							initCols = grid.get(i).size();
							for(int j = 0 ; j < Integer.parseInt(commands[3]) ; j++) {
								grid.get(i).add(initCols, "EMPTY");
							}
						}
						grid.get(Integer.parseInt(commands[2]) - 1).add(Integer.parseInt(commands[3]) - 1, commands[4]); //adds cell with input text at specified index
					} else {//if specified col number is too big, adds empty cols until correct position exists
						initCols = grid.get(Integer.parseInt(commands[2]) - 1).size();
						for(int i = 0 ; i < Integer.parseInt(commands[3]) - initCols - 1; i++) {
							grid.get(Integer.parseInt(commands[2]) - 1).add(initCols, "EMPTY");
						}
						grid.get(Integer.parseInt(commands[2]) - 1).add(Integer.parseInt(commands[3]) - 1 , commands[4]);
					}
				} else { //if specified row is too big, adds empty rows until the correct row exists, then adds col to specified position
					int initRows = grid.size();
					int initCols;
					for(int i = 0 ; i < Integer.parseInt(commands[2]) - initRows - 1 ; i++) { //adds empty rows up to the position of added col
						grid.add(initRows, new ArrayList<String>());
					}
					for(int i = 0 ; i < Integer.parseInt(commands[2]) - 1 ; i++) {//adds empty cols in each row up to position of new col
						initCols = grid.get(i).size();
						for(int j = 0 ; j < Integer.parseInt(commands[3]) - initCols ; j++) {
							grid.get(i).add(initCols, "EMPTY");
						}
					}
					grid.add(Integer.parseInt(commands[2]) - 1 , new ArrayList<String>());
					
					//same loop to add empty cells and specified cell 
					if((Integer.parseInt(commands[3]) - 1) <= grid.get(Integer.parseInt(commands[2]) - 1).size()) { //add a column to a specific row if specified col index is <= current number in that row
						grid.get(Integer.parseInt(commands[2]) - 1).add(Integer.parseInt(commands[3]) - 1, commands[4]);
					} else {//if specified col number is too big, adds empty cols until correct position exists
						initCols = grid.get(Integer.parseInt(commands[2]) - 1).size();
						for(int i = 0 ; i < Integer.parseInt(commands[3]) - initCols - 1; i++) {
							grid.get(Integer.parseInt(commands[2]) - 1).add(initCols, "EMPTY");
						}
						grid.get(Integer.parseInt(commands[2]) - 1).add(Integer.parseInt(commands[3]) - 1 , commands[4]);
					}
				}
			} else System.out.println("Not recognized as row or col");
		}
		else if(commands[0].equalsIgnoreCase("remove")) { //removes a col or full row
			if(commands[1].equalsIgnoreCase("row") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {//removes specified row
				grid.remove(Integer.parseInt(commands[2]) - 1);
			} else if(commands[1].equalsIgnoreCase("col") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {//removes specified col
				if(Integer.parseInt(commands[3]) - 1 < grid.get(Integer.parseInt(commands[2]) - 1).size()) {
					grid.get(Integer.parseInt(commands[2]) - 1).remove(Integer.parseInt(commands[3]) - 1);
				}
			}
		}
		else if(commands[0].equalsIgnoreCase("change")) { //changes an element to a new String
			if(commands[1].equalsIgnoreCase("col") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {
				if(Integer.parseInt(commands[3]) - 1 < grid.get(Integer.parseInt(commands[2]) - 1).size()) {
					grid.get(Integer.parseInt(commands[2]) - 1).set(Integer.parseInt(commands[3]) - 1, commands[4]);
				}
			}
		} 
		else if(commands[0].equalsIgnoreCase("copy")) {//copies a row or col, overwrites previous copy
			if(commands[1].equalsIgnoreCase("row") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {
				copyRow.removeAll(copyRow);
				for(int i = 0 ; i < grid.get(Integer.parseInt(commands[2]) - 1).size() ; i++) {
					copyRow.add(grid.get(Integer.parseInt(commands[2]) - 1).get(i));
				}
			} else if(commands[1].equalsIgnoreCase("col") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {
				if(Integer.parseInt(commands[3]) - 1 < grid.get(Integer.parseInt(commands[2]) - 1).size()) {
					copyCol.removeAll(copyCol);
					copyCol.add(grid.get(Integer.parseInt(commands[2]) - 1).get(Integer.parseInt(commands[3]) - 1));
				}
			}
		} 
		else if(commands[0].equalsIgnoreCase("pasteover")) {//pastes copied row or col over existing specified row/col
			if(commands[1].equalsIgnoreCase("row") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {//replaces full row
				grid.remove(Integer.parseInt(commands[2]) - 1);//removes original row at input position
				grid.add(Integer.parseInt(commands[2]) - 1, new ArrayList<String>());//adds copied row to input position
				for(int i = 0 ; i < copyRow.size() ; i++) grid.get(Integer.parseInt(commands[2]) - 1).add(copyRow.get(i));
			} else if(commands[1].equalsIgnoreCase("col") && (Integer.parseInt(commands[2]) - 1) < grid.size()) { //replaces single col
				if(Integer.parseInt(commands[3]) - 1 < grid.get(Integer.parseInt(commands[2]) - 1).size()) {
					grid.get(Integer.parseInt(commands[2]) - 1).remove(Integer.parseInt(commands[3]) - 1);
					grid.get(Integer.parseInt(commands[2]) - 1).add(Integer.parseInt(commands[3]) - 1, copyCol.get(0));
				}
			}
		}
		else if(commands[0].equalsIgnoreCase("paste")) {//pastes copied row/col into new specified row/col place
			if(commands[1].equalsIgnoreCase("row") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {//pastes full row
				grid.add(Integer.parseInt(commands[2]) - 1, new ArrayList<String>());//adds copied row to input position
				for(int i = 0 ; i < copyRow.size() ; i++) grid.get(Integer.parseInt(commands[2]) - 1).add(copyRow.get(i));
			} else if(commands[1].equalsIgnoreCase("col") && (Integer.parseInt(commands[2]) - 1) < grid.size()) {//pastes single col
				if(Integer.parseInt(commands[3]) - 1 < grid.get(Integer.parseInt(commands[2]) - 1).size()) {//adds copied col to input position
					grid.get(Integer.parseInt(commands[2]) - 1).add(Integer.parseInt(commands[3]) - 1, copyCol.get(0));
				}
			}
		}
		else if(commands[0].equalsIgnoreCase("save")) {//saves to file
			File saveFile = new File(commands[1] + ".txt");
			PrintStream out = new PrintStream(saveFile);
			for(int r = 0; r < grid.size(); r++) { //each cell with text is saved in the file in the format "cellText\t<row#>:<col#>"
				for(int c = 0; c < grid.get(r).size(); c++) {
					if(!grid.get(r).get(c).equals("EMPTY")) {
						out.println(grid.get(r).get(c) + "\t" + r + ":" + c);
					}
				}
			}
			out.close();
		}
		else if(commands[0].equalsIgnoreCase("open")) {//opens file
			File inputFile = new File(commands[1]);
			Scanner inFile = new Scanner(inputFile);
			grid.clear();
			while(inFile.hasNextLine()) {//takes each line from a file
				String line = inFile.nextLine();
				String[] data = line.split("\t"); //splits each line into text and location
				String[] location = data[1].split(":"); //splits location into row and col
				while(Integer.parseInt(location[0]) >= grid.size()) { //adds empty rows until necessary number is reached
					grid.add(new ArrayList<String>());
				}
				while(Integer.parseInt(location[1]) >= grid.get(Integer.parseInt(location[0])).size()) {//adds empty cols in each row until necessary number is reached
					grid.get(Integer.parseInt(location[0])).add("EMPTY");
				}
				grid.get(Integer.parseInt(location[0])).set(Integer.parseInt(location[1]), data[0]);//sets appropriate cells to text from input file
			}
			inFile.close();
		}
		else if(commands[0].equalsIgnoreCase("clear")) {//clears data
			grid.clear();
		}
		
	}
}