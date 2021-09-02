package com.wz.desensitizationRuleTool.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

/**
 * 分页工具类
 * 这里数据特殊，需要根据具体业务处理分页
 * 本工具类分页主要针对从excel读取获得的数据
 * @author Angelo
 * @date 2021/9/2 15:45
 **/
public class PageUtil {

    /**
     * 根据分页参数获取分页结果数据
     * @param pageIndex 分页索引
     * @param pageSize 分页大小
     * @param allDataList 全量数据
     * @return
     */
    public static ObservableList<Map<String, Object>> createPageList(int pageIndex, int pageSize,ObservableList<Map<String, Object>> allDataList){
        //定义分页结果集容器
        ObservableList<Map<String, Object>> resultList = FXCollections.observableArrayList();
//        循环pageSize次数，获取对等的数据记录
        for(int i =pageIndex*pageSize; i<(pageIndex*pageSize+pageSize-1); i++){
            if(i<allDataList.size()){
                resultList.add(allDataList.get(i));
            }
        }
        return resultList;
    }

}
