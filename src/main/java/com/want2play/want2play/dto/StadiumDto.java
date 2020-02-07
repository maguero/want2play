package com.want2play.want2play.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StadiumDto {

    private String id;
    @NotNull
    private String name;
    private String address;
    @NotNull
    private String city;
    private List<FieldDto> fields;

    public StadiumDto() {
    }

    public StadiumDto(String name, String address, String city) {
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

    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StadiumDto that = (StadiumDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(city, that.city) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, city, fields);
    }

    public static class Builder {

        private StadiumDto self;
        private String id;
        private String name;
        private String address;
        private String city;
        private List<FieldDto> fields;

        public Builder() {
            this.self = new StadiumDto();
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

        public Builder withFields(List<FieldDto> fields) {
            this.fields.addAll(fields);
            return this;
        }

        public StadiumDto build() {
            self.setId(this.id);
            self.setName(this.name);
            self.setAddress(this.address);
            self.setCity(this.city);
            self.setFields(this.fields);
            return self;
        }
    }

}
