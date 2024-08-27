package com.lingshi.shopping_search_service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.constant.Const;
import com.lingshi.shopping_common.entity.*;
import com.lingshi.shopping_common.service.IGoodsSearchService;
import com.lingshi.shopping_search_service.repository.GoodsESRepository;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.*;
import java.util.stream.Collectors;

@DubboService
public class GoodsSearchServiceImpl implements IGoodsSearchService {


    @Autowired
    private ElasticsearchClient client;


    @Autowired
    private GoodsESRepository goodsESRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @SneakyThrows
    public List<String> analyzer(String keyword, String analyzer) {
        //穿件分词请求
        AnalyzeRequest analyzeRequest = AnalyzeRequest.of(a -> a.index("goods").analyzer(analyzer).text(keyword));

        //发送分词请求,获取结果
        AnalyzeResponse response = client.indices().analyze(analyzeRequest);

        //获取出分词的结果数据
        List<String> results = response.tokens().stream().map(AnalyzeToken::token).toList();

        return results;
    }


    @Override
    @SneakyThrows
    public List<String> autoSuggest(String keyword) {

        //创建补全对象
        Suggester suggester = Suggester.of(s -> s.suggesters("prefix_suggestion",
                FieldSuggester.of(fs -> fs.completion(
                        cs -> cs.field("tags")
                                .skipDuplicates(true)
                                .size(10)))).text(keyword));
        //搜索
        SearchResponse<Map> searchResponse = client.search(s -> s.index("goods").suggest(suggester), Map.class);

        List<Suggestion<Map>> suggestions = searchResponse.suggest().get("prefix_suggestion");

        Suggestion<Map> suggestion = suggestions.get(0);

        //查询的补全结果
        List<CompletionSuggestOption<Map>> options = suggestion.completion().options();
        //转换补全结果
        List<String> result = options.stream().map(CompletionSuggestOption::text).toList();
        return result;
    }

    @Override
    public GoodsSearchResult search(GoodsSearchParam goodsSearchParam) {
        //1.构建查询条件
        NativeQuery nativeQuery = buildQuery(goodsSearchParam);
        //2.执行查询,获取结果
        SearchHits<GoodsES> searchHits = elasticsearchTemplate.search(nativeQuery, GoodsES.class);
        //获取数据
        List<GoodsES> goodsESList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        //3.封装分页
        Page<GoodsES> page = new Page<>();
        //设置当前页
        page.setCurrent(goodsSearchParam.getPage());
        //设置总条数
        page.setTotal(searchHits.getTotalHits());
        //设置每页条数
        page.setSize(goodsSearchParam.getSize());
        //设置当前页记录
        page.setRecords(goodsESList);

        GoodsSearchResult goodsSearchResult = new GoodsSearchResult();
        goodsSearchResult.setGoodsPage(page);
        goodsSearchResult.setGoodsSearchParam(goodsSearchParam);
        //4.封装面板
        buildGoodsPanel(goodsSearchResult,goodsSearchParam);

        return goodsSearchResult;
    }


    /**
     * 封装结果面板，查询前20条关联度最高的数据结果作为面板的结果显示
     * @param result 查询结果对象
     * @param goodsSearchParam 商品参数
     */
    private void buildGoodsPanel(GoodsSearchResult result,GoodsSearchParam goodsSearchParam){
        //4.1封装品牌
        goodsSearchParam.setPage(1);
        goodsSearchParam.setSize(20);
        goodsSearchParam.setSortFiled(null);
        goodsSearchParam.setSort(null);

        //构建查询减
        NativeQuery nativeQuery = buildQuery(goodsSearchParam);

        SearchHits<GoodsES> searchHits = elasticsearchTemplate.search(nativeQuery, GoodsES.class);

        List<GoodsES> goodsESList = searchHits.stream().map(SearchHit::getContent).toList();

        //品牌集合
        Set<String> brands = new HashSet<>();
        //类目集合
        Set<String> productTypes = new HashSet<>();
        //商品规格
        Map<String, Set<String>> specificationMap = new HashMap<>();
        for (GoodsES goods : goodsESList) {
            //4.1封装商品品牌
            brands.add(goods.getBrand());
            //4.2封装商品类目
            List<String> productType = goods.getProductType();
            productTypes.addAll(productType);

            // //4.3封装商品规格
            Map<String, List<String>> specification = goods.getSpecification();
            Set<Map.Entry<String, List<String>>> entries = specification.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                String specName = entry.getKey();//规格名称  内存
                List<String> options = entry.getValue();// 32G/64G/128G   32G/

                if(!specificationMap.containsKey(specName)){//如果没有此规格，直接添加新规格
                    specificationMap.put(specName,new HashSet<>(options));
                }else{
                    //如果有规格，之前基础上添加新的规格
                    specificationMap.get(specName).addAll(options);
                }
            }
        }

