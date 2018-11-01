package assignment.task.Model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ashish on 06-02-2017.
 */

public interface DataResponse {
    @GET("/search/users?q=followers:%3E=0&amp;per_page=20&amp;page=id")
    Call<Datas> get_data_list(@Query("id")Integer id);
}
