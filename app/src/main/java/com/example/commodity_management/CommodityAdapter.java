package com.example.commodity_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


//定义列表的适配器
public class CommodityAdapter extends BaseAdapter {
    List<Commodity> list;
    Context context;
//构造参数，传入列表要用的数据以及上下文
    public CommodityAdapter(Context context, List<Commodity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.commodlity_item,null);
            viewHolder.commodity_title = convertView.findViewById(R.id.commodity_title);
            viewHolder.commodity_introduction = convertView.findViewById(R.id.commodity_introduction);
            viewHolder.original_price = convertView.findViewById(R.id.original_price);
            viewHolder.discount_price = convertView.findViewById(R.id.discount_price);
            viewHolder.commodity_address = convertView.findViewById(R.id.commodity_address);
            viewHolder.list_image = convertView.findViewById(R.id.list_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//设置标题
        viewHolder.commodity_title.setText(list.get(position).getName());
        if (list.get(position).getModel().length() > 25){
            viewHolder.commodity_introduction.setText(list.get(position).getIntroduction().substring(0,25) + "...");
        }else{
            viewHolder.commodity_introduction.setText(list.get(position).getModel());
        }
        viewHolder.list_image.setImageURI(Uri.parse(list.get(position).getUri()));
       //
//设置原价
        viewHolder.original_price.setText("￥" + list.get(position).getDiscount_price());
//        设置折扣后的价格
        viewHolder.discount_price.setText("￥" + list.get(position).getOriginal_price());
//        设置地址
        viewHolder.commodity_address.setText(list.get(position).getAddress());
//        设置原价中划线
        viewHolder.discount_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }

    class ViewHolder{
        TextView commodity_title;
        TextView commodity_introduction;
        TextView original_price;
        TextView discount_price;
        TextView commodity_address;
        ImageView list_image;
    }
}
