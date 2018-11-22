package com.qcx.mini.utils.mapUtils;

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Text;

/**
 * Created by Administrator on 2018/6/28.
 */

public class QuMapPoint {
    private Marker marker;
    private Text text;
    private Object tag;//如果没有设置，默认返回-1

    public QuMapPoint(String tag) {
        this.tag = tag;
    }

    public void addOrChangePoint(MapUtil mapUtil, double[] point, String textStr, int imgId) {
        if (mapUtil == null || point == null) {
            return;
        }
        marker = mapUtil.addOrMoveMarker(marker, point, imgId);
        text = mapUtil.addOrChangeText(text, point, textStr);
    }

    public void remove() {
        if (marker != null) {
            marker.remove();
        }
        if (text != null) {
            text.remove();
        }
    }

    public void setTextVisible(boolean visible) {
        if (text != null) {
            text.setVisible(false);
        }
    }

    public void setMarkerVisible(boolean visible) {
        if (marker != null) {
            marker.setVisible(visible);
        }
    }


    public Object getTag() {
        return tag == null ? -1 : tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
