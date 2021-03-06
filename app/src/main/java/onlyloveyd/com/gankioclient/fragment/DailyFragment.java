/**
 * Copyright 2017 yidong
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package onlyloveyd.com.gankioclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import onlyloveyd.com.gankioclient.BuildConfig;
import onlyloveyd.com.gankioclient.activity.GankActivity;
import onlyloveyd.com.gankioclient.decorate.OnDatePickedListener;
import onlyloveyd.com.gankioclient.gsonbean.DailyBean;
import onlyloveyd.com.gankioclient.http.HttpMethods;
import onlyloveyd.com.gankioclient.utils.Constant;


/**
 * 文 件 名: DailyFragment
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：每日干货数据
 */
public class DailyFragment extends BaseFragment implements OnDatePickedListener {

    public static DailyFragment newInstance() {

        Bundle args = new Bundle();

        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        ((GankActivity) getActivity()).setOnDatePickedListener(this);
        refreshLayout.autoRefresh();
    }

    @Override
    public void initRefreshLayout() {
        super.initRefreshLayout();
        refreshLayout.setEnableLoadmore(false);
    }


    private void getDaily(int year, int month, int day) {
        Observer<DailyBean> observer = new Observer<DailyBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                endLoading();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                endLoading();
                onNetworkError();
            }

            @Override
            public void onNext(DailyBean dailyBean) {
                if (refreshLayout.isLoading()) {
                } else {
                    mVisitableList.clear();
                }
                if (dailyBean.getCategory() == null || dailyBean.getCategory().size() == 0) {
                    onDataEmpty();
                }
                if (dailyBean.getResults().getAndroid() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getAndroid());
                }
                if (dailyBean.getResults().getApp() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getApp());
                }
                if (dailyBean.getResults().getBonus() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getBonus());
                }
                if (dailyBean.getResults().getIOS() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getIOS());
                }
                if (dailyBean.getResults().getJs() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getJs());
                }
                if (dailyBean.getResults().getRec() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getRec());
                }
                if (dailyBean.getResults().getRes() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getRes());
                }
                if (dailyBean.getResults().getVideo() != null) {
                    mVisitableList.addAll(dailyBean.getResults().getVideo());
                }
                mMultiRecyclerAdapter.setData(mVisitableList);
            }
        };
        HttpMethods.getInstance().getDailyData(observer, year, month, day);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        doRefresh();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshLayout) {
    }


    @Override
    public void onDatePicked(int year, int month, int day) {
        refreshLayout.autoRefresh();
    }

    private void doRefresh() {
        System.err.println("yidong -- doRefresh");
        if (BuildConfig.DEBUG) {
            System.err.println(
                    "yidong --year= " + Constant.YEAR + " Month = " + Constant.MONTH + " day = "
                            + Constant.DAY);
        }
        if (Constant.YEAR == -1 && Constant.MONTH == -1 && Constant.DAY == -1) {
            Date date = new Date(System.currentTimeMillis());
            getDaily(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
        } else {
            getDaily(Constant.YEAR, Constant.MONTH + 1, Constant.DAY);
        }
    }
}
