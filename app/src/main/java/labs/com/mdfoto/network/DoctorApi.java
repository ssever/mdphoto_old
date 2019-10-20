package labs.com.mdfoto.network;

import labs.com.mdfoto.models.AddState;
import labs.com.mdfoto.models.Doctor;
import labs.com.mdfoto.models.LoginState;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by oktay on 12.06.2016.
 */
public interface DoctorApi {

    @POST("/md2/addDoctor")
    Call<AddState> addDoctor(@Body Doctor doctor);

    @GET("/md2/login")
    Call<LoginState> login(@Query("email") String email, @Query("password") String pass);
}
