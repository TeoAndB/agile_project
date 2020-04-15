package rcm;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ContainerStatus extends TimeStamp {

    private double temperature;

    private double humidity;

    private double atmPressure;

    /**
     * 
     * Container status constructor
     * 
     * 
     * 
     * @param timestamp   LocalDateTime with at least minute precision denoting the
     * 
     *                    time at which the measurement took place
     * 
     * @param temperature Double of the temperature in the container at the given
     * 
     *                    time
     * 
     * @param humidity    Double of the humidity in the container at the given time
     * 
     * @param atmPressure Double of the air pressure in the container at the given
     * 
     *                    time
     * 
     */

    public ContainerStatus(LocalDateTime timestamp, double temperature, double humidity, double atmPressure) {

        super(timestamp);

        this.temperature = temperature;

        this.humidity = humidity;

        this.atmPressure = atmPressure;

    }

    /**
     * 
     * Override of hashCode method to check all fields
     * 
     */

    @Override

    public int hashCode() {

        final int prime = 31;

        int result = 1;

        long temp;

        temp = Double.doubleToLongBits(atmPressure);

        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = Double.doubleToLongBits(humidity);

        result = prime * result + (int) (temp ^ (temp >>> 32));

        temp = Double.doubleToLongBits(temperature);

        result = prime * result + (int) (temp ^ (temp >>> 32));

        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());

        return result;

    }

    /**
     * 
     * Override of equals method to check all fields
     * 
     */

    @Override

    public boolean equals(Object obj) {

        if (this == obj)

            return true;

        if (obj == null)

            return false;

        if (getClass() != obj.getClass())

            return false;

        ContainerStatus other = (ContainerStatus) obj;

        if (Double.doubleToLongBits(atmPressure) != Double.doubleToLongBits(other.atmPressure))

            return false;

        if (Double.doubleToLongBits(humidity) != Double.doubleToLongBits(other.humidity))

            return false;

        if (Double.doubleToLongBits(temperature) != Double.doubleToLongBits(other.temperature))

            return false;

        if (timestamp == null) {

            if (other.timestamp != null)

                return false;

        } else if (!timestamp.equals(other.timestamp))

            return false;

        return true;

    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getAtmPressure() {
        return atmPressure;
    }
    
    

}