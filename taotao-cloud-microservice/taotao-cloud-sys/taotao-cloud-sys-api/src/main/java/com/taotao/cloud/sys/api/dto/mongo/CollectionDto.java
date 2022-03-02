
package com.taotao.cloud.sys.api.dto.mongo;


/**
 * 集合信息展示
 */
public class CollectionDto {
    private String collectionName;
//    private CommandResult stats;
//    private int documents;
//    private int views;
//    private int objects;
//    private double avgObjSize;
//    private double dataSize;
//    private int storageSize;
//    private int numExtents;
//    private int indexes;
//    private double indexSize;
//    private double scaleFactor;
//    private double fsUsedSize;
//    private double fsTotalSize;

    public CollectionDto() {
    }

	public CollectionDto(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
}
