package necer.ncalendardemo.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Fragment测试
 * Created by necer on 2017/12/22.
 */

public class TestFragment extends Fragment implements OnCalendarChangedListener {


    private NCalendar ncalendar;
    private RecyclerView recyclerView;
    private TextView tv_month;
    private TextView tv_date;


    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leave_shift, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        ncalendar = (NCalendar) rootView.findViewById(R.id.ncalendarrrr);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        tv_month = (TextView) rootView.findViewById(R.id.tv_month);
        tv_date = (TextView) rootView.findViewById(R.id.tv_date);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AAAdapter aaAdapter = new AAAdapter(getContext());
        recyclerView.setAdapter(aaAdapter);
        ncalendar.setOnCalendarChangedListener(this);

        ncalendar.post(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                list.add("2017-09-21");
                list.add("2017-10-21");
                list.add("2017-10-1");
                list.add("2017-10-15");
                list.add("2017-10-18");
                list.add("2017-10-26");
                list.add("2017-11-21");
                ncalendar.setPoint(list);
            }
        });
        return rootView;
    }

    @Override
    public void onCalendarChanged(DateTime dateTime) {
        tv_month.setText(dateTime.getMonthOfYear() + "月");
        tv_date.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月" + dateTime.getDayOfMonth() + "日");
        MyLog.d("dateTime::" + dateTime);
    }


    class AAAdapter extends RecyclerView.Adapter<TestFragment.AAAdapter.MyViewHolder> {

        private Context context;

        public AAAdapter(Context context) {
            this.context = context;
        }

        @Override
        public TestFragment.AAAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestFragment.AAAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_, parent,false));
        }

        @Override
        public void onBindViewHolder(TestFragment.AAAdapter.MyViewHolder holder, final int position) {
            TextView textView = holder.textView;
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


/*




    private NCalendar ncalendar;
    private RecyclerView recyclerView;
    private TextView tv_month;
    private TextView tv_date;





  @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_ncalendar,container, false);

        ncalendar = (NCalendar) view.findViewById(R.id.ncalendarrrr);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AAAdapter aaAdapter = new AAAdapter(getContext());
        recyclerView.setAdapter(aaAdapter);
        ncalendar.setOnCalendarChangedListener(this);

        ncalendar.post(new Runnable() {
            @Override
            public void run() {

                List<String> list = new ArrayList<>();
                list.add("2017-09-21");
                list.add("2017-10-21");
                list.add("2017-10-1");
                list.add("2017-10-15");
                list.add("2017-10-18");
                list.add("2017-10-26");
                list.add("2017-11-21");

                ncalendar.setPoint(list);
            }
        });

        return view;
    }

    @Override
    public void onCalendarChanged(DateTime dateTime) {
        tv_month.setText(dateTime.getMonthOfYear() + "月");
        tv_date.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月" + dateTime.getDayOfMonth() + "日");
    }*/
}
