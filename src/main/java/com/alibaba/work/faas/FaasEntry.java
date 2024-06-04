package com.alibaba.work.faas;
 
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
 
import com.alibaba.work.faas.common.AbstractEntry;
import com.alibaba.work.faas.common.FaasInputs;
import com.alibaba.work.faas.util.DESUtil;
import com.alibaba.work.faas.util.DingOpenApiUtil;
import com.alibaba.work.faas.util.YidaConnectorUtil;
import com.alibaba.work.faas.util.YidaConnectorUtil.ConnectorTypeEnum;
import com.aliyun.dingtalkyida_1_0.models.BatchSaveFormDataRequest;
 
import org.apache.commons.lang3.StringUtils;
 
/**
 * 您的业务逻辑请从此类开始
 * 
 * @Date 2023/09/21 11:16 AM
 * @Description 宜搭Faas连接器函数入口，FC handler：com.alibaba.work.faas.FaasEntry::handleRequest
 * @Version 2.0
 * @Author bufan
 * 
 **/
public class FaasEntry extends AbstractEntry {
	public FaasEntry() {}
    @Override
    public JSONObject execute(FaasInputs faasInputs) {
		System.out.println("faasInputs: " + JSON.toJSONString(faasInputs));
		//填充宜搭工具类的上下文, 必须调用此方法!!! 请不要删除
		initYidaUtil(faasInputs);
 
		//登录态里的钉钉userId
		String userId = (String) faasInputs.getYidaContext().get("userId");
		//登录态里的钉钉corpId
		String corpId = (String) faasInputs.getYidaContext().get("corpId");
 
		//业务传的实际入参(如果您配置了参数映射(也就是点击了连接器工厂里的解析Body按钮并配置了各个参数描述和映射), 那么就是业务实际参数经过参数映射处理后的值)
		Map<String,Object> input = faasInputs.getInputs();
 
		/**
		 * 示例1, 在doYourBusiness方法里编写您的业务代码, 也可以将业务代码封装到其他Class源文件或方法里, cloudIDE和您的本地IDE基本无区别。
		 */
		JSONObject result = new JSONObject();
		try {
		   Object businessResult = doYourBusiness(input);
		   result.put("success",true);
		   result.put("result",businessResult);
		   result.put("error","");
		   return result;
		} catch (Exception e) {
		   result.put("success",false);
		   result.put("result",null);
		   result.put("error",e.getMessage());
		   return result;
		}
		/**
		 * 返回的JSONObject并不是一定要带success、result、error, 下面的代码只是示例, 具体返回哪些key-value由您自己决定, 尽量与您在宜搭连接器工厂里配置的出参结构保持一致即可
		 */
		// JSONObject result = new JSONObject();
		// result.put("success",true);
		// result.put("result","恭喜您, 成功调用宜搭FASS连接器!");
		// result.put("error","");
 
		// return result;
	}
 
