package se.bachstatter.contactlabb3.models;

/**
 * Created by Jocek on 2014-09-17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Contact implements Parcelable {
    /**
     * Class variables
     */
    private String imgUrl;
    private String description;
    private String name;
    private int age;

    public static ArrayList<Contact> staticContactList;

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }


    /**
     * Constructor that sets all class variables.
     *
     * @param imgUrl
     * @param description
     * @param name
     * @param age
     */
    public Contact(String imgUrl, String name, String description, int age) {

        this.imgUrl = imgUrl;
        this.description = description;
        this.name = name;
        this.age = age;
    }

    /**
     * Parcable constructor
     * Takes a Parcel and sets the values to classvariables
     *
     * @param in
     */
    private Contact(Parcel in) {
        imgUrl = in.readString();
        name = in.readString();
        description = in.readString();
        age = in.readInt();
    }

    /**
     * Getter for retruning imgUrl
     * @return
     */
    public String getImgUrl() {
        return imgUrl;
    }
    /**
     * Getter for retruning description
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     * Getter for retruning name
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for retruning age
     * @return
     */
    public int getAge() {
        return age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.age);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}