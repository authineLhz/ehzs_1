package app.modules.bizbus;

import com.authine.cloudpivot.engine.enums.type.bizservice.MethodReturnType;
import com.authine.cloudpivot.engine.spi.metadata.vo.BaseMethodConfigVO;
import lombok.Getter;
import lombok.Setter;

/**
 * DataTable, DataSql sql statement config class
 *
 * @author zhangjie
 * @date 2020-04-09
 */
@Getter
@Setter
public class CustomMethodConfigVO extends BaseMethodConfigVO {
    private static final long serialVersionUID = 1L;

    private MethodReturnType returnType;    //返回值类型
}

