public class Reservation
{
	String flightNum;
	String flightInfo;
	boolean firstClass;
	Passenger passenger;
	String passengerName;
	int passengerPassport;
	String seat;
	
	//default
	public Reservation(String flightNum, String info)
	{
		this.flightNum = flightNum;
		this.flightInfo = info;
		this.firstClass = false;
	}
	// constructor for temporary reservations used for comparing
	public Reservation(String passengerName, int passengerPassport, String flightnum){
		this.passengerName = passengerName;
		this.passengerPassport = passengerPassport;
		this.flightNum = flightnum;
	}
	public Reservation(String flightNum, String info, Passenger passenger)
	{
		this.flightNum = flightNum;
		this.flightInfo = info;
		this.firstClass = false;
		this.passenger = passenger;
		this.passengerName = passenger.getName();
		this.passengerPassport = passenger.getPassport();
		this.seat = passenger.getSeat();
	}
	
	public boolean isFirstClass()
	{
		return firstClass;
	}

	public void setFirstClass()
	{
		this.firstClass = true;
	}

	public String getFlightNum()
	{
		return flightNum;
	}
	
	public String getFlightInfo()
	{
		return flightInfo;
	}

	public Passenger getPassenger(){return passenger;}

	public String getPassengerName() {
		return passengerName;
	}

	public int getPassengerPassport() {
		return passengerPassport;
	}

	public String getSeat() {
		return seat;
	}

	public void setFlightInfo(String flightInfo)
	{
		this.flightInfo = flightInfo;
	}

	public boolean equals(Reservation other){ //overrides the equals method toi compare for equality
		if (other.getFlightNum().equals(flightNum) && other.getPassengerName().equals(passengerName) && other.getPassengerPassport() == passengerPassport){
			return true;
		}
		return false;
	}

	public void print()
	{
		System.out.println(flightInfo);
	}
}