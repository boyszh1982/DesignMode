package com.nameless.base.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2017-08-08.
 */
public class FileChannelTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("c:/temp/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {

            //System.out.println("Read " + bytesRead);
            /*
            把limit换成当前BUFFER的长度位置。
            把指针位置换成0，那么0到position的位置就是BUFFER读到的内容。
            如果position=0 , limit=0 那么就没有内容读入。
            public final Buffer flip() {
                limit = position;
                position = 0;
                mark = -1;
                return this;
            }
             */
            //只要从buff里往外拿数据就要使用flip方法
            buf.flip();
            
            //测试buf.rewind();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            System.out.println("\n----- 以上第一次打印buff内容 -----");
            //重置取出位置，因为每次buf.get()位置都会变动，想重新取需要用buf.rewind();
            buf.rewind();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            System.out.println("\n----- 以上buf.rewind()后再打印buff内容 -----");
            
            //测试buf.compact(); //重置一下
            buf.rewind();
            
            System.out.println("打印第一个位置的字符："+(char) buf.get() + ",position=" + buf.position() );
            char c = (char) buf.get();
            System.out.println("打印第二个位置的字符："+ ( c == '\r' ? 'e':c) + ",position=" + buf.position() );
            //删除已经获取过的两个字符
            /* 也就是 dest[0 , 复制的长度] = src[srcPos , 复制的长度] 这样复制的
             * 那么这样就会有很多null值
             * {@link System.arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length)
             * public ByteBuffer compact() {
			        System.arraycopy(hb, ix(position()), hb, ix(0), remaining());
			        position(remaining());
			        limit(capacity());
			        discardMark();
			        return this;
			    }
             */
            // 这个方法需要怎么用？
            buf.compact();
            System.out.println(buf.getClass());
            //buf.clear();
            //再重置一下
            buf.rewind();
            //打印现在的buff
            
            while (buf.hasRemaining()) {
            	char cc = (char) buf.get();
                System.out.print( cc + ",position" + buf.position() + "\t");
            }
            
            //inChannel.write(buf);
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
