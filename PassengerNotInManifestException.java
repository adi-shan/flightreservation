public class PassengerNotInManifestException extends Exception{
    public PassengerNotInManifestException(String name, int passport){
        super("Passenger " + name + " " + passport + " Not Found");
    }
}