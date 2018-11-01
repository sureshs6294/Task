package assignment.task.Presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import assignment.task.Adapter.DataAdapter;
import assignment.task.Model.DataResponse;
import assignment.task.Model.Datas;
import assignment.task.Model.Item;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ashish on 28-09-2017.
 */

public class Intractor implements GetDataContract.Interactor{
    private GetDataContract.onGetDataListener mOnGetDatalistener;
    List<Item> allcountry = new ArrayList<>();
    List<String> allCountriesData = new ArrayList<>();
    DataAdapter adapter;

    public Intractor(GetDataContract.onGetDataListener mOnGetDatalistener){
        this.mOnGetDatalistener = mOnGetDatalistener;
    }
    @Override
    public void initRetrofitCall(Context context, String url,Integer id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //add loading progress view
//        allcountry.add(new Item());
//        adapter=new DataAdapter(context.getApplicationContext(),allcountry);
//        adapter.notifyItemInserted(allcountry.size()-1);

        DataResponse request = retrofit.create(DataResponse.class);
        retrofit2.Call<Datas> call = request.get_data_list(id);
        call.enqueue(new retrofit2.Callback<Datas>() {
            @Override
            public void onResponse(retrofit2.Call<Datas> call, retrofit2.Response<Datas> response) {
                if(response.isSuccessful()) {
                    //remove loading view
                  //  allcountry.remove(allcountry.size() - 1);
                    Datas jsonResponse = response.body();
                    allcountry = jsonResponse.getItems();
                    if(allcountry.size()>0)
                    {
                        for (int i = 0; i < allcountry.size(); i++) {
                            allCountriesData.add(allcountry.get(i).toString());
                        }
                        Log.d("Data", "Refreshed");
                        mOnGetDatalistener.onSuccess("List Size: " + allCountriesData.size(), allcountry);
                    }
                    else{
                        //result size 0 means there is no more data available at server
                       // adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                      //  Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                 //   adapter.notifyDataChanged();
                    }
                    else{

                }

            }
            @Override
            public void onFailure(retrofit2.Call<Datas> call, Throwable t) {
                Log.v("Error",t.getMessage());
                mOnGetDatalistener.onFailure(t.getMessage());
            }
        });
    }
}
