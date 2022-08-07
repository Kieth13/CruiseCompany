package model;


import java.sql.Date;
import java.util.Objects;

public class Cruise {
    private int id;
    private String name;
    private int shipId;
    private int placesReserved;
    private String routeFrom;
    private String routeTo;
    private int numOfPorts;
    private Date dayStart;
    private Date dayEnd;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShipId() {return shipId;}

    public void setShipId(int shipId) {this.shipId = shipId;}

    public int getPlacesReserved() {
        return placesReserved;
    }

    public void setPlacesReserved(int passengerAmount) {
        this.placesReserved = passengerAmount;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public int getNumOfPorts() {
        return numOfPorts;
    }

    public void setNumOfPorts(int numOfPorts) {
        this.numOfPorts = numOfPorts;
    }

    public Date getDayStart() {
        return dayStart;
    }

    public void setDayStart(Date dayStart) {
        this.dayStart = dayStart;
    }

    public Date getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(Date dayEnd) {
        this.dayEnd = dayEnd;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cruise)) return false;
        Cruise cruise = (Cruise) o;
        return id == cruise.id &&
                shipId == cruise.shipId &&
                placesReserved == cruise.placesReserved &&
                numOfPorts == cruise.numOfPorts &&
                Float.compare(cruise.price, price) == 0  &&
                name.equals(cruise.name) &&
                routeFrom.equals(cruise.routeFrom) &&
                routeTo.equals(cruise.routeTo) &&
                Objects.equals(dayStart, cruise.dayStart) &&
                Objects.equals(dayEnd, cruise.dayEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shipId, placesReserved, routeFrom, routeTo, numOfPorts, dayStart, dayEnd, price);
    }

    @Override
    public String toString() {
        return "Cruise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shipId=" + shipId +
                ", placesReserved=" + placesReserved +
                ", routeFrom='" + routeFrom + '\'' +
                ", routeTo='" + routeTo + '\'' +
                ", numOfPorts=" + numOfPorts +
                ", dayStart=" + dayStart +
                ", dayEnd=" + dayEnd +
                ", price=" + price +
                '}';
    }
}
