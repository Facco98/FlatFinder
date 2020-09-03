package it.unitn.disi.lpsmt.flatfinder.model.announce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Announce {

    @SerializedName("id")
    private Integer id;

    @SerializedName("username_creatore")
    private String creatorUsername;

    @SerializedName("tipologia")
    private LocalType type;

    @SerializedName("categoria")
    private Category category;

    @SerializedName("indirizzo")
    private String address;

    @SerializedName("affitto_mensile")
    private float rentPerMonth;

    @SerializedName("descrizione")
    private String description;

    @SerializedName("arredamento")
    private FornitureStatus fornitureStatus;

    @SerializedName("classe_energetica")
    private EnergeticClass energeticClass;

    @SerializedName("inizio_disponibilita")
    private Date start;

    @SerializedName("fine_disponibilita")
    private Date end;

    @SerializedName("numero_locali")
    private int nLocals;

    @SerializedName("numero_bagni")
    private int nBathrooms;

    @SerializedName("numero_paino")
    private int floor;

    @SerializedName("dimensione")
    private float size;

    @SerializedName("altre_spese")
    private Float extras;

    @SerializedName("data_annuncio")
    private Date date;

    @SerializedName("contatti")
    private String contact;

    @SerializedName("attivo")
    private boolean active = true;


    @Expose(serialize = true, deserialize = false)
    @SerializedName("latitudine")
    private Double latitudine;

    @Expose(serialize = true, deserialize = false)
    @SerializedName("longitudine")
    private Double longitudine;

    public Announce(Integer id, String creatorUsername, LocalType type, Category category, String address,
                    float rentPerMonth, String description, FornitureStatus fornitureStatus,
                    EnergeticClass energeticClass, Date start, Date end, int nLocals, int nBathrooms,
                    int floor, float size, Float extras, Date date, String contact, boolean active, Double latitudine,
                    Double longitudine) {

        this.id = id;
        this.creatorUsername = creatorUsername;
        this.type = type;
        this.category = category;
        this.address = address;
        this.rentPerMonth = rentPerMonth;
        this.description = description;
        this.fornitureStatus = fornitureStatus;
        this.energeticClass = energeticClass;
        this.start = start;
        this.end = end;
        this.nLocals = nLocals;
        this.nBathrooms = nBathrooms;
        this.floor = floor;
        this.size = size;
        this.extras = extras;
        this.date = date;
        this.contact = contact;
        this.active = active;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public Announce(Integer id, String creatorUsername, LocalType type, Category category, String address, float rentPerMonth, String description, FornitureStatus fornitureStatus, EnergeticClass energeticClass, Date start, Date end, int nLocals, int nBathrooms, int floor, float size, Float extras, Date date, String contact, Double latitudine, Double longitudine) {
        this.id = id;
        this.creatorUsername = creatorUsername;
        this.type = type;
        this.category = category;
        this.address = address;
        this.rentPerMonth = rentPerMonth;
        this.description = description;
        this.fornitureStatus = fornitureStatus;
        this.energeticClass = energeticClass;
        this.start = start;
        this.end = end;
        this.nLocals = nLocals;
        this.nBathrooms = nBathrooms;
        this.floor = floor;
        this.size = size;
        this.extras = extras;
        this.date = date;
        this.contact = contact;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public Announce() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public LocalType getType() {
        return type;
    }

    public void setType(LocalType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public Float getExtras() {
        return extras;
    }

    public void setExtras(Float extras) {
        this.extras = extras;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRentPerMonth() {
        return rentPerMonth;
    }

    public void setRentPerMonth(float rentPerMonth) {
        this.rentPerMonth = rentPerMonth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FornitureStatus getFornitureStatus() {
        return fornitureStatus;
    }

    public void setFornitureStatus(FornitureStatus fornitureStatus) {
        this.fornitureStatus = fornitureStatus;
    }

    public EnergeticClass getEnergeticClass() {
        return energeticClass;
    }

    public void setEnergeticClass(EnergeticClass energeticClass) {
        this.energeticClass = energeticClass;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getnLocals() {
        return nLocals;
    }

    public void setnLocals(int nLocals) {
        this.nLocals = nLocals;
    }

    public int getnBathrooms() {
        return nBathrooms;
    }

    public void setnBathrooms(int nBathrooms) {
        this.nBathrooms = nBathrooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString(){

        StringBuilder sb = new StringBuilder();
        sb.append("ANNOUNCE[").append("tipologia=").append(this.getType());
        sb.append(", arredamento=").append(this.getFornitureStatus()).append(", classeEnergetica=");
        sb.append(this.getEnergeticClass()).append(", affintoM=").append(this.getRentPerMonth()).append("]");
        return sb.toString();

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }
}
