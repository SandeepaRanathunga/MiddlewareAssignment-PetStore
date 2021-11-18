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
    public Response getPets(@QueryParam("petName") String petName,
                            @QueryParam("petAge") int petAge,
                            @QueryParam("petType") String petType
    ) {
        if (petName != null)
            return this.findByName(petName);
        if (petAge != 0)
            return this.findByAge(petAge);
        if (petType != null)
            return this.findByType(petType);
        if (this.pets.size() == 0)
            return Response.status(Status.NOT_FOUND)
                    .entity(new ExceptionStatus(404, "Records not found"))
                    .build();

        return Response.ok(this.pets).build();
    }

    //    find by name
    private Response findByName(String petName) {
        List<Pet> matchedPets = new ArrayList<Pet>();
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetName().equals(petName)) {
                matchedPets.add(currentPet);
            }
        }
        if (matchedPets.size() > 0) {
            return Response.ok(matchedPets).build();
        }
        return Response.status(Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "Matching records not found"))
                .build();
    }

    //    find by age
    private Response findByAge(int petAge) {
        List<Pet> matchedPets = new ArrayList<Pet>();
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetAge() == petAge) {
                matchedPets.add(currentPet);
            }
        }
        if (matchedPets.size() > 0) {
            return Response.ok(matchedPets).build();
        }
        return Response.status(Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "Matching records not found"))
                .build();
    }

    //    find by type
    private Response findByType(String petType) {
        List<Pet> matchedPets = new ArrayList<Pet>();
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetType().equals(petType)) {
                matchedPets.add(currentPet);
            }
        }
        if (matchedPets.size() > 0) {
            return Response.ok(matchedPets).build();
        }
        return Response.status(Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "Matching records not found"))
                .build();
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Pet for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
            @APIResponse(responseCode = "404", description = "No Pet found for the id.")})
    @GET
    @Path("{petId}")
    public Response getPet(@PathParam("petId") int petId) {
        Pet pet = this.findPetById(petId);
        if (pet != null) {
            return Response.ok(pet).build();
        }
        return Response.status(Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "No records found for Id"))
                .build();
    }

    @APIResponses(value = {@APIResponse(responseCode = "200", description = "Add Pet")})
    @POST
    @Schema(ref = "Pet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPet(Pet pet) {
        if (this.findPetById(pet.getPetId()) != null) {
            return Response.status(Status.CONFLICT)
                    .entity(new ExceptionStatus(409, "Id already exists"))
                    .build();
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
    public Response updatePet(Pet pet, @PathParam("petId") int petId) {
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetId() == petId) {
                if (pet.getPetName() != null)
                    currentPet.setPetName(pet.getPetName());
                if (pet.getPetAge() != null)
                    currentPet.setPetAge(pet.getPetAge());
                if (pet.getPetType() != null)
                    currentPet.setPetType(pet.getPetType());
                return Response.ok(currentPet).build();
            }
        }
        return Response.status(Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "No records found for Id"))
                .build();
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Pet for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
            @APIResponse(responseCode = "404", description = "No Pet found for the id.")})
    @DELETE
    @Path("{petId}")
    public Response deletePet(@PathParam("petId") int petId) {
        Pet pet = this.findPetById(petId);
        if (pet != null) {
            pets.remove(pet);
            return Response.ok(pet).build();
        }
        return Response.status(Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "No records found for Id"))
                .build();
    }

    //    find by id
    private Pet findPetById(int petId) {
        for (Pet currentPet : this.pets) {
            if (currentPet.getPetId() == petId) {
                return currentPet;
            }
        }
        return null;
    }

}
