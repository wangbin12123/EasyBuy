package cn.easybuy.dao.user;

import java.sql.SQLException;
import java.util.List;

import cn.easybuy.dao.IBaseDao;
import cn.easybuy.entity.User;
import cn.easybuy.param.UserParam;

/***
 * UserDao 用户业务的dao层
 * 从父类继承下的被使用的方法
 * User getById(userId);
 * Integer userDao.getRowCount(params);
 * List<User> userDao.getRowList(params);
 */
public interface UserDao extends IBaseDao {

	int add(User user) throws Exception;//新增用户信息

	int update(User user) throws Exception;//更新用户信息

	public int deleteUserById(String id) throws Exception;
	
	public List<User> getUserList(Integer currentPageNo,Integer pageSize)throws Exception;
	
	public Integer count() throws Exception;
	
	public User getUser(Integer id,String loginName) throws Exception;
}
