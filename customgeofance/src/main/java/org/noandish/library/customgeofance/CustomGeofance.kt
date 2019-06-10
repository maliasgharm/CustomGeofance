package org.noandish.library.raycastintersectpolygon

import android.util.Log
import java.util.ArrayList

/**
 * Created by Aliasghar on 12.08.2018 - 12 : 45
 */

class CustomGeofance {
    private var onEnterRangePolygonListener: OnEnterRangePolygonListener? = null
    private var paramsRangePolygons = ArrayList<ParamsRangePolygons>()
    var lastLocation: LatLngRange? = null

    fun setOnEnterRangePolygonListener(onEnterRangePolygonListener: OnEnterRangePolygonListener): CustomGeofance {
        this.onEnterRangePolygonListener = onEnterRangePolygonListener
        if (lastLocation != null)
            changeLocation(lastLocation!!)
        return this
    }

    fun addAllRange(paramsRangePolygon: ParamsRangePolygons): CustomGeofance {
        this.paramsRangePolygons.add(paramsRangePolygon)
        if (lastLocation != null)
            changeLocation(lastLocation!!)
        return this
    }

    fun removeRangeListener(paramsRangePolygon: ParamsRangePolygons) {
        this.paramsRangePolygons.remove(paramsRangePolygon)
        if (lastLocation != null)
            changeLocation(lastLocation!!)
    }

    fun clear() {
        this.paramsRangePolygons.clear()
        if (lastLocation != null)
            changeLocation(lastLocation!!)
    }

    interface OnEnterRangePolygonListener {
        fun onEnteredGeo(any: ParamsRangePolygons)
        fun onExitedGeo(paramsRangePolygon: ParamsRangePolygons)
    }

    fun changeLocation(tap: LatLngRange) {
        Log.w("changeLocation", "changeLocation ${tap.lat},${tap.lng} , ${paramsRangePolygons.size}")
        for (paramsRangePolygons in paramsRangePolygons) {
            val vertices = paramsRangePolygons.vertices
            var intersectCount = 0
            if (vertices.size <= 0)
                break
            for (j in 0 until vertices.size - 1) {
                Log.w("point", "point ${vertices[j].lat} , ${vertices[j].lng}")
                if (rayCastIntersect(tap, vertices[j], vertices[j + 1])) {
                    intersectCount++
                }
            }
            if (vertices.size <= 0)
                break
            if (rayCastIntersect(tap, vertices[vertices.size - 1], vertices[0])) {
                intersectCount++
            }
            Log.w("changeLocation", "changeLocation ${paramsRangePolygons.entered} , ${paramsRangePolygons.exited}")
            if (intersectCount % 2 == 1 && !paramsRangePolygons.entered) {
                paramsRangePolygons.entered = true
                paramsRangePolygons.exited = false
                if (onEnterRangePolygonListener != null)
                    onEnterRangePolygonListener!!.onEnteredGeo(paramsRangePolygons)
                paramsRangePolygons.onRangePolygonListener?.onChangeStatus(
                    EventRangePolygon.EVENT_IN_RANGE,
                    paramsRangePolygons
                )
                Log.w("changeLocation", "changeLocation EVENT_IN_RANGE")
            } else if (intersectCount % 2 == 0 && !paramsRangePolygons.exited) {
                paramsRangePolygons.entered = false
                paramsRangePolygons.exited = true
                if (onEnterRangePolygonListener != null)
                    onEnterRangePolygonListener!!.onExitedGeo(paramsRangePolygons)
                paramsRangePolygons.onRangePolygonListener?.onChangeStatus(
                    EventRangePolygon.EVENT_OUT_RANGE,
                    paramsRangePolygons
                )
                Log.w("changeLocation", "changeLocation EVENT_OUT_RANGE")
            }
        }
    }

    private fun rayCastIntersect(tap: LatLngRange, vertA: LatLngRange, vertB: LatLngRange): Boolean {

        val aY = vertA.lat
        val bY = vertB.lat
        val aX = vertA.lng
        val bX = vertB.lng
        val pY = tap.lat
        val pX = tap.lng

        if (aY > pY && bY > pY || aY < pY && bY < pY
            || aX < pX && bX < pX
        ) {
            return false // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        val m = (aY - bY) / (aX - bX) // Rise over run
        val bee = -aX * m + aY // y = mx + b
        val x = (pY - bee) / m // algebra is neat!

        return x > pX

    }

}
