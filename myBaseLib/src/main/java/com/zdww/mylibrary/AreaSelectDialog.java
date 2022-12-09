package com.zdww.mylibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gsww.baselib.utils.Constant;
import com.gsww.baselib.utils.aes.AesCbcUtils;
import com.gsww.jzfp.http.OkHttpUtils;
import com.gsww.jzfp.ui.fpdx.povertyobj.model.AreaDetailBean;
import com.gsww.jzfp.utils.Cache;
import com.gsww.jzfp.utils.Logger;
import com.gsww.jzfp.view.CustomProgressDialog;
import com.gsww.jzfp_henan.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 市、区县、街道（村）选择
 *  //todo 异步请求 导致 activity 、fragment 内存泄漏和空指针问题
 * @author tangxianqiang
 * @since 2020/3/18
 */
public class AreaSelectDialog extends DialogFragment {
    private View rootView;
    private int screenHeight;
    /*tab layout 中的区域数据*/
    private ArrayList<AreaDetailBean.AreaListDTO> areasSelect = new ArrayList<>();
    /*当前正在选择的区域序列，初始为0，表示河南省下城市选择，对应tab layout*/
    private int currentSelect = 0;
    private RecyclerView areaList;
    private AreaListAdapter areaListAdapter;
    private Dialog progressDialog;
    private String areaCode;
    private AreaDetailBean.AreaListDTO currentSelectAreaStr;
    private boolean isUp;
    private int position = -1;
    private boolean isManual = false;
    private SwipeRefreshLayout refresher;
    private Call callNow;
    /*最多选择几级*/
    private int maxLevel = 999;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AreaDialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_area_select, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenHeight = displayMetrics.heightPixels;
        }
        rootView.findViewById(R.id.confirmArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAreaSelectedListener!=null) {
                    if (areaListAdapter.getSelected() != null) {
                        areasSelect.remove(areasSelect.size() -1);
                        areasSelect.add(areaListAdapter.getSelected());
                        areasSelect.add(new AreaDetailBean.AreaListDTO());
                    }
                    onAreaSelectedListener.onAreaSelected(areasSelect);
                }
                dismiss();
            }
        });
        rootView.findViewById(R.id.areaClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        areaList = rootView.findViewById(R.id.areaDataList);
        areaListAdapter = new AreaListAdapter(getActivity(), new ArrayList<>(), new OnCityItemClickListener() {
            @Override
            public void onCityItemClick(AreaDetailBean.AreaListDTO data, int position) {

            }
        });
        areaList.setAdapter(areaListAdapter);

        refresher = rootView.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (currentSelect > 0) {
                        areaCode = areasSelect.get(currentSelect - 1).getAreaCode();
                    } else {
                        areaCode = Cache.USER_AREA_CODE.get("areaCode").toString();
                    }
                    getAreaList(AesCbcUtils.encryptPkcs7(areaCode, Constant.AES_KEY),true);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取区域失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initData();
    }

    /**
     * 默认显示河南省下的城市列表
     */
    private void initData() {
        //默认是河南省
        AreaDetailBean.AreaListDTO defaultCity = new AreaDetailBean.AreaListDTO();
        defaultCity.setAreaName("请选择");
        areasSelect.add(defaultCity);
        setTabLayout();
        areaCode = Cache.USER_AREA_CODE.get("areaCode").toString();
        try {
            isUp = true;
            getAreaList(AesCbcUtils.encryptPkcs7(areaCode, Constant.AES_KEY),false);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "获取区域失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 动态设置当前选中的区域节点
     */
    private void setTabLayout() {
        Log.i("onTabSelected", "setTabLayout");
        isManual = true;
        TabLayout tabArea = rootView.findViewById(R.id.tabArea);
        tabArea.clearOnTabSelectedListeners();
        tabArea.removeAllTabs();
        for (int i = 0; i < areasSelect.size(); i++) {
            Log.i("setTabLayout", "" + (areasSelect.size() - 1 - i));
            tabArea.addTab(tabArea.newTab().setText(areasSelect.get(i).getAreaName()));
        }
        if (currentSelect > 0) {
            isManual = true;
            tabArea.getTabAt(currentSelect - 1).select();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isManual = true;
                tabArea.getTabAt(currentSelect).select();
            }
        }, 100);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isManual = false;
            }
        }, 500);
        tabArea.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isManual) {
                    isManual = false;
                    Log.i("onTabSelected", "onTabSelected by manual");
                    Log.i("onTabSelected", "onTabSelected by manual end");
                    return;
                }
                Log.i("onTabSelected", "onTabSelected by onClick");
                com.gsww.jzfp.ui.fpdx.povertyobj.AreaSelectDialog.this.position = -1;
                areaListAdapter.setOnSelected(-1);
                int position = tab.getPosition();
                ArrayList<AreaDetailBean.AreaListDTO> areasSelectTemp = new ArrayList<>();
                for (int i = 0; i < position; i++) {
                    areasSelectTemp.add(areasSelect.get(i));
                }
                areasSelect.clear();
                areasSelect.addAll(areasSelectTemp);
                AreaDetailBean.AreaListDTO defaultCity = new AreaDetailBean.AreaListDTO();
                defaultCity.setAreaName("请选择");
                areasSelect.add(defaultCity);
                currentSelect = position;
                currentSelectAreaStr = null;
                try {
                    if (currentSelect > 0) {
                        areaCode = areasSelect.get(currentSelect - 1).getAreaCode();
                    } else {
                        areaCode = Cache.USER_AREA_CODE.get("areaCode").toString();
                    }
                    getAreaList(AesCbcUtils.encryptPkcs7(areaCode, Constant.AES_KEY),false);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取区域失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setAreaData() {

    }

    /**
     * 获取指定的区域列表
     *
     * @param areaCode 指定的上级区域
     */
    private void getAreaList(String areaCode,boolean isRefresh) {
        if (!isRefresh) {
            progressDialog = CustomProgressDialog.show(getActivity(), "", "加载中...", true);
            progressDialog.show();
        }
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.basic_sys+"/sys-area-code/getAreaNameByParent")
                .newBuilder();
        urlBuilder.addQueryParameter("areaCode", areaCode);
        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        callNow = client.newCall(request);
        callNow.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //activity 可能finish
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresher.setRefreshing(false);
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Logger.info(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dataStr = new String(response.body().bytes());
                Log.i("getMonitorListData", dataStr);
                JsonObject dataObj = new JsonParser().parse(dataStr).getAsJsonObject();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresher.setRefreshing(false);
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        try {
                            if (dataObj.get("code").getAsInt() == 0) {
                                AreaDetailBean beanData = new Gson().fromJson(dataObj.get("data").getAsJsonObject(),
                                        new TypeToken<AreaDetailBean>() {
                                        }.getType());
                                List<AreaDetailBean.AreaListDTO> recordsDTOS = beanData.getAreaList();
                                if (recordsDTOS == null || recordsDTOS.size() == 0) {
                                    //下一级没有数据，返回
                                    if (areaListAdapter != null && currentSelectAreaStr != null) {
                                        areaListAdapter.setOnSelected(position);
                                    }
                                } else {
                                    areaListAdapter = new AreaListAdapter(getActivity(), recordsDTOS, new OnCityItemClickListener() {
                                        @Override
                                        public void onCityItemClick(AreaDetailBean.AreaListDTO data, int position) {
                                            //如果区域选择的等级等于maxLevel，直接返回，否则继续请求
                                            com.gsww.jzfp.ui.fpdx.povertyobj.AreaSelectDialog.this.position = position;
                                            if(areasSelect.size() == maxLevel){
                                                areaListAdapter.setOnSelected(position);
                                                return;
                                            }
                                            try {
                                                currentSelectAreaStr = data;
                                                getAreaList(AesCbcUtils.encryptPkcs7(data.getAreaCode(), Constant.AES_KEY),false);
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "获取区域失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    areaList.setAdapter(areaListAdapter);

                                    if (!isRefresh) {
                                        if (currentSelectAreaStr != null) {
                                            areasSelect.remove(areasSelect.size() - 1);
                                            areasSelect.add(currentSelectAreaStr);
                                            AreaDetailBean.AreaListDTO defaultCity = new AreaDetailBean.AreaListDTO();
                                            defaultCity.setAreaName("请选择");
                                            areasSelect.add(defaultCity);
                                            currentSelect = currentSelect + 1;
                                        }
                                        setTabLayout();
                                    }

                                }
                            }
                        } catch (Exception e) {
                            Logger.info(e);
                            Toast.makeText(getActivity(), "获取区域失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        if (callNow != null) {
            callNow.cancel();
            callNow = null;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.AreaDialogFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = (int) (screenHeight * 0.7f);
                lp.dimAmount = 0.4f;
                getDialog().getWindow().setAttributes(lp);
            }
        }
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.AreaListViewHolder> {

        private Context mContext;
        private List<AreaDetailBean.AreaListDTO> datas;
        private OnCityItemClickListener onItemClickListener;

        public AreaListAdapter(Context mContext, List<AreaDetailBean.AreaListDTO> datas, OnCityItemClickListener onItemClickListener) {
            this.mContext = mContext;
            this.datas = datas;
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public AreaListAdapter.AreaListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new AreaListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_area_list, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AreaListAdapter.AreaListViewHolder viewHolder, int i) {
            viewHolder.areaName.setText(datas.get(i).getAreaName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onCityItemClick(datas.get(i), i);
                }
            });
            if (i == position) {
                viewHolder.areaSelect.setVisibility(View.VISIBLE);
            } else {
                viewHolder.areaSelect.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }


        public void setOnSelected(int position) {
            this.position = position;
            if (position > -1) {
                notifyDataSetChanged();
            }
        }

        public AreaDetailBean.AreaListDTO getSelected(){
            if (position < 0) {
                return null;
            }
            if (position > datas.size() -1) {
                return null;
            }
            return datas.get(position);
        }

        private int position = -1;

        class AreaListViewHolder extends RecyclerView.ViewHolder {

            TextView areaName;
            ImageView areaSelect;

            public AreaListViewHolder(@NonNull View itemView) {
                super(itemView);
                areaName = itemView.findViewById(R.id.areaName);
                areaSelect = itemView.findViewById(R.id.areaSelect);
            }
        }

    }

    public interface OnCityItemClickListener {
        void onCityItemClick(AreaDetailBean.AreaListDTO data, int position);
    }

    public interface OnAreaSelectedListener{
        void onAreaSelected(ArrayList<AreaDetailBean.AreaListDTO> areasSelect);
    }

    private OnAreaSelectedListener onAreaSelectedListener;

    public void setOnAreaSelectedListener(OnAreaSelectedListener onAreaSelectedListener) {
        this.onAreaSelectedListener = onAreaSelectedListener;
    }
}
