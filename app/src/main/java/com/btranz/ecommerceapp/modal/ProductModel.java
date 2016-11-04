package com.btranz.ecommerceapp.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductModel implements Parcelable, Comparable<ProductModel> {
    private int id;
    private String title;
    private String description;
    private double cost;
    private double finalPrice;
    private int count;
    private String thumbnail;
    private String share;
    private String tag;
    private int discount;
    private int rating;
    private String standCost;
    private String expCost;
    private String subTitle;
    private String subTitle1;
    public ProductModel() {
        super();
    }

    private ProductModel(Parcel in) {
        super();
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.cost = in.readDouble();
        this.finalPrice = in.readDouble();
        this.count = in.readInt();
        this.thumbnail = in.readString();
        this.share = in.readString();
        this.tag = in.readString();
        this.discount = in.readInt();
        this.rating = in.readInt();
        this.standCost = in.readString();
        this.expCost = in.readString();
        this.subTitle = in.readString();
        this.subTitle1 = in.readString();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStandCost() {
        return standCost;
    }

    public void setStandCost(String standCost) {
        this.standCost = standCost;
    }

    public String getExpCost() {
        return expCost;
    }

    public void setExpCost(String expCost) {
        this.expCost = expCost;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public String getSubTitle1() {
        return subTitle1;
    }

    public void setSubTitle1(String subTitle1) {
        this.subTitle1 = subTitle1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getDescription());
        parcel.writeDouble(getCost());
        parcel.writeDouble(getFinalPrice());
        parcel.writeInt(getCount());
        parcel.writeString(getThumbnail());
        parcel.writeString(getShare());
        parcel.writeString(getTag());
        parcel.writeInt(getDiscount());
        parcel.writeInt(getRating());
        parcel.writeString(getStandCost());
        parcel.writeString(getExpCost());
        parcel.writeString(getSubTitle());
        parcel.writeString(getSubTitle1());
    }
    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
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
        ProductModel other = (ProductModel) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + title +  ", description=" + description +", cost=" + cost +", finalPrice=" + finalPrice +", count=" + count + ", imageUrl="
                + thumbnail +  ", share=" + share + ", Tag=" + tag +", Discount=" + discount + ", rating=" + rating + ", standCost=" + standCost + ", expCost=" + expCost +", subTitle=" + subTitle +", subTitle1=" + subTitle1 +"]";
    }


    @Override
    public int compareTo(ProductModel other) {
//        int compareFinalPrice = ((ProductModel) other).getFinalPrice();
        return 0;
    }
}
