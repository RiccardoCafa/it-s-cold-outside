package Controllers;

public class WeatherException extends Exception {

    private String Message;

    public WeatherException(String message) {
        this.Message = message;
    }
}
