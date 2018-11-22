package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/5/10.
 */

public class AddWayPointInfoWindowAdapter implements AMap.InfoWindowAdapter {
    private LayoutInflater inflater;
    private Context context;
    private View view;
    private TextView tv_title;
    private View v_delete;
    private View v_wayPoint;
    private View v_endPoint;
    private View v_line;
    private Marker marker;
    private boolean isWayPointMarker;
    private OnInfoWindowClickListener listener;

    public AddWayPointInfoWindowAdapter(@NonNull Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public void setListener(OnInfoWindowClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (view == null) {
            view = inflater.inflate(R.layout.view_add_way_point_info_window, null);
            tv_title = view.findViewById(R.id.view_add_way_point_info_window_title);
            v_delete = view.findViewById(R.id.view_add_way_point_info_window_delete);
            v_wayPoint = view.findViewById(R.id.view_add_way_point_info_window_add_point);
            v_endPoint = view.findViewById(R.id.view_add_way_point_info_window_set_end_point);
            v_line = view.findViewById(R.id.view_add_way_point_info_window_line);
            v_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onDelete(AddWayPointInfoWindowAdapter.this.marker);
                    }
                }
            });
            v_wayPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onAdd(AddWayPointInfoWindowAdapter.this.marker);
                    }
                }
            });
            v_endPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onEnd(AddWayPointInfoWindowAdapter.this.marker);
                    }
                }
            });
        }
        this.marker = marker;
        if (marker != null) {
            if (TextUtils.isEmpty(marker.getTitle())) {
                tv_title.setText("正在获取位置信息...");
            } else {
                tv_title.setText(marker.getTitle());
            }
            if (!isWayPointMarker) {
                v_wayPoint.setVisibility(View.VISIBLE);
                v_endPoint.setVisibility(View.VISIBLE);
                v_line.setVisibility(View.VISIBLE);
                v_delete.setVisibility(View.GONE);
            } else {
                v_wayPoint.setVisibility(View.GONE);
                v_endPoint.setVisibility(View.GONE);
                v_delete.setVisibility(View.VISIBLE);
                v_line.setVisibility(View.GONE);
            }
        }
        return view;
    }

    public void notifyDataChanged() {
        getInfoWindow(marker);
    }

    public void setWayPointMarker(boolean wayPointMarker) {
        isWayPointMarker = wayPointMarker;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public interface OnInfoWindowClickListener{
        void onDelete(Marker marker);
        void onAdd(Marker marker);
        void onEnd(Marker marker);
    }
}
