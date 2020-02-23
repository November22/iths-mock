package com.iths.mock;

import com.iths.mock.impl.MockQuartzFacadeImpl;
import com.iths.mock.untils.MockUtils;
import com.iths.quartz.dto.ScheduleJobDTO;
import com.iths.quartz.facade.ScheduleJobFacade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

/**
 * PowerMock 使用案例
 * @author sen.huang
 * @description
 * @date 2020/2/23.
 */
@RunWith(PowerMockRunner.class) // 告诉JUnit使用PowerMockRunner进行测试
// 所有需要测试的类列在此处，适用于模拟final类或有final, private, static, native方法的类，mockNew对象时，必须把提前准备
@PrepareForTest({MockUtils.class, MockQuartzFacadeImpl.class})
@PowerMockIgnore("javax.management.*") //为了解决使用powermock后，提示classloader错误
public class MockFacadeTest {

    @InjectMocks
    private MockQuartzFacadeImpl mockQuartzFacade;

    @Mock
    private ScheduleJobFacade scheduleJobFacade;

    /**
     * Mock RPC接口
     */
    @Test
    public void testMockRpcCreate(){
        ScheduleJobDTO returnDto = new ScheduleJobDTO();
        returnDto.setId("9527");
        PowerMockito.when(scheduleJobFacade.create(ArgumentMatchers.any(ScheduleJobDTO.class))).thenReturn(returnDto);
        String result = mockQuartzFacade.create(returnDto);

        Assert.assertEquals(result,"9527");
    }

    /**
     * Mock静态方法
     */
    @Test
    public void testMockStatic(){
        PowerMockito.mockStatic(MockUtils.class);
        PowerMockito.when(MockUtils.nextInt(ArgumentMatchers.any(Integer.class))).thenReturn(5);

        Assert.assertEquals(5, MockUtils.nextInt(10));
    }


    /**
     * Mock new对象
     */
    @Test
    public void testMockNewObj() throws Exception {
        //Mock一个文件对象
        File mockFile = PowerMockito.mock(File.class);
        //调用new File(String path)时返回 mockFile
        //如果withArguments不传入 ArgumentMatchers.anyString()，传入一个固定的字符串，如"abc"，
        // 这样new File传入参数不为"abc"时，不会返回mockFile，而是返回一个null
        PowerMockito.whenNew(File.class).withArguments(ArgumentMatchers.anyString()).thenReturn(mockFile);
        //调用mockFile.exists 时返回true
        PowerMockito.when(mockFile.exists()).thenReturn(true);

        //不会创建文件，每次exists都为true
        Assert.assertFalse(mockQuartzFacade.makeFile("cccddss"));
    }

    /**
     * Mock 私有方法
     * @throws Exception
     */
    @Test
    public void testMockPrivate() throws Exception {
        MockQuartzFacadeImpl spy = PowerMockito.spy(mockQuartzFacade);

        PowerMockito.when(spy, "mockPrivate", ArgumentMatchers.anyInt()).thenReturn(true);

        Assert.assertTrue(spy.mockPrivate(3));

    }

}
