package com.example.key.beekeepernote.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.key.beekeepernote.R;
import com.example.key.beekeepernote.activities.ActionActivity_;
import com.example.key.beekeepernote.interfaces.Communicator;
import com.example.key.beekeepernote.models.Beehive;

import java.util.List;

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


    public RecyclerAdapter(List<Beehive> beehiveList, String nameApiary, int mode) {
            this.mBeehiveList = beehiveList;
            this.nameApiary = nameApiary;
            this.selectMode = mode;
        }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
            public ImageView imageBeehive;
            private ClickListener mClickListener;
            private Beehive mBeehive;
            private TextView mBeehiveNumber;
            private TextView mCountBeecolony;
            private View itemView;

            public ViewHolder(View itemView, ClickListener listener) {
                super(itemView);

                mClickListener = listener;
                this.itemView = itemView;
                imageBeehive = (ImageView)itemView.findViewById(R.id.imageBeeHive);
                mBeehiveNumber = (TextView)itemView.findViewById(R.id.textNumberBeeColony);
                mCountBeecolony = (TextView)itemView.findViewById(R.id.textCountBeecolony);
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
            holder.itemView.setBackgroundResource(R.drawable.green_frame);
            communicator = (Communicator)context;
            communicator.setDataForTools(holder.mBeehive, holder.itemView, nameApiary);
        }
            holder.mBeehiveNumber.setText(String.valueOf(holder.mBeehive.getNumberBeehive()));
            holder.mCountBeecolony.setText(String.valueOf(holder.mBeehive.getBeeColonies().size()));

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