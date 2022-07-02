package com.taotao.cloud.sys.biz.support.image_combiner.element;


import com.taotao.cloud.sys.biz.support.image_combiner.enums.Direction;

/**
 * CombineElement
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:16:10
 */
public abstract class CombineElement<T extends CombineElement> {
    private int x;                  //起始坐标x，相对左上角
    private int y;                  //起始坐标y，相对左上角
    private boolean center;         //是否居中
    private Direction direction = Direction.LeftRight;    //绘制方向
    private float alpha = 1.0f;     //透明度

    public int getX() {
        return x;
    }

	@SuppressWarnings("unchecked")
    public T setX(int x) {
        this.x = x;
        return (T) this;
    }

    public int getY() {
        return y;
    }

	@SuppressWarnings("unchecked")
    public T setY(int y) {
        this.y = y;
        return (T) this;
    }

    public boolean isCenter() {
        return center;
    }

	@SuppressWarnings("unchecked")
    public T setCenter(boolean center) {
        this.center = center;
        return (T) this;
    }

    public Direction getDirection() {
        return direction;
    }

	@SuppressWarnings("unchecked")
    public T setDirection(Direction direction) {
        this.direction = direction;
        return (T) this;
    }

    public float getAlpha() {
        return alpha;
    }

	@SuppressWarnings("unchecked")
    public T setAlpha(float alpha) {
        this.alpha = alpha;
        return (T) this;
    }
}
