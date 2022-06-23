package com.taotao.cloud.sys.biz.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.sys.api.web.dto.mongo.CollectionDto;
import com.taotao.cloud.sys.api.web.dto.mongo.MongoQueryParam;
import com.taotao.cloud.sys.biz.service.IMongoService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.IteratorUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MongoService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 16:45:23
 */
@Service
public class MongoServiceImpl implements IMongoService {

	@Autowired(required = false)
	private MongoClient mongoClient;

	@Override
	public List<String> databaseNames() {
		MongoIterable<String> mongoIterable = mongoClient.listDatabaseNames();
		MongoCursor<String> mongoCursor = mongoIterable.iterator();
		List<String> list = IteratorUtils.toList(mongoCursor);
		list.remove("admin");
		list.remove("local");
		return list;
	}

	@Override
	public List<CollectionDto> collectionNames(String databaseName) {
		MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
		MongoCursor<String> iterator = mongoDatabase.listCollectionNames().iterator();
		List<CollectionDto> collectionDtos = new ArrayList<>();
		while (iterator.hasNext()) {
			String collectionName = iterator.next();
			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

			// todo 查询集合中的数据
			CollectionDto collectionDto = new CollectionDto(collectionName);
			collectionDtos.add(collectionDto);
		}
		return collectionDtos;
	}

	@Override
	public PageModel<String> queryDataPage(MongoQueryParam mongoQueryParam, PageQuery pageQuery) {
		List<String> objects = new ArrayList<>();

		String filterJson = mongoQueryParam.getFilter();
		String sortJson = mongoQueryParam.getSort();

		BasicDBObject filterBson =
			filterJson == null ? new BasicDBObject() : BasicDBObject.parse(filterJson);
		BasicDBObject sortBson =
			sortJson == null ? new BasicDBObject() : BasicDBObject.parse(sortJson);
		sortBson.append("_id", -1);

		MongoDatabase database = mongoClient.getDatabase(mongoQueryParam.getDatabaseName());
		MongoCollection<Document> collection = database.getCollection(
			mongoQueryParam.getCollectionName());
		FindIterable<Document> limit = collection.find(filterBson).sort(sortBson)
			.skip(pageQuery.currentPage()).limit(pageQuery.pageSize());
		for (Document document : limit) {
			String json = document.toJson();
			objects.add(json);
		}

		// 查询数据总数
		long countDocuments = collection.countDocuments(filterBson);

		return PageModel.of(countDocuments, 20, 1, 20, objects);
	}

}
