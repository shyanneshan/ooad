package cn.edu.xmu.provider.server;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.provider.model.bo.GoodsSKU;
import cn.edu.xmu.provider.model.vo.CouponActivityVo;
import cn.edu.xmu.provider.model.vo.GoodsCartVo;
import cn.edu.xmu.provider.model.vo.GoodsSkuSimpleRetVo;


import java.util.List;

public interface GoodsService {

    /**
     * @description: 获取SKU的BO对象
     * @author: Wc
     * @date: Created at 2020/12/14
     */
    ReturnObject<GoodsSkuSimpleRetVo> getGoodsSkuById(Long skuid);

    /**
     * @description: 通过SKU Id获取价格
     * @author: Wc
     * @date: Created at 2020/12/14
     */
    Long getPriceBySkuId(Long skuId);

    /**
     * @description: 通过skuid列表获取GoodsSkuSimpleRetVo
     * @author: Wc
     * @date: Created at 2020/12/14
     */
    ReturnObject<List<GoodsSkuSimpleRetVo>> getGoodsSkuListById(List<Long> skuid);

    /**
     * @description:判断sku是否是此商店的商品
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:46
     */
    Boolean checkSkuIdByShopId(Long shopId,Long skuId);

    /**
     * 给收藏使用，通过skuid判断商品是否存在
     * @param skuId
     * @return
     */
    ReturnObject<Object> checkSkuId(Long skuId);

    /**
     * 给足迹使用，通过shop筛选sku
     * @param shopId
     * @param skuIds
     * @return
     */
    ReturnObject<List<GoodsSkuSimpleRetVo>> getGoodsSkuListBySkuIdAndShopId(Long shopId,List<Long> skuIds);

    /**
     * 给购物车部分使用其1（用户获取购物车列表)
     * @param skuIds
     * @return
     */
    ReturnObject<List<GoodsCartVo>> getGoodsCartListBySkuIdList(List<Long> skuIds);

    /**
     * 给购物车部分使用其2（用户增加商品进入购物车）
     * @param skuId
     * @return
     */
    ReturnObject<GoodsCartVo> getGoodsCartBySkuId(Long skuId);

    /**
     * 给购物车部分使用3，判断两个skuId是否属于同一家店
     * @param skuId1
     * @param skuId2
     * @return
     */
    ReturnObject<Boolean> checkSkuIdsFromASpu(Long skuId1, Long skuId2);

    /**
     * 给购物车部分使用4，判断通过skuid获取商品originalPrice
     * @param skuId
     * @return
     */
    ReturnObject<Long> getOriginalPriceBySkuId(Long skuId);

    /**
     * @description: 通过店铺id获取SKU Id列表
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:48
     */
    List<Long> getSkuIdListByShopId(Long shopId);

    /**
     * @description: 通过店铺id获取SKU的bo列表
     * @author: Feiyan Liu
     * @date: Created at 2020/12/8 19:48
     */
    List<GoodsSKU> getSkuListByShopId(Long shopId);

    /**
     * @description: 通过skuId组装GoodsCartVo
     * @author: Feiyan Liu
     * @date: Created at 2020/12/10 19:35
     */

    GoodsCartVo getCartByskuId(Long Sku);

    /**
     * @description: 根据skuid获得当前有效的活动
     * @author: Feiyan Liu
     * @date: Created at 2020/12/10 19:34
     */
    List<CouponActivityVo> getCouponActivityIdBySkuId(Long id);
}
