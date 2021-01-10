package com.cmyz.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmyz.security.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ：cmyz
 * @date ：2021/1/10 11:55
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
