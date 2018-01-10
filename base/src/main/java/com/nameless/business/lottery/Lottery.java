package com.nameless.business.lottery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Lottery implements Cloneable,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5958761103131404779L;
	private String level = null;
	private BigDecimal prize = null;
	private String desc = null;

    Lottery (String level,BigDecimal prize,String desc ) {
        this.level = level;
        this.prize = prize;
        this.desc = desc;
    }
    /**
     *
     * @param cnt
     * @return
     * @throws CloneNotSupportedException
     */
    public List<Lottery> getLotteryList(int cnt) throws CloneNotSupportedException {
        return getLotteryList(cnt , true);
    }

    /**
     *
     * @param cnt
     * @param isClone
     *          true：list中的每个元素都是独立的对象，可以改变其中任意对象的值。
     *          false：list中每个元素都是相同的对象，如果修改其中任意元素的值，其他对象值也会改变。
     * @return
     * @throws CloneNotSupportedException
     */
    public List<Lottery> getLotteryList(int cnt,boolean isClone) throws CloneNotSupportedException {
        List<Lottery> result = new ArrayList<>();
        for(int i=0;i< cnt;i++){
            if(isClone) {
                //写法一,所有奖项为不通对象,注意使用方法会有不同.
                result.add(Lottery.class.cast(this.clone()));
            }
            else{
                //写法二,所有奖项为同一对象,注意使用方法会有不同.
                result.add(this);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%s]等奖，金额[%s]。",level,prize);
    }
    
}
