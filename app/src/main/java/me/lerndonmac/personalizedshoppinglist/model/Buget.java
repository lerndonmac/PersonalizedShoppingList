package me.lerndonmac.personalizedshoppinglist.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "buget")
public class Buget {
    @DatabaseField(columnName = "buget")
    private Double buget;
    @DatabaseField(columnName = "id",id = true)
    private int id;
    @DatabaseField(columnName = "bugetText")
    private String bugetText;

    public Double getBuget() {
        return buget;
    }

    public void setBuget(double buget) {
        this.buget = buget;
    }

    public String getBugetText() {
        return bugetText;
    }

    public void setBugetText(String bugetText) {
        this.bugetText = bugetText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
