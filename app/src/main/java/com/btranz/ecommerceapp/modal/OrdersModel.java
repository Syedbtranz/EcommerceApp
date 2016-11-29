package com.btranz.ecommerceapp.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OrdersModel implements Parcelable {
    private int id;
    private String title;
    private String payment;
    private String date;
    private String status;
    private double cost;
    private int process;
    private int qnty;
    private String thumbnail;
    public OrdersModel() {
        super();
    }

    private OrdersModel(Parcel in) {
        super();
        this.id = in.readInt();
        this.title = in.readString();
        this.payment = in.readString();
        this.date = in.readString();
        this.status = in.readString();
        this.cost = in.readDouble();
        this.qnty = in.readInt();
        this.process = in.readInt();
        this.thumbnail = in.readString();
    }
    // getter and setter method for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // getter and setter method for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // getter and setter method for payment
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    // getter and setter method for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // getter and setter method for date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // getter and setter method for cost
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    // getter and setter method for process
    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    // getter and setter method for process
    public int getQnty() {
        return qnty;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

    // getter and setter method for thumbnail
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getPayment());
        parcel.writeString(getDate());
        parcel.writeString(getStatus());
        parcel.writeDouble(getCost());
        parcel.writeInt(getProcess());
        parcel.writeInt(getQnty());
        parcel.writeString(getThumbnail());
    }
    public static final Creator<OrdersModel> CREATOR = new Creator<OrdersModel>() {
        public OrdersModel createFromParcel(Parcel in) {
            return new OrdersModel(in);
        }

        public OrdersModel[] newArray(int size) {
            return new OrdersModel[size];
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        OrdersModel other = (OrdersModel) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + title + ", payment=" + payment + ", date=" + date +", status=" + status + ", cost=" + cost + ", qnty=" + qnty +  ", process=" + process + ", imageUrl="
                + thumbnail + "]";
    }
}
