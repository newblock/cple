package com.qcx.mini.utils.mapUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.qcx.mini.R;

/**
 * Created by Administrator on 2018/1/25.
 */

public class DarkInfoWindowAdapter implements AMap.InfoWindowAdapter {
    private LayoutInflater inflater;
    View infoWindow = null;

    public DarkInfoWindowAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
//        if (infoWindow == null) {
            infoWindow = inflater.inflate(R.layout.view_info_window, null);
//        }
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        if (infoWindow == null) {
//            infoWindow = inflater.inflate(
//                    R.layout.view_info_window, null);
//        }
//        render(marker, infoWindow);
        return null;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改
        TextView textView = view.findViewById(R.id.view_info_window_text);
        textView.setText(marker.getTitle());
    }
}
