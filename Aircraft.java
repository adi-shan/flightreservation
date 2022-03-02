public class Aircraft implements Comparable<Aircraft>
{
  int numEconomySeats;
  int numFirstClassSeats;
  
  String model;
  String[][] seatLayout; //2d array of seats
  int rows;
  int columns;
  
  public Aircraft(int seats, String model) // default constructor for non firstclass flights
  {
  	this.numEconomySeats = seats;
  	this.numFirstClassSeats = 0;
  	this.model = model;
  	this.rows = 4;
  	this.columns = numEconomySeats / rows;
  	this.seatLayout = new String[columns][rows];
  	initSeatLayout(rows, columns, numFirstClassSeats);
  }

  public Aircraft(int economy, int firstClass, String model) // Constructor for a firstclass flight
  {
  	this.numEconomySeats = economy;
  	this.numFirstClassSeats = firstClass;
  	this.model = model;
  	this.rows = 4;
  	this.columns = (numEconomySeats + numFirstClassSeats) / rows;
  	this.seatLayout = new String[columns][rows];
  	initSeatLayout(rows, columns, numFirstClassSeats);
  }
	/** @return number of economy seats*/
	public int getNumSeats()
	{
		return numEconomySeats;
	}
	/** @return economy and firstclass seats combined*/
	public int getTotalSeats()
	{
		return numEconomySeats + numFirstClassSeats;
	}
	/** @return number of first class seats seats*/
	public int getNumFirstClassSeats()
	{
		return numFirstClassSeats;
	}
	/** @return model of aircraft*/
	public String getModel()
	{
		return model;
	}
	/**
	 * set the aircraft model
	 * @param model aircraft model*/
	public void setModel(String model)
	{
		this.model = model;
	}
	/** prints the description of the aircraft*/

	public String[][] getSeatLayout(){
		return seatLayout;
	} //gets seat layout

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void print()
	{
		System.out.println("Model: " + model + "\t Economy Seats: " + numEconomySeats + "\t First Class Seats: " + numFirstClassSeats);
	}

	/**
	 * Creates the seat layout 2d array during the constructor execution
	 * iterates through column amount and uses the value for the seat number
	 * iterates through row amount and also uses that number as an index to get the specified letter from the alphabet
	 * @param rows how many rows should be created
	 * @param columns how many columns should be created
	 * @param numFirstClass number is used to add the + in the first class seats at the head of the plane
	 */
	public void initSeatLayout(int rows, int columns, int numFirstClass){
		String[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""); //uses alphabet but only from A-D for 4 seaters
		for (int i = 0; i < columns; i++){ //loop through columns
			for (int u = 0; u < rows; u++){ //loop through rows
				if (numFirstClass > 0){ //if there is still first class passenger seats that need to be created
					seatLayout[i][u] =(i + 1) + alphabet[u] + ("+"); //create the seat number with a +
					numFirstClass --; //decrements amount of first class seats left to be made
				}
				else {
					seatLayout[i][u] = (i + 1) + alphabet[u]; //create seat without a +
				}
			}

		}
	}

	/**
	 * compareTo method fom comparable interface which compares itself with another Aircraft
	 * @param o other Aircraft
	 * @return 1 if this plane has more seats than other, -1 if other has more, 0 is equal
	 *
	 * */
	@Override
	public int compareTo(Aircraft o) {
		if (numEconomySeats < o.numEconomySeats){
			return -1;
		}
		else if (numEconomySeats > o.numEconomySeats){
			return 1;
		} // Compared if current object had less economy seats than the other, -1 for less 1 for more
		else{
			if (numFirstClassSeats < o.numFirstClassSeats){
				return -1;
			}
			else if (numFirstClassSeats > o.numFirstClassSeats){
				return 1;
			} // If the economy seats are equal, it compares first class and returns accordingly
		}
		return 0;
	}
}