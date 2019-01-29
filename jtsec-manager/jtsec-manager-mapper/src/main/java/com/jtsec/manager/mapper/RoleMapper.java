package com.jtsec.manager.mapper;

import com.jtsec.manager.pojo.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RoleMapper {

    int deleteByPrimaryKey (Integer roleId);

    int insert (Role record);

    int insertSelective (Role record);

    Role selectByPrimaryKey (Integer roleId);

    int updateByPrimaryKeySelective (Role record);

    int updateByPrimaryKey (Role record);

	List<Role> selectRoleByVarible (@Param ("map") Map<String, Object> map);

    Integer delBatch (@Param ("list") List<Integer> roleIdList);

    Role selectRoleById (@Param ("id") Integer id);

	List<Role> selectRolesByUserId (@Param ("userId") Integer userId);
}