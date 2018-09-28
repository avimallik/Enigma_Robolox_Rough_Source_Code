package com.enigmarobolox.arm_avi.enigmarobolox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Arm_AVI on 3/27/2018.
 */

public class ListViewCustomAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<DefenseInfoModel> defenseInfoModelArrayList;

    private ValueFilter valueFilter;
    private ArrayList<DefenseInfoModel> mStringFilterList;


    public ListViewCustomAdapter(Context context, ArrayList<DefenseInfoModel> defenseInfoModelArrayList) {
        super();
        this.context = context;
        this.defenseInfoModelArrayList = defenseInfoModelArrayList;
        mStringFilterList =  defenseInfoModelArrayList;
    }


    @Override
    public int getCount() {
        return defenseInfoModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return defenseInfoModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_layout, null, true);

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.title.setText(defenseInfoModelArrayList.get(position).getTitle());
        holder.phone.setText(defenseInfoModelArrayList.get(position).getPhone());

        return convertView;
    }

    private class ViewHolder {
        protected TextView title, phone;
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {

            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<DefenseInfoModel> filterList=new ArrayList<DefenseInfoModel>();
                for(int i=0;i<mStringFilterList.size();i++){
                    if((mStringFilterList.get(i).getTitle().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        DefenseInfoModel contacts = new DefenseInfoModel();
                        contacts.setTitle(mStringFilterList.get(i).getTitle());
                        contacts.setPhone(mStringFilterList.get(i).getPhone());
                        filterList.add(contacts);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=mStringFilterList.size();
                results.values=mStringFilterList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            defenseInfoModelArrayList =(ArrayList<DefenseInfoModel>) results.values;
            notifyDataSetChanged();
        }
    }



}
