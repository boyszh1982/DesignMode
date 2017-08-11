package com.nameless.base.nio;

import java.io.ByteArrayInputStream;

public class ByteArrayInputStreamTest {

	public static void main(String[] args) {
		
		byte[] src = new byte[] { 0, 60, 0, 60, 31, 97, 109, 113, 46, 99, 116, 97, 103, 45, 68, 110, 111, 57, 54, 105,
				87, 86, 67, 89, 84, 101, 105, 87, 106, 95, 100, 77, 110, 100, 55, 81, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 5,
				104, 101, 108, 108, 111 };
		
		ByteArrayInputStream in = new ByteArrayInputStream(src);
		
		byte[] dest = new byte[8];
		in.read(dest, 0, 8);
		System.out.println(dest[7]);
		
		
		
	}
}
