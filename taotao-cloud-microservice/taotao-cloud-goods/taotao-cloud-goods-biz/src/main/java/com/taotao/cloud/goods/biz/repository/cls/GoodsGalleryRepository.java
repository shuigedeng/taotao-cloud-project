package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.GoodsGallery;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class GoodsGalleryRepository extends BaseCrSuperRepository<GoodsGallery, Long> {

	public GoodsGalleryRepository(EntityManager em) {
		super(GoodsGallery.class, em);
	}
}
