package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.UnOrderDriverTravelDetailEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.utils.mapUtils.MapUtil;
import com.qcx.mini.utils.mapUtils.NextDrivingRouteOverlay;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeStartOrEndLocationActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {
    public final static int TYPE_START = 1;
    public final static int TYPE_END = 2;


    private MapUtil mMapUtil;
    private AMap mAMap;
    private RouteSearch mRouteSearch;
    private Marker locationMarker;
    private int markerImgRes;
//    private List<LatLonPoint> mAlllatlonPoints;
    private Tip tip;

    private double[] location;
    private String locationName;
    private int strategy;
    private List<UnOrderDriverTravelDetailEntity.TravelStation> travelStations;
    private String wayPoints;
//    private int startIndex;
//    private int endIndex;

    @BindView(R.id.change_start_or_end_location_mapView)
    MapView mMapView;
    @BindView(R.id.change_start_or_end_location_img)
    ImageView iv_img;
    @BindView(R.id.change_start_or_end_location_text)
    TextView tv_location;

    @OnClick(R.id.change_start_or_end_location_close)
    void close() {
        finish();
    }

    @OnClick(R.id.change_start_or_end_location_submit)
    void submit() {
        if (tip != null) {
            Intent intent = new Intent();
            intent.putExtra("location", tip);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_change_start_or_end_location;
    }

    private boolean isMoveMap;

    @Override
    public void initView(Bundle savedInstanceState) {
//        mAlllatlonPoints = new ArrayList<>();
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtil = new MapUtil(this, mAMap);
        mMapUtil.iniMap();
        mAMap.getUiSettings().setGestureScaleByMapCenter(true);
        mAMap.getUiSettings().setZoomInByScreenCenter(true);
        mMapUtil.changeMapStyle();
        if (mRouteSearch == null) {
            mRouteSearch = new RouteSearch(this);
            mRouteSearch.setRouteSearchListener(this);
        }
        String startAddress = getIntent().getStringExtra("startAddress");
        double[] start = getIntent().getDoubleArrayExtra("start");
        String endAddress = getIntent().getStringExtra("endAddress");
        double[] end = getIntent().getDoubleArrayExtra("end");
        location = getIntent().getDoubleArrayExtra("location");
        locationName = getIntent().getStringExtra("locationName");
        strategy = getIntent().getIntExtra("strategy", 0);
        int type = getIntent().getIntExtra("type", 0);
        travelStations = getIntent().getParcelableArrayListExtra("travelStations");
        wayPoints = getIntent().getStringExtra("wayPoints");
//        startIndex = getIntent().getIntExtra("startIndex", 0);
//        endIndex = getIntent().getIntExtra("endIndex", travelStations == null ? 0 : travelStations.size());

        if (start == null || end == null || start.length != 2 || end.length != 2) {
            ToastUtil.showToast("路径规划失败，请重试");
            finish();
        }

        if (type == TYPE_START) {
            markerImgRes = R.mipmap.icon_map_up;
            iv_img.setImageResource(R.mipmap.icon_map_scd);
        } else if (type == TYPE_END) {
            markerImgRes = R.mipmap.icon_map_down;
            iv_img.setImageResource(R.mipmap.icon_map_xcd);
        }
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LogUtil.i("marker="+marker.getPosition().toString());
                return false;
            }
        });

        routeSearch(start, end);
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                MarkerOptions markerOptions = new MarkerOptions()
                        .anchor(0.5f,0.96f)
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), markerImgRes)));
                locationMarker = mAMap.addMarker(markerOptions);
                int w = UiUtils.getPixelH() / 2;
                int h = (mMapView.getHeight()) / 2;
                locationMarker.setPositionByPixels(w, h);
            }
        });
        mMapUtil.addText(start, startAddress);
        mMapUtil.addText(end, endAddress);
        addTravelStations(travelStations);
        mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    isMoveMap = true;
                }
            }
        });

        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(!isMoveMap) return;
                int w = UiUtils.getPixelH() / 2;
                int h = (mMapView.getHeight()) / 2;
                locationMarker.setPositionByPixels(w, h);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isMoveMap) return;
                double[] location = new double[]{cameraPosition.target.longitude, cameraPosition.target.latitude};
                double minDistacne = Double.MAX_VALUE;
                UnOrderDriverTravelDetailEntity.TravelStation minDistanceStation = null;
                for (UnOrderDriverTravelDetailEntity.TravelStation station : travelStations) {
                    double distance = MapUtil.calculateLineDistance(location, station.getLocation());
                    if (distance < minDistacne) {
                        minDistacne = distance;
                        minDistanceStation = station;
                    }
                }
                if (minDistanceStation != null) {
                    final LatLng latLng=new LatLng(minDistanceStation.getLocation()[1], minDistanceStation.getLocation()[0]);
                    mAMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            locationMarker.setPosition(latLng);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    tv_location.setText(minDistanceStation.getAddress());
                    if(tip==null){
                        tip=new Tip();
                    }
                    tip.setPostion(new LatLonPoint(minDistanceStation.getLocation()[1],minDistanceStation.getLocation()[0]));
                    tip.setName(minDistanceStation.getAddress());
                }
            }
        });
    }

    private void routeSearch(double[] start, double[] end) {
        mAMap.clear();
        LatLonPoint dStart = new LatLonPoint(start[1], start[0]);
        LatLonPoint dEnd = new LatLonPoint(end[1], end[0]);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(dStart, dEnd);
        LogUtil.i("strategy="+strategy);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, strategy, CommonUtil.getLatLonPints(wayPoints), null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        UiUtils.setStatusBarLightMode(this, Color.TRANSPARENT);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS
                && driveRouteResult != null
                && driveRouteResult.getPaths() != null
                && driveRouteResult.getPaths().size() > 0) {
            NextDrivingRouteOverlay routeOverlay = mMapUtil.drawPath(driveRouteResult);
            if (routeOverlay != null) {
                mMapUtil.showAllMarkers(150, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        showLocation();
                    }

                    @Override
                    public void onCancel() {

                    }
                }, routeOverlay.getStartMarker(), routeOverlay.getEndMarker());
            }

//            DrivePath drivePath = driveRouteResult.getPaths().get(0);
//            List<DriveStep> drivePaths = drivePath.getSteps();
//            for (DriveStep step : drivePaths) {
//                List<LatLonPoint> latlonPoints = step.getPolyline();
//                mAlllatlonPoints.addAll(latlonPoints);
//            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private void addTravelStations(List<UnOrderDriverTravelDetailEntity.TravelStation> stations) {
        if (stations != null) {
            for (int i = 0; i < stations.size(); i++) {
                mMapUtil.addMarkerToMap(stations.get(i).getLocation(), R.mipmap.icon_map_site, 0.5f);
            }
        }
    }

    private void showLocation() {
        LatLng latLng = new LatLng(location[1], location[0]);
        mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        tv_location.setText(locationName);
    }
}
