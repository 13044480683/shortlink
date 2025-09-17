package com.nageoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.constant.RedisConstants;
import com.nageoffer.shortlink.admin.common.convention.exception.ClientException;
import com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserMapper;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper=Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername,username);
        UserDO userDO=baseMapper.selectOne(queryWrapper);
        UserRespDTO result=new UserRespDTO();
        BeanUtils.copyProperties(userDO,result);
        return result;
    }

    @Override
    public Boolean getHaveUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void registerUser(UserRegisterReqDTO requestparams) {
        //先检测用户名是否存在
        if (getHaveUsername(requestparams.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_EXIST);
        }else {
            RLock lock=redissonClient.getLock(RedisConstants.LOCK_USER_REGISTER_KEY+requestparams.getUsername());
            try {
                if (lock.tryLock()){
                    int result=baseMapper.insert(BeanUtil.toBean(requestparams,UserDO.class));
                    if (result<1){
                        throw new ClientException(UserErrorCodeEnum.USER_INSERT_ERROR);
                    }
                    userRegisterCachePenetrationBloomFilter.add(requestparams.getUsername());
                }else{
                    throw new ClientException(UserErrorCodeEnum.USER_EXIST);
                }
            }finally {
                lock.unlock();
            }

        }
    }

    @Override
    public UserLoginRespDTO loginUser(UserLoginReqDTO requestParams) {
        //要做一个防止重复登录的功能
        LambdaQueryWrapper<UserDO> queryWrapper=Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername,requestParams.getUsername())
                .eq(UserDO::getPassword,requestParams.getPassword());
        if (baseMapper.selectOne(queryWrapper)!=null){
               //存在 登录成功 redis记录token 并且返回给用户token
        }else {
            throw new ClientException("用户名不存在或密码错误");
        }
        return new UserLoginRespDTO();

    }
}
