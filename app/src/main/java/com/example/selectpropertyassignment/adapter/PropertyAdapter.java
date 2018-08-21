package com.example.selectpropertyassignment.adapter;

import android.content.Context;

import android.support.v4.app.FragmentActivity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.selectpropertyassignment.R;
import com.example.selectpropertyassignment.database.FacilityDetailEntity;
import com.example.selectpropertyassignment.database.FacilityEntity;
import com.example.selectpropertyassignment.expandable.AbstractExpandableDataProvider;
import com.example.selectpropertyassignment.expandable.ExpandableItemIndicator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class PropertyAdapter extends AbstractExpandableItemAdapter<PropertyAdapter.MyGroupViewHolder, PropertyAdapter.MyChildViewHolder> {
    private static final String TAG = "MyExpandableItemAdapter";
    private Context context;
    private ArrayList<RealmList<FacilityDetailEntity>> facility = new ArrayList<>();
    private RealmResults<FacilityEntity> facilityEntities;
    private Realm realm;

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {

    }

    private AbstractExpandableDataProvider mProvider;

    public PropertyAdapter(AbstractExpandableDataProvider dataProvider, Date date, FragmentActivity activity) {
        realm = Realm.getDefaultInstance();
        mProvider = dataProvider;
        context = activity;
        setHasStableIds(true);
        updateData();
    }

    public void updateData() {
        facilityEntities = realm.where(FacilityEntity.class).findAll();
        facility.clear();
        for(FacilityEntity facilityEntity : facilityEntities){
            RealmList<FacilityDetailEntity> facilityDetailEntities = facilityEntity.getOptions();
            facility.add(facilityDetailEntities);

        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return facilityEntities.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return facility.get(groupPosition).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        final View v = inflater.inflate(R.layout.list_group_item, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.property_row_item, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {

        FacilityEntity facilityEntity = facilityEntities.get(groupPosition);
        if(facilityEntity == null){
            return;
        }

        holder.propertyName.setText(facilityEntity.getName());
        // mark as clickable
        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
                holder.propertyName.setTextColor(ContextCompat.getColor(context, R.color.menuTextColor));

            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
                holder.propertyName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);

        }
    }

    @Override
    public void onBindChildViewHolder(final MyChildViewHolder holder, final int groupPosition, final int childPosition, int viewType) {
        final AbstractExpandableDataProvider.ChildData item = mProvider.getChildItem(groupPosition, childPosition);
        final FacilityDetailEntity facilityDetailEntity = facility.get(groupPosition).get(childPosition);
        if(facilityDetailEntity == null){
            return;
        }

        holder.propertyType.setText(facilityDetailEntity.getName());

        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;
        holder.mContainer.setBackgroundResource(bgResId);
        int drawableValue = getImage(facilityDetailEntity.getIcon());
        if(drawableValue == 0){
            holder.iconView.setImageResource(R.drawable.no_room);
        }else {
            Glide.with(context)
                    .asBitmap()
                    .load(drawableValue)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder))
                    .into(holder.iconView);
        }

        holder.itemView.setOnClickListener(view -> showChooseItem(facilityDetailEntity.getName()));

    }

    private void showChooseItem(String itemName){
        new AlertDialog.Builder(context)
                .setMessage("You Choose "+"'"+itemName+"'")
                .setPositiveButton("OK", null).show();
    }


    private int getImage(String imageName) {
        return  context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        public FrameLayout mContainer;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer =  v.findViewById(R.id.container);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        @BindView(R.id.indicator) ExpandableItemIndicator mIndicator;
        @BindView(R.id.property_name) TextView propertyName;
        public MyGroupViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        @BindView(R.id.property_type) TextView propertyType;
        @BindView(R.id.iconView) ImageView iconView;
        public MyChildViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        if (mProvider.getGroupItem(groupPosition).isPinned()) {
            return false;
        }
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }

}

