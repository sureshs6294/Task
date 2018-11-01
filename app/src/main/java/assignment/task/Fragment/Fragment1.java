package assignment.task.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import assignment.task.Adapter.DataAdapter;
import assignment.task.Model.DataResponse;
import assignment.task.Model.Datas;
import assignment.task.Model.Item;
import assignment.task.Presenter.GetDataContract;
import assignment.task.Presenter.Presenter;
import assignment.task.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment1 extends Fragment implements GetDataContract.View {

    private Presenter mPresenter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DataAdapter adapter;
    ProgressDialog progressDoalog;
    Integer n=1;
    List<Item> allcountry = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment1, container, false);
        mPresenter = new Presenter(this);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);

//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(R.style.AppTheme_Dark_Dialog);
        progressDoalog.show();
        adapter = new DataAdapter(getActivity(), allcountry);

        adapter.setLoadMoreListener(new DataAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        //int index = list.size() - 1;
                        n++;
                        mPresenter.getDataFromURL(getActivity(), "",n);
                        Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
                       // loadMore(n);
                    }
                });
            }
        });
        mPresenter.getDataFromURL(getActivity(), "",n);
        return view;
    }

    @Override
    public void onGetDataSuccess(String message, final List<Item> list) {
        allcountry=list;
        adapter = new DataAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDoalog.dismiss();
    }
    @Override
    public void onGetDataFailure(String message) {
        Log.d("Status",message);
        progressDoalog.dismiss();
    }

    private void loadMore(int index){
        //add loading progress view
        allcountry.add(new Item());
        adapter.notifyItemInserted(allcountry.size()-1);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        DataResponse request = retrofit.create(DataResponse.class);
        retrofit2.Call<Datas> call = request.get_data_list(index);
        call.enqueue(new retrofit2.Callback<Datas>() {
            @Override
            public void onResponse(retrofit2.Call<Datas> call, retrofit2.Response<Datas> response) {
                if(response.isSuccessful()){
                    //remove loading view
                    allcountry.remove(allcountry.size()-1);

                    Datas jsonResponse = response.body();
                    allcountry = jsonResponse.getItems();
                    if(allcountry.size()>0){
                        //add loaded data
                       // allcountry.addAll(allcountry);
                        adapter = new DataAdapter(getActivity(), allcountry);
                        recyclerView.setAdapter(adapter);
                    }else{//result size 0 means there is no more data available at server
                       // adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(getActivity(),"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                }else{
                  //  Log.e(TAG," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Datas> call, Throwable t) {
               // Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }
}