        result.setBrands(brands);
        result.setProductType(productTypes);
        result.setSpecifications(specificationMap);
    }

    private NativeQuery buildQuery(GoodsSearchParam goodsSearchParam) {
        //1.创建查询构建对象
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();

        //组织条件对象
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        //2.根据关键字查询
        String keyword = goodsSearchParam.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(q -> q.query(keyword).fields("goodsName", "caption", "brand"));
            boolQuery.must(multiMatchQuery._toQuery());
        } else {
            MatchAllQuery matchAllQuery = new MatchAllQuery.Builder().build();
            boolQuery.must(matchAllQuery._toQuery());
        }
        //3.如果有品牌，则品牌的精准查询
        String brand = goodsSearchParam.getBrand();
        if (StrUtil.isNotBlank(brand)) {
            TermQuery termQuery = TermQuery.of(q -> q.field("brand").value(brand));
            boolQuery.must(termQuery._toQuery());
        }
        //4.如果有价格，则按照价格范围查询 range
        Double lowPrice = goodsSearchParam.getLowPrice();
        Double highPrice = goodsSearchParam.getHighPrice();
        if (Objects.nonNull(lowPrice) && lowPrice > 0) {
            RangeQuery gteQuery = RangeQuery.of(q -> q.field("price").gte(JsonData.of(lowPrice)));
            boolQuery.must(gteQuery._toQuery());
        }
        if (Objects.nonNull(highPrice) && highPrice > 0) {
            RangeQuery lteQuery = RangeQuery.of(q -> q.field("price").lte(JsonData.of(highPrice)));
            boolQuery.must(lteQuery._toQuery());
        }
        //5.如果有商品规格，则规格查询
        Map<String, String> specificationOption = goodsSearchParam.getSpecificationOption();
        if(CollUtil.isNotEmpty(specificationOption)){
            Set<Map.Entry<String, String>> entries = specificationOption.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String specification = entry.getKey();//机身内存
                String specOption = entry.getValue(); //32G/64G/128G
                TermQuery termQuery = TermQuery.of(q -> q.field("specification." + specification + ".keyword").value(specOption));
                boolQuery.must(termQuery._toQuery());
            }
        }
        //设置查询条件
        nativeQueryBuilder.withQuery(boolQuery.build()._toQuery());

        //6.如果有排序，则排序
        String sortFiled = goodsSearchParam.getSortFiled();//NEW:新品 PRICE:价格
        String sortParam = goodsSearchParam.getSort();//DESC ASC
        if(StrUtil.isNotBlank(sortFiled) && StrUtil.isNotBlank(sortParam)){
            //排序对象
            Sort sort = null;
            if(Const.NEW.equals(sortFiled)){
                if(Const.ASC.equals(sortParam)){
                    //新品的升序就是id的降序
                    sort = Sort.by(Sort.Order.desc("id"));
                }
                if(Const.DESC.equals(sortParam)){
                    sort = Sort.by(Sort.Order.asc("id"));
                }
            }else if(Const.PRICE.equals(sortFiled)){
                if(Const.ASC.equals(sortParam)){
                    sort = Sort.by(Sort.Order.asc("price"));
                }
                if(Const.DESC.equals(sortParam)){
                    sort = Sort.by(Sort.Order.desc("price"));
                }
            }
            //分页加入构建对象
            nativeQueryBuilder.withSort(sort);
        }
        //7.分页
        PageRequest pageRequest = PageRequest.of(goodsSearchParam.getPage() - 1, goodsSearchParam.getSize());
        nativeQueryBuilder.withPageable(pageRequest);

        //构建查询条件
        NativeQuery query = nativeQueryBuilder.build();
        return query;
    }

    @Override
    public void syncGoodsToES(GoodsDesc goodsDesc) {
        //创建GoodsEs对象
        GoodsES goodsES = new GoodsES();
        //设置属性数据
        goodsES.setId(goodsDesc.getId());
        goodsES.setGoodsName(goodsDesc.getGoodsName());
        goodsES.setCaption(goodsDesc.getCaption());
        goodsES.setPrice(goodsDesc.getPrice());
        goodsES.setHeaderPic(goodsDesc.getHeaderPic());

        goodsES.setBrand(goodsDesc.getBrand().getName());

        //设置搜索标签
        List<String> tags = new ArrayList<>();
        tags.addAll(analyzer(goodsDesc.getGoodsName(), Const.IK_SMART));
        tags.addAll(analyzer(goodsDesc.getCaption(), Const.IK_SMART));
        goodsES.setTags(tags);

        //商品类目
        List<String> productTypeNames = new ArrayList<>();
        productTypeNames.add(goodsDesc.getProductType1().getName());
        productTypeNames.add(goodsDesc.getProductType2().getName());
        productTypeNames.add(goodsDesc.getProductType3().getName());

        goodsES.setProductType(productTypeNames);

        //商品规格集合
        HashMap<String, List<String>> specificationMap = new HashMap<>();

        List<Specification> specifications = goodsDesc.getSpecifications();
        for (Specification specification : specifications) {
            //获取规格名称
            String specName = specification.getSpecName();

            //获取所有的规格选项
            List<SpecificationOption> specificationOptions = specification.getSpecificationOptions();
            //转换成规格项
            List<String> options = specificationOptions.stream().map(SpecificationOption::getOptionName).toList();

            specificationMap.put(specName, options);
        }
        //设置商品的规格数据
        goodsES.setSpecification(specificationMap);

        //将封装的数据存储到ES
        goodsESRepository.save(goodsES);
    }

    @Override
    public void delete(Long id) {
        goodsESRepository.deleteById(id);
    }
}
