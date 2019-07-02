package moffice.meta.com.molibrary.models.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by karan on 20-09-2017 for Save and
 * Retentive table SalesEnquiry table data.
 */
public class SalesEnquiry implements Serializable {

    @DatabaseField(columnName = "EnqID", generatedId = true)
    @SerializedName("EnqID")
    @Expose
    private int EnqID;

    @DatabaseField(columnName = "EnqIDUUID", generatedId = true)
    @SerializedName("EnqIDUUID")
    @Expose
    private int EnqIDUUID;

    @DatabaseField(columnName = "DateTime")
    @SerializedName("DateTime")
    @Expose
    private Date dateTime;

    @DatabaseField(columnName = "CallerNumber")
    @SerializedName("CallerNumber")
    @Expose
    private String CallerNumber;

    @DatabaseField(columnName = "CallerName")
    @SerializedName("CallerName")
    @Expose
    private String CallerName;

    @DatabaseField(columnName = "Product")
    @SerializedName("Product")
    @Expose
    private String Product;

    @DatabaseField(columnName = "Notes")
    @SerializedName("Notes")
    @Expose
    private String Notes;

    @DatabaseField(columnName = "EnqStatus")
    @SerializedName("EnqStatus")
    @Expose
    private String EnqStatus;
    /**
     * 1. NEW
     * 2. Followup
     * 3. Closed
     * 4. Discarded
     */
    @DatabaseField(columnName = "SendStatus")
    @SerializedName("SendStatus")
    @Expose
    private String SendStatus;

    @SerializedName("Status")
    @Expose
    private int Status;
    @DatabaseField(columnName = "OrderValue")
    @SerializedName("OrderValue")
    @Expose
    private float OrderValue;
    @DatabaseField(columnName = "CurrencyType")
    @SerializedName("CurrencyType")
    @Expose

    private String AssignedUser;
    @DatabaseField(columnName = "AssignedUser")
    @SerializedName("AssignedUser")
    @Expose

    private String AssignedUserUUID;
    @DatabaseField(columnName = "AssignedUserUUID")
    @SerializedName("AssignedUserUUID")
    @Expose
    private int CurrencyType;

    public SalesEnquiry() {
    }

    public SalesEnquiry(int EnqID, String DateTime, String time, String callerNumber, String callerName,
                        String product,  int EnqStatus, float orderValue, int currencyType) {
        this.EnqID = EnqID;
        DateTime = DateTime;
        CallerNumber = callerNumber;
        CallerName = callerName;
        Product = product;
        EnqStatus = EnqStatus;
        OrderValue = orderValue;
        CurrencyType = currencyType;
    }

    public int getEnqID() {
        return EnqID;
    }

    public void setEnqID(int EnqID) {
        this.EnqID = EnqID;
    }

    public String getCallerNumber() {
        return CallerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        CallerNumber = callerNumber;
    }

    public String getCallerName() {
        return CallerName;
    }

    public void setCallerName(String callerName) {
        CallerName = callerName;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public float getOrderValue() {
        return OrderValue;
    }

    public void setOrderValue(float orderValue) {
        OrderValue = orderValue;
    }

    public int getCurrencyType() {
        return CurrencyType;
    }

    public void setCurrencyType(int currencyType) {
        CurrencyType = currencyType;
    }
}
