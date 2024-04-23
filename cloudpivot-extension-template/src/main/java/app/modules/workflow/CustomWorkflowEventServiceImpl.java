package app.modules.workflow;

import com.authine.cloudpivot.engine.api.model.runtime.WorkflowInstanceModel;
import com.authine.cloudpivot.engine.domain.runtime.BizObject;
import com.authine.cloudpivot.engine.enums.type.IMMessageType;
import com.authine.cloudpivot.engine.workflow.spi.WorkflowEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 流程事件订阅
 *
 * @author luoee
 * @date 2022/12/5
 */
//@Service
@Slf4j
public class CustomWorkflowEventServiceImpl implements WorkflowEventService {

    @Override
    public void doEvent(IMMessageType msgType, BizObject bizObject, Object model) {
        //流程启动事件
        if (IMMessageType.WFSTART == msgType && model instanceof WorkflowInstanceModel) {
            log.info("流程启动成功，流程实例id为{}", ((WorkflowInstanceModel) model).getId());
        }
    }
}
