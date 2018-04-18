package cn.easybuy.service.user;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.easybuy.dao.order.UserAddressDao;
import cn.easybuy.dao.order.UserAddressDaoImpl;
import cn.easybuy.dao.user.UserDao;
import cn.easybuy.dao.user.UserDaoImpl;
import cn.easybuy.dao.user.UserMapper;
import cn.easybuy.entity.User;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.utils.DataSourceUtil;
import cn.easybuy.utils.MyBatisUtil;
import cn.easybuy.utils.Pager;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;


public class UserServiceImpl implements UserService {
	
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Override
	public boolean add(User user){
		Integer count=0;  //所影响的行数
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			//count = session.insert("cn.easybuy.dao.user.UserMapper.add",user);
			//使用接口方式实现查询
			count = session.getMapper(UserMapper.class).add(user);
			session.commit();
		}catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally {
			MyBatisUtil.closeSession(session);
			return count>0;
		}
	}

	@Override
	public boolean update(User user) {
		Integer count=0;  //所影响的行数
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			//count = session.insert("cn.easybuy.dao.user.UserMapper.add",user);
			//使用接口方式实现查询
			count = session.getMapper(UserMapper.class).update(user);
			session.commit();
		}catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally {
			MyBatisUtil.closeSession(session);
			return count>0;
		}
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		Integer count=0;  //所影响的行数
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			//count = session.insert("cn.easybuy.dao.user.UserMapper.add",user);
			//使用接口方式实现查询
			count = session.getMapper(UserMapper.class).delete(userId);
			session.commit();
		}catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally {
			MyBatisUtil.closeSession(session);
			return count>0;
		}
	}

	@Override
	public User getUser(Integer userId, String loginName) {
		SqlSession session =null;
		User user=null;
		try {
			session = MyBatisUtil.createSession();
			user=session.getMapper(UserMapper.class).getUser(userId,loginName);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MyBatisUtil.closeSession(session);
			return user;
		}
	}

	@Override
	public List<User> getUserList(Integer currentPageNo, Integer pageSize){
		SqlSession session = null;
		List<User> userList = new ArrayList<User>();
		try {
			session = MyBatisUtil.createSession();
			int total = count();
			Pager pager = new Pager(total, pageSize, currentPageNo);
			userList=session.getMapper(UserMapper.class).getUserList((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MyBatisUtil.closeSession(session);
			return userList;
		}
	}
	

	@Override
	public int count() {
		Integer count=0;  //所影响的行数
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			count=session.getMapper(UserMapper.class).count();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MyBatisUtil.closeSession(session);
			return count;
		}
	}
}
