package com.example.mapview.richpath.animator

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.Interpolator
import com.example.mapview.richpath.RichPath

class AnimationBuilder(
    private val richPathAnimator: RichPathAnimator,
    private val paths: Array<out RichPath>
) {

    companion object {
        private const val DEFAULT_DURATION = 300L
        private const val DEFAULT_START_DELAY = 0L
    }

    val animators = arrayListOf<ValueAnimator>()

    private var duration = DEFAULT_DURATION
    private var startDelay = DEFAULT_START_DELAY
    private var interpolator: Interpolator? = null
    private var repeatMode: RepeatMode = RepeatMode.Restart
    private var repeatCount = 0

    private fun property(propertyName: String, vararg values: Float) {
        for (path in paths) {
            val objectAnimator = ObjectAnimator.ofFloat(path, propertyName, *values)
            applyAnimatorProperties(objectAnimator, path)
        }
    }

    fun start(): RichPathAnimator {
        richPathAnimator.start()
        return richPathAnimator
    }

    @Deprecated("It doesn't make sense to cancel while you are still building")
    fun cancel() {
        richPathAnimator.cancel()
    }

    fun duration(duration: Long) = apply {
        this.duration = duration
        for (animator in animators) {
            animator.duration = duration
        }
    }

    fun interpolator(interpolator: Interpolator) = apply {
        this.interpolator = interpolator
        for (animator in animators) {
            animator.interpolator = interpolator
        }
    }

    fun repeatMode(repeatMode: RepeatMode) = apply {
        this.repeatMode = repeatMode
        for (animator in animators) {
            animator.repeatMode = repeatMode.value
        }
    }

    fun repeatCount(repeatCount: Int) = apply {
        this.repeatCount = repeatCount
        for (animator in animators) {
            animator.repeatCount = repeatCount
        }
    }

    fun fillColor(vararg colors: Int) = apply {
        color("fillColor", *colors)
    }

    fun strokeColor(vararg colors: Int) = apply {
        color("strokeColor", *colors)
    }

    private fun color(propertyName: String, vararg colors: Int) = apply {
        for (path in paths) {
            val objectAnimator = ObjectAnimator.ofInt(path, propertyName, *colors)
            objectAnimator.setEvaluator(ArgbEvaluator())
            applyAnimatorProperties(objectAnimator, path)
        }
    }

    private fun applyAnimatorProperties(animator: ValueAnimator, path: RichPath?) {
        path ?: return
        animator.duration = duration
        animator.startDelay = startDelay
        animator.repeatMode = repeatMode.value
        animator.repeatCount = repeatCount
        interpolator?.let { animator.interpolator = it }
        //add animator to the animators list
        this.animators.add(animator)
    }

    fun scaleX(vararg values: Float): AnimationBuilder = apply {
        property("scaleX", *values)
    }

    fun scaleY(vararg values: Float): AnimationBuilder = apply {
        property("scaleY", *values)
    }

    fun scale(vararg values: Float): AnimationBuilder = apply {
        scaleX(*values)
        scaleY(*values)
    }

    fun width(vararg values: Float): AnimationBuilder = apply {
        property("width", *values)
    }

    fun height(vararg values: Float): AnimationBuilder = apply {
        property("height", *values)
    }
}
