package app.modules.platform;

import com.alibaba.fastjson.JSON;
import com.authine.cloudpivot.engine.api.exceptions.ServiceException;
import com.authine.cloudpivot.engine.enums.ErrCode;
import com.authine.cloudpivot.engine.spi.service.SystemService;
import com.authine.cloudpivot.foundation.orm.api.util.JdbcUrlParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luoee
 * @date 2022/12/5
 */
//@Service
@Slf4j
public class CustomSystemServiceImpl implements SystemService {

    private static final String ORACLE = "oracle";
    private static final String DM = "dm";

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${cloudpivot.bizobject.db.type}")
    private String dbType;

    /**
     * 获取数据源信息，url 端口 账号 密码
     * 主要用于推送给报表使用
     *
     * @return
     */
    @Override
    public String getDataSourceInfo() {
        Map<String, String> map = new HashMap<>(9);
        map.put("username", username);
        map.put("password", password);
        map.put("dbType", dbType.toUpperCase());
        map.put("server", JdbcUrlParseUtil.getHostName(url));
        map.put("port", String.valueOf(JdbcUrlParseUtil.getPort(url)));
        try {
            String database;
            String[] split;
            // 获取oracle和达梦的database需要特殊处理
            if (ORACLE.equalsIgnoreCase(dbType)) {
                // jdbc:oracle:thin:@172.18.15.113:1521:helowin
                split = url.split(":");
                String data = split[split.length - 1];
                database = data.substring(data.indexOf('/') + 1);
            } else if (DM.equalsIgnoreCase(dbType)) {
                database = username;
            } else {
                //mysql, SqlServer, PostgreSql
                database = JdbcUrlParseUtil.getDatabase(url);
            }
            map.put("database", database);
            return new String(Base64.getMimeEncoder().encode(JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8))
                    , Charset.defaultCharset());
        } catch (Exception e) {
            log.error("获取数据库信息出错：", e);
            ServiceException serviceException = new ServiceException(ErrCode.UNKNOW_ERROR);
            serviceException.initCause(e);
            throw serviceException;
        }
    }
}
