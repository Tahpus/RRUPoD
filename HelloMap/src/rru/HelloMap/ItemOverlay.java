package rru.HelloMap;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays;
	private Context mContext;
    public double latSelect;
    public double longSelect;
	
	public ItemOverlay(Drawable defaultMarker, Context context) {
		  //เมธอด boundCenterBottom ช่วยในการกำหนดให้ จุดกึ่งกลาง ด้านล่างสุดของภาพ marker ชี้ที่จุดพิกัดพอดี (ลองนึกถึงภาพเข็มหมุดที่ชี้ตรงจุด)
		  super(boundCenterBottom(defaultMarker));
		  mOverlays = new ArrayList<OverlayItem>();
		  mContext = context;
	}
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	
	//ถูกเรียกใช้เมื่อมีการสัมผัสบน Marker ยังพิกัดตำแหน่งต่าง ๆ
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);	  
	  //รับค่าละติจูด ลองกิจูดจากจุดที่ผู้ใช้เลือก เพื่อนำมาสร้าง Location สำหรับใช้ในการคำนวณหาระยะห่าง
	  latSelect=item.getPoint().getLatitudeE6()/1E6;
	  longSelect=item.getPoint().getLongitudeE6()/1E6;
	  
	  //สร้าง Location เก็บตำแหน่งที่ผู้ใช้เลือก สำหรับใช้ในการคำนวณหาระยะห่าง
	  Location ploc=new Location("plo1");
      ploc.setLatitude(latSelect);
      ploc.setLongitude(longSelect);
      
      //สร้าง Location เก็บตำแหน่งปัจจุบัน สำหรับใช้ในการคำนวณหาระยะห่าง
	  Location ploc2=new Location("plo2");
      ploc2.setLatitude(HelloMap.curLat);
      ploc2.setLongitude(HelloMap.curLong);
      
      //คำนวณระยะห่างระหว่างตำแหน่งที่ผู้ใช้เลือกกับตำแหน่งปัจจุบัน (หน่วยที่ได้เป็นเมตร)
      float dist=ploc.distanceTo(ploc2)/1000;
	  
      //สร้าง AlertDialog แสดงข้อความจากตำแหน่งที่ผู้ใช้เลือก พร้อมทั้งระยะห่างจากตำแหน่งปัจจุบัน
      AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet()+"\n"+String.valueOf(dist)+" KiloMeters");
	  dialog.show();  
	  return true;
	}
}
