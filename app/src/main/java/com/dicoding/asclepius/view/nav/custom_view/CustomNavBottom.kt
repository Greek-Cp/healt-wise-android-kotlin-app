package com.dicoding.asclepius.view.nav.custom_view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomNavBottom : BottomNavigationView {
    private var mPath: Path? = null
    private var mPaint: Paint? = null
    val CURVE_CIRCLE_RADIUS = 256 / 3
    var mFirstCurveStartPoint = Point()
    var mFirstCurveEndPoint = Point()
    var mFirstCurveControlPoint2 = Point()
    var mFirstCurveControlPoint1 = Point()
    var mSecondCurveStartPoint = Point()
    var mSecondCurveEndPoint = Point()
    var mSecondCurveControlPoint1 = Point()
    var mSecondCurveControlPoint2 = Point()
    var mNavigationBarWidth = 0
    var mNavigationBarHeight = 0

    constructor(context: Context?) : super(context!!) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mPath = Path()
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        checkNightMode()
    }

    private fun checkNightMode() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> mPaint!!.color = ContextCompat.getColor(context, R.color.md_theme_dark_onPrimary)
            Configuration.UI_MODE_NIGHT_NO -> mPaint!!.color = ContextCompat.getColor(context, R.color.md_theme_light_onPrimary)
            else -> mPaint!!.color = ContextCompat.getColor(context, R.color.md_theme_light_onTertiaryContainer)
        }
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mNavigationBarWidth = width
        mNavigationBarHeight = height
        mFirstCurveStartPoint.set((mNavigationBarWidth / 2 - CURVE_CIRCLE_RADIUS * 2 - CURVE_CIRCLE_RADIUS / 3), 0)
        mFirstCurveEndPoint.set((mNavigationBarWidth / 2), CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4)
        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint.set((mNavigationBarWidth / 2 + CURVE_CIRCLE_RADIUS * 2 + CURVE_CIRCLE_RADIUS / 3), 0)
        mFirstCurveControlPoint1.set(mFirstCurveStartPoint.x + CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4, mFirstCurveStartPoint.y)
        mFirstCurveControlPoint2.set(mFirstCurveEndPoint.x - CURVE_CIRCLE_RADIUS * 2 + CURVE_CIRCLE_RADIUS, mFirstCurveEndPoint.y)
        mSecondCurveControlPoint1.set(mSecondCurveStartPoint.x + CURVE_CIRCLE_RADIUS * 2 - CURVE_CIRCLE_RADIUS, mSecondCurveStartPoint.y)
        mSecondCurveControlPoint2.set(mSecondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4), mSecondCurveEndPoint.y)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath!!.reset()
        mPath!!.moveTo(0f, 0f)
        mPath!!.lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())
        mPath!!.cubicTo(
            mFirstCurveControlPoint1.x.toFloat(), mFirstCurveControlPoint1.y.toFloat(),
            mFirstCurveControlPoint2.x.toFloat(), mFirstCurveControlPoint2.y.toFloat(),
            mFirstCurveEndPoint.x.toFloat(), mFirstCurveEndPoint.y.toFloat()
        )
        mPath!!.cubicTo(
            mSecondCurveControlPoint1.x.toFloat(), mSecondCurveControlPoint1.y.toFloat(),
            mSecondCurveControlPoint2.x.toFloat(), mSecondCurveControlPoint2.y.toFloat(),
            mSecondCurveEndPoint.x.toFloat(), mSecondCurveEndPoint.y.toFloat()
        )
        mPath!!.lineTo(mNavigationBarWidth.toFloat(), 0f)
        mPath!!.lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
        mPath!!.lineTo(0f, mNavigationBarHeight.toFloat())
        mPath!!.close()
        canvas.drawPath(mPath!!, mPaint!!)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig != null) {
            checkNightMode()
            invalidate()
        }
    }
}
