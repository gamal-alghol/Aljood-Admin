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
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.annotations.Color;
import com.example.aljoodadmin.annotations.ColorString;
import com.example.aljoodadmin.fragment.ContactLenses;
import com.example.aljoodadmin.model.ContactLense;
import com.example.aljoodadmin.model.Lense;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterContactLense extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ContactLense>> _listDataChild;
    int groupPosition;
    String selectedColor;
    String lenseId ,listDataHeader;

    public ExpandableListAdapterContactLense(Context context, List<String> listDataHeader,
                                             HashMap<String, List<ContactLense>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
//

    }



    public interface OnItemClickListener {
        void onItemClicked(String document, String lenseId,int lense, boolean isCheck);

    }

    private ExpandableListAdapterContactLense.OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(ExpandableListAdapterContactLense.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

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

        final ContactLense lense = (ContactLense) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        final TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        final TextView avilable = (TextView) convertView.findViewById(R.id.avilable);

        ConstraintLayout container = convertView.findViewById(R.id.container);
        if (lense.getId().equals("brine")){

            txtListChild.setText("size = " +Math.round(lense.getSph()));

        }else {
            txtListChild.setText("SPH = " + lense.getSph() + " // Color = " +lense.getColor());
        }
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
                            ,Math.round(lense.getSph())
                            , false);
                }else{
                    Log.d("ttt",_listDataHeader.size()+"");
                    onItemClickListener.onItemClicked(
                            _listDataHeader.get(groupPosition)
                            , lense.getId()
                            ,Math.round(lense.getSph())
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

    private void selectedColorMetod(int color) {
        switch (color) {
            case Color.transparent_lences :
                selectedColor = ColorString.transparent_lences;
                break;
            case Color.grey :
                selectedColor = ColorString.grey;
                break;
            case Color.sterling_grey :
                selectedColor = ColorString.sterling_grey;
                break;
            case Color.blue :
                selectedColor = ColorString.blue;
                break;
            case Color.green :
                selectedColor = ColorString.green;
                break;
            case Color.brouwn :
                selectedColor = ColorString.brouwn;
                break;
            case Color.amethyst :
                selectedColor = ColorString.amethyst;
                break;
            case Color.gemstone_green :
                selectedColor = ColorString.gemstone_green;
                break;
            case Color.turquoise :
                selectedColor = ColorString.turquoise;
                break;
            case Color.honey :
                selectedColor = ColorString.honey;
                break;
            case Color.pure_hazel :
                selectedColor = ColorString.pure_hazel;
                break;

        }
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
