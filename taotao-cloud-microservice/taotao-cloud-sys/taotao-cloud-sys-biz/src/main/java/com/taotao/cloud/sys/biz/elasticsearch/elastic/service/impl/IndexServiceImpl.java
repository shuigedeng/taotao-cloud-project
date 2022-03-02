package com.taotao.cloud.sys.biz.elasticsearch.elastic.service.impl;

import com.taotao.cloud.sys.biz.elasticsearch.elastic.service.IndexService;
import com.taotao.cloud.sys.biz.elasticsearch.po.Sougoulog;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;



@Service
public class IndexServiceImpl implements IndexService {
	@Autowired
	RestHighLevelClient client;
	
	@Override
	public void indexDoc(String indexName, String id, Map<String, Object> doc) {
		IndexRequest indexRequest = new IndexRequest(indexName).id(id).source(doc); 
		try {
		    IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		    System.out.println("新增成功" + response.toString());
		} catch(ElasticsearchException e ) {
		    if (e.status() == RestStatus.CONFLICT) {
		    	System.out.println("写入索引产生冲突"+e.getDetailedMessage());
		    }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void indexDocs(String indexName, List<Map<String, Object>> docs) {
		 try {
	            if (null == docs || docs.size() <= 0) {
	                return;
	            }
	            BulkRequest request = new BulkRequest();
	            for (Map<String, Object> doc : docs) {
	                request.add(
	                		new IndexRequest(indexName).id((String)doc.get("key")).source(doc)
	                            );
	            }
	            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
	            if (bulkResponse != null) {
	                for (BulkItemResponse bulkItemResponse : bulkResponse) {
	                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();

	                    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
	                            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
	                        IndexResponse indexResponse = (IndexResponse) itemResponse;
	                        System.out.println("新增成功" + indexResponse.toString());
	                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
	                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
	                        System.out.println("修改成功" + updateResponse.toString());
	                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
	                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
	                        System.out.println("删除成功" + deleteResponse.toString());
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public int deleteDoc(String indexName, String id) {
		DeleteResponse deleteResponse = null;
		DeleteRequest request = new DeleteRequest(indexName, id);
		try {
			deleteResponse = client.delete(request, RequestOptions.DEFAULT);
			System.out.println("删除成功" + deleteResponse.toString());
			if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
				System.out.println("删除失败，文档不存在" + deleteResponse.toString());
				return -1;
			}
		} catch (ElasticsearchException e) {
			if (e.status() == RestStatus.CONFLICT) {
				System.out.println("删除失败，版本号冲突" + deleteResponse.toString());
				return -2;
		    }
		} catch (IOException e) {
			e.printStackTrace();
			return -3;
		}
		return 1;
	}

	@Override
	public void createMapping(String indexname, XContentBuilder mapping) {
		try {
			CreateIndexRequest index = new CreateIndexRequest(indexname);
			index.source(mapping);
			client.indices().create(index, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean existIndex(String indexname) {
		GetIndexRequest request = new GetIndexRequest(indexname);
		try {
			boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
			return exists;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void updateDoc(String indexName, String id, Map<String, Object> doc) {
		UpdateRequest request = new UpdateRequest(indexName, id).doc(doc);
		request.docAsUpsert(true);
		try {
			UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
			long version = updateResponse.getVersion();
			
			if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
				System.out.println("insert success, version is " + version);
			} else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
				System.out.println("update success, version is " + version);
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void indexDocWithRouting(String indexName, String route, Map<String, Object> doc) {
		IndexRequest indexRequest = new IndexRequest(indexName).id((String)doc.get("key")).source(doc); 
		indexRequest.routing(route);
		try {
		    IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		    System.out.println("新增成功" + response.toString());
		} catch(ElasticsearchException e ) {
		    if (e.status() == RestStatus.CONFLICT) {
		    	System.out.println("写入索引产生冲突"+e.getDetailedMessage());
		    }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}



	@Override
	public void indexDocsWithRouting(String indexName, List<Map<String, Object>> docs) {
		try {
            if (null == docs || docs.size() <= 0) {
                return;
            }
            BulkRequest request = new BulkRequest();
            for (Map<String, Object> doc : docs) {
            	HashMap<String, Object> join = (HashMap<String, Object>)doc.get("joinkey");
            	String route = (String)join.get("parent");
                request.add(new IndexRequest(indexName).id((String)doc.get("key"))
                            .source(doc).routing(route));
            }
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            if (bulkResponse != null) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        System.out.println("新增成功" + indexResponse.toString());
                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        System.out.println("修改成功" + updateResponse.toString());
                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                        System.out.println("删除成功" + deleteResponse.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}



	@Override
	public void indexJsonDocs(String indexName, List<Sougoulog> docs) {
		 try {
	            if (null == docs || docs.size() <= 0) {
	                return;
	            }
	            BulkRequest request = new BulkRequest();
	            for (Sougoulog doc : docs) {
	                request.add(
	                		new IndexRequest(indexName).id(String.valueOf(doc.getId())).source(JSON.toJSONString(doc), XContentType.JSON)
	                            );
	            }
	            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
	            if (bulkResponse != null) {
	                for (BulkItemResponse bulkItemResponse : bulkResponse) {
	                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();

	                    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
	                            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
	                        IndexResponse indexResponse = (IndexResponse) itemResponse;
	                        System.out.println("新增成功" + indexResponse.toString());
	                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
	                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
	                        System.out.println("修改成功" + updateResponse.toString());
	                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
	                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
	                        System.out.println("删除成功" + deleteResponse.toString());
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
