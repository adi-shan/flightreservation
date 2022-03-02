public class DuplicatePassengerException extends Exception{

    /**
     * passes the message created to the superclass
     * @param passenger used for getting the passenger name and passport to create the message
     */
    public DuplicatePassengerException(Passenger passenger){
        super("Duplicate Passenger "+ passenger.getName() + " " + passenger.getPassport());
    }
}