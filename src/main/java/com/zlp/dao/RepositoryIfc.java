package com.zlp.dao;

import java.util.List;
import java.util.Map;

import com.zlp.dao.enums.OrderEnum;
import com.zlp.exception.AddErrorException;
import com.zlp.exception.DeleteErrorException;
import com.zlp.exception.UpdateErrorException;

/**
 * 描述：由于升级，改动太大
 * @author zhoulongpeng
 *
 */
public interface RepositoryIfc {
	
	
	
	/**
	 * 描述：根据对象保存一个对象条数据到数据库 
	 * @param clazz 保存到数据库的对象的类型
	 * @param entity 保存到数据库的对象
	 * @return boolean
	 * @throws AddErrorException 
	 */
	boolean insertOne(Class<?> clazz, Object entity) throws AddErrorException;
	/**
	 * 描述：根据对象保存同一对象多条数据到数据库 
	 * @param clazz 保存到数据库的对象的类型
	 * @param entity 保存到数据库的对象
	 * @return boolean
	 * @throws AddErrorException 
	 */
	boolean insertMany(Class<?> clazz, List<Object> entitys) throws AddErrorException;
	/**
	 * 描述：根据表明保存一个对象条数据到数据库
	 * @param table
	 * @param vals {key:value}
	 * @return boolean
	 * @throws AddErrorException
	 */
	boolean insertOne(String table, Map<String, Object> vals) throws AddErrorException;
	/**
	 * 描述：根据对象保存不同对象多条数据到数据库 
	 * @param clazz 保存到数据库的对象的类型
	 * @param entity 保存到数据库的对象
	 * @return boolean
	 * @throws AddErrorException 
	 */
	boolean insertMany(String table, List<Map<String, Object>> entitys) throws AddErrorException;
	
	
	
	
	
	
	
	
	
