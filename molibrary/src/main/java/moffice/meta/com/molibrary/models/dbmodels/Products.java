package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by user on 12/20/2017.
 */

public class Products implements Serializable {
    @DatabaseField(columnName = "_id", generatedId = true)
    @SerializedName("_id")
    @Expose
    private int _id;



    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    @DatabaseField(columnName = "ProductName")
    @SerializedName("ProductName")
    @Expose
    private String ProductName;

    @DatabaseField(columnName = "RecUUID")
    @SerializedName("RecUUID")
    @Expose
    private String RecUUID;

    //0-Not send, 1- Send , 2 - Failed
    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private int SendStatus;

    public String getRecUUID() {
        return RecUUID;
    }

    public void setRecUUID(String recUUID) {
        RecUUID = recUUID;
    }

    public int getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(int sendStatus) {
        SendStatus = sendStatus;
    }
}
