package com.example.aljoodadmin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.model.Lense;
import com.example.aljoodadmin.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterLense extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Lense>> _listDataChild;
int groupPosition;
String lenseId ,listDataHeader;

    public ExpandableListAdapterLense(Context context, List<String> listDataHeader,
                                      HashMap<String, List<Lense>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
//

    }



    public interface OnItemClickListener {
        void onItemClicked(String document, String lenseId, boolean isCheck);

    }

    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        Log.d("ttt","setOnItemClickListener");
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

        final Lense lense = (Lense) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        final TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        final TextView avilable = (TextView) convertView.findViewById(R.id.avilable);

        // final SwitchMaterial  switchMaterial=convertView.findViewById(R.id.switch1);
        ConstraintLayout container = convertView.findViewById(R.id.container);
        //    switchMaterial.setChecked(false);


        txtListChild.setText("SPH = " + lense.getSph() + " // cly = " + lense.getCyl());
        if(lense.isAvailable()){
            avilable.setText("متوفر");
            avilable.setTextColor(_context.getResources().getColor(R.color.green));

        }else{
            avilable.setText("غير متوفر");
            avilable.setTextColor(_context.getResources().getColor(R.color.red));


        }
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lense.isAvailable()){
                    Log.d("ttt",_listDataHeader.size()+"");

                    onItemClickListener.onItemClicked(
                            _listDataHeader.get(groupPosition)
                            , lense.getId()
                            , false);
                }else{
                    Log.d("ttt",_listDataHeader.size()+"");
                    onItemClickListener.onItemClicked(
                            _listDataHeader.get(groupPosition)
                            , lense.getId()
                            , true);
                }
            }
        });

/*        switchMaterial.setChecked(lense.isAvailable());
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchMaterial.setChecked(false);

            }

        });*/

        Animation animation = AnimationUtils.loadAnimation(_context, R.anim.anim);
        convertView.startAnimation(animation);
        txtListChild.setBackgroundColor(_context.getResources().getColor(R.color.back_Child_2));
        txtListChild.setTextColor(_context.getResources().getColor(R.color.white));
        convertView.setBackgroundColor(_context.getResources().getColor(R.color.back_Child));
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        } catch (NullPointerException e) {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        if (isExpanded) {
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_top, 0);
        } else {
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_bottom, 0);

        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}