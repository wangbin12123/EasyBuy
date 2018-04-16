package cn.easybuy.dao.user;

import java.util.List;

import cn.easybuy.entity.User;

public interface UserMapper {
	public int add(User user);
	public int delete(Integer id);
	public int update(User user);
	public List<User> getUserList(Integer currentPageNo,Integer pageSize);
}
