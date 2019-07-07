package adpaterrecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Ultimatecode.atmlocatorfinder.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AJAY DHIMAN on 03-04-2016.
 */
public class AdapterVirtualMAp extends RecyclerView.Adapter<AdapterVirtualMAp.ViewHolder> {

    private View view;
    private Context resultScreen;
    private Intent intent;
    private ArrayList<HashMap<String, Object>> storevitualmapinfo;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView tv_loc,tv_line;
        public LinearLayout ll_click;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            tv_loc = (TextView) view.findViewById(R.id.tv_loc);
            tv_line = (TextView) view.findViewById(R.id.tv_line);
            ll_click = (LinearLayout) view.findViewById(R.id.ll_click);

        }
    }

    public AdapterVirtualMAp(Context context, ArrayList<HashMap<String, Object>> storeinfo2) {
        resultScreen = context;
        storevitualmapinfo = storeinfo2;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_virtualmap, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterVirtualMAp.ViewHolder holder, final int position) {

        holder.tv_loc.setText(storevitualmapinfo.get(position).get("mapvInfo").toString());
        if((storevitualmapinfo.size()-1) == position){
            holder.tv_line.setVisibility(View.GONE);
        }
        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return storevitualmapinfo.size();
    }

}
