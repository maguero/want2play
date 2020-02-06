package com.want2play.want2play.dto;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PlayerDto {

    @Id
    private String id;
    @NotNull
    private String name;

    public PlayerDto() {
    }

    public PlayerDto(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDto player = (PlayerDto) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class PlayerUpdateDto {

        public PlayerUpdateDto() {
        }

        public PlayerUpdateDto(String name) {
            this.name = name;
        }

        @NotNull
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
