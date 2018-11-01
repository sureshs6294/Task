package assignment.task.Presenter;

import android.content.Context;

import java.util.List;

import assignment.task.Model.Item;


/**
 * Created by Ashish on 28-09-2017.
 */

public interface GetDataContract {
    interface View{
        void onGetDataSuccess(String message, List<Item> list);
        void onGetDataFailure(String message);
    }
    interface Presenter{
        void getDataFromURL(Context context, String url,Integer id);
    }
    interface Interactor{
        void initRetrofitCall(Context context, String url,Integer id);

    }
    interface onGetDataListener{
        void onSuccess(String message, List<Item> list);
        void onFailure(String message);
    }
}
