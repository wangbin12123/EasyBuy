package cn.easybuy.service.user;

import cn.easybuy.dao.order.UserAddressMapper;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.param.UserAddressParam;
import cn.easybuy.utils.MyBatisUtil;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

/**
 * Created by bdqn on 2016/5/12.
 */
public class UserAddressServiceImpl implements UserAddressService {
    /**
     * 查询用户地址列表
     *
     * @param id
     * @return
     * @throws Exception
     */
    public List<UserAddress> queryUserAdressList(Integer id) throws Exception{
        SqlSession session = null;
        List<UserAddress> userAddressList = null;
        try {
            session = MyBatisUtil.createSession();
            UserAddressParam params = new UserAddressParam();
            params.setUserId(id);
            userAddressList = session.getMapper(UserAddressMapper.class).queryUserAddressList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	MyBatisUtil.closeSession(session);
        	session.rollback();
        }
        return userAddressList;
    }
    /**
     * 添加用户地址
     *
     * @param id
     * @param address
     * @return
     */
    @Override
    public Integer addUserAddress(Integer id, String address,String remark) {
        SqlSession session = null;
        Integer userAddressId = null;
        try {
            session = MyBatisUtil.createSession();
            UserAddress userAddress=new UserAddress();
            userAddress.setUserId(id);
            userAddress.setAddress(address);
            userAddress.setRemark(remark);
            userAddressId = session.getMapper(UserAddressMapper.class).add(userAddress);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	MyBatisUtil.closeSession(session);
        	session.rollback();
        }
        return userAddressId;
    }
    @Override
    public UserAddress getUserAddressById(Integer id) {
    	SqlSession session = null;
        UserAddress userAddress= null;
        try {
        	session = MyBatisUtil.createSession();
            userAddress = session.getMapper(UserAddressMapper.class).getUserAddressById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        	MyBatisUtil.closeSession(session);
        	session.rollback();;
            return  userAddress;
        }
    }
}
