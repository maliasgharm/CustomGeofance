package org.noandish.library.raycastintersectpolygon

import org.noandish.library.raycastintersectpolygon.LatLngRange
import org.noandish.library.raycastintersectpolygon.OnRangePolygonListener
import java.util.ArrayList

class ParamsRangePolygons(
        val id: Long,
        val onRangePolygonListener: OnRangePolygonListener,
        val vertices: ArrayList<LatLngRange>,
        val any : Any
) {
    var entered = false
    var exited = true
}