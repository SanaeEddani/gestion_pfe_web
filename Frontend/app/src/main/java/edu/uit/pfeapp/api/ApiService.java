package edu.uit.pfeapp.api;

import java.util.List;
import edu.uit.pfeapp.model.Departement;
import edu.uit.pfeapp.model.Filiere;
import edu.uit.pfeapp.model.UserRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/filiere")
    Call<List<Filiere>> getFiliere();

    @GET("/departement")
    Call<List<Departement>> getDepartement();

    @POST("/register")
    Call<Void> registerUser(@Body UserRequest userRequest);
}
