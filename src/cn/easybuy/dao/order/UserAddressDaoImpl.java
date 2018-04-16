package cn.easybuy.dao.order;
import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.entity.Product;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.param.UserAddressParam;
import cn.easybuy.utils.EmptyUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bdqn on 2016/5/12.
 */
public class UserAddressDaoImpl extends BaseDaoImpl implements UserAddressDao {

    public UserAddressDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public UserAddress tableToClass(ResultSet rs) throws Exception {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(rs.getInt("id"));
        userAddress.setAddress(rs.getString("address"));
        userAddress.setUserId(rs.getInt("userId"));
        userAddress.setCreateTime(rs.getDate("createTime"));
        userAddress.setRemark(rs.getString("remark"));
        return userAddress;
    }

	@Override
	public List<UserAddress> queryUserAddressList(UserAddressParam params) {
		List<Object> paramsList=new ArrayList<Object>();   
		List<UserAddress> userAddresseList=new ArrayList<UserAddress>();
		StringBuffer sql=new StringBuffer("  select id,userId,address,createTime,isDefault,remark from easybuy_user_address  where 1=1 ");
		if(EmptyUtils.isNotEmpty(params.getUserId())){
			sql.append(" and userId = ? ");
			paramsList.add(params.getUserId());
		}
		if(EmptyUtils.isNotEmpty(params.getAddress())){
			sql.append(" and address like ? ");
			paramsList.add("%"+params.getAddress()+"%");
		}
		if(EmptyUtils.isNotEmpty(params.getSort())){
			sql.append(" order by " + params.getSort()+" ");
		}
		if(params.isPage()){
			sql.append(" limit  " + params.getStartIndex() + "," + params.getPageSize());
		}
		ResultSet resultSet = this.executeQuery(sql.toString(),paramsList.toArray());
		try {
			while (resultSet.next()) {
				UserAddress userAddress = this.tableToClass(resultSet);
				userAddresseList.add(userAddress);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			this.closeResource(resultSet);
		}
		return userAddresseList;
	}

	@Override
	public Integer add(UserAddress userAddress) {
		Integer id=0;
		String sql=" INSERT into easybuy_user_address(userId,address,createTime,isDefault,remark) VALUES(?,?,?,?,?) ";
        try {
            Object param[]=new Object[]{userAddress.getUserId(),userAddress.getAddress(),new Date(),0,userAddress.getRemark()};
            id=this.executeInsert(sql,param);
            userAddress.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	this.closeResource();
        }
        return id;
	}

	@Override
	public UserAddress getUserAddressById(Integer addressId) {
		List<Object> paramsList=new ArrayList<Object>();   
		StringBuffer sql=new StringBuffer(" select id,userId,address,createTime,isDefault,remark from easybuy_user_address  where id=? ");
		UserAddress userAddress =null;
		ResultSet resultSet = this.executeQuery(sql.toString(),new Object[]{addressId});
		try {
			while (resultSet.next()) {
				userAddress= this.tableToClass(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			this.closeResource(resultSet);
		}
		return userAddress;
	}
}
