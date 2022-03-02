public class SeatOccupiedException extends Exception{
    public SeatOccupiedException(String seat){
        super("Seat " + seat + " already occupied");
    }
}