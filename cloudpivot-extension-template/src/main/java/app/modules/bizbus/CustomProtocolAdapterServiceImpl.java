package app.modules.bizbus;

import com.authine.cloudpivot.engine.spi.adapter.AbstractProtocolAdapterService;
import com.authine.cloudpivot.engine.spi.metadata.AdapterConfigParam;
import com.authine.cloudpivot.engine.spi.metadata.ProtocolAdapter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 自定义协议适配器
 */
@Slf4j
//@Component
public class CustomProtocolAdapterServiceImpl extends AbstractProtocolAdapterService {

    private final String CUSTOM_PROTOCOL_SERVICE_PARAM = "param";

    private ProtocolAdapter protocolAdapter = new ProtocolAdapter() {
        @Override
        public String getCode() {
            return "custom_protocol";
        }

        @Override
        public String getName() {
            return "Custom Protocol 适配器";
        }

        @Override
        public AdapterConfigParam getConfig() {
            final List<AdapterConfigParam.Param> commonList = Lists.newArrayList();
            commonList.add(new AdapterConfigParam.Param(CUSTOM_PROTOCOL_SERVICE_PARAM, "集成服务参数",
                    "集成服务参数", true));
            return new AdapterConfigParam(Collections.emptyList(), commonList);
        }

        @Override
        public String getDescription() {
            return "Custom Protocol Adapter";
        }
    };

    @Override
    public ProtocolAdapter getProtocolAdapter() {
        return protocolAdapter;
    }
}
