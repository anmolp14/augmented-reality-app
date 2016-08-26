package com.pclub.arnavigation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADMIN on 27-05-2016.
 */
public class GDPath{
    /**
     * A path is a list of GDPoints
     */
    List<GDPoint> mPath;
    /**
     * The distance of the path
     */
    int mDistance;
    /**
     * The duration of the path
     */
    int mDuration;
    /**
     * The travel mode of the path
     */
    String mTravelMode;
    /**
     * The Html text associated with the path
     */
    String mHtmlText;

    /**
     * @param path The list of GDPoint that makes the path
     */
    public GDPath(List<GDPoint> path) {
        super();
        this.mPath = path;
    }

    /**
     * @return the mPath
     */
    public final List<GDPoint> getPath() {
        return mPath;
    }

    /**
     * @param mPath the mPath to set
     */
    public final void setPath(List<GDPoint> mPath) {
        this.mPath = mPath;
    }

    /**
     * @return the mPath
     */


    /**
     * @return the mDistance
     */
    public final int getDistance() {
        return mDistance;
    }

    /**
     * @return the mDuration
     */
    public final int getDuration() {
        return mDuration;
    }

    /**
     * @return the mTravelMode
     */
    public final String getTravelMode() {
        return mTravelMode;
    }

    /**
     * @return the mHtmlText
     */
    public final String getHtmlText() {
        return mHtmlText;
    }

    /**
     * @param mPath the mPath to set
     */
    public final void setmPath(List<GDPoint> mPath) {
        this.mPath = mPath;
    }

    /**
     * @param mDistance the mDistance to set
     */
    public final void setDistance(int distance) {
        this.mDistance = distance;
    }

    /**
     * @param mDuration the mDuration to set
     */
    public final void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * @param mTravelMode the mTravelMode to set
     */
    public final void setTravelMode(String travelMode) {
        this.mTravelMode = travelMode;
    }

    /**
     * @param mHtmlText the mHtmlText to set
     */
    public final void setHtmlText(String htmlText) {
        this.mHtmlText = htmlText;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder strB=new StringBuilder("GPath\r\n");
        for(GDPoint point:mPath) {
            strB.append(point.toString());
            strB.append(point.toString());
            strB.append(",");
        }
        return strB.toString();
    }

}
