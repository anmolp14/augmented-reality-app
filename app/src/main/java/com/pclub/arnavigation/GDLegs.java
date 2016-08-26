package com.pclub.arnavigation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADMIN on 27-05-2016.
 */
public class GDLegs {
    /**
     * A GDLegs is a list of GDPath
     */
    List<GDPath> mPathsList;
    /**
     * The distance of the leg
     */
    int mDistance;
    /**
     * The duration of the leg
     */
    int mDuration;
    /**
     * Starting address
     */
    String mStartAddress;
    /**
     * Ending Address
     */
    String mEndAddress;

    /**
     * @param pathsList
     */
    public GDLegs(List<GDPath> pathsList) {
        super();
        this.mPathsList = pathsList;
    }

    /**
     * @return the mLegsList
     */
    public final List<GDPath> getPathsList() {
        return mPathsList;
    }

    /**
     * @param mLegsList the mLegsList to set
     */
    public final void setPathsList(List<GDPath> mPathsList) {
        this.mPathsList = mPathsList;
    }

    /**
     * @return the mDistance
     */
    public final int getmDistance() {
        return mDistance;
    }

    /**
     * @param mDistance the mDistance to set
     */
    public final void setmDistance(int mDistance) {
        this.mDistance = mDistance;
    }

    /**
     * @return the mDuration
     */
    public final int getmDuration() {
        return mDuration;
    }

    /**
     * @param mDuration the mDuration to set
     */
    public final void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * @return the mStartAddress
     */
    public final String getmStartAddress() {
        return mStartAddress;
    }

    /**
     * @param mStartAddress the mStartAddress to set
     */
    public final void setmStartAddress(String mStartAddress) {
        this.mStartAddress = mStartAddress;
    }

    /**
     * @return the mEndAddress
     */
    public final String getmEndAddress() {
        return mEndAddress;
    }

    /**
     * @param mEndAddress the mEndAddress to set
     */
    public final void setmEndAddress(String mEndAddress) {
        this.mEndAddress = mEndAddress;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder strB=new StringBuilder("GLegs\r\n");
        for(GDPath path:mPathsList) {
            strB.append(path.toString());
            strB.append("\r\n");
        }
        return strB.toString();
    }
}
