package com.jtsec.manager.mapper;

import com.jtsec.manager.pojo.model.RoleMenuKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface RoleMenuMapper {

    int deleteByPrimaryKey (RoleMenuKey key);

    int insert (RoleMenuKey record);

    int insertSelective (RoleMenuKey record);

	Integer insertBatch (@Param ("list") List<RoleMenuKey> roleMenuKeyList);

	Integer delBatch (@Param ("list") List<Integer> roleIdList);
}