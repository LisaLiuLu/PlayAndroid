package com.example.playandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.playandroid.http.ApiService;
import com.example.playandroid.model.MainPageBean;
import com.example.playandroid.ui.DataAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("ValidFragment")
class MainFragment extends Fragment {
    private List<MainPageBean.DataBean.DatasBean> mList = new ArrayList();
    private DataAdapter mAdapter;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ApiService apiService;


    private int mPageCount = -1;
    private static final int PAGE_START = 0;  //表示是否显示了页脚ProgressBar（即下一页正在加载）
    private boolean isLoading = false;  // 如果当前页面是最后一页（页面加载后分页将停止）
    private boolean isLastPage = false;  //总的页面加载数。初始加载为第0页，之后再加载2页。
    //    private int TOTAL_PAGES = 3;  // 表示分页正在加载的页面。
    private int currentPage = PAGE_START;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, null);
        recyclerView = view.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        initData();

        mAdapter = new DataAdapter(getContext(), mList);
        mAdapter.setOnItemClickListen(new DataAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("lulu", "onItemClick:                "+position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                Log.i("lulu", "onItemClick:                "+mList.size());
                intent.putExtra("data", mList.get(position).getLink());
                Log.i("lulu", "onItemClick:                "+intent + position);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;
                getHttpData(apiService, currentPage);


            }

            @Override
            public int getTatalPageCount() {
                return mPageCount;
            }

            @Override
            public boolean isLastPage() {

               if (currentPage == mPageCount){
                   isLastPage = true;
               }else {
                   isLastPage = false;
               }
                return isLastPage;
            }

            @Override
            public boolean isLoading() {

                return isLoading;
            }
        });

        return view;

    }


    private void initData() {
//        for (int i = 0; i <100 ; i++){
//            list.add(i);0++
//        }
        Log.i("cyz", "onResponse: initData ");
        final String url = HttpUtil.BASE_URL + "/article/list/1/json";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        getHttpDataFrist(apiService, 0);

//        Call<MainPageBean> call = apiService.getData("https://www.wanandroid.com/article/list/1/json");
//        call.enqueue(new Callback<MainPageBean>() {
//            @Override
//            public void onResponse(Call<MainPageBean> call, Response<MainPageBean> response) {
//                Log.i("mainFragment", "onResponse: " + response);
//                List<MainPageBean.DataBean.DatasBean> datas = response.body().getData().getDatas();
//                mAdapter.setData(datas);
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<MainPageBean> call, Throwable t) {
//                Log.i("mainFragment", "失败 ");
//            }
//        });


    }

    private void getHttpDataFrist(ApiService apiService, int page) {
        Call<MainPageBean> call = apiService.getMainPage(page);
        Log.i("cyz", "getHttpDataFrist: getHttpDataFrist");
        call.enqueue(new Callback<MainPageBean>() {
            @Override
            public void onResponse(Call<MainPageBean> call, Response<MainPageBean> response) {
                mPageCount = response.body().getData().getPageCount();
//                List<MainPageBean.DataBean.DatasBean> datas = response.body().getData().getDatas();
                mList= response.body().getData().getDatas();
                mAdapter.setData(mList);
                Log.i("cyz", "onResponse: mlist " + mList.size());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainPageBean> call, Throwable t) {
                Log.i("cyz", "onResponse: onFailure " + t.getMessage());
            }
        });


    }

    private void getHttpData(ApiService apiService, int page) {
        Call<MainPageBean> call = apiService.getMainPage(page);
        Log.i("cyz", "getHttpData: getHttpData");
        call.enqueue(new Callback<MainPageBean>() {
            @Override
            public void onResponse(Call<MainPageBean> call, Response<MainPageBean> response) {
                mPageCount = response.body().getData().getPageCount();
                List<MainPageBean.DataBean.DatasBean> datasBeans = response.body().getData().getDatas();
                isLoading = false;
                mAdapter.addAll(datasBeans);
                Log.i("cyz", "onResponse: datasBeans " + datasBeans.size());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainPageBean> call, Throwable t) {
                Log.i("cyz", "getHttpData onResponse: onFailure " + t.getMessage());
            }
        });
    }
}
