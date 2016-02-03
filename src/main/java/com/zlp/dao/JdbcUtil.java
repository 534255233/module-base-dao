package com.zlp.dao;

import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.zlp.log.LoggerFactory;
import com.zlp.util.CommonUtil;

/**
 * 
 * @author zhoulongpeng
 * @date   2016-02-03
 *
 */
public class JdbcUtil {
	
	private static Logger log = LoggerFactory.getLogger(JdbcUtil.class);
	
	private static MongoDatabase db = null;
	private static MongoDatabase fsdb = null;
	
	private JdbcUtil() {}
	
	private final static ResourceBundle rb;
	private static String host = null;
	private static Integer port = null;
	private static String username = null;
	private static String password = null;
	private static String dbname = null;
	private static String dburl = null;
	
	static {
		rb = ResourceBundle.getBundle(CommonUtil.configFileName());
		host = rb.getString("mongodb.host");
		port = Integer.parseInt(rb.getString("mongodb.port"));
		username = rb.getString("mongodb.username");
		password = rb.getString("mongodb.password");
		dbname = rb.getString("mongodb.dbnane");
		
		StringBuffer builder = new StringBuffer();
		builder.append("mongodb://");
		builder.append(username);
		builder.append(":");
		builder.append(password);
		builder.append("@");
		builder.append(host);
		builder.append(":");
		builder.append(port);
		builder.append("/?authSource=");
		builder.append(dbname);
		
		dburl = builder.toString();
	}
	
	public void init() {
		getMongoDb();
	}
	
	/**
	 * 连接数据库
	 * @return
	 */
	public static MongoDatabase getMongoDb() {
		if(db == null) {
			log.info(dburl);
			ConnectionString uri = new ConnectionString(dburl);
			MongoClient mongoClient = MongoClients.create(uri);
			db = mongoClient.getDatabase(dbname);
		}
		return db;
	}
	
	public static MongoDatabase getMongoFsDb() {
		if(fsdb == null) {
			ConnectionString uri = new ConnectionString(dburl);
			MongoClient mongoClient = MongoClients.create(uri);
			fsdb = mongoClient.getDatabase(dbname);
			
//			MongoCredential credential = MongoCredential.createCredential(username, dbname, password.toCharArray());
		}
		return fsdb;
	}
	
	
	/**
	 * 获取collection实例
	 * @param name
	 * @return
	 */
	public static MongoCollection<Document> getCollection(String name) {
		return getMongoDb().getCollection(name);
	}
	
	/**
	 * 初始化mongodb GridFS
	 * @return
	 * @throws UnknownHostException 
	 
	public static GridFS getMongoFs() {
		return new GridFS(getMongoFsDb());
	}*/
	
	public static void main(String[] args) throws Exception {
//		InputStream is = JdbcUtil.class.getResourceAsStream("../../../../"+file);
//		System.out.println(is);
		
//		MongoDatabaseCursor cursor = JdbcUtil.getCollection("user").find();
//		while(cursor.hasNext()) {
//			MongoDatabaseObject user = cursor.next();
//			System.out.println(user.get("nickname"));
//		}
		
		
	}
	
}
