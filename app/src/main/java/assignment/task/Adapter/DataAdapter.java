package assignment.task.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import assignment.task.Activity.MainActivity;
import assignment.task.Fragment.Fragment2;
import assignment.task.Model.Item;
import assignment.task.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Item> list = new ArrayList<>();
    MainActivity myActivity;
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    OnLoadMoreListener loadMoreListener=null;
    boolean isLoading = false, isMoreDataAvailable = true;

    public DataAdapter(FragmentActivity context, List<Item> list) {
        this.context = (Activity) context;
        myActivity = (MainActivity) context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MyViewHolder(inflater.inflate(R.layout.card_item,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
//        MyViewHolder myViewHolder = new MyViewHolder(view);
//        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        if(getItemViewType(position)==TYPE_MOVIE){
            ((MyViewHolder)holder).bindData(list.get(position),context,myActivity);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getAvatarUrl()!=null){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountryName;
        ImageView avatars;
        CardView cv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCountryName = (TextView) itemView.findViewById(R.id.name);
            avatars = (ImageView) itemView.findViewById(R.id.avatar);
            cv = (CardView) itemView.findViewById(R.id.card_view);
        }
        void bindData(Item list, Context context, final MainActivity myActivity){
            tvCountryName.setText(list.getLogin());
            final String name = list.getLogin();
            final String imgUrl = list.getAvatarUrl();

            if (context != null) {
                Glide.with(context)
                        .load(imgUrl)
                        .transition(withCrossFade())
                        .apply(new RequestOptions().override(100, 100)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background).centerCrop()
                        )
                        .into(avatars);
            }
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment2 fragmentB = new Fragment2();
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", name);
                    bundle.putString("image", imgUrl);
                    fragmentB.setArguments(bundle);
                    FragmentManager manager = myActivity.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame_container, fragmentB);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
    static class LoadHolder extends RecyclerView.ViewHolder{
        LoadHolder(View itemView) {
            super(itemView);
        }
    }
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }
    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}