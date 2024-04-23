package app.modules.bizbus;

import com.alibaba.fastjson.JSON;
import com.authine.cloudpivot.engine.api.exceptions.ServiceException;
import com.authine.cloudpivot.engine.api.facade.BizServiceFacade;
import com.authine.cloudpivot.engine.api.model.bizservice.BizServiceMethodModel;
import com.authine.cloudpivot.engine.api.model.bizservice.FinalParamType;
import com.authine.cloudpivot.engine.api.model.bizservice.ParameterModel;
import com.authine.cloudpivot.engine.enums.ErrCode;
import com.authine.cloudpivot.engine.enums.type.bizservice.MethodReturnType;
import com.authine.cloudpivot.engine.spi.metadata.Options;
import com.authine.cloudpivot.engine.spi.metadata.vo.BaseMethodConfigVO;
import com.authine.cloudpivot.engine.spi.metadata.vo.ServiceConfigVO;
import com.authine.cloudpivot.engine.spi.runtime.ProtocolAdapterRuntimeService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoee
 * @date 2022/12/5
 */
@Slf4j
public class CustomProtocolAdapterRuntimeServiceImpl implements ProtocolAdapterRuntimeService {

    private BizServiceFacade bizServiceFacade;
    private final ServiceConfigVO bizServiceConfig;
    private final String bizServiceCode;

    public CustomProtocolAdapterRuntimeServiceImpl(BizServiceFacade bizServiceFacade,
                                                   String bizServiceCode,
                                                   ServiceConfigVO bizServiceConfig) {
        this.bizServiceFacade = bizServiceFacade;
        this.bizServiceCode = bizServiceCode;
        this.bizServiceConfig = bizServiceConfig;
    }


    @Override
    public BizServiceMethodModel getBizServiceMethod(String bizServiceMethodCode) {
        return bizServiceFacade.getStaticBizServiceMethodByCode(bizServiceCode, bizServiceMethodCode);
    }

    @Override
    public List<BizServiceMethodModel> getBizServiceMethods() {
        return bizServiceFacade.getStaticBizServiceMethods(bizServiceCode);
    }
    @Override
    public Object invoke(String bizServiceMethodCode, Map<String, Object> args, Options options) {
        if (log.isDebugEnabled()) {
            log.debug("bizServiceMethodCode = {}, args = {}, options = {}", bizServiceMethodCode, args, JSON.toJSONString(options));
        }

        Optional<BizServiceMethodModel> optional = getBizServiceMethods().stream().filter(bs -> bizServiceMethodCode.equals(bs.getCode())).findAny();
        if (!optional.isPresent()) {
            throw new ServiceException(ErrCode.BIZ_METHOD_EMPTY, "业务集成方法不存在");
        }

        BizServiceMethodModel serviceMethod = optional.get();
        CustomMethodConfigVO configVO = (CustomMethodConfigVO)getConfig(serviceMethod.getConfigJson());
        MethodReturnType returnType = configVO.getReturnType();
        if (log.isDebugEnabled()) {
            log.debug("returnType = {}", returnType);
        }

        List<ParameterModel> inputParameters = serviceMethod.getInputParameterModels();
        Map<String, Object> finalArgs = bizServiceFacade.getFinalArgs(FinalParamType.COMMON, inputParameters, args);
        try {
            Object result = null;
            if (MethodReturnType.SingleObject == returnType) {
                ;
            } else if (MethodReturnType.List == returnType) {
                result = Lists.newArrayList();
            } else {
                ;
            }
            return result;
        } catch (Exception e){
            log.error("CustomProtocol调用未知错误", e);
            throw new ServiceException(ErrCode.UNKNOW_ERROR, "CustomProtocol调用未知错误");
        }
    }

    @Override
    public BaseMethodConfigVO getConfig(String configJson){
        return JSON.parseObject(configJson, CustomMethodConfigVO.class);
    }
}
