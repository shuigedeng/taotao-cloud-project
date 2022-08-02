package com.taotao.cloud.sys.biz.modules.database.service.meta.processor;

import com.sanri.tools.modules.database.service.meta.dtos.ClientInfo;
import com.sanri.tools.modules.database.service.meta.dtos.Function;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientInfoPropertiesProcessor implements ResultSetHandler<List<ClientInfo.Property>> {
    @Override
    public List<ClientInfo.Property> handle(ResultSet rs) throws SQLException {
        List<ClientInfo.Property> properties = new ArrayList<>();
        while (rs.next()){
            final String name = rs.getString(1);
            final int maxLen = rs.getInt(2);
            final String defaultValue = rs.getString(3);
            final String description = rs.getString(4);

            final ClientInfo.Property property = new ClientInfo.Property(name, maxLen, defaultValue, description);
            properties.add(property);
        }
        return properties;
    }
}
