package com.apivideo.service;

import com.apivideo.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UsersService extends IService<Users> {
    Users findByUsername(String username);
}
