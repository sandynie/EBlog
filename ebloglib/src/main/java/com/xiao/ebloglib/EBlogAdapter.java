package com.xiao.ebloglib;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EBlogAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private static final int VIEW_TYPE_CONTENT = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    /**
     * 没有数据的时候，显示这是一条底线
     */
    public static final int FOOTER_STATE_NO_MORE = 1;
    /**
     * 加载更多中
     */
    public static final int FOOTER_STATE_LOADING = 2;
    /**
     * 加载完成，还有更多的状态
     */
    public static final int FOOTER_STATE_MORE = 3;
    /**
     * Footer状态标记
     */
    private int mFooterViewStatus = FOOTER_STATE_MORE;

    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private List<EBlogDataBean> mDatas = new ArrayList<>();
    /**
     * 点击加载更多的响应
     */
    private View.OnClickListener mOnFooterClickListener;

    private static class EBLogItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvTitle;
        private TextView mTvContent;
        private ImageButton mBtnZan;
        private ImageButton mBtnFollow;
        private TextView mZanTimes;
        private TextView mFollowTimes;


        public EBLogItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_item_title);
            mTvContent = itemView.findViewById(R.id.tv_item_content);
            mBtnFollow = itemView.findViewById(R.id.btn_item_follow);
            mBtnZan = itemView.findViewById(R.id.btn_item_zan);
            mZanTimes = itemView.findViewById(R.id.tv_item_zan_times);
            mFollowTimes = itemView.findViewById(R.id.tv_item_follow_times);
            mBtnZan.setTag(itemView);
            mBtnFollow.setTag(itemView);
        }

        public void setClickListener(View.OnClickListener clickListener){
            this.mBtnFollow.setOnClickListener(clickListener);
            this.mBtnZan.setOnClickListener(clickListener);
        }
    }

    private static class VideoFooterViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvInfo;

        public VideoFooterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvInfo = itemView.findViewById(R.id.tv_item_footer_info);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);

        void onFollowClick(View v,int position);

        void onZanClick(View v,int position);
    }


    public EBlogAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<EBlogDataBean> data) {
        if (data == null) {
            mDatas.clear();
        } else {
            mDatas = data;
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTENT) {
            View item = LayoutInflater.from(mContext).inflate(R.layout.item_eblog, parent, false);
            return new EBLogItemViewHolder(item);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            View item = LayoutInflater.from(mContext).inflate(R.layout.item_eblog_footer, parent, false);
            return new VideoFooterViewHolder(item);
        } else { // default分支
            View item = LayoutInflater.from(mContext).inflate(R.layout.item_eblog, parent, false);
            return new EBLogItemViewHolder(item);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EBLogItemViewHolder) {
            EBlogDataBean li = mDatas.get(position);
            if (!TextUtils.isEmpty(li.getmTitle())) {
                ((EBLogItemViewHolder) holder).mTvTitle.setText(li.getmTitle());
            }

            if (!TextUtils.isEmpty(li.getmContent())) {
                ((EBLogItemViewHolder) holder).mTvContent.setText(li.getmContent());
            }

            ((EBLogItemViewHolder) holder).mFollowTimes.setText(li.getmFollowTimes()+"");
            ((EBLogItemViewHolder) holder).mZanTimes.setText(li.getmZanTimes()+"");


            // 标记ItemClicked 用
            holder.itemView.setTag(position);
            EBLogItemViewHolder viewHolder = (EBLogItemViewHolder)holder;
            viewHolder.itemView.setOnClickListener(this);
            viewHolder.setClickListener(this);
        } else if (holder instanceof VideoFooterViewHolder) {
            // 点击加载更多的Footer
            switch (mFooterViewStatus) {
                case FOOTER_STATE_MORE:
                    ((VideoFooterViewHolder) holder).mTvInfo.setText(R.string.click_to_load_more);
                    break;
                case FOOTER_STATE_LOADING:
                    ((VideoFooterViewHolder) holder).mTvInfo.setText(R.string.loading);
                    break;
                case FOOTER_STATE_NO_MORE:
                default:
                    ((VideoFooterViewHolder) holder).mTvInfo.setText(R.string.i_am_bottom_line);
                    break;
            }

            // 有数据的时候透传出去
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFooterViewStatus == FOOTER_STATE_MORE &&
                            mOnFooterClickListener != null) {
                        mOnFooterClickListener.onClick(v);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size()) {
            return VIEW_TYPE_FOOTER;
        }

        return VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        // 有数据的时候才需要加Footer
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size() + 1;
        }

        return 0;
    }


    public EBlogDataBean getItemDataByIndex(int index){
        if (index < 0 || index > mDatas.size()) {
            return null;
        }

        return mDatas.get(index);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            if (v.getId() == R.id.btn_item_follow){
                View parent = (View) v.getTag();
                mItemClickListener.onFollowClick(parent,(Integer)parent.getTag());
            }else if (v.getId() == R.id.btn_item_zan){
                View parent = (View) v.getTag();
                mItemClickListener.onZanClick(parent,(Integer)parent.getTag());
            }else {
                mItemClickListener.onItemClick(v, (Integer) v.getTag());
            }
        }
    }

    public void setOnFooterClickListener(View.OnClickListener clickListener) {
        mOnFooterClickListener = clickListener;
    }

    public void setOnItemClickListener(OnItemClickListener click) {
        mItemClickListener = click;
    }

    public void setFooterStatus(int status) {
        if (mFooterViewStatus != status) {
            mFooterViewStatus = status;
            // 最后一行需要改变
            notifyItemChanged(mDatas.size());
        }
    }
}
