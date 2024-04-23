package app.modules.bizbus;

import com.authine.cloudpivot.engine.api.facade.BizServiceFacade;
import com.authine.cloudpivot.engine.spi.metadata.ProtocolAdapter;
import com.authine.cloudpivot.engine.spi.metadata.vo.ServiceConfigVO;
import com.authine.cloudpivot.engine.spi.runtime.ProtocolAdapterRuntimeFactory;
import com.authine.cloudpivot.engine.spi.runtime.ProtocolAdapterRuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自定义协议适配器（运行态）
 *
 * @author luoee
 * @date 2022/12/5
 */
//@Service
public class CustomProtocolAdapterRuntimeFactory implements ProtocolAdapterRuntimeFactory {

    @Autowired
    private BizServiceFacade bizServiceFacade;

    @Override
    public ProtocolAdapterRuntimeService getRuntimeService(ProtocolAdapter protocolAdapter, String bizServiceCode,
                                                           ServiceConfigVO bizServiceConfig) {
        return new CustomProtocolAdapterRuntimeServiceImpl(bizServiceFacade, bizServiceCode, bizServiceConfig);
    }

    @Override
    public String getProtocolAdapterType() {
        return "custom_protocol";
    }

}
