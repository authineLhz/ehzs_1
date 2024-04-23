package app.modules.notify;

import com.authine.cloudpivot.engine.api.model.dto.MessageDTO;
import com.authine.cloudpivot.engine.api.model.dto.SimpleMsgDTO;
import com.authine.cloudpivot.engine.service.message.IMessageService;
import com.authine.cloudpivot.engine.service.organization.exception.DataSourceException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作消息通知
 *
 * @author luoee
 * @date 2022/12/5
 */
//@Service
public class CustomMessageService implements IMessageService {

    @Override
    public List<String> sendMsg(MessageDTO message) throws DataSourceException {
        return null;
    }

    @Override
    public List<String> sendSimpleTextMsg(SimpleMsgDTO message) throws DataSourceException {
        return null;
    }

    @Override
    public String initChannel() {
        return "Custom";
    }
}
