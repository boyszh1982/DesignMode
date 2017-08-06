package com.nameless.base.memcached;

import com.schooner.MemCached.SchoonerSockIOPool;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class MemcachedTest {

	/**
-d 守护进程 -v -vv -vvv 日志级别不同
/usr/local/memcached/bin/memcached -d -m 10m -p 11211 -u root
/usr/local/memcached/bin/memcached -m -10m -p 11212 -u root
 * 
memcache.php 监控程序 安装过程
apt-get install apache2
sudo /etc/init.d/apache2 restart
apt-get install php libapache2-mod-php
vi /var/www/html/info.php
-
<?
	echo phpinfo();
?>
-
apt-get install mysql-server
apt-get install mysql-client
apt-get install phpmyadmin
sudo ln -s /usr/share/phpmyadmin /var/www

http://localhost/phpmyadmin

##
安装时遇到 xxx code(1) 问题需要，
mv /var/lib/dpkg/info /var/lib/dpkg/info.bak
mkdir /var/lib/dpkg/info
安装完成后
rm /var/lib/dpkg/info
mv /var/lib/dpkg/info.bak /var/lib/dpkg/info

##
php编码不编译问题处理
安装完之后，html文件可以正常解析，PHP源码直接输出，不能被解析。
通常情况下是apache未加载php模块，通常情况下需要修改httpd.conf文件，但是在ubuntu下为apache2.conf文件
修改如下：
设置

LoadModule php5_module /usr/lib/apache2/modules/llibphp7.0.so
AddType application/x-httpd-php .php
AddType application/x-httpd-php-source .phps

/usr/lib/apache2/modules/libphp5.so换成你的路径。
如果apache2.conf中该项配置，直接加入即可。
然后重启apache即可 
sudo /etc/init.d/apache2 restart

	 */
	
	static {
		String[] serverlist = {
				"192.168.128.143:11211",
				"192.168.128.143:11212",
				"192.168.128.143:11213",
				};

		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers(serverlist);
		pool.initialize();
		//System.out.println(pool);
	}
	

	public static void main(String[] args) {
		//SchoonerSockIOPool p = SchoonerSockIOPool.getInstance();
		//System.out.println(p.toString());
		
		MemCachedClient client = new MemCachedClient();
		client.add("key", "Hello Memcached ");
		client.get("key");
		System.out.println("---"+client.get("key"));
		
	}
}
