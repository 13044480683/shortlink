package com.nageoffer.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.dao.entity.GroupDO;
import com.nageoffer.shortlink.admin.dao.mapper.GroupMapper;
import com.nageoffer.shortlink.admin.service.GroupService;
import com.nageoffer.shortlink.admin.util.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    public Boolean checkGroupExist(String GID){
        LambdaQueryWrapper queryWrapper= Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid,GID)
                //ntodo 获取真实的用户ID在后续过程中
                .eq(GroupDO::getUsername,null);
        GroupDO result=baseMapper.selectOne(queryWrapper);
        if (result==null){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void insetGroup(String name) {
        String GID=null;
        do {
            GID=RandomStringGenerator.generateRandomString();
        }while (!checkGroupExist(GID));
        baseMapper.insert(new GroupDO().builder()
                .gid(GID)
                .sortOrder(0)
                .name(name)
                .username("当前用户")
                .build()
        );
    }

}
