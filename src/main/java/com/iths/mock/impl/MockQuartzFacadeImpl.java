package com.iths.mock.impl;

import com.iths.mock.MockQuartzFacade;
import com.iths.quartz.dto.ScheduleJobDTO;
import com.iths.quartz.facade.ScheduleJobFacade;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author sen.huang
 * @description
 * @date 2020/2/23.
 */
public class MockQuartzFacadeImpl implements MockQuartzFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockQuartzFacadeImpl.class);


    private ScheduleJobFacade scheduleJobFacade;

    @Override
    public String create(ScheduleJobDTO dto){
        LOGGER.info(ToStringBuilder.reflectionToString(dto));
        return scheduleJobFacade.create(dto).getId();
    }

    /**
     * 是否创建文件
     * @param path
     * @return
     * @throws IOException
     */
    public boolean makeFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            return file.createNewFile();
        }
        return false;
    }

    public String insert(ScheduleJobDTO dto){
        Map<String, String> insert = new UserService().insert();
        return insert.get("id");
    }


    /**
     * 用来Mock私有
     * @param num
     * @return
     */
    public boolean mockPrivate(Integer num){
        return isFive(num);
    }

    /**
     * 可以除5除尽
     * @param num
     * @return
     */
    private boolean isFive(Integer num){
        return num%5 == 0;
    }


}


