package com.jtsec.manager.mapper;

import com.jtsec.manager.pojo.model.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MenuMapper {

    int deleteByPrimaryKey (Integer menuId);

    int insert (Menu record);

    int insertSelective (Menu menu);

    Menu selectByPrimaryKey (Integer menuId);

    int updateByPrimaryKeySelective (Menu record);

    int updateByPrimaryKey (Menu record);

	List<Menu> selectMenuByUserVarible (@Param ("map") Map<String, Object> map);

    List<Menu> selectMenus ();

	List<String> selectPermsByUserId (@Param ("userId") Integer userId);
}