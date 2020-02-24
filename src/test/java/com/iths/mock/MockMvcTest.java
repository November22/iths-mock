package com.iths.mock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iths.ruleengine.dto.RuleGenerateParamDTO;
import com.iths.ruleengine.dto.RuleInputCategoryDTO;
import com.iths.ruleengine.dto.RuleRuleDTO;
import com.iths.ruleengine.facade.RuleRuleFacade;
import com.iths.ruleengine.facade.RuleTemplateFacade;
import com.iths.ruleengine.web.RuleController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @link https://www.cnblogs.com/jpfss/p/10950904.html
 * MockMvc测试
 * @author sen.huang
 * @description
 * @date 2020/2/23.
 */
public class MockMvcTest {
    private RuleController controller = new RuleController();

    private  MockMvc mockMvc;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Mock URI
     * @throws Exception
     */
    @Test
    public void testMockUri() throws Exception {
        RuleTemplateFacade templateFacade = PowerMockito.mock(RuleTemplateFacade.class);
        RuleInputCategoryDTO returnDto = new RuleInputCategoryDTO();
        returnDto.setKy("input");
        returnDto.setName("输入框");
        returnDto.setDataClass("java.lang.String");

        PowerMockito.when(templateFacade.createInputCategory(ArgumentMatchers.any(RuleInputCategoryDTO.class))).thenReturn(returnDto);
        ReflectionTestUtils.setField(controller, "ruleTemplateFacade", templateFacade);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rule/createCategory")
                .param("ky", "input")
                .param("name", "输入框")
                .param("dataClass", "java.lang.String");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertFalse((boolean)JSONObject.parseObject(content).get("hasError"));
    }


    /**
     * Mock RequestBody
     * /rule/createRule
     */
    @Test
    public void testMockRequestBody() throws Exception {
        RuleRuleFacade ruleFacade = PowerMockito.mock(RuleRuleFacade.class);

        RuleRuleDTO ruleRuleDTO = new RuleRuleDTO();
        ruleRuleDTO.setId("9527");
        PowerMockito.when(ruleFacade.createByParam(ArgumentMatchers.any(RuleGenerateParamDTO.class))).thenReturn(ruleRuleDTO);
        ReflectionTestUtils.setField(controller, "ruleFacade", ruleFacade);

        JSONObject requestData = new JSONObject();
        requestData.put("id","9527");
        requestData.put("templateId","t9527");
        requestData.put("ruleName","规则名称");
        JSONArray array = new JSONArray();
        JSONObject a1 = new JSONObject();
        a1.put("conditionVal", "+");
        a1.put("calculateVal", "123");
        JSONObject a2 = new JSONObject();
        a2.put("conditionVal", "-");
        a2.put("calculateVal", "456");
        array.add(a1);
        array.add(a2);

        requestData.put("conditionCalculateVOS", array);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/rule/createRule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestData.toJSONString());
        MvcResult result = mockMvc.perform(builder).andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertFalse((boolean)JSONObject.parseObject(content).get("hasError"));

    }
}
