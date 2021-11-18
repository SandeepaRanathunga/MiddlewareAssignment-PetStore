package com.example.petstore.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.petstore.exceptions.ExceptionStatus;
import com.example.petstore.entity.Pet;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Path("/v1/pets")
@Produces("application/json")
public class PetResource {

    private List<Pet> pets;

    public PetResource() {
        this.pets = new ArrayList<Pet>();
    }
    @GET
    @Schema(ref = "Pet")
    @Produces(MediaType.APPLICATION_JSON)
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
            if (currentPet.getPetType().getName().equals(petType)) {
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

    @GET
    @Schema(ref = "Pet")
    @Produces(MediaType.APPLICATION_JSON)
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

    @POST
    @Schema(ref = "Pet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPet(Pet pet) {
        pet.setPetId(this.pets.size()+1);
        this.pets.add(pet);
        return Response.ok(pet).build();
    }

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

    @DELETE
    @Schema(ref = "Pet")
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
