import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class FlightManager
{
  // Contains map of Flights departing from Toronto in a single day
  Map<String, Flight> flights = new TreeMap<>();
  
  String[] cities = 	{"Dallas", "New York", "London", "Paris", "Tokyo"};

  Map<String, Integer> flightTimeMap = new HashMap<>(); //Using a map to set the times for each flight duration! Destination name is the key, and the duration is the value.

  // Contains list of available airplane types and their seat capacity
  ArrayList<Aircraft> airplanes = new ArrayList<Aircraft>();
  
  Random random = new Random(); // random number generator - google "Java class Random". Use this in generateFlightNumber
  
  
  public FlightManager() {

      // Creates some aircraft types with max seat capacities divisible by 4
      airplanes.add(new Aircraft(12, "Bombardier 5000"));
      airplanes.add(new Aircraft(88, "Boeing 737"));
      airplanes.add(new Aircraft(160, "Airbus 320"));
      airplanes.add(new Aircraft(36, "Dash-8 100"));
      airplanes.add(new Aircraft(444, 16, "Boeing 747"));

      // adds times to the flightmap
      flightTimeMap.put("DALLAS", 3);
      flightTimeMap.put("NEWYORK", 1);
      flightTimeMap.put("LONDON", 7);
      flightTimeMap.put("PARIS", 8);
      flightTimeMap.put("TOKYO", 16);

      // generate flights
      String flightNum;
      Flight flight;
      Aircraft airplaneToUse = null;
      try {
          Scanner scanner = new Scanner(new File("src\\flights.txt")); // creates a scanner to the file in src/flights.txt
          while (scanner.hasNextLine()) {
              String line = scanner.nextLine(); //gets the entire line from the scanner
              Scanner lineScanner = new Scanner(line); //creates a new scanner for just inside the line
              String airline = lineScanner.next().replaceAll("_", " ");
              String des = lineScanner.next().replaceAll("_", " "); // replaces underscore with spaces
              String dep = lineScanner.next(); //gets departure location
              String capacity = lineScanner.next(); //gets minimum capacity amount
              int dur = flightTimeMap.get(des.replaceAll(" ", "").toUpperCase()); // gets duration by searching through flight time map with the given destination

              flightNum = generateFlightNumber(airline); //generates random flight num with the method
              if (dur >= 10) { // a longhaul flight
                  for (Aircraft aircraft : airplanes){ //iterate through aircrafts to choose a suitable aircraft with enough seats, and checks if it has first class seats
                      if (aircraft.getNumSeats() > Integer.parseInt(capacity) && aircraft.getNumFirstClassSeats() > 0){// find an aircraft with first class seats
                          airplaneToUse = aircraft; //sets the airplane to use variable
                          break;
                      }
                  }
                  flight = new LongHaulFlight(flightNum, airline, des, dep, dur, airplaneToUse); //creates a lonhaul flight object
              }
              else{
                  for (Aircraft aircraft : airplanes){
                      if (aircraft.getNumSeats() > Integer.parseInt(capacity)){ //find an aircraft with the minimum required amount of seats
                          airplaneToUse = aircraft;
                          break;
                      }
                  }
                  flight = new Flight(flightNum, airline, des, dep, dur, airplaneToUse); // creates a regular flight
              }
              flights.put(flightNum, flight); //adds the flight to the flights map
          }
      }
      catch (FileNotFoundException f){ //prints the message if the file is not found, but it should always be found
          System.out.println(f.getMessage());
      }
  }
  
  /*
   * This private helper method generates and returns a flight number string from the airline name parameter
   * For example, if parameter string airline is "Air Canada" the flight number should be "ACxxx" where xxx is 
   * a random 3 digit number between 101 and 300 (Hint: use class Random - see variable random at top of class)
   * you can assume every airline name is always 2 words. 
   * 
   */
    /**
     * Generates a flight number with the Random class
     * First takes the first letters in the two words of the airline name (Eg Air Canada AC)
     * Generates a random number from 0 to 199 and adds 101 to result to make it from 101 to 300
     * @param airline airline name passed in
     * */
  private String generateFlightNumber(String airline)
  {
    // Your code here
      String name = airline.charAt(0) +  "" + airline.charAt(airline.indexOf(" ") + 1);
      return name + (random.nextInt(199) + 101);
  }

    /**
     * Prints all the flights in the flights map, by iterating through the keyset
     * */
  public void printAllFlights()
  {
      for (String key : flights.keySet()){
          System.out.println(flights.get(key));
      }
  }

    /**
     * prints the manifest by iterating through the flights and printing the manifest for the specified one
     * @param flightnum //the flight's flightnumber
     */
  public void printManifest(String flightnum){
      for (String key : flights.keySet()){
          Flight flight = flights.get(key);
          if (flight.getFlightNum().equals(flightnum)){
              flight.printPassengerManifest();
          }
      }
  }

    /**
     * **OLD METHOD**
     *Checks if seats are available by using the flightnumber to iterate through the flights and find the flight,
     * uses the .seatsAvailable() method from the Flight class to detemine if there are any available
     * @param flightNum used to find flight
     * @throws FlightNotFoundException if a flight isnt found
     * @throws FlightFullException if a flight is full
     * @return true if flight is successfully checked
     * */
  public boolean seatsAvailable(String flightNum) throws FlightNotFoundException, FlightFullException {
      for (String key : flights.keySet()){
          Flight flight = flights.get(key);
          if (flight.getFlightNum().equals(flightNum)) {
              if (flight.seatsAvailable()) {
                  return true;
              } else {
                  throw new FlightFullException(flightNum);
              }
          }
      }
      throw new FlightNotFoundException();
  }
    /**
     * Reserves seat on flight
     * Iterates through flights, finds a flight with the flight number parameter
     * Use flight.seatsAvailable(), return a new reservation
     * @param flightNum number used to find flight
     * @param name used in creating Passenger
     * @param passport used in creating Passenger
     * @throws FlightFullException if flight is full
     * @throws FlightNotFoundException if flight is not found
     * @throws DuplicatePassengerException if a duplicate passenger is found
     * @throws InvalidSeatException if the seat being reserved is not paty of the seat layout
     * @throws SeatOccupiedException if the seat is already occupied
     * @return reservation with parameters flightnum and flight.toString()
     * */
  public Reservation reserveSeatOnFlight(String flightNum, String name, int passport, String seatType, String seat) throws FlightFullException, FlightNotFoundException, DuplicatePassengerException, InvalidSeatException, SeatOccupiedException {
      for (String key : flights.keySet()){
          Flight flight = flights.get(key);
            if (flight.getFlightNum().equals(flightNum)) {
                if (!isDuplicateSeat(seat, flight)) { //checks if seat already exists
                    if (validSeat(seat, flight)) { // checks if seat is a valid seat
                        if (seatType.equals(LongHaulFlight.firstClass) && flight instanceof LongHaulFlight) {//firstclass
                            LongHaulFlight longflight = (LongHaulFlight) flight; //cast to a long haul flight
                            Passenger passenger = new Passenger(name, passport, seat);
                            if (longflight.reserveSeat(LongHaulFlight.firstClass, passenger)) {
                                if (longflight.numFirstClassPassengers < longflight.aircraft.numFirstClassSeats) {
                                    Reservation newRes = new Reservation(flightNum, longflight.toString() + " " + passenger.toString() + "\n         FCL", passenger);
                                    newRes.setFirstClass();
                                    return newRes;
                                } else {
                                    throw new FlightFullException(LongHaulFlight.firstClass, flightNum);
                                }

                            } else {
                                throw new DuplicatePassengerException(passenger);
                            }
                        } else { //economy seat
                            if (flight.seatsAvailable()) {
                                Passenger passenger = new Passenger(name, passport, seat);
                                if (flight.reserveSeat(passenger)) {
                                    return new Reservation(flightNum, flight.toString() + " " + passenger.toString(), passenger);
                                } else {
                                    throw new DuplicatePassengerException(passenger);
                                }
                            } else {
                                throw new FlightFullException(flightNum);
                            }
                        }
                    }
                    else{
                        throw new InvalidSeatException(seat);
                    }
                }
                else{
                    throw new SeatOccupiedException(seat);
                }
            }
            }
        throw new FlightNotFoundException(flightNum);
        }


    /**
     * Cancel an existing passenger reservation
     * search through flights and use.cancelseat() on the one that matches flight number from res
     * @param res reservation for finding flightnumber
     * @param name for cancelseatPSNGR
     * @param passport for cancelseatPSNGR
     * @return true if successful
     * */
    public boolean cancelReservation(Reservation res, int passport, String name) {
        for (String key : flights.keySet()){
            Flight flight = flights.get(key);
            if (flight.getFlightNum().equals(res.getFlightNum())) {
                if (res.isFirstClass()){
                    LongHaulFlight longflight = (LongHaulFlight) flight;
                    longflight.cancelSeat(LongHaulFlight.firstClass, passport, name);
                }
                else {
                    flight.cancelSeat(passport, name);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * iterates through the flights map keyset, and prints the seatlayour 2d array inside of the aircraft.
     * When a reserved seat is detected during the iteration using the duplicateSeat method , it gets replaced with "XX"
     * @param flightnum  flight's flight number
     * @throws FlightNotFoundException  gets thrown if a matching flight is not found
     */
    public void printSeats(String flightnum) throws FlightNotFoundException {
        boolean found = false; //used to check if a matching flight was ever found
        int counter = 0;
        for (String key : flights.keySet()) { //iterate through flights map
            Flight flight = flights.get(key);
            if (flight.getFlightNum().equals(flightnum)){ //flight in iteration is matching to the flighnumber
                for (int u = 0; u < flight.getAircraft().getRows(); u++){ //first 2d array iteration
                    counter += 1;
                    if (counter == 3){ //checks if counter reached 3 different additions, which prints a blank line for formatting
                        System.out.println();
                    }
                    for (int i = 0; i < flight.getAircraft().getColumns(); i ++){ //second level of iteration
                        String seat = (flight.getAircraft().getSeatLayout()[i][u]); //gets the seat at the iteration point
                        if (isDuplicateSeat(seat, flight)){ //if the seat is occupied, prints XX
                            System.out.print("XX ");
                        }
                        else{ //otherwise, prints the seat value
                            System.out.print(seat + " ");
                        }

                    }
                    System.out.println(); //line for formatting
                }
            }
            found = true;
        }
        if (!found){ //if a flight isnt found, throws the exception
            throw new FlightNotFoundException();
        }
        System.out.println(); //formatting
        System.out.println("XX = Occupied    + = First Class");

    }
    /**
     * checks if the seat already exists
     * iterates through the flight seatmap keyset, if the key of the flight is matching, returns true.
     * @param seat
     * @param flight
     * @return true if matching key is found, false otherwise
     */
    public boolean isDuplicateSeat(String seat, Flight flight){
        for (String key : flight.seatMap.keySet()){
            if (key.equalsIgnoreCase(seat)){
                return true;
            }
        }
        return false;
    }

    /**
     * iterates through all of the seat layout, and finds if there is a seat name valid seat name for the given parameter
     * @param seat seat to look for
     * @param flight  flight to look through
     * @return  true if the seat exists, false otehrwise
     */
    public boolean validSeat(String seat, Flight flight){
        String[][] arr = flight.getAircraft().getSeatLayout();
        for (String[] arr1 : arr){
            for (String elem : arr1){
                if (elem.equalsIgnoreCase(seat)){
                    return true;
                }
            }
        }
        return false;
    }

  // Prints all aircraft in airplanes array list. 
  // See class Aircraft for a print() method
    /**
     * performs aircraft.print() on all aircrafts in airplanes arraylist
     * */
  public void printAllAircraft()
  {
  	for (Aircraft aircraft : airplanes){
  	    aircraft.print();
    }
  }
  
  // Sort the array list of Aircraft objects 
  // This is one line of code. Make sure class Aircraft implements the Comparable interface
    /**
     * sort aircrafts using the compareto method in aircraft
     * */
  public void sortAircraft()
  {
  	Collections.sort(airplanes);
  }

    /**
     * finds a matching flight in the flights map, calls initpassengerqueue on the flight
     *
     * @param flightnum flight number to be found
     * @throws PassengerQueueInvalidException if the queue was already initialized
     */
  public void initPassengerQueueForFlight(String flightnum) throws PassengerQueueInvalidException {
      for (String key : flights.keySet()){
          if (key.equalsIgnoreCase(flightnum)){
              if (flights.get(key).passengerQueue.isEmpty()) {
                  flights.get(key).initPassengerQueue();
              }
              else{
                  throw new PassengerQueueInvalidException("Passenger Queue already initialized");
              }
          }
      }
  }

    /**
     * searches through flights for a valid flight with the flightnumber, calls the printpassengerqueue method on it
     * @param flightnum flight number to be found
     */
  public void printPassengerQueueForFlight(String flightnum) {
      for (String key : flights.keySet()) {
          if (key.equalsIgnoreCase(flightnum)) {
            flights.get(key).printPassengerQueue();
          }
      }
  }

    /**
     * finds valid flight with flight number, calls the board() method on the flight with the row interval
     * @param flightnum flight number to be found
     * @param startRow start of the interval
     * @param endRow end of the interval
     * @throws PassengerQueueInvalidException if no passengers have been preboarded (from flight.board)
     */
  public void boardFlight(String flightnum, int startRow, int endRow) throws PassengerQueueInvalidException {
      for (String key : flights.keySet()) {
          if (key.equalsIgnoreCase(flightnum)) {
              Flight flight = flights.get(key);
              flight.board(startRow, endRow);


          }
      }
  }


}
