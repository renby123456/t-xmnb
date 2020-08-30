package com.jk.controller;

import com.jk.entity.ProductBean;


import com.jk.entity.ProductEntity;
import com.jk.service.UserService;
import com.jk.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("product")
public class ProductController {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

/*    @Resource
    private ItemRepository itemRepository;*/

    @Resource
    private UserService userService;

    //查看商品详情
    @RequestMapping("productbyid")
    public ProductEntity productbyid(@RequestParam("id")Integer id, Model model){
        return userService.productbyid(id);
    }
    //导航栏查询数据
    @RequestMapping("findProductByCid")
    public List<ProductBean> findProductByCid(@RequestParam(name = "page",defaultValue = "1") Integer page,
                                @RequestParam(name = "rows",defaultValue = "20")Integer rows,String adTitle,Integer skuCid3){
        List<ProductBean> productLis = userService.findProductByCid(adTitle,skuCid3);
        return productLis;
    }
/*    //首页初始化查询
    @RequestMapping("findProductList")
    public Map findProductList(@RequestParam(name = "page",defaultValue = "1") Integer page,
                               @RequestParam(name = "rows",defaultValue = "20")Integer rows){
            Map productLists = userService.findProductLists(page, rows);
            return productLists;
    }*/

    @RequestMapping("findProductList")
    public Map findProductList(ProductEntity productEntity, @PageableDefault(page = 0,size = 21) Pageable pageable){
        //搜索条件
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
/*        if(!StringUtils.isEmpty(productEntity.getAd_title())){
            //模糊查询
            queryBuilder.must(QueryBuilders.matchQuery("ad_title",productEntity.getAd_title()));
        }
        //价格区间
        if(productEntity.getStartPrice()!=null){
            queryBuilder.must(QueryBuilders.rangeQuery("pc_price").gte(productEntity.getStartPrice()));
        }if(productEntity.getEndPrice()!=null){
            queryBuilder.must(QueryBuilders.rangeQuery("pc_price").lte(productEntity.getEndPrice()));
        }*/
        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("ad_title");
        highlightBuilder.preTags("<em style='color:red'>");
        highlightBuilder.postTags("</em>");
        //返回符合分页条查高亮的所有字段
        NativeSearchQuery builder = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(queryBuilder).withHighlightBuilder(highlightBuilder).build();
        //查询ES里的数据
        SearchHits<ProductEntity> search = elasticsearchRestTemplate.search(builder, ProductEntity.class);
        HashMap<String, Object> map = new HashMap<>();
        //查询符合所有条件的条数
        long count = elasticsearchRestTemplate.count(builder, ProductEntity.class);
        map.put("total",count);
        List<SearchHit<ProductEntity>> searchHits = search.getSearchHits();
        ArrayList<ProductEntity> productEntitys = new ArrayList<>();
        for (SearchHit<ProductEntity> searchHit : searchHits) {
            //设置需要高亮展示的字段
            List<String> ad_title = searchHit.getHighlightField("ad_title");
            ProductEntity content = searchHit.getContent();
            content.setAd_title(ad_title.size()>0 ? ad_title.get(0) : content.getAd_title());
            productEntitys.add(content);
        }
        map.put("rows",productEntitys);
        return map;
    }
    //个人清洁","宠物","女鞋","箱包","钟表","珠宝","男鞋","运动","户外","房产","汽车","汽车用品","母婴","玩具乐器","食品","酒类","生鲜","特产","艺术","礼品鲜花","农资产品","医药保健","计生情趣","图书","文娱","电子书","空调挂机","中央空调","移动空调","变频空调","烘干机","油烟机","燃气灶","消毒柜","洗碗机","电热水器","太阳能热水器","冰箱","酒柜","电烤箱","破壁机","电饭煲","电压力锅","电炖锅","豆浆机","料理机","咖啡机","电饼铛","榨汁机","原汁机","电水壶","热水瓶","微波炉","多用途锅","养生壶","电磁炉","面包机","面条机","电陶炉","煮蛋器","电烧烤炉","电风扇","冷风扇","空气净化器","吸尘器","除螨仪","扫地机器人","除湿机","干衣机","加湿器","蒸汽拖把","拖地机挂烫机","熨斗","电话机","饮水机","净水器","取暖电器","毛球修剪器","生活电器配件","剃须刀","电动牙刷","电吹风","美容器","洁面仪","按摩器","健康秤卷","直发器剃","脱毛器","理发器","足浴盆","足疗机","按摩椅","家庭影院","KTV音响","迷你音响","DVD","功放","回音壁","麦克风","手机壳","贴膜","手机存储卡","数据线","充电器","手机","耳机","创意","配件","手机饰品","手机电池","苹果","周边","移动电源","蓝牙耳机","手机支架","车载配件","拍照配件","数码相机","单电","微单相机","单反相机","拍立得","运动相机","摄像机镜头","户外器材","影棚器材","冲印服务","数码相框","智能手环","智能手表","智能眼镜","智能机器人","运动跟踪器","健康监测","智能配饰","智能家居","体感车","无人机
    @RequestMapping("createIndexMySQL")
    public Boolean createIndexMySQL(ProductBean productBean) {
        try {
           String[] type = {"个人清洁","宠物","女鞋","箱包","钟表","珠宝","男鞋","运动","户外","房产","汽车","汽车用品","母婴","玩具乐器","食品","酒类","生鲜","特产","艺术","礼品鲜花","农资产品","医药保健","计生情趣","图书","文娱","电子书","空调挂机","中央空调","移动空调","变频空调","烘干机","油烟机","燃气灶","消毒柜","洗碗机","电热水器","太阳能热水器","冰箱","酒柜","电烤箱","破壁机","电饭煲","电压力锅","电炖锅","豆浆机","料理机","咖啡机","电饼铛","榨汁机","原汁机","电水壶","热水瓶","微波炉","多用途锅","养生壶","电磁炉","面包机","面条机","电陶炉","煮蛋器","电烧烤炉","电风扇","冷风扇","空气净化器","吸尘器","除螨仪","扫地机器人","除湿机","干衣机","加湿器","蒸汽拖把","拖地机挂烫机","熨斗","电话机","饮水机","净水器","取暖电器","毛球修剪器","生活电器配件","剃须刀","电动牙刷","电吹风","美容器","洁面仪","按摩器","健康秤卷","直发器剃","脱毛器","理发器","足浴盆","足疗机","按摩椅","家庭影院","KTV音响","迷你音响","DVD","功放","回音壁","麦克风","手机壳","贴膜","手机存储卡","数据线","充电器","手机","耳机","创意","配件","手机饰品","手机电池","苹果","周边","移动电源","蓝牙耳机","手机支架","车载配件","拍照配件","数码相机","单电","微单相机","单反相机","拍立得","运动相机","摄像机镜头","户外器材","影棚器材","冲印服务","数码相框","智能手环","智能手表","智能眼镜","智能机器人","运动跟踪器","健康监测","智能配饰","智能家居","体感车","无人机"};
            for (String s : type) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("callback", "jQuery5403817");
                params.put("area", 27);
                params.put("enc", "utf-8");
                params.put("keyword", s);
                params.put("page", 1);
                params.put("adType", 7);
                params.put("ad_ids", "291:33");
                params.put("xtest", "new_search");
                String result = HttpClientUtil.get("https://search-x.jd.com/Search", params);
                String replace = result.replace("jQuery5403817(", "");
                String substring = replace.substring(0, replace.length() - 1);
                JSONObject jsonObject = JSON.parseObject(substring);
                JSONArray jsonArray = jsonObject.getJSONArray("291");
                String jsonString = jsonArray.toJSONString();

                List<ProductBean> productBeanss = JSON.parseArray(jsonString, ProductBean.class);
                IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(ProductBean.class);
                boolean exists = indexOperations.exists();
                if (!exists) {
                    indexOperations.createMapping(ProductBean.class);
                }
                userService.saveAll(productBeanss);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


   /* @RequestMapping("createIndex")
    public void createIndex() throws Exception {
        String[] type = {"个人清洁","宠物","女鞋","箱包","钟表","珠宝","男鞋","运动","户外","房产","汽车","汽车用品","母婴","玩具乐器","食品","酒类","生鲜","特产","艺术","礼品鲜花","农资产品","医药保健","计生情趣","图书","文娱","电子书","空调挂机","中央空调","移动空调","变频空调","烘干机","油烟机","燃气灶","消毒柜","洗碗机","电热水器","太阳能热水器","冰箱","酒柜","电烤箱","破壁机","电饭煲","电压力锅","电炖锅","豆浆机","料理机","咖啡机","电饼铛","榨汁机","原汁机","电水壶","热水瓶","微波炉","多用途锅","养生壶","电磁炉","面包机","面条机","电陶炉","煮蛋器","电烧烤炉","电风扇","冷风扇","空气净化器","吸尘器","除螨仪","扫地机器人","除湿机","干衣机","加湿器","蒸汽拖把","拖地机挂烫机","熨斗","电话机","饮水机","净水器","取暖电器","毛球修剪器","生活电器配件","剃须刀","电动牙刷","电吹风","美容器","洁面仪","按摩器","健康秤卷","直发器剃","脱毛器","理发器","足浴盆","足疗机","按摩椅","家庭影院","KTV音响","迷你音响","DVD","功放","回音壁","麦克风","手机壳","贴膜","手机存储卡","数据线","充电器","手机","耳机","创意","配件","手机饰品","手机电池","苹果","周边","移动电源","蓝牙耳机","手机支架","车载配件","拍照配件","数码相机","单电","微单相机","单反相机","拍立得","运动相机","摄像机镜头","户外器材","影棚器材","冲印服务","数码相框","智能手环","智能手表","智能眼镜","智能机器人","运动跟踪器","健康监测","智能配饰","智能家居","体感车","无人机"};
        for (String s : type) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("callback", "jQuery5403817");
            params.put("area", 27);
            params.put("enc", "utf-8");
            params.put("keyword", s);
            params.put("page", 1);
            params.put("adType", 7);
            params.put("ad_ids", "291:33");
            params.put("xtest", "new_search");
            String result = HttpClientUtil.get("https://search-x.jd.com/Search", params);
            String replace = result.replace("jQuery5403817(", "");
            String substring = replace.substring(0, replace.length() - 1);
            JSONObject jsonObject = JSON.parseObject(substring);
            JSONArray jsonArray = jsonObject.getJSONArray("291");
            String jsonString = jsonArray.toJSONString();

            List<ProductEntity> productBeans = JSON.parseArray(jsonString, ProductEntity.class);
            IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(ProductEntity.class);
            boolean exists = indexOperations.exists();
            if (!exists) {
                indexOperations.createMapping(ProductEntity.class);
            }
            itemRepository.saveAll(productBeans);

        }
    }*/
}
