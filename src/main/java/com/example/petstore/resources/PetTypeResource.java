package com.example.petstore.resources;

import com.example.petstore.exceptions.ExceptionStatus;
import com.example.petstore.entity.PetType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/v1/petTypes")
@Produces("application/json")
public class PetTypeResource {
    private List<PetType> petTypes;

    public PetTypeResource() {
        this.petTypes = new ArrayList<PetType>();
    }

    @GET
    @Schema(ref = "PetType")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPetTypes() {
        if (this.petTypes.size() == 0)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ExceptionStatus(404, "Records not found"))
                    .build();

        return Response.ok(this.petTypes).build();
    }

    @GET
    @Schema(ref = "PetType")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{petTypeId}")
    public Response getPetType(@PathParam("petTypeId") int petTypeId) {
        PetType petType = this.findPetTypeById(petTypeId);
        if (petType != null) {
            return Response.ok(petType).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "No records found for Id"))
                .build();
    }

    @POST
    @Schema(ref = "PetType")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPetType(PetType petType) {
        petType.setId(this.petTypes.size() + 1);
        this.petTypes.add(petType);
        return Response.ok(petType).build();
    }

    @PUT
    @Path("{petTypeId}")
    @Schema(ref = "PetType")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePet(PetType petType, @PathParam("petTypeId") int petTypeId) {
        for (PetType currentPetType : this.petTypes) {
            if (currentPetType.getId() == petTypeId) {
                if (petType.getName() != null)
                    currentPetType.setName(petType.getName());
                return Response.ok(currentPetType).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "No records found for Id"))
                .build();
    }

    @DELETE
    @Schema(ref = "PetType")
    @Path("{petTypeId}")
    public Response deletePetType(@PathParam("petTypeId") int petTypeId) {
        PetType petType = this.findPetTypeById(petTypeId);
        if (petType != null) {
            petTypes.remove(petType);
            return Response.ok(petType).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ExceptionStatus(404, "No records found for Id"))
                .build();
    }

    //    find by id
    private PetType findPetTypeById(int petTypeId) {
        for (PetType currentPetType : this.petTypes) {
            if (currentPetType.getId() == petTypeId) {
                return currentPetType;
            }
        }
        return null;
    }

}
