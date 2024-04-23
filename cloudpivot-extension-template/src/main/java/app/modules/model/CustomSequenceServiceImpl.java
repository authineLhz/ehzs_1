package app.modules.model;

import com.authine.cloudpivot.engine.api.model.runtime.BizObjectModel;
import com.authine.cloudpivot.engine.model.spi.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单据号策略
 *
 * @author luoee
 * @date 2022/12/5
 */
@Slf4j
//@Service
public class CustomSequenceServiceImpl implements SequenceService {

//    @Autowired
//    private SequenceSettingService sequenceSettingService;
    @Autowired
    private RedisTemplate redis;

    /**
     * 生成单据号
     *
     * @param schemaCode
     * @param formCode
     * @param bizObjectModel
     * @return
     */
    @Override
    public String createSequenceNo(String schemaCode, String formCode, BizObjectModel bizObjectModel) {
        //最大长度限制为32位
        return "1234567890";
    }

    /**
     * 批量生成单据号(数据导入时使用)
     * 内置的方法实现将单据号放入缓存队列
     *
     * @param schemaCode
     * @param formCode
     * @param bizObjectModelList
     * @return
     */
    @Override
    public String batchGenerateSequenceNo(String schemaCode, String formCode, List<BizObjectModel> bizObjectModelList) {
//        return sequenceSettingService.batchSequenceNo(schemaCode, formCode, bizObjectModelList);
        return null;
    }

    /**
     * 从缓存队列中获取单据号(数据导入时使用)
     *
     * @param schemaCode
     * @param formCode
     * @param bizObjectId
     * @return
     */
    @Override
    public String genSequenceNoFromQueue(String schemaCode, String formCode, String bizObjectId) {
//        return sequenceSettingService.getSequenceNoFromQueue(schemaCode, formCode, bizObjectId);
        return null;
    }

}
