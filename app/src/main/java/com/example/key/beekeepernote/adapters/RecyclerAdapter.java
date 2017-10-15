package com.example.key.beekeepernote.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.key.beekeepernote.R;
import com.example.key.beekeepernote.activities.ActionActivity_;
import com.example.key.beekeepernote.interfaces.Communicator;
import com.example.key.beekeepernote.models.Beehive;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.key.beekeepernote.activities.StartActivity.MODE_CLEAN_ITEM;
import static com.example.key.beekeepernote.activities.StartActivity.MODE_MULTI_SELECT;
import static com.example.key.beekeepernote.activities.StartActivity.MODE_SELECT_ALL;

/**
 * Created by Key on 09.07.2017.
 */
public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public static final String USER_SELECTED_BEEHIVE = "user_selected_beehive" ;
    public static final String NAME_APIARY = "name_apiary" ;
    private List<Beehive> mBeehiveList;
    private int selectMode;
    public String nameApiary;
    public Communicator communicator;
        //use context to intent Url
    public Context context;
    private int mHheight ;
    private int mWigth;

    public RecyclerAdapter(List<Beehive> beehiveList, String nameApiary, int mode) {
            this.mBeehiveList = beehiveList;
            this.nameApiary = nameApiary;
            this.selectMode = mode;
        }

        public void setList(List<Beehive> beehiveList, String nameApiary, int mode){
            this.mBeehiveList = beehiveList;
            this.nameApiary = nameApiary;
            this.selectMode = mode;
        }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
            public ImageView imageBeehive;
            private ClickListener mClickListener;
            private Beehive mBeehive;
            private TextView mBeehiveNumber;
            private View mItemView;
            private ProgressBar mCheckedTimeProgress1;
            private ProgressBar mCheckedTimeProgress2;
            private ProgressBar mFoodProgress;
            private CircleImageView imageEye;


            public ViewHolder(View itemView, ClickListener listener) {
                super(itemView);

                mClickListener = listener;
                this.mItemView = itemView;
                imageBeehive = (ImageView)itemView.findViewById(R.id.imageBeeHive);
                mBeehiveNumber = (TextView)itemView.findViewById(R.id.textNumberBeeColony);
                mCheckedTimeProgress1 = (ProgressBar)itemView.findViewById(R.id.progressBarCheckedTime1);
                mCheckedTimeProgress1.setMax(10);
                mCheckedTimeProgress2 = (ProgressBar)itemView.findViewById(R.id.progressBarCheckedTime2);
                mCheckedTimeProgress2.setMax(10);
                mFoodProgress = (ProgressBar)itemView.findViewById(R.id.progressBarFood);
                mFoodProgress.setMax(10);
                imageEye = (CircleImageView)itemView.findViewById(R.id.imageEye);
                mBeehive = null;
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);

            }
            @Override
            public void onClick(View v) {
                mClickListener.onPressed(mBeehive, v);
            }

            @Override
            public boolean onLongClick(View view) {
                mClickListener.onLongPressed(mBeehive, view);
                return true;
            }




            public interface ClickListener {
                void onPressed(Beehive beehive, View view);
                void onLongPressed(Beehive beehive, View view);
            }
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_beehive, parent, false);
            context = mView.getContext();
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int wight = metrics.widthPixels;

            ViewGroup.LayoutParams params = mView.getLayoutParams();
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                params.width = (wight / 3) - 5;
                params.height = params.width;
            }else if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                params.width = (wight / 5) - 3;
                params.height = params.width;
            }
            mView.setLayoutParams(params);
            mWigth = (wight / 3) - 5;
            ViewHolder mHolder = new ViewHolder(mView, new ViewHolder.ClickListener() {
                @Override
                public void onPressed(Beehive beehive, View view) {
                    if (selectMode == MODE_CLEAN_ITEM ) {
                        if (beehive != null) {
                            Intent actionActivityIntent = new Intent(context, ActionActivity_.class);
                            actionActivityIntent.putExtra(USER_SELECTED_BEEHIVE, beehive);
                            actionActivityIntent.putExtra(NAME_APIARY, nameApiary);
                            context.startActivity(actionActivityIntent);
                        }
                    }else {
                        communicator = (Communicator)context;
                        communicator.setDataForTools(beehive, view, nameApiary);
                    }
                }

                @Override
                public void onLongPressed(Beehive beehive, View view) {
                    if (selectMode == MODE_CLEAN_ITEM ) {
                        communicator = (Communicator) context;
                        communicator.setDataForTools(beehive, view, nameApiary);
                        selectMode = MODE_MULTI_SELECT;
                    }
                }

            });

            return mHolder;
        }



    @Override
        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {

            holder.mBeehive = mBeehiveList.get(position);
        if (selectMode == MODE_SELECT_ALL){
            holder.mItemView.setBackgroundResource(R.drawable.green_frame);
            communicator = (Communicator)context;
            communicator.setDataForTools(holder.mBeehive, holder.mItemView, nameApiary);
        }
            long diff = holder.mBeehive.getBeeColonies().get(0).getCheckedTime() + (11 * 24 * 60 * 60 * 1000) - Calendar.getInstance().getTime().getTime();
            int deys1 = (int)diff /24 / 60 / 60 / 1000;
            holder.mCheckedTimeProgress1.setProgress(deys1);
        if (holder.mBeehive.getBeeColonies().size() > 1) {
            long diff2 = holder.mBeehive.getBeeColonies().get(1).getCheckedTime() + (11 * 24 * 60 * 60 * 1000) - Calendar.getInstance().getTime().getTime();
            int deys2 = (int) diff2 / 24 / 60 / 60 / 1000;
            holder.mCheckedTimeProgress2.setProgress(deys2);
        }else{
            holder.mCheckedTimeProgress2.setVisibility(View.INVISIBLE);
        }

            if (mWigth != 0){
                ViewGroup.LayoutParams params = holder.mBeehiveNumber.getLayoutParams();
                params.width = mWigth / 4;
                params.height = mWigth / 4;
                holder.mBeehiveNumber.setLayoutParams(params);


                ViewGroup.LayoutParams paramsProgress = holder.mCheckedTimeProgress1.getLayoutParams();
                paramsProgress.height = mWigth / 23;
                holder.mCheckedTimeProgress1.setLayoutParams(paramsProgress);
                holder.mCheckedTimeProgress2.setLayoutParams(paramsProgress);
                ViewGroup.LayoutParams paramsFoodProgress = holder.mFoodProgress.getLayoutParams();
                paramsFoodProgress.height = mWigth / 23;
                holder.mFoodProgress.setLayoutParams(paramsFoodProgress);
                ViewGroup.LayoutParams paramsEye = holder.imageEye.getLayoutParams();
                paramsEye.width = mWigth / 7;
                paramsEye.height = mWigth / 7;
                holder.imageEye.setLayoutParams(paramsEye);
            }
            holder.mBeehiveNumber.setText(String.valueOf(holder.mBeehive.getNumberBeehive()));


            switch (holder.mBeehive.getTypeBeehive()){
                case 1: holder.imageBeehive.setImageResource(R.drawable.ic_beehive_one);
                    return;
                case 2: holder.imageBeehive.setImageResource(R.drawable.ic_beehive_left);
            }


        }

        @Override
        public int getItemCount() {
            return mBeehiveList.size();
        }

    }