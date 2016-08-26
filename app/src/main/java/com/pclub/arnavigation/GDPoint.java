package com.pclub.arnavigation;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by ADMIN on 27-05-2016.
 */
public class GDPoint{
    double mLat;
    double mLng;
    /**
     * The corresponding LatLng
     * Not in the JSon Object. It's an helpful attribute
     */
    private LatLng mLatLng = null;

    /**
     * The builder
     * @param coordinate retrieve from JSon
     */
    public GDPoint(double lat,double lng) {
        super();
        this.mLat = lat;
        this.mLng=lng;
    }

    /**
     * @return The LatLng Object linked with that point
     */
    public LatLng getLatLng() {
        if (mLatLng == null) {
            mLatLng = new LatLng(mLat,mLng);
        }
        return mLatLng;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "["+mLat+","+mLng+"]";
    }

}
