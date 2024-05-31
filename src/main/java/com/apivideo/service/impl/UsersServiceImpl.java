package com.apivideo.service.impl;

import com.apivideo.entity.Users;
import com.apivideo.mapper.UsersMapper;
import com.apivideo.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users findByUsername(String username) {
        return this.lambdaQuery().eq(Users::getUsername, username).one();
    }
}
