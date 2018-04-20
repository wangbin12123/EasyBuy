package cn.easybuy.dao.order;

import java.util.List;

import cn.easybuy.entity.UserAddress;
import cn.easybuy.param.UserAddressParam;

public interface UserAddressMapper {
	//查询当前用户所有地址
	public List<UserAddress> queryUserAddressList(UserAddressParam param);
	//增加用户地址
	public Integer add(UserAddress userAddress);
	//根据id查询用户地址
	public UserAddress getUserAddressById(Integer addressId);
}
