package com.example.playandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.WebViewActivity;
import com.example.playandroid.model.MainPageBean;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView time;
        TextView title;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_author);
            time = itemView.findViewById(R.id.tv_time);
            title = itemView.findViewById(R.id.tv_title);


        }
    }


    private int TYPE_HEADER = 1001;
    private Context mContext;
    private List<MainPageBean.DataBean.DatasBean> mList;
    private boolean isLoadingAdded = false;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int[] images = new int[]{R.mipmap.app, R.mipmap.flutter, R.mipmap.ms};

    public DataAdapter(Context context, List<MainPageBean.DataBean.DatasBean> list) {
        this.mContext = context;
        this.mList = list;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        for(int i = 0; i < images.length; i ++) {
            ImageView im = new ImageView(mContext);
            im.setBackgroundResource(images[i]);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("lulu", "onClick: ");
                }
            });
            imageViews.add(im);
        }
    }

    public void setData(List<MainPageBean.DataBean.DatasBean> dataList) {
        this.mList = dataList;
        Log.i("cyz", "setData: "+mList.size());
        //notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout, viewGroup, false);

        if (i == TYPE_HEADER){
            View headerView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.headerlayout,viewGroup,false);
            return new DataAdapter.HeaderViewHolder(headerView);

        }
        return new DataAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyHolder ){
            String author = mList.get(i).getAuthor();
            String time = mList.get(i).getNiceDate();
            String title = mList.get(i).getTitle();
           // String link = mList.get(i).getLink();

            ((MyHolder)viewHolder).author.setText(time);
            ((MyHolder)viewHolder).time.setText(time);
            ((MyHolder)viewHolder).title.setText(title);
            if (mOnItemClickListen != null){
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewHolder.getAdapterPosition();
                        mOnItemClickListen.onItemClick(v,position);

                    }
                });
            }
        } else {
            Log.i("lisa", "onBindViewHolder: ....  我是頭部具");
        }


    }

    @Override
    public int getItemCount() {
        Log.i("cyz", "getItemCount: "+mList.size());
        return mList.size() + 1;

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    //点击事件接口
    public interface OnItemClickListen{
        void  onItemClick(View view,int position);
    }

    private OnItemClickListen mOnItemClickListen;
    public void setOnItemClickListen(OnItemClickListen onItemClickListen){
        this.mOnItemClickListen = onItemClickListen;
    }

    public void add(MainPageBean.DataBean.DatasBean fruit) {
        mList.add(fruit);
        notifyItemInserted(mList.size() - 1);
    }

    public void addAll(List<MainPageBean.DataBean.DatasBean> list) {
        for (MainPageBean.DataBean.DatasBean mc : list) {
            add(mc);
        }
    }

    public void remove(MainPageBean.DataBean.DatasBean city) {
        int position = mList.indexOf(city);
        if (position > -1) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            mList.remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        mList.add(new MainPageBean.DataBean.DatasBean());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mList.size() - 1;
        MainPageBean.DataBean.DatasBean item = getItem(position);

        if (item != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public MainPageBean.DataBean.DatasBean getItem(int position) {
        return mList.get(position);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        private ViewPager viewPager;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        viewPager = itemView.findViewById(R.id.view_pager);

            PagerAdapter adapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return imageViews.size();
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    container.addView(imageViews.get(position));
                    return imageViews.get(position);
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                    super.destroyItem(container, position, object);
                    container.removeView(imageViews.get(position));
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                    return view == o;
                }
            };
            viewPager.setAdapter(adapter);


        }
    }
}
