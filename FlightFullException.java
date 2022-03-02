public class FlightFullException extends Exception{
    public FlightFullException(){
        super("Flight is full");
    } // default

    /**
     * Sends message to superclass that the glight with the flightnumber is full
     * @param flightnum flight's flightnumber
     */
    public FlightFullException(String flightnum){
        super("Flight " + flightnum + " is full");
    } // with flightnumber

    /**
     * Sends a message to superclass but with the seat type, fcl or economy as well
     * @param flightnum flight's flightnumber
     * @param seattype the seat type, economy or first class
     */
    public FlightFullException(String flightnum, String seattype){ // with flightnumber and seattype
        super(seattype + " Flight " + flightnum + " is full");
    }
}