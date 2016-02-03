package com.zlp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.DistinctIterable;
import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zlp.dao.BaseDao;
import com.zlp.dao.JdbcUtil;
import com.zlp.dao.RepositoryIfc;
import com.zlp.dao.enums.OperateEnum;
import com.zlp.dao.enums.OrderEnum;
import com.zlp.exception.AddErrorException;
import com.zlp.exception.DeleteErrorException;
import com.zlp.exception.UpdateErrorException;
import com.zlp.log.LoggerFactory;


public abstract class AbstractMongoAsyncRepo extends BaseDao implements RepositoryIfc {
	
//	private final String UPDATE_ONE = "$set";
//	private final String UPDATE_MANY = "$inc";//operations
	private final String OPERATOR_SET = "$set";
	
	private static Logger log = LoggerFactory.getLogger(AbstractMongoAsyncRepo.class);

	/*****************************************insertOne*********************************************/
	
	@Override
	public boolean insertOne(Class<?> clazz, Object entity) throws AddErrorException {
		String table = super.getTableName(clazz);
		Document document = super.converToDocument(clazz, entity, OperateEnum.ADD);
		return this.insertOne(JdbcUtil.getCollection(table), document);
	}
	
	@Override
	public boolean insertOne(String table, Map<String, Object> vals) throws AddErrorException {
		Document document = super.mapToDocument(vals);
		return this.insertOne(JdbcUtil.getCollection(table), document);
	}
	
