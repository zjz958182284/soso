package com.example.sosocar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosocar.Entity.OrderListBean;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter {

    private List<OrderListBean> list;
    private LayoutInflater inflater;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    private  onItemClickListener listener;
    public RecycleViewAdapter(Context context,List<OrderListBean> datas) {
        super();
        inflater=LayoutInflater.from(context);
        this.list=datas;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.order_item,parent,false);
        return  new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
      MyViewHolder myViewHolder=(MyViewHolder)holder;
      OrderListBean bean=list.get(position);
      myViewHolder.origin.setText(bean.getOrigin_address());
      myViewHolder.destination.setText(bean.getDestination_address());
        myViewHolder.order_time.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(bean.getCreateTime()));
        if(bean.getOrder_type()==1)
            myViewHolder.order_type.setText("预约");
        int status=bean.getStatus();
        switch (status){
            case 0:myViewHolder.order_status.setText("匹配失败");break;
            case 2:myViewHolder.order_status.setText("已出发");break;
            case 3:myViewHolder.order_status.setText("去支付");break;
            case 4:myViewHolder.order_status.setText("已支付");break;
            case 5:myViewHolder.order_status.setText("订单取消");break;

        }
        myViewHolder.goto_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(position);
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView order_type;
        private  TextView order_time;
        private  TextView order_status;
        private  TextView origin;
        private TextView destination;
        private ImageButton goto_order_detail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_status=itemView.findViewById(R.id.order_status);
            order_type=itemView.findViewById(R.id.order_type);
            order_time=itemView.findViewById(R.id.order_time);
            origin=itemView.findViewById(R.id.origin);
            destination=itemView.findViewById(R.id.destination);
            goto_order_detail=itemView.findViewById(R.id.goto_order_detail);
        }
    }

   public interface onItemClickListener {

        void itemClick(int position);
    }
}
