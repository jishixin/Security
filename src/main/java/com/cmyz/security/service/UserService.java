package com.cmyz.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmyz.security.pojo.User;

import java.util.List;

/**
 * @author ：cmyz
 * @date ：2021/1/10 11:58
 */
public interface UserService extends IService<User> {

    User getUserByUsername(String username);

    List<String> getRolesById(Integer id);

    List<String> getResourcesById(Integer id);

}
