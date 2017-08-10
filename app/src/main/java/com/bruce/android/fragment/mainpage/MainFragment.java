package com.bruce.android.fragment.mainpage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bruce.android.R;
import com.bruce.android.http.Caller;
import com.bruce.android.http.HttpClient;
import com.bruce.android.http.HttpResponseHandler;
import com.bruce.android.http.RestApiResponse;
import com.bruce.android.model.CompanyBean;
import com.bruce.android.ui.pulltorefresh.PullToRefreshBase;
import com.bruce.android.ui.pulltorefresh.PullToRefreshListView;
import com.bruce.android.ui.quickadapter.BaseAdapterHelper;
import com.bruce.android.ui.quickadapter.QuickAdapter;
import com.bruce.android.utils.CommUtil;
import com.bruce.android.utils.SharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

import static com.bruce.android.utils.Contents.CARDNUMBER;

/**
 * 首页Fragment
 */
public class MainFragment extends Fragment {

    private Activity context;
    private boolean isLoadAll = false;

    @Bind(R.id.listView)
    PullToRefreshListView listView;
    @Bind(R.id.btnBack)
    Button mBtnBack;
    @Bind(R.id.textHeadTitle)
    TextView mHeaderTitle;
    QuickAdapter<CompanyBean> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_shop_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initView();
        loadData();
    }

    void initView() {
        mBtnBack.setVisibility(View.VISIBLE);
        mHeaderTitle.setText("公司列表");

        adapter = new QuickAdapter<CompanyBean>(context, R.layout.item_company_list) {
            @Override
            protected void convert(BaseAdapterHelper helper, CompanyBean companyBean) {
                helper.setText(R.id.item_company_name, companyBean.getNSRMC())
                        .setText(R.id.item_company_tax_num, companyBean.getNSRSBH())
                        .setText(R.id.item_combelong, companyBean.getSJGSDQ()); // 自动异步加载图片
            }
        };

        listView.withLoadMoreView();
        listView.setAdapter(adapter);
        // 下拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        // 加载更多
        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData();
            }
        });
        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                UIHelper.showHouseDetailActivity(context);
//                CommUtil.showAlert("lalala",context);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Picasso.with(context).pauseTag(context);
                } else {
                    Picasso.with(context).resumeTag(context);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void loadData() {
        if (isLoadAll) {
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.onRefreshComplete();
                }
            }, 1000);
            return;
        }
        listView.setLoadMoreViewTextLoading();
       String idCard =  SharedPreferences.getInstance().getString(CARDNUMBER,"");
        HashMap<String, String> params = new HashMap<>();
        params.put("secret", Caller.getCompListParams(idCard));
        HttpClient.get(Caller.BASE_IP,params, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
                String success = response.getSuccess();
                String message = response.getMsg();
                String value = response.getValue();
                List<CompanyBean> companyBeanList = JSON.parseArray(value,CompanyBean.class);
                listView.updateLoadMoreViewText(companyBeanList);
                isLoadAll = companyBeanList.size() < HttpClient.PAGE_SIZE;
                adapter.addAll(companyBeanList);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                isLoadAll = false;
                CommUtil.showToast("登录失败",context);
                listView.setLoadMoreViewTextError();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(context).pauseTag(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(context).cancelTag(context);
    }


    @OnClick(R.id.btnBack)
    public void btnBack(View view){
        context.finish();
    }

    /**
     * 将数据归属地区编号和企业所属分局存到Map中
     */
    private Map<String, String> companyNameMap() {
        String[] companyNameList = this.getResources().getStringArray(R.array.company_list_array);
        Map<String, String> NsrsbhMap = new HashMap<String, String>();
        for (String comName : companyNameList) {
            //截取  13209241240|射阳县国税局四分局管理二股
            String NSRSBH = comName.substring(0, comName.indexOf("|"));// 纳税人识别号/税号
            String COMNAME = comName.substring(comName.indexOf("|")+1, comName.length());//公司名称

            NsrsbhMap.put(NSRSBH, COMNAME);

        }
        return NsrsbhMap;

    }
}