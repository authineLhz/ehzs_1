package app.modules.workflow;

/*import com.authine.cloudpivot.engine.workflow.spi.context.WorkflowActivityContext;
import com.authine.cloudpivot.engine.workflow.spi.function.ParticipantFunction;

import java.util.Arrays;
import java.util.List;

*//**
 * 自定义查找参与者函数
 *
 *//*
public class CustomParticipantFunction extends ParticipantFunction {

    *//**
     * 参与者函数查找逻辑
     * 方法名固定为【findParticipants】
     * 方法入参顺序入getFullName方法定义的参数一致
     * 最后一个入参不需要getFullName方法声明【系统自动注入】，固定为节点上下文【WorkflowActivityContext】
     * @param creater
     * @param createdDeptId
     * @param activityContext 节点上下文
     * @return 返回值用户ID
     *//*
    public List<String> findParticipants(String creater, String createdDeptId, WorkflowActivityContext activityContext) {
        //找人逻辑
        //返回List<String>格式
        return Arrays.asList(creater, createdDeptId);
    }

    *//**
     * 函数名
     * @return
     *//*
    @Override
    public String getName() {
        return "CustomParticipantFunction";
    }

    *//**
     * 函数全名
     * @return
     *//*
    @Override
    public String getFullName() {
        return "CustomParticipantFunction({creater}, {createdDeptId})";
    }

    *//**
     * 函数显示名称
     * @return
     *//*
    @Override
    public String getDisplayName() {
        return "自定义查找参与者函数示例";
    }

    *//**
     * 函数描述
     * @return
     *//*
    @Override
    public String getDescription() {
        return "CustomParticipantFunction({creater},{createdDeptId}),支持数据项、系统参数、函数输入。\n" +
                "函数输入需要填入参数，参数支持数据项、系统参数、常量；\n" +
                "参数格式要求：数据项、系统参数用{}包裹，常量用\"\"（英文）包裹";
    }
}*/
