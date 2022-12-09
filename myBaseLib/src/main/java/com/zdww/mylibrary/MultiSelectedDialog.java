package com.zdww.mylibrary;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsww.jzfp.ui.newsys.model.MultiSelectedBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjie
 * @Create At 2022/4/27 14:52
 * @desc 多选框dialog
 */
public class MultiSelectedDialog extends DialogFragment {
    private View rootView;
    private int screenHeight;
    private List<MultiSelectedBean.DataBean> selectedList = new ArrayList<>();
    private List<MultiSelectedBean.DataBean> datas;

    public static com.gsww.jzfp.view.MultiSelectedDialog newInstance(Bundle bundle) {
        MultiSelectedDialog fragment = new cMultiSelectedDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AreaDialogFragment);
        Bundle bundle = getArguments();
        if (bundle != null) {
            datas = (List<MultiSelectedBean.DataBean>) bundle.getSerializable("data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_multi_layout, container, false);
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
        initView();
    }

    public List<MultiSelectedBean.DataBean> getSelectedList() {
        return selectedList;
    }

    public void updateSelectedList(MultiSelectedBean.DataBean data) {
        int position = datas.indexOf(data);
        datas.get(position).setSelected(false);
        selectedList.remove(data);
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

    private void initView() {
        TextView confirm = rootView.findViewById(R.id.confirm);
        TextView cancel = rootView.findViewById(R.id.cancel);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        MultiSelectedAdapter multiSelectedAdapter = new MultiSelectedAdapter();
        recyclerView.setAdapter(multiSelectedAdapter);
        confirm.setOnClickListener(v -> {
//            if (onConfirmClickedListener != null) {
//                onConfirmClickedListener.onConfirmClicked();
//            }
            dismissAllowingStateLoss();
        });
        cancel.setOnClickListener(v -> {

            dismissAllowingStateLoss();
        });

    }


    class MultiSelectedAdapter extends RecyclerView.Adapter<MultiSelectedAdapter.MultiSelectedViewHolder> {

        @NonNull
        @Override
        public MultiSelectedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MultiSelectedViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.multi_selected_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MultiSelectedViewHolder multiSelectedViewHolder, int i) {
            MultiSelectedBean.DataBean dataBean = datas.get(i);
            multiSelectedViewHolder.tvName.setText(dataBean.getDictName());
            if (dataBean.isSelected()) {
                multiSelectedViewHolder.ivSelected.setVisibility(View.VISIBLE);
                selectedList.add(dataBean);
            } else {
                multiSelectedViewHolder.ivSelected.setVisibility(View.INVISIBLE);
            }

            multiSelectedViewHolder.itemView.setOnClickListener(v -> {
                boolean selected = dataBean.isSelected();
                if (selected) {
                    multiSelectedViewHolder.ivSelected.setVisibility(View.INVISIBLE);
                    dataBean.setSelected(false);
                    selectedList.remove(dataBean);
                } else {
                    multiSelectedViewHolder.ivSelected.setVisibility(View.VISIBLE);
                    dataBean.setSelected(true);
                    selectedList.add(dataBean);
                }
                if (onConfirmClickedListener != null) {
                    onConfirmClickedListener.onConfirmClicked();
                }

            });
        }


        @Override
        public int getItemCount() {
            return datas == null ? 0 : datas.size();
        }

        class MultiSelectedViewHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            ImageView ivSelected;

            public MultiSelectedViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                ivSelected = itemView.findViewById(R.id.iv_selected);
            }
        }

    }

    public interface OnConfirmClickedListener {
        void onConfirmClicked();
    }

    private OnConfirmClickedListener onConfirmClickedListener;

    public void setOnConfirmClickedListener(OnConfirmClickedListener onConfirmClickedListener) {
        this.onConfirmClickedListener = onConfirmClickedListener;
    }

}
