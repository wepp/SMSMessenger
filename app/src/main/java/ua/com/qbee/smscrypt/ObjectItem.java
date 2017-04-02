package ua.com.qbee.smscrypt;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectItem implements Comparable<ObjectItem> {

    public String title;
    public String date;
    public Uri image;
    public String message;
    public long dateL;
    public String address;
    public String addressReal;
    public boolean read;

    public ObjectItem() {
        this.dateL = 0;
        this.address = "";
        this.title = "";
        this.image = null;
        this.message = "";
    }

    public ObjectItem(String title, String messsage, String date, Uri image) {
        this.dateL = 0;
        this.address = "";
        this.title = title;
        this.image = image;
        this.message = messsage;
        this.date = date;
    }

    public void putDate(String st) {
        dateL = Long.valueOf(st);
        Date d = new Date(dateL);
        this.date = new SimpleDateFormat("HH:mm:ss; dd.MM.yyyy").format(d);
    }

    public void putDate(long st) {
        Date d = new Date(st);
        this.date = new SimpleDateFormat("HH:mm:ss; dd.MM.yyyy").format(d);
        dateL = st;
    }

    public void setAdd(String in) {
        addressReal = in;
        title = in;
        if (in.length() < 10) {
            address = in;
            return;
        }
        for (int i = 10; i > 0; i--)
            address += in.charAt(in.length() - i);
    }

    public int compareTo(ObjectItem d) {
        long d1 = dateL;
        long d2 = d.dateL;
        if (d1 < d2) return +1;
        if (d1 > d2) return -1;
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        if (message.length() < 15)
            this.message = message;
        else
            this.message = message.substring(0, 14) + "...";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public void setRead(boolean r) {
        this.read = r;
    }

    public boolean getRead() {
        return read;
    }
}