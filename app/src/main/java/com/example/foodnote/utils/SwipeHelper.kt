package com.example.foodnote.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

abstract class SwipeHelper(
    context: Context,
    private val recyclerView: RecyclerView,
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.START or ItemTouchHelper.END
){
    private var swipedLastPosition = -1
    private var gestureDetector: GestureDetector? = null

    private val buttonsBufferLeft : MutableMap<Int, ButtonGroup> = mutableMapOf()
    private val buttonsBufferRight : MutableMap<Int, ButtonGroup> = mutableMapOf()

    private var buttonGroup: ButtonGroup? = null


    private val queue = object : LinkedList<Int>(){
        override fun add(element: Int): Boolean {
            if (contains(element)) {
                return false
            }
            return super.add(element)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, event ->
        gestureDetector?.onTouchEvent(event)
        false
    }


    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    private val gestureListener: GestureDetector.SimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent): Boolean {
            if(buttonGroup != null){
                for (button in buttonGroup!!.buttons){
                    if(button.handle(event.x, event.y)) {
                        queue.add(swipedLastPosition)
                        swipedLastPosition = -1
                        recoverSwipedItems()
                        return true
                    }

                }
            }
            return false
        }
    }

    init {
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(touchListener)
    }

    /***
     * Восстановление items в adapter
     */
    @Synchronized
    private fun recoverSwipedItems() {
        while (!queue.isEmpty()) {
            val position = queue.poll() ?: return
            if(position > -1)
                recyclerView.adapter?.notifyItemChanged(position)
        }
    }

    /***
     * Скорость swipe, которая будет учитываться
     */
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.00001f * defaultValue
    }

    /**
     * Максимальная скорость, которую ItemTouchHelper будет когда либо вычислять при перемещении указателя
     */
    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 10.0f * defaultValue//5
    }

    /**
     * Доля, на которую пользователь должен переместить представление, чтобы оно считалось пройденным
     */
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 2f//10
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    private fun drawButtons(
        canvas: Canvas,
        buttons: List<Button>,
        itemView: View,
        dX: Float,
        buttonBinding: ButtonBinding
    ) {
        if(dX < 0) {
            //swipe влево
            var right = itemView.right
            buttons.forEach { button ->
                val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(dX)
                val left = right - width
                button.draw(
                    canvas,
                    RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat()),
                    buttonBinding,
                    SwipeDirection.START
                )
                right = left.toInt()
            }
        }
        else {
            var left = itemView.left
            buttons.forEach { button ->
                val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(dX)
                val right = left + width
                button.draw(
                    canvas,
                    RectF(
                        left.toFloat(),
                        itemView.top.toFloat(),
                        right,
                        itemView.bottom.toFloat()
                    ),
                    buttonBinding,
                    SwipeDirection.END
                )
                left = right.toInt()
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val position = viewHolder.adapterPosition
        var maxDX = dX
        val itemView = viewHolder.itemView
        if(position < 0) {
            swipedLastPosition = position
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                /**
                 * Очистка буфера в противоположной стороне
                 */
                buttonsBufferLeft.clear()
                if (!buttonsBufferRight.containsKey(position)) {
                    buttonsBufferRight[position] = addButtonsRight(position)
                }
                val buttonGroup = buttonsBufferRight[position]
                if (buttonGroup == null) return
                else {
                    val buttons = buttonGroup.buttons
                    if (buttons.isNullOrEmpty()) return
                    maxDX = max(-buttons.intrinsicWidth(), dX)
                    drawButtons(c, buttons, itemView, maxDX, buttonGroup.buttonBinding)
                }
            } else if (dX > 0) {
                /**
                 * Очистка буфера в противоположной стороне
                 */
                buttonsBufferRight.clear()
                //swipe вправо
                if (!buttonsBufferLeft.containsKey(position)) {
                    buttonsBufferLeft[position] = addButtonsLeft(position)
                }
                val buttonGroup = buttonsBufferLeft[position]
                if (buttonGroup == null) return
                else {
                    val buttons = buttonGroup.buttons
                    if (buttons.isNullOrEmpty()) return
                    maxDX = min(buttons.intrinsicWidth(), dX)
                    drawButtons(c, buttons, itemView, maxDX, buttonGroup.buttonBinding)
                }
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            maxDX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        /**
         * Восстановили все прошлые
         */

        val position = viewHolder.adapterPosition
        if(swipedLastPosition != position)
            queue.add(swipedLastPosition)
        swipedLastPosition = position

        buttonGroup = when {
            buttonsBufferLeft.containsKey(swipedLastPosition) -> buttonsBufferLeft[swipedLastPosition]!!
            buttonsBufferRight.containsKey(swipedLastPosition) -> buttonsBufferRight[swipedLastPosition]!!
            else -> null
        }
        buttonsBufferRight.clear()
        buttonsBufferLeft.clear()
        recoverSwipedItems()
    }

    sealed class SwipeDirection {
        object START : SwipeDirection()
        object END : SwipeDirection()
    }

    sealed class ButtonBinding{
        object EdgeRecyclerView : ButtonBinding()
        object EdgeItemView : ButtonBinding()
    }

    abstract fun addButtonsRight(position: Int) : ButtonGroup
    abstract fun addButtonsLeft(position: Int) : ButtonGroup
    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder) = false

    class Button(
        private val context: Context,
        private val title: String,
        textSize: Float,
        @ColorRes private val colorRes: Int,
        private val horizontalPadding: Float = 0f,
        private val icon: ButtonIcon? = null,
        private val clickListener: ButtonClickListener
    ){
        private var clickableRegion : RectF? = null
        private val textSizeInPixel: Float = textSize * context.resources.displayMetrics.density
        var intrinsicWidth : Float
        init{
            val paint = Paint()
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT
            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)
            intrinsicWidth = titleBounds.width() + 2 * horizontalPadding
            if(icon != null) {
                intrinsicWidth += icon.iconWidth + icon.marginStart + icon.marginEnd
            }
        }
        fun draw(
            canvas: Canvas,
            rect: RectF,
            buttonBinding: ButtonBinding,
            swipeDirection: SwipeDirection
        ){
            val paint = Paint()

            //Отрисовка background
            paint.color = ContextCompat.getColor(context, colorRes)
            canvas.drawRect(rect, paint)


            //Отрисовка title
            paint.color = ContextCompat.getColor(context, android.R.color.white)
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT

            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)

            val y = rect.height() / 2 + titleBounds.height() / 2 - titleBounds.bottom
            if(icon != null){
                icon.draw(canvas, rect, horizontalPadding)
                when(icon.iconLocation){
                    is ButtonIcon.IconLocation.START -> {
                        when (swipeDirection) {
                            is SwipeDirection.START -> {
                                when (buttonBinding) {
                                    is ButtonBinding.EdgeRecyclerView -> {
                                        canvas.drawText(
                                            title,
                                            rect.right - horizontalPadding - titleBounds.width(),
                                            rect.top + y,
                                            paint
                                        )
                                    }
                                    is ButtonBinding.EdgeItemView -> {
                                        canvas.drawText(
                                            title,
                                            rect.left + icon.marginStart + icon.iconWidth + icon.marginEnd ,
                                            rect.top + y,
                                            paint
                                        )
                                    }
                                }
                            }
                            is SwipeDirection.END -> {
                                when (buttonBinding) {
                                    is ButtonBinding.EdgeRecyclerView -> {
                                        canvas.drawText(
                                            title,
                                            rect.left - horizontalPadding - titleBounds.width() - icon.marginEnd - icon.iconWidth - icon.marginStart,
                                            rect.top + y,
                                            paint
                                        )
                                    }
                                    is ButtonBinding.EdgeItemView -> {
                                        canvas.drawText(
                                            title,
                                            rect.right - horizontalPadding - titleBounds.width() - icon.marginEnd - icon.iconWidth - icon.marginStart,
                                            rect.top + y,
                                            paint
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is ButtonIcon.IconLocation.END -> {
                        canvas.drawText(
                            title,
                            rect.right -  horizontalPadding - titleBounds.width() - icon.marginEnd - icon.iconWidth - icon.marginStart,
                            rect.top + y,
                            paint
                        )
                    }
                }
            }
            else{
                canvas.drawText(title, rect.left + horizontalPadding, rect.top + y, paint)
            }
            clickableRegion = rect
        }

        fun handle(x: Float, y: Float) : Boolean {
            clickableRegion?.let {
                if (it.contains(x, y)) {
                    clickListener.onClick()
                    return true
                }
            }
            return false
        }

        /*
        @iconLocation
        0 - left (start)
        1 - right (end)
         */
        class ButtonIcon(
            private val context: Context,
            @DrawableRes val iconDrawableRes: Int,
            val iconLocation: IconLocation = IconLocation.START,
            val width: Int = 0,
            val height: Int = 0,
            val marginStart: Int = 0,
            val marginEnd: Int = 0
        ){
            var iconWidth : Int = context.resources.getDimensionPixelSize(width)
            private var iconHeight : Int
            private var intrinsicWidth : Int
            init {
                iconHeight = context.resources.getDimensionPixelSize(height)
                intrinsicWidth = marginStart +  iconWidth + marginEnd
            }

            fun draw(canvas: Canvas, rect: RectF, horizontalPadding: Float){
                val iconBounds = Rect()
                val iconDrawable = ContextCompat.getDrawable(context, iconDrawableRes)
                when(iconLocation){
                    is IconLocation.START -> {
                        if (iconHeight > rect.bottom - rect.top) {
                            iconHeight = (rect.bottom - rect.top).toInt()
                        }
                        val marginHeight = (rect.bottom - rect.top - iconHeight) / 2
                        with(iconBounds) {
                            left = (rect.left + marginStart).toInt()
                            top = (rect.top + marginHeight).toInt()
                            right = (rect.left + iconWidth + marginEnd).toInt()
                            bottom = (rect.bottom - marginHeight).toInt()
                        }
                        iconDrawable?.bounds = iconBounds
                        iconDrawable?.draw(canvas)
                    }
                    is IconLocation.END -> {
                        if (iconHeight > rect.bottom - rect.top) {
                            iconHeight = (rect.bottom - rect.top).toInt()
                        }


                        val marginHeight = (rect.bottom - rect.top - iconHeight) / 2
                        with(iconBounds) {
                            left = (rect.right - horizontalPadding - marginEnd - iconWidth).toInt()
                            top = (rect.top + marginHeight).toInt()
                            right = (rect.right - horizontalPadding - marginEnd).toInt()
                            bottom = (rect.bottom - marginHeight).toInt()
                        }
                        iconDrawable?.bounds = iconBounds
                        iconDrawable?.draw(canvas)
                    }
                }
            }

            sealed class IconLocation{
                object START : IconLocation()
                object END: IconLocation()
            }
        }
    }

    interface ButtonClickListener{
        fun onClick()
    }

    class ButtonGroup(
        val buttons: List<Button> = listOf(),
        val buttonBinding: ButtonBinding = ButtonBinding.EdgeItemView
    )
}



private fun List<SwipeHelper.Button>.intrinsicWidth(): Float {
    if (isEmpty()) return 0.0f
    return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
}