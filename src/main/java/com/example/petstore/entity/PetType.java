package com.example.petstore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PetType")
public class PetType {

    @Schema(required = true, description = "id")
    @JsonProperty("id")
    private int id;

    @Schema(required = true, description = "name")
    @JsonProperty("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