	/**
	 * 描述：根据条件更新整个对象数据
	 * @param clazz 更新的对象对应的类
	 * @param filter 更新的条件 {key:value}
	 * @param entity 更新的对象
	 * @return Object
	 */
	boolean updateOne(Class<?> clazz, Map<String, Object> filter, Object entity) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新指定的字段
	 * @param clazz 更新的对象对应的类
	 * @param filter 更新的条件 {key:value}
	 * @param vals 更新的值 {key:value}
	 * @return Object
	 */
	boolean updateOne(Class<?> clazz, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新指定的字段
	 * @param clazz 更新的对象对应的类
	 * @param filter 更新的条件 {key:value}
	 * @param vals 更新的值 {key:value}
	 * @return Object
	 */
	boolean updateOne(String table, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新指定的字段
	 * @param clazz 更新的对象对应的类
	 * @param filter 更新的条件 {operate:"gt", flied:"age", value:90}
	 * @param vals 更新的值 {key:value}
	 * @return Object
	 */
	boolean updateMany(Class<?> clazz, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新指定的字段
	 * @param clazz 更新的对象对应的类
	 * @param filter 更新的条件 {operate:"gt", flied:"age", value:90}
	 * @param vals 更新的值 {key:value}
	 * @return Object
	 */
	boolean updateMany(String table, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新整个对象数据
	 * @param clazz 更新的对象对应的类
	 * @param id id值
	 * @param entity 更新的对象
	 * @return Object
	 */
	boolean updateById(Class<?> clazz, Object id, Object entity, String... idfield) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新整个对象数据
	 * @param clazz 更新的对象对应的类
	 * @param id id值
	 * @param vals 更新的值 {key:value}
	 * @return Object
	 */
	boolean updateById(Class<?> clazz, Object id, Map<String, Object> vals, String... idfield) throws UpdateErrorException;
	/**
	 * 描述：根据条件更新整个对象数据
	 * @param clazz 更新的对象对应的类
	 * @param id id值
	 * @param vals 更新的值 {key:value}
	 * @return Object
	 */
	boolean updateById(String table, Object id, Map<String, Object> vals, String... idfield) throws UpdateErrorException;
	
	
	
	
	
	/**
	 * 描述：根据条件删除数据
	 * @param clazz 
	 * @param filter  {key:value}
	 * @return boolean
	 */
	boolean deleteOne(Class<?> clazz, Map<String, Object> filter) throws DeleteErrorException;
	/**
	 * 描述：根据条件删除数据
	 * @param clazz 
	 * @param filter  {operate:"gt", flied:"age", value:90}
	 * @return boolean
	 */
	boolean deleteMany(Class<?> clazz, Map<String, Object> filter) throws DeleteErrorException;
	/**
	 * 描述：根据条件删除数据
	 * @param table 
	 * @param filter 
	 * @return boolean
	 */
	boolean deleteOne(String table, Map<String, Object> filter) throws DeleteErrorException;
	/**
	 * 描述：根据条件删除数据
	 * @param table 
	 * @param filter  {operate:"gt", flied:"age", value:90}
	 * @return boolean
	 */
	boolean deleteMany(String table, Map<String, Object> filter) throws DeleteErrorException;
	/**
	 * 描述：根据id删除纪录
	 * @param clazz
	 * @param id
	 * @return boolean
	 */
	boolean deleteById(Class<?> clazz, Object id,  String... idfield) throws DeleteErrorException;
	/**
	 * 描述：根据id删除纪录
	 * @param table
	 * @param id
	 * @return boolean
	 */
	boolean deleteById(String table, Object id,  String... idfield) throws DeleteErrorException;
	
	
	
	
	
	/**
	 * 描述：根据id查询一条数据
	 * @param clazz
	 * @param id
	 * @param idfield
	 * @return Object
	 */
	Object findById(Class<?> clazz, Object id, String... idfield);
	/**
	 * 描述：根据id查询一条数据
	 * @param clazz
	 * @param id
	 * @param idfield
	 * @return Object
	 */
	Object findById(String table, Object id, String... idfield);
	/**
	 * 描述：根据条件查询一条，简单的and关系
	 * @param clazz 表实体
	 * @param query 条件，map类型 {"field":""}
	 * @return 只返回一条数据 Object
	 */
	Object findOne(Class<?> clazz, Map<String, Object> filter);
	/**
	 * 描述：根据条件查询一条，简单的and关系
	 * @param clazz 表实体
	 * @param query 条件，map类型 {"field":""}
	 * @return 只返回一条数据 Object
	 */
	Object findOne(String table, Map<String, Object> filter);
	/**
	 * 描述：根据条件查询排序到第一条数据
	 * @param clazz 表实体
	 * @param filter 条件，map类型 {"field":"", "operator":"", "val":""}
	 * @param sort 排序，map类型
	 * @return 只返回一条数据 JSONObject
	 */
	Object findOne(Class<?> clazz, Map<String, Object> filter, Map<String, OrderEnum> sort);
	/**
	 * 描述：根据条件查询排序到第一条数据
	 * @param clazz 表实体
	 * @param filter 条件，map类型 {"field":"", "operator":"", "val":""}
	 * @param sort 排序，map类型
	 * @return 只返回一条数据 JSONObject
	 */
	Object findOne(String table, Map<String, Object> filter, Map<String, OrderEnum> sort);

	
	
	
	
	
	/**
	 * 描述：一个条件分页查询数据列表接口
	 * @param clazz
	 * @param entity
	 * @param page
	 * @param pageSize
	 * @param sort 排序，null表示没有排序
	 * @return 总条数跟当前页的数据 JSONObject
	 */
	Map<String, Object> query(Class<?> clazz, Map<String, Object> filter, int page, int pageSize, Map<String, OrderEnum> sort);
	/**
	 * 描述：一个条件分页查询数据列表接口
	 * @param table
	 * @param entity
	 * @param page
	 * @param pageSize
	 * @param order 排序，null表示没有排序
	 * @return 总条数跟当前页的数据 JSONObject
	 */
	Map<String, Object> query(String table, Map<String, Object> filter, int page, int pageSize, Map<String, OrderEnum> sort);
	/**
	 * 描述：根据条件查询符合条件的全部数据
	 * @param clazz
	 * @param filter
	 * @param sort
	 * @return 返回符合条件的全部数据/当查询条件filter为null的时候，返回null
	 */
	List<?> query(Class<?> clazz, Map<String, Object> filter, Map<String, OrderEnum> sort);
	/**
	 * 描述：根据条件查询一条或者多条数据
	 * @param table
	 * @param filter
	 * @param sort
	 * @return 返回一条或者多条数据/当查询条件filter为null的时候，返回null
	 */
	List<?> query(String table, Map<String, Object> filter, Map<String, OrderEnum> sort);
	/**
	 * 描述：查询所有数据
	 * @param clazz
	 * @param sort
	 * @return 所有数据
	 */
	List<?> queryAll(Class<?> clazz, Map<String, OrderEnum> sort);
	/**
	 * 描述：查询所有数据
	 * @param table
	 * @param sort
	 * @return 所有数据
	 */
	List<?> queryAll(String table, Map<String, OrderEnum> sort);
	
	
	
	/**
	 * 描述：查找不重复的纪录
	 * @param clazz
	 * @param field 指定不重复的字段
	 * @param filter
	 * @return List<?>
	 */
	List<?> distinct(Class<?> clazz, String field, Map<String, Object> filter);
	/**
	 * 描述：查找不重复的纪录
	 * @param clazz
	 * @param field 指定不重复的字段
	 * @param filter
	 * @return List<?>
	 */
	List<?> distinct(String table, String field, Map<String, Object> filter);
	
	
	
	/**
	 * 描述：查找不重复的纪录
	 * @param clazz
	 * @param field 指定不重复的字段
	 * @param query
	 * @return List<?>
	List<?> distinct(Class<?> clazz, String field, Map<String, Object> query);*/
	
	/**
	 * 描述：保存数据到mongodb GridFS
	 * @param in
	 * @param filename
	 * @param mimeType
	 * @param alias
	 * @param metadata
	 * @return 成功：返回id  失败：返回null
	String createFile(InputStream in, String filename, String mimeType, String alias, Map<String, Object> metadata) throws IOException;
	*/
	/**
	 * 描述：保存数据到mongodb GridFS
	 * @param bytes
	 * @param filename
	 * @param mimeType
	 * @param alias
	 * @param metadata
	 * @return 成功：返回id  失败：返回null
	String createFile(byte[] bytes, String filename, String mimeType, String alias, Map<String, Object> metadata) throws IOException;
	 */
	/**
	 * 描述：多个条件并行分页查询文件列表接口
	 * @param entity
	 * @param page
	 * @param pageSize
	 * @param order 排序，null表示没有排序
	 * @return 总条数跟当前页的数据 JSONObject
	JSONObject fileList(List<Map<String, Object>> query, int page, int pageSize, Map<String, Integer> order) throws IOException;
	 */
	/**
	 * 描述：从mongodb读取文件基本信息
	 * @param id  文件id
	 * @return Map<String, Object>
	 * @throws IOException
	Map<String, Object> readFile(Object id) throws IOException;
	 */
	/**
	 * 描述：从mongodb读取文件到输出流
	 * @param id  文件id
	 * @param out 接收文件的输出流
	 * @return Map<String, Object>
	 * @throws IOException
	
	void readFile(Object id, OutputStream out) throws IOException;
	 */
	/**
	 * 描述：从mongodb的offset开始读取length长度文件到输出流
	 * @param id  文件id
	 * @param out 接收文件的输出流
	 * @param offset 开始读取文件的位置
	 * @param length 读取文件的长度
	 * @return Map<String, Object>
	 * @throws IOException
	 
	void readFile(Object id, OutputStream out, int offset, int length);
	*/
	/**
	 * 描述：根据ID删除文件
	 * @param id
	 * @param out
	 * @return
	 * @throws IOException
	 
	void removeFile(Object id);
	*/
	/**
	 * 描述：根据query条件删除符合条件的所有文件
	 * @param id
	 * @param out
	 * @return
	 * @throws IOException
	
	void removeFile(Map<String, Object> query);
	 */
}
