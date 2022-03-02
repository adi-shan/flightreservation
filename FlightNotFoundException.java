public class FlightNotFoundException extends Exception{
    /**
     * No parameters, default
     * */
    public FlightNotFoundException(){
        super("Flight Not Found");
    }
    /**
     * @param flightnum flight number to be passed to use in the message creation
     * */
    public FlightNotFoundException(String flightnum){
        super("Flight " + flightnum + " Not Found");
    }
}