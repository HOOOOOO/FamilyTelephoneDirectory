package com.example.sysucjl.familytelephonedirectory.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sysucjl.familytelephonedirectory.R;
import com.example.sysucjl.familytelephonedirectory.data.RecordItem;
import com.example.sysucjl.familytelephonedirectory.data.RecordSegment;
import com.example.sysucjl.familytelephonedirectory.tools.ColorUtils;
import com.example.sysucjl.familytelephonedirectory.tools.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class RecordExpandAdapter implements ExpandableListAdapter{

    private Context mContext;
    private List<RecordItem> mRecordItems;
    public RecordExpandAdapter(Context context, List<RecordItem> recordItems){
        this.mContext = context;
        this.mRecordItems = recordItems;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mRecordItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mRecordItems.get(groupPosition).getRecordSegments().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public RecordSegment getChild(int groupPosition, int childPosition) {
        return mRecordItems.get(groupPosition).getRecordSegments().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        RecordGroupHolder recordGroupHolder = null;
        if(convertView == null){
            recordGroupHolder = new RecordGroupHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_record_item, parent, false);
            recordGroupHolder.ivRecordIcon = (ImageView) convertView.findViewById(R.id.iv_record_icon);
            recordGroupHolder.tvRecordName = (TextView) convertView.findViewById(R.id.tv_record_name);
            recordGroupHolder.tvRecordDate = (TextView) convertView.findViewById(R.id.tv_record_date);
            recordGroupHolder.llBackground = (LinearLayout) convertView.findViewById(R.id.ll_background);
            recordGroupHolder.vRecordDivide = (View) convertView.findViewById(R.id.v_recordd_divide);
            recordGroupHolder.llRecordType = (LinearLayout) convertView.findViewById(R.id.ll_record_type);
            recordGroupHolder.llChoose = (LinearLayout) convertView.findViewById(R.id.ll_choose);
            recordGroupHolder.tvCallBack = (TextView) convertView.findViewById(R.id.tv_callback);
            recordGroupHolder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
            recordGroupHolder.tvAvatarName = (TextView) convertView.findViewById(R.id.tv_avatar_name);
            recordGroupHolder.ivAvatarSim = (ImageView) convertView.findViewById(R.id.img_avatar_sim);
            recordGroupHolder.tvPhoneNum = (TextView) convertView.findViewById(R.id.tv_phonenum);
            convertView.setTag(recordGroupHolder);
        }else{
            recordGroupHolder = (RecordGroupHolder) convertView.getTag();
        }
        final RecordItem recordItem = mRecordItems.get(groupPosition);

        if(isExpanded) {
            recordGroupHolder.llBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            recordGroupHolder.vRecordDivide.setVisibility(View.VISIBLE);
            recordGroupHolder.llChoose.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(recordItem.getName())){
                if(!TextUtils.isEmpty(recordItem.getNumber())) {
                    recordGroupHolder.tvPhoneNum.setVisibility(View.VISIBLE);
                    recordGroupHolder.tvPhoneNum.setText(recordItem.getNumber());
                }else{
                    recordGroupHolder.tvPhoneNum.setVisibility(View.GONE);
                }
            }else{
                recordGroupHolder.tvPhoneNum.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(recordItem.getNumber())){
                recordGroupHolder.tvCallBack.setVisibility(View.GONE);
            }else{
                recordGroupHolder.tvCallBack.setVisibility(View.VISIBLE);
                recordGroupHolder.tvCallBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + recordItem.getNumber()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        mAdapterListener.collapseGroup(groupPosition);
                    }
                });
            }
        }
        else {
            recordGroupHolder.llBackground.setBackgroundColor(Color.parseColor("#00000000"));
            recordGroupHolder.vRecordDivide.setVisibility(View.GONE);
            recordGroupHolder.llChoose.setVisibility(View.GONE);
        }
        recordGroupHolder.llBackground.setTag(groupPosition);

        if(!TextUtils.isEmpty(recordItem.getName())) {
            char c = recordItem.getName().charAt(0);
            recordGroupHolder.tvRecordName.setText(recordItem.getName());
            //System.out.println(recordItem.getName()+" "+recordItem.getName().hashCode());
            recordGroupHolder.ivAvatarSim.setVisibility(View.GONE);
            recordGroupHolder.tvAvatarName.setVisibility(View.VISIBLE);
            recordGroupHolder.tvAvatarName.setText(""+recordItem.getName().charAt(0));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                recordGroupHolder.ivRecordIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(ColorUtils.getColor(recordItem.getName().hashCode()))));
            }
        }
        else {
            recordGroupHolder.tvPhoneNum.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(recordItem.getNumber())){
                recordGroupHolder.tvRecordName.setText(Tools.getNumberFormat(recordItem.getNumber()));
            }else{
                recordGroupHolder.tvRecordName.setText("未知");
            }
            recordGroupHolder.ivAvatarSim.setVisibility(View.VISIBLE);
            recordGroupHolder.tvAvatarName.setVisibility(View.GONE);
            //System.out.println(recordItem.getNumber()+" "+recordItem.getNumber().hashCode());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                recordGroupHolder.ivRecordIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(ColorUtils.getColor(recordItem.getNumber().hashCode()))));
            }
        }
        recordGroupHolder.llRecordType.removeAllViews();
        for(int i = 0; i < recordItem.getRecordSegments().size(); i++){
            if(i == 3){
                TextView textView = new TextView(mContext);
                textView.setTextSize(12);
                textView.setText("("+recordItem.getRecordSegments().size()+")");
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                recordGroupHolder.llRecordType.addView(textView);
                break;
            }
            int type = recordItem.getRecordSegments().get(i).getType();
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(25, 15));
            switch (type){
                case CallLog.Calls.OUTGOING_TYPE:
                    imageView.setImageResource(R.drawable.ic_outgoing);
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    imageView.setImageResource(R.drawable.ic_incoming);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    imageView.setImageResource(R.drawable.ic_missed);
                    break;
            }
            recordGroupHolder.llRecordType.addView(imageView);
        }
        recordGroupHolder.tvRecordDate.setText(Tools.getRecordItemData(recordItem.getCallTime()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        RecordChildHolder recordChildHolder = null;
        if(convertView == null){
            recordChildHolder = new RecordChildHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.record_segment_item, parent, false);
            recordChildHolder.imgRecordType = (ImageView) convertView.findViewById(R.id.img_record_type);
            recordChildHolder.tvRecordType = (TextView) convertView.findViewById(R.id.tv_record_type);
            recordChildHolder.tvRecordDate = (TextView) convertView.findViewById(R.id.tv_record_date);
            recordChildHolder.tvRecordDuration = (TextView) convertView.findViewById(R.id.tv_record_duration);
            recordChildHolder.imgShadow = (ImageView) convertView.findViewById(R.id.img_shadow);
            convertView.setTag(recordChildHolder);
        }else{
            recordChildHolder = (RecordChildHolder) convertView.getTag();
        }
        RecordSegment recordSegment = getChild(groupPosition, childPosition);
        int callcode = recordSegment.getType();
        switch (callcode) {
            case CallLog.Calls.OUTGOING_TYPE:
                recordChildHolder.imgRecordType.setBackgroundResource(R.drawable.ic_outgoing);
                recordChildHolder.tvRecordType.setText("外拨电话");
                recordChildHolder.tvRecordDuration.setVisibility(View.VISIBLE);
                break;
            case CallLog.Calls.INCOMING_TYPE:
                recordChildHolder.imgRecordType.setBackgroundResource(R.drawable.ic_incoming);
                recordChildHolder.tvRecordDuration.setVisibility(View.VISIBLE);
                recordChildHolder.tvRecordType.setText("来电");
                break;
            case CallLog.Calls.MISSED_TYPE:
                recordChildHolder.imgRecordType.setBackgroundResource(R.drawable.ic_missed);
                recordChildHolder.tvRecordType.setText("未接电话");
                recordChildHolder.tvRecordDuration.setVisibility(View.GONE);
                break;
        }
        recordChildHolder.tvRecordDate.setText(String.valueOf(Tools.getRecordSegmentDate(recordSegment.getCallTime())));
        recordChildHolder.tvRecordDuration.setText(Tools.getDuration(recordSegment.getDuration()));
        if(childPosition == getChildrenCount(groupPosition) - 1){
            recordChildHolder.imgShadow.setVisibility(View.VISIBLE);
        }else{
            recordChildHolder.imgShadow.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class RecordGroupHolder{
        public View vRecordDivide;
        public ImageView ivRecordIcon, ivAvatarSim;
        public TextView tvRecordName, tvCallBack, tvDelete, tvPhoneNum;
        public TextView tvRecordDate, tvAvatarName;
        public LinearLayout llBackground;
        public LinearLayout llRecordType;
        public LinearLayout llChoose;
    }

    class RecordChildHolder{
        public ImageView imgRecordType;
        public TextView tvRecordType;
        public TextView tvRecordDate;
        public TextView tvRecordDuration;
        public ImageView imgShadow;
    }

    private RecordAdapterListener mAdapterListener;

    public void setOnRecordAdapterListener(RecordAdapterListener adapterListener){
        this.mAdapterListener = adapterListener;
    }

    public interface RecordAdapterListener{
        public void collapseGroup(int groupPosition);
    }
}
