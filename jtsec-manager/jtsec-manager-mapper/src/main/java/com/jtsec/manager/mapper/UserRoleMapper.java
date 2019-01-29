package com.jtsec.manager.mapper;

import com.jtsec.manager.pojo.model.UserRoleKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface UserRoleMapper {

    int deleteByPrimaryKey (UserRoleKey key);

    int insert (UserRoleKey record);

    int insertSelective (UserRoleKey record);

    Integer insertBatch (@Param ("list") List<UserRoleKey> userRoleKeyList);

    Integer delBatch (@Param ("list") List userIdList);
}