package com.czm.wormforwb.mapper;

import com.czm.wormforwb.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> queryAllUserInfo();

    List<String> querySubIdByUser(@Param("uid") String uid);

}
