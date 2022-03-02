public class InvalidSeatException extends Exception{
    public InvalidSeatException(String seat){
        super("Seat " + seat + " is not a valid seat");
    }
}