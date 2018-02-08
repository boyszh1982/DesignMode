package com.nameless.business.lottery;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class LotteryConsole {

	public static void main(String[] args) throws CloneNotSupportedException, InterruptedException {
		
        LotteryConsole mlt = new LotteryConsole();
        ArrayBlockingQueue<Lottery> lotterys = mlt.getLotteryList();
        for(int i=0;i<500;i++){
            Lottery lottery = mlt.getLottery(lotterys);
            System.out.println(String.format("剩余奖品数量%s,当前奖项%s", lotterys.size() , lottery));
        }
    }

    public Lottery getLottery(ArrayBlockingQueue<Lottery> lottys) throws CloneNotSupportedException, InterruptedException {
        if(lottys.isEmpty()){
            return new Lottery("8",new BigDecimal("18"),"八等奖");
        }
        return lottys.take();
    }

    public ArrayBlockingQueue<Lottery> getLotteryList() throws CloneNotSupportedException {
        List<Lottery> lotteryList = new ArrayList<>();
        
        lotteryList.addAll(new Lottery("1",new BigDecimal("888"),"一等奖").getLotteryList(10));
        lotteryList.addAll(new Lottery("2",new BigDecimal("588"),"二等奖").getLotteryList(25));
        lotteryList.addAll(new Lottery("3",new BigDecimal("188"),"三等奖").getLotteryList(40));
        lotteryList.addAll(new Lottery("4",new BigDecimal("88") ,"四等奖").getLotteryList(75));
        lotteryList.addAll(new Lottery("5",new BigDecimal("68") ,"五等奖").getLotteryList(100));
        lotteryList.addAll(new Lottery("6",new BigDecimal("58") ,"六等奖").getLotteryList(125));
        lotteryList.addAll(new Lottery("7",new BigDecimal("28") ,"七等奖").getLotteryList(125));
 
        //随机排序
        Collections.shuffle(lotteryList);
        ArrayBlockingQueue<Lottery> deque = new ArrayBlockingQueue<Lottery>(lotteryList.size());
        deque.addAll(lotteryList);
        return deque;
    }
	
}
