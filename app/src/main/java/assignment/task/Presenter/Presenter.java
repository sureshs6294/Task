package assignment.task.Presenter;

import android.content.Context;

import java.util.List;

import assignment.task.Model.Item;

/**
 * Created by Ashish on 28-09-2017.
 */

public class Presenter implements GetDataContract.Presenter, GetDataContract.onGetDataListener {
    private GetDataContract.View mGetDataView;
    private Intractor mIntractor;
    public Presenter(GetDataContract.View mGetDataView){
        this.mGetDataView = mGetDataView;
        mIntractor = new Intractor(this);
    }
    @Override
    public void getDataFromURL(Context context, String url,Integer id) {
        mIntractor.initRetrofitCall(context,url,id);
    }

    @Override
    public void onSuccess(String message, List<Item> allCountriesData) {
        mGetDataView.onGetDataSuccess(message, allCountriesData);
    }

    @Override
    public void onFailure(String message) {
        mGetDataView.onGetDataFailure(message);
    }
}
