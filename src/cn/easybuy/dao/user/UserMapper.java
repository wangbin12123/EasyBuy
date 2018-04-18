package cn.easybuy.dao.user;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import cn.easybuy.entity.User;

public interface UserMapper {
	public int add(User user);
	public int delete(Integer id);
	public int update(User user);
	public List<User> getUserList(@Param("currentPageNo")Integer currentPageNo,@Param("pageSize")Integer pageSize);
	public Integer count();
	public User getUser(@Param("id")Integer id,@Param("loginName")String loginName);
}
