package cn.easybuy.utils;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {
	private static SqlSessionFactory factory;
	static {
		try {
			InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
			factory = new SqlSessionFactoryBuilder().build(is);
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	public static SqlSession createSession() {
		return factory.openSession(false);
	}
	public static void closeSession(SqlSession session) {
		if(null!=session) {
			session.close();
		}
	}
}
