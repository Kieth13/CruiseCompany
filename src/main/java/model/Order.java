package model;

import model.enums.Status;

import java.util.Objects;

public class Order {
    private int id;
    private int cruiseId;
    private int userId;
    private int numOfPassengers;
    private float totalPrice;
    private Status status;
    private String userDocs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(int cruiseId) {
        this.cruiseId = cruiseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUserDocs() {
        return userDocs;
    }

    public void setUserDocs(String userDocs) {
        this.userDocs = userDocs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id &&
                cruiseId == order.cruiseId &&
                userId == order.userId &&
                numOfPassengers == order.numOfPassengers &&
                Float.compare(order.totalPrice, totalPrice) == 0 &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cruiseId, userId, numOfPassengers, totalPrice, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cruiseId=" + cruiseId +
                ", userId=" + userId +
                ", numOfPassengers=" + numOfPassengers +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }
}
