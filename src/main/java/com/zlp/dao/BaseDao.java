package com.zlp.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.zlp.dao.annotation.Table;
import com.zlp.dao.enums.OperateEnum;
import com.zlp.dao.enums.OrderEnum;

/**
 * 描述：封装的通用方法和调用数据库driver方法
 * @author zhoulongpeng
 * @date   2016-02-02
 */
public abstract class BaseDao {
	
//	protected final int ADD = 1; 
//	protected final int UPDATE = 0; 
	
	/** 
	 * 描述：获取表明
	 * 注：如果有自定义注解，则从注解获取表名；如果没有使用自定义注解，则获取类名（小写）的作为表名
	 * @param clazz
	 * @return
	 */
	protected String getTableName(Class<?> clazz) {
		Table tab = clazz.getAnnotation(Table.class);
		if(tab != null) return tab.value();
		return clazz.getSimpleName().toLowerCase();
	}
	
	/**
	 * 描述：把普通javabean转换成mongodb的保存对象Document
	 * @param clazz  
	 * @param operate    新增操作/修改操作
	 * @return
	 */
	protected Document converToDocument(Class<?> clazz, Object instance, OperateEnum operate) {
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		Document entity = new Document();
		for(Method m : methods) {
			if(m.getName().startsWith("set")) continue;
			final String lowerMethodName = m.getName().toLowerCase();
			for(Field field : fields) {
				final String fieldName = field.getName();
				if(lowerMethodName.replace("get", "").equals(fieldName.toLowerCase()) || 
				   lowerMethodName.replace("is", "").equals(fieldName.toLowerCase()) ) {
					Object val = null;
					try {
						val = m.invoke(instance, new Object[]{});
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					} 
					//修改动作不需要append id进去
					if(operate == OperateEnum.UPDATE && ("_id".equalsIgnoreCase(fieldName) || "id".equalsIgnoreCase(fieldName))) continue;
					entity.append(fieldName, val);
					break;
				}
			}
		}
		return entity;
	}
	
	/**
	 * 描述：map转换成Document
	 * @param filter {key : value}
	 * @return
	 */
	protected Document mapToDocument(Map<String, ?> filter) {
		if(filter == null) return null;
		Document entity = new Document();
		for(Map.Entry<String, ?> entry : filter.entrySet()) {
			entity.append(entry.getKey(), entry.getValue());
		}
		return entity;
	}
	
	/**
	 * 描述：map转换成Document
	 * @param filter {field : key, operator : $eq, value : value}
	 * @return
	 */
	protected Document map2Document(Map<String, Object> filter) {
		if(filter == null) return null;
		if(filter.size() < 1) return null;
		String field = filter.get("field").toString(); 
		String operator = filter.get("operator").toString(); 
		Document query = new Document(field, new Document(operator, filter.get("val")));
		return query;
	}
	
	/**
	 * 描述：map转换成Document
	 * @param filter {key : value}
	 * @return
	 */
	protected Bson mapToBson(Map<String, Object> filter) {
		if(filter == null || filter.size() < 1) return null;
		int i = 0;
		Bson[] bson = new Bson[filter.size()];
		for(Map.Entry<String, Object> entry : filter.entrySet()) {
			bson[i++] =  Filters.eq( entry.getKey(),  entry.getValue());
		}
		if(bson.length < 2) return bson[0];
		else return Filters.and(bson);
	}
	
	/**
	 * 描述：map转换成Document
	 * @param query {field : key, operator : $eq, value : value}
	 * @return
	 */
	protected Bson map2Bson(Map<String, Object> filter) {
		if(filter == null || filter.size() < 1) return null;
		if(!filter.containsKey("operator")) return this.mapToBson(filter);
		
		String operator = filter.get("operator").toString(); 
		String field = filter.get("field").toString();
		switch(operator) {
			case "lt": return Filters.lt(field, filter.get("val"));
			case "lte": return Filters.lte(field, filter.get("val"));
			case "gt": return Filters.gt(field, filter.get("val"));
			case "gte": return Filters.gte(field, filter.get("val"));
			case "in": return Filters.in(field, (Object[]) filter.get("val"));
			case "all": return Filters.all(field, filter.get("val"));
			case "regex": return Filters.regex(field, filter.get("val").toString());
			default : return Filters.eq(field, filter.get("val"));
		}
	}
	/**
	 * 描述：list转换成Document
	 * @param filter {field : key, operator : $eq, value : value}
	 * @return
	 */
	protected Document list2Document(List<Map<String, Object>> filter) {
		if(filter == null) return null;
		if(filter.size() < 1) return null;
		Document query = new Document();
		for(Map<String, Object> map : filter) {
			String field = map.get("field").toString();
			String operator = map.get("operator").toString();
			Object val = map.get("val");
			query.append(field, new Document(operator, val));
		}
		return query;
	}
	
	/**
	 * 描述：
	 * @param value id值
	 * @param id    id字段名
	 * @return
	 */
	protected Map<String, Object> filterById(Object value, String... id) {
		String _id = "_id";
		if(id != null && id.length > 0) _id = id[0];
		Map<String, Object> filter = new HashMap<>();
		filter.put(_id, value);
		return filter;
	}
	
	/**
	 * 描述：
	 * @param sort {key, value}; key：排序字段，value：排序方式
	 * @return
	 */
	protected Bson mapToSortBson(Map<String, OrderEnum> sort) {
		if(sort == null) return null;
		Bson bson = null;
		for(Map.Entry<String, OrderEnum> entry : sort.entrySet()) {
			if(entry.getValue() == OrderEnum.ASC) bson = Sorts.ascending(entry.getKey());
			else  bson = Sorts.descending(entry.getKey());
		}
		return bson;
	}
	
}
