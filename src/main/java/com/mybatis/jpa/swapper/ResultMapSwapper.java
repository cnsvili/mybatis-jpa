package com.mybatis.jpa.swapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mybatis.jpa.meta.ColumnMetaResolver;
import com.mybatis.jpa.util.AssociationUtil;
import com.mybatis.jpa.util.PersistentUtil;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import javax.persistence.Id;

/**
 * @author svili
 */
public class ResultMapSwapper {

    private Configuration configuration;

    /**
     * Result Maps collection,key : id
     */
    private ConcurrentHashMap<String, ResultMap> resultMaps = new ConcurrentHashMap<>();

    public ResultMapSwapper(Configuration configuration){
        this.configuration = configuration;
    }

    public ResultMap reloadResultMap(String resource, String id, Class<?> type) {
        if (!resultMaps.containsKey(id)) {
            resultMaps.put(id, resolveResultMap(resource, id, type));
        }

        return resultMaps.get(id);
    }

    public void registerResultMap(ResultMap resultMap) {
        configuration.addResultMap(resultMap);
    }

    public ResultMap resolveResultMap(String resource, String id, Class<?> type) {
        List<ResultMapping> resultMappings = resolveResultMappings(resource, id, type);
        return new ResultMap.Builder(configuration, id, type, resultMappings).build();
    }

    public List<ResultMapping> resolveResultMappings(String resource, String id, Class<?> type) {
        List<ResultMapping> resultMappings = new ArrayList<>();

        MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, resource);

        List<Field> fields = PersistentUtil.getPersistentFields(type);

        for (Field field : fields) {
            // java field name
            String property = field.getName();
            // sql column name
            String column = PersistentUtil.getColumnName(field);
            Class<?> javaType = field.getType();

            //resultMap is not need jdbcType
            JdbcType jdbcType = null;

            String nestedSelect = null;
            String nestedResultMap = null;
            if (AssociationUtil.isAssociationField(field)) {
                // OneToOne
                nestedResultMap = id + "_association[" + javaType.getSimpleName() + "]";
                registerResultMap(resolveResultMap(resource, nestedResultMap, javaType));
            }

            String notNullColumn = null;
            String columnPrefix = null;
            String resultSet = null;
            String foreignColumn = null;
            // if primaryKey,then flags.add(ResultFlag.ID);
            List<ResultFlag> flags = new ArrayList<>();
            if (field.isAnnotationPresent(Id.class)) {
                flags.add(ResultFlag.ID);
            }
            // lazy or eager
            boolean lazy = false;
            // enum
            Class<? extends TypeHandler<?>> typeHandlerClass = ColumnMetaResolver.resolveTypeHandler(field);

            ResultMapping resultMapping = assistant.buildResultMapping(type, property, column,
                    javaType, jdbcType, nestedSelect, nestedResultMap, notNullColumn, columnPrefix,
                    typeHandlerClass, flags, resultSet, foreignColumn, lazy);
            resultMappings.add(resultMapping);
        }
        return resultMappings;

    }


}
