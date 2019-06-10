package org.noandish.library.raycastintersectpolygon

enum class EventRangePolygon(type: Int) {
    EVENT_OUT_RANGE(0),
    EVENT_IN_RANGE(1),
    EVENT_OTHER(2);

    val value: Int = type

}
