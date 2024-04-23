package app.modules.app;

import com.authine.cloudpivot.app.integration.spi.AppNameSpaceService;
import com.authine.cloudpivot.foundation.util.api.NameSpaceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 自定义应用命名空间
 *
 * @author luoee
 * @date 2022/12/7
 */
@Slf4j
//@Service
public class CustomAppNameSpaceServiceImpl implements AppNameSpaceService {

    /**
     * 初始化应用命名空间，这个会影响数据库建表语句，不建议太长
     *
     * @return
     */
    @Override
    public String initAppNameSpace() {
        return NameSpaceUtils.caculateNameSpace();
    }
}
