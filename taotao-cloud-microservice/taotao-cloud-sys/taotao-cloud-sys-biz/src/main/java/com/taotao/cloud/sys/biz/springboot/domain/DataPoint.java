package com.taotao.cloud.sys.biz.springboot.domain;

import java.io.Serializable;
/**
 * 
 * @author duhongming
 *
 */
public class DataPoint implements Serializable{  
	  
    /**
	 * 
	 */
	private static final long serialVersionUID = -8781521592742322452L;

	/** the x value */  
    public Double x;  
  
    /** the y value */  
    public Double y;  
  
    public DataPoint() {
		super();
	}

	/** 
     * Constructor. 
     *  
     * @param x 
     *            the x value 
     * @param y 
     *            the y value 
     */  
    public DataPoint(Double x, Double y) {  
        this.x = x;  
        this.y = y;  
    }

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}  
    
}  
