package org.noandish.library.raycastintersectpolygon

import org.noandish.library.raycastintersectpolygon.LatLngRange
import org.noandish.library.raycastintersectpolygon.OnRangePolygonListener
import java.util.ArrayList

class ParamsRangePolygons {
    var id = -1L
    var onRangePolygonListener: OnRangePolygonListener? = null
    var vertices: ArrayList<LatLngRange> = ArrayList()
    var any: Any? = null

    constructor(
        id: Long = -1,
        vertices: ArrayList<LatLngRange>,
        onRangePolygonListener: OnRangePolygonListener? = null,
        any: Any
    ) {
        this.id = id
        this.vertices = vertices
        this.onRangePolygonListener = onRangePolygonListener
        this.any = any
    }
    constructor(
        vertices: ArrayList<LatLngRange>,
        onRangePolygonListener: OnRangePolygonListener? = null,
        any: Any
    ) {
        this.vertices = vertices
        this.onRangePolygonListener = onRangePolygonListener
        this.any = any
    }

    constructor(
        vertices: ArrayList<LatLngRange>,
        any: Any
    ) {
        this.vertices = vertices
        this.any = any
    }

    constructor(
        vertices: ArrayList<LatLngRange>
    ) {
        this.vertices = vertices
    }

    var entered = false
    var exited = true
}