	/**
	 * 将相关参数存到宜搭工具类里, 必须要调用此方法!!! 请不要删除!!!
	 *
	 * @param faasInputs
	 */
	private static final void initYidaUtil(FaasInputs faasInputs){
		/**
		 * 用于调用钉钉开放平台OpenAPI的accessToken, 宜搭提供的, 仅申请了钉钉开放平台的部分OpenAPI的调用权限
		 * 如果此accessToken不满足您的需求,可在钉钉开放平台创建您自己的钉钉应用并获取appKey和APPSecret并使用com.alibaba.work.faas.util.DingOpenApiUtil获取您自己的customAccessToken
		 *
		 * @see com.alibaba.work.faas.util.DingOpenApiUtil#getCustomAccessTokenThenCache(String,String)
		 */
		String accessToken = (String) faasInputs.getYidaContext().get("accessToken");
		// 设置钉开放平台访问token, 后续无需再设置
		DingOpenApiUtil.setAccessToken(accessToken);
 
		// try {
		//     //调用该方法就会自动存储customAccessToken, 之后请不要调用DingOpenApiUtil.setAccessToken(accessToken)将返回的customAccessToken覆盖宜搭传入的accessToken;
		//     DingOpenApiUtil.getCustomAccessTokenThenCache("您的钉钉应用appKey", "您的钉钉应用appSecret");
		// } catch (Exception e) {
		//     System.out.println("getCustomAccessTokenThenCache error:"+e.getMessage());
		// }
		/**
		 *调用宜搭连接器依赖consumeCode
		 */
		String consumeCode = (String)faasInputs.getYidaContext().get("consumeCode");
		//设置连接器消费码, 后续无需再设置
		YidaConnectorUtil.setConsumeCode(consumeCode);
	}
	private Object doYourBusiness(Map<String,Object> input) throws Exception{
		String content = (String)input.get("content");
String password = (String)input.get("password");
Integer type = Integer.parseInt(String.valueOf(input.get("type")));
/**
*在这里编写您的业务代码, 也可以将业务代码封装到其他类或方法里.
*/
JSONObject result = new JSONObject();
result.put("success",false);
result.put("result","");
result.put("error","");
String encryptContent;
if (0 == type) {
/**
* 加密
*/
encryptContent = DESUtil.encrypt(content, password);
System.out.println("加密后的字符串:" + encryptContent);
if (StringUtils.isEmpty(encryptContent)) {
result.put("error", "empty string got!");
return DESUtil.encrypt(content, password);
}
result.put("result", encryptContent);
result.put("success", true);
}
else {
/**
* 解密
*/
encryptContent = DESUtil.decrypt(content, password);
System.out.println("解密后的字符串:" + encryptContent);
if (StringUtils.isEmpty(encryptContent)) {
result.put("error", "empty string got!");
return result;
}
result.put("result", encryptContent);
result.put("success", true);
}
System.out.println("返回:" + JSON.toJSONString(result));
return result.get("result");
    }
 
    public static void main(String[] args) {
        Map<String, Object> m = new HashMap();
        m.put("content", "xxxx");
        m.put("password", "YYYYYYYY");
        m.put("type", 0);
		// //取实际的入参
		// String param1 = (String)input.get("参数1");
		// String param2 = (String)input.get("参数2");
		// String paramN = (String)input.get("参数N");
		// //业务处理
		// return "doYourBusiness成功";
	}
    private Object invokeYidaConnector(FaasInputs faasInputs) throws Exception {
        Map<String, Object> input = faasInputs.getInputs();
        String userId = (String)faasInputs.getYidaContext().get("userId");
        String corpId = (String)faasInputs.getYidaContext().get("corpId");
        JSONObject connectorActionInputs = new JSONObject();
        connectorActionInputs.put("unionId", Arrays.asList(userId));
        connectorActionInputs.put("subject", (String)input.get("title"));
        connectorActionInputs.put("creatorId", Arrays.asList(userId));
        connectorActionInputs.put("description", "Faas连接器里调用钉钉官方连接器创建待办");
        connectorActionInputs.put("dueTime", System.currentTimeMillis() + 600000L);
        connectorActionInputs.put("priority", 10L);
        YidaConnectorUtil.YidaResponse response = YidaConnectorUtil.invokeService("G-CONN-1016B8AEBED50B01B8D00009", "G-ACT-1016B8B1911A0B01B8D0000I", ConnectorTypeEnum.DING_INNER_CONNECTOR, (Long)null, connectorActionInputs);
        if (Objects.nonNull(response) && response.isSuccess()) {
            try {
                return YidaConnectorUtil.extractYidaConnectorExecutionResult(response);
            } catch (Exception var8) {
                throw new Exception(var8.getMessage());
            }
        } else {
            throw new Exception(Objects.isNull(response) ? "执行宜搭连接器失败" : response.getErrorCode() + " " + response.getErrorMsg());
        }
    }
}