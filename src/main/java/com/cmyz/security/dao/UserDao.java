package com.cmyz.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmyz.security.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ：cmyz
 * @date ：2021/1/10 11:55
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT name FROM role WHERE id in (SELECT role_id FROM user_role WHERE user_id = #{id})")
    List<String> selectRolesByUserId(Integer id);

    @Select("SELECT res FROM resource WHERE id IN(SELECT  res_id FROM role_res WHERE res_id in (SELECT role_id FROM user_role WHERE user_id = #{id}))")
    List<String> selectResourcesByUserId(Integer id);

}
