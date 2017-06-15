package necer.ncalendardemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.utils.Utils;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2017/6/7.
 */

public class AAAdapter extends RecyclerView.Adapter<AAAdapter.MyViewHolder> {

    private Context context;

    public AAAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_, null,true));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


       // View itemView = holder.itemView;

       // TextView textView = (TextView) itemView.findViewById(R.id.tv_);
//
        TextView textView = holder.textView;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(context, 70));

        textView.setLayoutParams(layoutParams);

        textView.setText("-----"+position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("position:::::" + position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_);
        }
    }
}




