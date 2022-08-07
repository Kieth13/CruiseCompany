package model;

import java.util.Objects;

public class Ship {
    private int id;
    private String name;
    private int passengerAmount;

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

    public int getPassengerAmount() {
        return passengerAmount;
    }

    public void setPassengerAmount(int passengerAmount) {
        this.passengerAmount = passengerAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return id == ship.id &&
                passengerAmount == ship.passengerAmount &&
                name.equals(ship.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, passengerAmount);
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", passengerAmount=" + passengerAmount +
                '}';
    }
}
