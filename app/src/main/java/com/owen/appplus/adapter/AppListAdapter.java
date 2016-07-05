package com.owen.appplus.adapter;

import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.owen.appplus.R;
import com.owen.appplus.bean.BeanApp;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Owen on 2016/7/1.
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private onItemOptionMenuClickListener mOnItemOptionMenuClickListener;
    private List<BeanApp> mAppInfoList;

    public void setAppInfoList(List<BeanApp> appInfoList) {
        mAppInfoList = appInfoList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BeanApp appInfo = mAppInfoList.get(position);

        holder.itemContainer.setTag(appInfo);
        holder.itemContainer.setOnClickListener(mItemClickListener);
        holder.ivAppIcon.setImageBitmap(appInfo.getIcon());
        holder.tvAppName.setText(appInfo.getName());
        holder.tvAppVersionName.setText(appInfo.getVersionName());
        holder.tvAppPackageName.setText(appInfo.getPackageName());
        holder.ivBtnMenu.setTag(appInfo);
        holder.ivBtnMenu.setOnClickListener(mOptionMenuClickListener);
    }

    @Override
    public int getItemCount() {
        return mAppInfoList == null ? 0 : mAppInfoList.size();
    }

    private View.OnClickListener mItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick((BeanApp) v.getTag());
            }
        }
    };

    private View.OnClickListener mOptionMenuClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.item_pop_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (mOnItemOptionMenuClickListener != null) {
                        mOnItemOptionMenuClickListener.onItemOptionMenuClick(item.getItemId(), (BeanApp) view.getTag());
                    }
                    return true;
                }
            });

            try {
                Field mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
                mFieldPopup.setAccessible(true);
                MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
                mPopup.setForceShowIcon(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            popupMenu.show();
        }
    };

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemOptionMenuClickListener(onItemOptionMenuClickListener listener) {
        mOnItemOptionMenuClickListener = listener;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_container) RelativeLayout itemContainer;
        @Bind(R.id.iv_app_icon) ImageView ivAppIcon;
        @Bind(R.id.tv_app_name) TextView tvAppName;
        @Bind(R.id.tv_app_version_name) TextView tvAppVersionName;
        @Bind(R.id.app_package_name) TextView tvAppPackageName;
        @Bind(R.id.iv_btn_menu) ImageButton ivBtnMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BeanApp appInfo);
    }

    public interface onItemOptionMenuClickListener {
        void onItemOptionMenuClick(int menuItemId, BeanApp appInfo);
    }
}
