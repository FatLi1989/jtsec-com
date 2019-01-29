package com.jtsec.manager.mapper;

import com.jtsec.manager.pojo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    int deleteByPrimaryKey (Integer userId);

    Integer insert (User record);

    int insertSelective (User record);

    User selectByPrimaryKey (Integer userId);

    int updateByPrimaryKeySelective (User record);

    int updateByPrimaryKey (User record);

	List<User> queryUserbyVarible (@Param ("map") Map<String, Object> map);

    User selectUser (@Param ("id") Integer id);

    Integer delBatch (@Param ("list") List userIdList);

	List<User> selectUsers (@Param ("map") Map<String, Object> map);

	User getUserByLoginName (@Param ("loginName") String loginName);
}