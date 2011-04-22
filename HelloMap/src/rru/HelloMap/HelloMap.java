package rru.HelloMap;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
///////////////22-04-2011
public class HelloMap extends MapActivity {
	    private LocationManager lm;
	    private LocationListener locationListener;
	    private MapController mapViewController;
	    private MapView mapView;
	    private ItemOverlay itemoverlay1,itemoverlay2,itemoverlay3;
	    private OverlayItem overlayitem1,overlayitem2,overlayitem3;
	    private List<Overlay> mapOverlays;
	    private Drawable drawable1,drawable2,drawable3;
	    public static double curLat,curLong;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        
        //ใช้งานปุ่ม ZoomController
        mapView.setBuiltInZoomControls(true);
        
        mapViewController = mapView.getController();
        
        
  
        //ตัวแปรเก็บ List ของ Overlay บนแผนที่
        mapOverlays = mapView.getOverlays();
        
        //นำภาพ Marker มาใช้งาน
        drawable1 = this.getResources().getDrawable(R.drawable.androidmarker);
        drawable2 = this.getResources().getDrawable(R.drawable.thai);
        drawable3 = this.getResources().getDrawable(R.drawable.home);
        
        //สร้าง Object ของคลาส ItemOverlay โดยกำหนดค่าภาพ Marker และ Context ของคลาสปัจจุบันให้กับ constructor 
        itemoverlay1 = new ItemOverlay(drawable1,this);
        itemoverlay2 = new ItemOverlay(drawable2,this);          
        itemoverlay3 = new ItemOverlay(drawable3,this);
        
        //สร้างจุดพิกัดสำหรับแสดงบนแผนที่
        GeoPoint point1 = new GeoPoint((int)(13.549095*1e6),(int)(101.615777*1e6));
        
        //สร้าง Object OverlayItem โดยกำหนดจุดพิกัด, หัวเรื่อง และข้อความให้กับ constructor
        overlayitem1 = new OverlayItem(point1, "Sawasdee", "I'm in Chachoengsao");
        
        //จุดพิกัดที่ 2
        GeoPoint point2 = new GeoPoint((int)(13.67613*1e6),(int)(101.070579*1e6));
        overlayitem2 = new OverlayItem(point2, "Sawasdee", "I'm in Rajabhat Rajanagarindra U'");  

        //เพิ่ม overlayitem ให้กับ object itemoverlay
        itemoverlay1.addOverlay(overlayitem1);
        itemoverlay2.addOverlay(overlayitem2);
        
        //เพิ่ม overlay บนแผนที่
        mapOverlays.add(itemoverlay1);
        mapOverlays.add(itemoverlay2);
        
        //รับค่าตำแหน่งพิกัดปัจจุบันจากอุปกรณ์ GPS
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);    
        locationListener = new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    
        //กำหนดจุดศูนย์กลางเริ่มต้นจากตำแหน่งปัจจุบัน
        GeoPoint initGeoPoint = new GeoPoint(
         (int)(lm.getLastKnownLocation(
          LocationManager.GPS_PROVIDER)
          .getLatitude()*1000000),
         (int)(lm.getLastKnownLocation(
          LocationManager.GPS_PROVIDER)
          .getLongitude()*1000000));
        overlayitem3 = new OverlayItem(initGeoPoint, "Sawasdee", "I'm Here");
    	itemoverlay3.addOverlay(overlayitem3);
    	mapOverlays.add(itemoverlay3);
        mapViewController.setCenter(initGeoPoint);
         
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Inner Class สำหรับตรวจจับการเปลี่ยนแปลงพิกัดตำแหน่งปัจจุบัน
    private class MyLocationListener implements LocationListener 
    {   	
        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {                               
            	GeoPoint p = new GeoPoint((int)(loc.getLatitude()*1E6),
						  				  (int)(loc.getLongitude()*1E6));
			
            	//กำหนดค่าพิกัดตำแหน่งปัจจบันให้กับตัวแปร เพื่อนำไปใช้ในการคำนวณระยะห่าง
            	curLat=loc.getLatitude();
            	curLong=loc.getLongitude();            	
            	
            	//สร้างจุดพิกัดตามตำแหน่งปัจจุบัน และแสดงภาพ Marker บนแผนที่
            	GeoPoint point3 = new GeoPoint((int)(curLat*1e6),
            								   (int)(curLong*1e6));
            	overlayitem3 = new OverlayItem(point3, "Sawasdee", "I'm Here");
            	itemoverlay3.addOverlay(overlayitem3);
            	mapOverlays.add(itemoverlay3);
                
            	//ย้ายตำแหน่งการแสดงผลบนแผนที่ไปยังตำแหน่งปัจจุบัน
            	mapViewController.animateTo(p);
            	
            	//กำหนดระดับการซูม
                mapViewController.setZoom(16);                
            }else{
            	//กำหนดตำแหน่งเริ่มต้น ในกรณีไม่สามารถใช้งาน GPS ได้
           	 	GeoPoint point3 = new GeoPoint((int)(13.67613*1e6),
           	 								   (int)(101.070579*1e6));
           	 	overlayitem3 = new OverlayItem(point3, "Sawasdee", "I'm here");
           	 	itemoverlay3.addOverlay(overlayitem3);
           	 	mapOverlays.add(itemoverlay3);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}