package com.gaiamount.module_im.new_friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.widgets.improved.NOScrolledListView;


/**
 * Created by yukun on 16-10-19.
 */
public class GroupActionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int TYPE_1=0;
    private int TYPE_2=1;
    Context context;

    public GroupActionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==TYPE_1){
            view= LayoutInflater.from(context).inflate(R.layout.group_viewholder_1,null);
            ViewHolder_1 holder_1=new ViewHolder_1(view);
            return holder_1;
        }else if(viewType==TYPE_2){
            view= LayoutInflater.from(context).inflate(R.layout.group_viewholder_1,null);
            ViewHolder_2 holder_2=new ViewHolder_2(view);
            return holder_2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder_1){
            ((ViewHolder_1) holder).textView.setText("申请加入");
           ((ViewHolder_1) holder).noScrolledListView.setAdapter(new NoScrolListAdapter(context));

        }else if(holder instanceof ViewHolder_2){
            ((ViewHolder_2) holder).textView.setText("全部小组");
            ((ViewHolder_2) holder).noScrolledListView.setAdapter(new NoScrolGroupListAdapter(context));
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==1){
            return TYPE_1;
        }else {
            return TYPE_2;
        }
    }

    class ViewHolder_1 extends RecyclerView.ViewHolder{
        TextView textView;
        NOScrolledListView noScrolledListView;

        public ViewHolder_1(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.action_title);
            noScrolledListView= (NOScrolledListView) itemView.findViewById(R.id.action_listview);
        }
    }

    class ViewHolder_2 extends RecyclerView.ViewHolder{
        TextView textView;
        NOScrolledListView noScrolledListView;

        public ViewHolder_2(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.action_title);
            noScrolledListView= (NOScrolledListView) itemView.findViewById(R.id.action_listview);

        }
    }
}