	private boolean insertOne(MongoCollection<Document> collection, Document document) throws AddErrorException {
		final CountDownLatch downLatch = new CountDownLatch(1);
		StringBuffer builder = new StringBuffer();
		collection.insertOne(document, new SingleResultCallback<Void>() {
			@Override
			public void onResult(final Void result, final Throwable t) {
				if(t == null) builder.append(true);
				else {
					t.printStackTrace();
					builder.append(false);
				}
				downLatch.countDown();
			}
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		if(builder.length() < 1) return false;
		return Boolean.parseBoolean(builder.toString());
	}

	/*****************************************insertMany*********************************************/
	
	@Override
	public boolean insertMany(Class<?> clazz, List<Object> entitys) throws AddErrorException {
		if(entitys == null || entitys.size() < 1) return false;
		String table = super.getTableName(clazz);
		List<Document> documents = new ArrayList<>();
		for(Object o : entitys) {
			documents.add(super.converToDocument(clazz, o, OperateEnum.ADD));
		}
		return this.insertMany(JdbcUtil.getCollection(table), documents);
	}

	@Override
	public boolean insertMany(String table, List<Map<String, Object>> entitys) throws AddErrorException {
		if(entitys == null || entitys.size() < 1) return false;
		List<Document> documents = new ArrayList<>();
		for(Map<String, Object> o : entitys) {
			documents.add(super.mapToDocument(o));
		}
		return this.insertMany(JdbcUtil.getCollection(table), documents);
	}
	
	private boolean insertMany(MongoCollection<Document> collection, List<Document> documents) throws AddErrorException {
		final CountDownLatch downLatch = new CountDownLatch(1);
		StringBuffer builder = new StringBuffer();
		collection.insertMany(documents, new SingleResultCallback<Void>() {
			@Override
			public void onResult(final Void result, final Throwable t) {
				if(t == null) builder.append(true);
				else builder.append(false);
				downLatch.countDown();
			}
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		if(builder.length() < 1) return false;
		return Boolean.parseBoolean(builder.toString());
	}
	
	
	
	/*****************************************updateOne*********************************************/

	@Override
	public boolean updateOne(Class<?> clazz, Map<String, Object> filter, Object entity) throws UpdateErrorException {
		String table = super.getTableName(clazz);
		Document document = super.converToDocument(clazz, entity, OperateEnum.UPDATE);
		return this.updateOne(JdbcUtil.getCollection(table), super.mapToBson(filter), document);
	}

	@Override
	public boolean updateOne(Class<?> clazz, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException {
		String table = super.getTableName(clazz);
		return this.updateOne(table, filter, vals);
	}
	
	@Override
	public boolean updateOne(String table, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException {
		Document document = super.mapToDocument(vals);
		return this.updateOne(JdbcUtil.getCollection(table), super.mapToBson(filter), document);
	}
	
	private boolean updateOne(MongoCollection<Document> collection, Bson filter, Document document) throws UpdateErrorException {
		if(filter == null || document == null) return false;
		final CountDownLatch downLatch = new CountDownLatch(1);
		StringBuffer builder = new StringBuffer();
		collection.updateOne(filter, new Document(this.OPERATOR_SET, document), new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(final UpdateResult result, final Throwable t) {
				if(result == null) builder.append(false);
				else builder.append(result.getModifiedCount() > 0);
				downLatch.countDown();
			}
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		if(builder.length() < 1) return false;
		return Boolean.parseBoolean(builder.toString());
	}
	
	/*****************************************updateMany*********************************************/

	@Override
	public boolean updateMany(Class<?> clazz, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException {
		String table = super.getTableName(clazz);
		return this.updateMany(table, filter, vals);
	}
	
	@Override
	public boolean updateMany(String table, Map<String, Object> filter, Map<String, Object> vals) throws UpdateErrorException {
		Document document = super.mapToDocument(vals);
		return this.updateMany(JdbcUtil.getCollection(table), super.map2Bson(filter), document);
	}
	
	private boolean updateMany(MongoCollection<Document> collection, Bson filter, Document document) throws UpdateErrorException {
		if(filter == null || document == null) return false;
		final CountDownLatch downLatch = new CountDownLatch(1);
		StringBuffer builder = new StringBuffer();
		collection.updateMany(filter, new Document(this.OPERATOR_SET, document), new SingleResultCallback<UpdateResult>() {
			@Override
			public void onResult(final UpdateResult result, final Throwable t) {
				if(result == null) builder.append(false);
				else builder.append(result.getModifiedCount() >= 0);
				downLatch.countDown();
			}
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		if(builder.length() < 1) return false;
		return Boolean.parseBoolean(builder.toString());
	}
	
	/*****************************************updateById*********************************************/

	@Override
	public boolean updateById(Class<?> clazz, Object id, Object entity, String... idfield) throws UpdateErrorException {
		if(id == null) return false;
		return this.updateOne(clazz, super.filterById(id, idfield), entity);
	}
	
	@Override
	public boolean updateById(Class<?> clazz, Object id, Map<String, Object> vals, String... idfield) throws UpdateErrorException {
		String table = super.getTableName(clazz);
		return this.updateById(table, id, vals, idfield);
	}
	
	@Override
	public boolean updateById(String table, Object id, Map<String, Object> vals, String... idfield) throws UpdateErrorException {
		if(id == null) return false;
		return this.updateOne(table, super.filterById(id, idfield), vals);
	}
	
	/*****************************************deleteOne*********************************************/

	@Override
	public boolean deleteOne(Class<?> clazz, Map<String, Object> filter) throws DeleteErrorException {
		String table = super.getTableName(clazz);
		return this.deleteOne(table, filter);
	}

	@Override
	public boolean deleteOne(String table, Map<String, Object> filter) throws DeleteErrorException {
		return this.deleteOne(JdbcUtil.getCollection(table), super.mapToBson(filter));
	}
	
	private boolean deleteOne(MongoCollection<Document> collection, Bson filter) {
		if(filter == null) return false;
		final CountDownLatch downLatch = new CountDownLatch(1);
		StringBuffer builder = new StringBuffer();
		collection.deleteOne(filter, new SingleResultCallback<DeleteResult>() {
			@Override
			public void onResult(final DeleteResult result, final Throwable t) {
				if(result == null) builder.append(false);
				else builder.append(result.getDeletedCount() >= 0);
				downLatch.countDown();
			}
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		if(builder.length() < 1) return false;
		return Boolean.parseBoolean(builder.toString());
	}
	
	private boolean deleteMany(MongoCollection<Document> collection, Bson filter) {
		if(filter == null) return false;
		final CountDownLatch downLatch = new CountDownLatch(1);
		StringBuffer builder = new StringBuffer();
		collection.deleteMany(filter, new SingleResultCallback<DeleteResult>() {
			@Override
			public void onResult(final DeleteResult result, final Throwable t) {
				if(result == null) builder.append(false);
				else builder.append(result.getDeletedCount() >= 0);
				downLatch.countDown();
			}
		});
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		if(builder.length() < 1) return false;
		return Boolean.parseBoolean(builder.toString());
	}
	
	/*****************************************deleteMany*********************************************/
	
	@Override
	public boolean deleteMany(Class<?> clazz, Map<String, Object> filter) throws DeleteErrorException {
		String table = super.getTableName(clazz);
		return this.deleteMany(table, filter);
	}

	@Override
	public boolean deleteMany(String table, Map<String, Object> filter) throws DeleteErrorException {
		return this.deleteMany(JdbcUtil.getCollection(table), super.map2Bson(filter));
	}
	
	/*****************************************deleteById*********************************************/

	@Override
	public boolean deleteById(Class<?> clazz, Object id, String... idfield) throws DeleteErrorException {
		String table = super.getTableName(clazz);
		return this.deleteById(table, id, idfield);
	}

	@Override
	public boolean deleteById(String table, Object id, String... idfield) throws DeleteErrorException {
		if(id == null) return false;
		return this.deleteOne(table, super.filterById(id, idfield));
	}

	/*****************************************findOne*********************************************/
	
	@Override
	public Object findOne(Class<?> clazz, Map<String, Object> filter) {
		String table = super.getTableName(clazz);
		return this.findOne(table, filter);
	}
	
	@Override
	public Object findOne(String table, Map<String, Object> filter) {
		return this.findOne(JdbcUtil.getCollection(table), super.mapToBson(filter), null);
	}

	@Override
	public Object findOne(Class<?> clazz, Map<String, Object> filter, Map<String, OrderEnum> sort) {
		String table = super.getTableName(clazz);
		return this.findOne(table, filter, sort);
	}
	
	@Override
	public Object findOne(String table, Map<String, Object> filter, Map<String, OrderEnum> sort) {
		return this.findOne(JdbcUtil.getCollection(table), super.mapToBson(filter), super.mapToSortBson(sort));
	}
	
	/**描述：最终到数据库查找一条纪录的方法*/
	private Object findOne(MongoCollection<Document> collection, Bson filter, Bson sort) {
		final CountDownLatch downLatch = new CountDownLatch(1);
		Set<String> set = new HashSet<>();
        SingleResultCallback<Document> callback = new SingleResultCallback<Document>() {
            @Override
            public void onResult(final Document document, final Throwable t) {
            	if(document != null) set.add(document.toJson());
                downLatch.countDown();
            }
        };
        
        FindIterable<Document> iter = null;
        if(filter == null) iter = collection.find();
        else iter = collection.find(filter);
        if(sort == null) iter.first(callback);
        else iter.sort(sort).first(callback);
        
        try {
			downLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        if(set.isEmpty()) return null;
        return set.iterator().next();
	}
	
	/*****************************************findById*********************************************/

	@Override
	public Object findById(Class<?> clazz, Object id, String... idfield) {
		String table = super.getTableName(clazz);
		return this.findById(table, id, idfield);
	}

	@Override
	public Object findById(String table, Object id, String... idfield) {
		return this.findOne(table, super.filterById(id, idfield));
	}

	/*****************************************query*********************************************/
	
	@Override
	public Map<String, Object> query(Class<?> clazz, Map<String, Object> filter, 
									 int page, int pageSize, Map<String, OrderEnum> sort) {
		String table = super.getTableName(clazz);
		return this.query(table, filter, page, pageSize, sort);
	}

	@Override
	public Map<String, Object> query(String table, Map<String, Object> filter,
									 int page, int pageSize, Map<String, OrderEnum> sort) {
		return this.query(JdbcUtil.getCollection(table), super.map2Bson(filter), page, pageSize, super.mapToSortBson(sort));
	}
	
	/** 描述：最终分页查询数据库列表方法 */
	private Map<String, Object> query(MongoCollection<Document> collection, Bson filter, int page, int pageSize, Bson sort) {
		final CountDownLatch downLatch = new CountDownLatch(2);
		
		//总条数 
		StringBuffer  builder = new StringBuffer();
		collection.count(filter, new SingleResultCallback<Long>() {
			@Override
			public void onResult(final Long result, final Throwable t) {
				builder.append(result);
				downLatch.countDown();
			}
		});
		
		FindIterable<Document> list = null;
		if(filter == null) list = collection.find();
		else list = collection.find(filter);
		if(sort != null) list.sort(sort);
		list.skip((page-1)*pageSize).limit(pageSize);
		
		List<String> array = new ArrayList<>();
		Block<Document> printDocumentBlock = new Block<Document>() {
			@Override
			public void apply(Document document) {
				array.add(document.toJson());
			}
		};
		SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
			@Override
			public void onResult(final Void result, final Throwable t) {
				log.info("Operation Finished!");
				downLatch.countDown();
			}
		};
		list.forEach(printDocumentBlock, callbackWhenFinished);
		
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		
		Integer total = Integer.parseInt(builder.toString());
		Map<String, Object> map = new HashMap<>();
		map.put("total", total);
		map.put("items", array);
		return map;
	}

	@Override
	public List<?> query(Class<?> clazz, Map<String, Object> filter, Map<String, OrderEnum> sort) {
		String table = super.getTableName(clazz);
		return this.query(table, filter, sort);
	}

	@Override
	public List<?> query(String table, Map<String, Object> filter, Map<String, OrderEnum> sort) {
		return this.query(JdbcUtil.getCollection(table), super.map2Bson(filter), super.mapToSortBson(sort));
	}
	
	/*****************************************queryAll*********************************************/

	@Override
	public List<?> queryAll(Class<?> clazz, Map<String, OrderEnum> sort) {
		String table = super.getTableName(clazz);
		return this.queryAll(table, sort);
	}

	@Override
	public List<?> queryAll(String table, Map<String, OrderEnum> sort) {
		return this.query(JdbcUtil.getCollection(table), null, super.mapToSortBson(sort));
	}
	
	/** 描述：最终分页查询数据库列表方法 */
	private List<?> query(MongoCollection<Document> collection, Bson filter, Bson sort) {
		final CountDownLatch downLatch = new CountDownLatch(1);
		
		FindIterable<Document> list = null;
		if(filter == null) list = collection.find();
		else list = collection.find(filter);
		if(sort != null) list = list.sort(sort);
		
		if(list == null) return null;
		
		List<String> array = new ArrayList<>();
		list.into(new ArrayList<Document>(),  new SingleResultCallback<List<Document>>() {
			@Override
	        public void onResult(final List<Document> result, final Throwable t) {
				if(result != null) {
					for(Document doc : result) {
						array.add(doc.toJson());
					}
				}
				downLatch.countDown();
	        }
		});
		
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("queue take excaption!!!!!!");
		}
		return array;
	}

	/*****************************************distinct*********************************************/
	
	@Override
	public List<?> distinct(Class<?> clazz, String field, Map<String, Object> filter) {
		String table = super.getTableName(clazz);
		return this.distinct(table, field, filter);
	}
	
	@Override
	public List<?> distinct(String table, String field, Map<String, Object> filter) {
		return this.distinct(JdbcUtil.getCollection(table), field, super.map2Bson(filter));
	}
	
	private List<?> distinct(MongoCollection<Document> collection, String fieldName, Bson filter) {
		final CountDownLatch downLatch = new CountDownLatch(1);
		
		Class<Document> resultClass = null;
		DistinctIterable<Document> iterable = collection.distinct(fieldName, resultClass);
		if(filter != null ) iterable.filter(filter);
		
		if(iterable == null) return null;
		
		List<String> array = new ArrayList<>();
		iterable.into(new ArrayList<Document>(),  new SingleResultCallback<List<Document>>() {
			@Override
	        public void onResult(final List<Document> result, final Throwable t) {
				if(result != null) {
					for(Document doc : result) {
						array.add(doc.toJson());
					}
				}
				downLatch.countDown();
	        }
		});
		
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.info("InterruptedException e");
		}
		
		return array;
	}
	
	
}
