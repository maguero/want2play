package com.want2play.want2play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "stadiums")
public class Stadium {

    @Id
    private String id;
    private String name;
    private String address;
    private String city;
    private List<Field> fields;

    public Stadium(String name, String address, String city) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.city = city;
        this.fields = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stadium stadium = (Stadium) o;
        return Objects.equals(id, stadium.id) &&
                Objects.equals(name, stadium.name) &&
                Objects.equals(address, stadium.address) &&
                Objects.equals(city, stadium.city) &&
                Objects.equals(fields, stadium.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, city, fields);
    }
}
