package com.yy.dao;

import com.yy.entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaDao {

    /**
     * 列出区域列表
     * @return
     */
    List<Area> queryArea();
}
