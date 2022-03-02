import java.util.Comparator;

/**
 * Passenger has a name, passport, seatnum
 * initialize them based off of what was passed in
 * */
public class Passenger implements Comparable<Passenger> {
    private String name;
    private int passport;
    private String seat;
    public Passenger(String name, int passport, String seat){
        this.name = name;
        this.passport = passport;
        this.seat = seat;
    }
    /**
     * getter methods
     * */
    public String getName(){
        return name;
    }
    public int getPassport(){
        return passport;
    }
    public String getSeat(){
        return seat;
    }
    public void setBoarded(boolean boarded){
        this.boarded = boarded;
    }

    /**
     * equals method which checks if 2 Passengers are the same with name and passport
     * @return true if successful
     * */
    public boolean equals (Passenger p){
        if (this.name.equals(p.getName()) && this.passport == p.getPassport()){
            return true;
        }
        return false;
    }

    /**
     * Used for comparing to another passenger in the priority queue
     * @param p other passenger to compare
     * @return return 1 if greater than, -1 if less than, 0 is equal
     */
    @Override
    public int compareTo(Passenger p) {
        return this.getSeat().compareTo(p.getSeat());
    }

    /**
     * method which returns a string of object information
     * */
    public String toString(){
        return "Name: "+ this.name + " Passport Number: " + this.passport + " Seat Number: " + this.seat;
    }

    /**
     * a toString method but for the pasman command
     * @return a string for PASMAN with required details
     */
    public String toManifString(){
        return this.name + " " + this.passport + " " + this.seat;
    }


}