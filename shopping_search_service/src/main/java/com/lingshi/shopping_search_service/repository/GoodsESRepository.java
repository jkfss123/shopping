package com.lingshi.shopping_search_service.repository;

import com.lingshi.shopping_common.entity.GoodsES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsESRepository extends ElasticsearchRepository<GoodsES,Long> {

}
