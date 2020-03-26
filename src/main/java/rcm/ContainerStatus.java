package rcm;

public class ContainerStatus {
    private double temperature;
    private double humidity;
    private double atmPressure;

    public ContainerStatus(double temperature, double humidity, double atmPressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.atmPressure = atmPressure;
    }

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
        return result;
    }

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
        return true;
    }

}
