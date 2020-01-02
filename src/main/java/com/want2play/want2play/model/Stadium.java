package com.want2play.want2play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "stadiums")
public class Stadium {

    @Id
    private String id;
    @NotNull
    private String name;
    private String address;
    @NotNull
    private String city;
    private List<Field> fields;

    public Stadium() {
    }

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

    public static class Builder {

        private Stadium self;
        private String id;
        private String name;
        private String address;
        private String city;
        private List<Field> fields;

        public Builder() {
            this.self = new Stadium();
            this.fields = new ArrayList<>();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withFields(List<Field> fields) {
            this.fields.addAll(fields);
            return this;
        }

        public Stadium build() {
            self.setId(this.id);
            self.setName(this.name);
            self.setAddress(this.address);
            self.setCity(this.city);
            self.setFields(this.fields);
            return self;
        }
    }
}
