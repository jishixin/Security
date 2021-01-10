package com.cmyz.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmyz.security.dao.UserDao;
import com.cmyz.security.pojo.User;
import com.cmyz.security.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：cmyz
 * @date ：2021/1/10 11:59
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    UserDao userDao;

    public User getUserByUsername(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userDao.selectOne(wrapper);
    }

    @Override
    public List<String> getRolesById(Integer id) {
        return userDao.selectRolesByUserId(id);
    }

    @Override
    public List<String> getResourcesById(Integer id) {
        return userDao.selectResourcesByUserId(id);
    }

}
