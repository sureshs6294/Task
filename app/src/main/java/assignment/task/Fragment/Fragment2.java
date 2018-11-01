package assignment.task.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import assignment.task.R;

public class Fragment2 extends Fragment {

    ImageView imageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.activity_fragment2, container, false);
        Bundle bundle = getArguments();
        String name = bundle.getString("NAME");
        String image = bundle.getString("image");
        Log.d("name", ">" + name);
        Log.d("image", ">" + image);

        imageView = (ImageView) view.findViewById(R.id.view_image);

        collapsingToolbarLayout=(CollapsingToolbarLayout)view.findViewById(R.id.user_collapsing_toolbar);
        toolbar=(Toolbar)view.findViewById(R.id.user_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle(name);
        if (image != null) {
            Glide.with(getActivity()).load(image).into(imageView);
        }
        return view;

    }

}
