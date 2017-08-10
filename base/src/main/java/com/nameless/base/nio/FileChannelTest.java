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
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());

            }
            //重置取出位置，因为每次buf.get()位置都会变动，想重新取需要用buf.rewind();
            buf.rewind();
            inChannel.write(buf);
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
