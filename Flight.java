import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeMap;

/*
 *  Class to model an airline flight. In this simple system, all flights originate from Toronto
 *  
 *  This class models a simple flight that has only economy seats
 */
public class Flight
{
	public enum Status {DELAYED, ONTIME, ARRIVED, INFLIGHT};
	public enum FlightType {SHORTHAUL, MEDIUMHAUL, LONGHAUL};

	public String flightNum;
	public String airline;
	public String origin, dest;
	public String departureTime;
	public Status status; // see enum Status above. google this to see how to use it
	public int flightDuration;
	public Aircraft aircraft;
	protected int passengers; // count of (economy) passengers on this flight - initially 0
	protected ArrayList<Passenger> manifest;
	protected TreeMap<String, Passenger> seatMap;
	protected PriorityQueue<Passenger> passengerQueue;
	public FlightType flightType;
  
	public Flight()
	{
		this.flightNum = "";
		this.airline = "Airline";
		this.dest = "unknown";
		this.origin = "Toronto";
		this.departureTime = "0000";
		this.flightDuration = 0;
		this.aircraft = new Aircraft(0, "default");
		this.passengers = 0;
		this.status = Status.ONTIME;
		this.flightType = FlightType.MEDIUMHAUL;
		this.manifest = new ArrayList<>();
		this.seatMap = new TreeMap<>();
		this.passengerQueue = new PriorityQueue<>();

	}


	public Flight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		this.flightNum = flightNum;
		this.airline = airline;
		this.dest = dest;
		this.origin = "Toronto";
		this.departureTime = departure;
		this.flightDuration = flightDuration;
		this.aircraft = aircraft;
		this.passengers = 0;
		this.status = Status.ONTIME;
		this.flightType = FlightType.MEDIUMHAUL;
		this.manifest = new ArrayList<>(); // Arraylist holding all the Passenger objects
		this.seatMap = new TreeMap<>();
		this.passengerQueue = new PriorityQueue<>();
		
	}
	// Getter and setter Methods
	public String getFlightNum()
	{
		return flightNum;
	}
	public void setFlightNum(String flightNum)
	{
		this.flightNum = flightNum;
	}
	public String getAirline()
	{
		return airline;
	}
	public void setAirline(String airline)
	{
		this.airline = airline;
	}
	public String getOrigin()
	{
		return origin;
	}
	public void setOrigin(String origin)
	{
		this.origin = origin;
	}
	public String getDest()
	{
		return dest;
	}
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	public String getDepartureTime()
	{
		return departureTime;
	}
	public void setDepartureTime(String departureTime)
	{
		this.departureTime = departureTime;
	}
	public FlightType getFlightType() {
		return flightType;
	}
	public void setFlightType(FlightType flightType) {
		this.flightType = flightType;
	}
	public Status getStatus()
	{
		return status;
	}
	public void setStatus(Status status)
	{
		this.status = status;
	}
	public int getFlightDuration()
	{
		return flightDuration;
	}
	public void setFlightDuration(int dur)
	{
		this.flightDuration = dur;
	}
	public int getPassengers()
	{
		return passengers;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}
	public void setPassengers(int passengers)
	{
		this.passengers = passengers;
	}
	public ArrayList<Passenger> getManifest(){return manifest;} // gets passenger list
	public TreeMap<String, Passenger> getSeatMap() {
		return seatMap;
	}
	public void setSeatMap(TreeMap<String, Passenger> seatMap) {
		this.seatMap = seatMap;
	}

	/** Checks is seats are available by comparing aircraft seats with number of passengers
	 * @return true if more seats that passengers, false otherwise
	 * */
	public boolean seatsAvailable()
	{
		// your code here
		if (aircraft.getNumSeats() > passengers) {
			return true;
		}
		return false;
	}

	/**
	 * Cancels a seat and removes passenger from the manifest and seatmap, by comparing name and passport passed in with iterated iterated list of passengers
	 * decrements passenger count
	 * @param passport passport to use in finding the Passenger
	 * @param name name to use in finding the passenger
	 *
	 * */
	public void cancelSeat(int passport, String name)
	{
		Passenger comparePassenger = new Passenger(name, passport, "TEMP");
		if (passengers > 0) {
			for (Passenger passenger : manifest){
				if (passenger.equals(comparePassenger)){
					manifest.remove(passenger);
					seatMap.remove(passenger.getSeat());
					passengers -= 1;
					break;
				}
			}
		}
	}

	/**
	 *Reserves a seat with a passenger
	 * adds 1 to passengers, adds the passenger to manifest and seatMap
	 * @param passenger passenger object to add to manifest
	 * @return true is successful
	 * */
	public boolean reserveSeat(Passenger passenger) // with passenger info
	{
			if (noDuplicate(passenger)){
				passengers += 1;
				manifest.add(passenger);
				seatMap.put(passenger.getSeat(), passenger);
			}
			else{
				return false;
			}
			return true;


	}

	/**
	 * Checks if there already exists a passenger in the manifest
	 * Iterates through manifest and uses passenger.equals(other passenger) to compare
	 * @param p Passenger object that is used to check if a duplicate of it exists in the list
	 * @return true if the p passed in is not in the manifest, false if its already in it
	 * */
	public boolean noDuplicate(Passenger p){
		for (Passenger passenger : manifest){
			if (passenger.equals(p)){
				return false;
			}
		}
		return true;
	}

	/**
	 * called from flightmanager to create the passenger priority queue
	 * adds each passenger in the manifest into the queue
	 */
	public void initPassengerQueue(){
		for (Passenger passenger : manifest){
			passengerQueue.add(passenger);
		}
	}

	/**
	 * iterates through the queue, boards each passenger, changes their boarding status, and sets the plane inflight afterwards
	 * uses regex to get the row number from the seat number
	 * checks if seat us in the requyired range
	 * @param startRow starting row to be boarded
	 * @param endRow ending row to be boarded
	 */
	public void board(int startRow, int endRow) throws PassengerQueueInvalidException {
		if (passengerQueue.isEmpty()){
			throw new PassengerQueueInvalidException("No passengers have been preboarded");
		}
		Iterator<Passenger> iterator = passengerQueue.iterator();
		while (iterator.hasNext()){
			Passenger passenger = iterator.next();
			String seat = passenger.getSeat();

			int passRow = Integer.parseInt(seat.replaceAll("\\D", "")); //just the number in the seat is stored

			if (startRow<= passRow && passRow <= endRow){ //checks if the seat is within the given range
				passenger.setBoarded(true);
				iterator.remove();
			}
		}
		this.setStatus(Status.INFLIGHT);
	}

	/**
	 * iterates through the manifest and calls .toManifString to print the passenger in the correct format
	 */
	public void printPassengerManifest(){
		for (Passenger passenger : manifest){
			System.out.println(passenger.toManifString());
		}
	}
	/**
	 * String which describes the object
	 * @return a string of the flight description
	 * */
	public void printPassengerQueue(){
		System.out.println(passengerQueue.toString());
	}
	public String toString()
	{
		 return airline + "\t Flight:  " + flightNum + "\t Dest: " + dest + "\t Departing: " + departureTime + "\t Duration: " + flightDuration + "\t Status: " + status;
		
	}

  
}