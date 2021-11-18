package com.example.petstore;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/v1/pets")
@Produces("application/json")
public class PetResource {

    private List<Pet> pets;

    public PetResource() {
        this.pets = new ArrayList<Pet>();
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "All Pets", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet")))})
    @GET
    public Response getPets() {
        if (this.pets.size() == 0)
            return Response.status(Status.NOT_FOUND).build();
        return Response.ok(this.pets).build();
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Pet for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
            @APIResponse(responseCode = "404", description = "No Pet found for the id.")})
    @GET
    @Path("{petId}")
    public Response getPet(@PathParam("petId") int petId) {
        Pet pet=this.findPetById(petId);
        if(pet !=null){
            return Response.ok(pet).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Add Pet")})
    @POST
    @Schema(ref = "Pet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPet(Pet pet) {
        if(this.findPetById(pet.getPetId())!=null){
            return Response.status(Status.CONFLICT).build();
        }
        this.pets.add(pet);
        return Response.ok(pet).build();
    }

    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Add Pet")})
    @PUT
    @Path("{petId}")
    @Schema(ref = "Pet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePet(Pet pet,@PathParam("petId") int petId) {
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetId() == petId) {
                if(pet.getPetName()!=null)
                    currentPet.setPetName(pet.getPetName());
                if(pet.getPetAge()!=null)
                    currentPet.setPetAge(pet.getPetAge());
                if(pet.getPetType()!=null)
                    currentPet.setPetType(pet.getPetType());
                return Response.ok(currentPet).build();
            }
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    private Pet findPetById(int petId){
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetId() == petId) {
                return currentPet;
            }
        }
        return null;
    }
}
