package cn.edu.xmu.advertisement.dao;

import cn.edu.xmu.advertisement.mapper.AdvertisementPoMapper;
import cn.edu.xmu.advertisement.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.advertisement.model.bo.Advertisement;
import cn.edu.xmu.advertisement.model.po.AdvertisementPo;
import cn.edu.xmu.advertisement.model.po.AdvertisementPoExample;
import cn.edu.xmu.advertisement.model.po.TimeSegmentPo;
import cn.edu.xmu.advertisement.model.po.TimeSegmentPoExample;
import cn.edu.xmu.advertisement.model.vo.AdvertisementRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.edu.xmu.ooad.util.ResponseCode.RESOURCE_ID_NOTEXIST;


@Repository
public class AdvertisementDao {


    @Autowired
    private AdvertisementPoMapper advertisementPoMapper;

    @Autowired
    private TimeSegmentPoMapper timeSegmentPoMapper;


    private static final Logger logger = LoggerFactory.getLogger(AdvertisementDao.class);







    public ReturnObject getAdByDefault(Long id){

        Byte BeDefault = 1;
        Byte notBeDefault = 0;

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);
        if (advertisementPo ==null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        if(advertisementPo.getBeDefault().equals(BeDefault)){

            advertisementPo.setBeDefault(notBeDefault);
            advertisementPoMapper.updateByPrimaryKey(advertisementPo);
            return new ReturnObject(ResponseCode.OK);
        }
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andBeDefaultEqualTo(BeDefault);

        List<AdvertisementPo> poList = advertisementPoMapper.selectByExample(example);
        for(AdvertisementPo oldDefaultPo:poList){

            oldDefaultPo.setBeDefault(notBeDefault);
            advertisementPoMapper.updateByPrimaryKeySelective(oldDefaultPo);
        }

        advertisementPo.setBeDefault(BeDefault);
        advertisementPoMapper.updateByPrimaryKey(advertisementPo);

        return new ReturnObject(ResponseCode.OK);
    }



    /**
     * ????????????id??????????????????
     *
     * @param advertisementPo
     * @return
     */
    public ReturnObject<Object> modifyAdvertiseDefault(AdvertisementPo advertisementPo) {
        try {
            int ret = advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
            Long id = advertisementPo.getId();
            //????????????????????????
            if (ret == 0) {
                logger.info("?????????????????????????????????id = " + id);
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("?????? id = " + id + " ??????????????????");
                return new ReturnObject<>();
            }
        } catch (Exception e) {
            logger.error("Internal error Happened:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }


    public List<AdvertisementPo> getAdByExmple() {

        //???????????????????????????????????????
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        Byte bedefaults = 1;
        criteria.andBeDefaultEqualTo(bedefaults);

        if (advertisementPoMapper.selectByExample(example).get(0) == null)
            return null;

        else {
            List<AdvertisementPo> advertisementPos = advertisementPoMapper.selectByExample(example);
            return advertisementPos;
        }


    }


    /**
     * ID??????????????????
     *
     * @param id
     * @return
     * @author ww
     */
    public AdvertisementPo findAdById(Long id) {

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);

        return advertisementPo;
    }


    /**
     * ID?????????????????????????????????
     *
     * @param id
     * @return
     */
    public ReturnObject deleteAd(Long id) {

        if (advertisementPoMapper.selectByPrimaryKey(id) == null)
            return new ReturnObject<>(RESOURCE_ID_NOTEXIST);

        //???????????????????????????608?????????????????????
        //??????????????????????????????
        //     if (advertisementPoMapper.selectByPrimaryKey(id).getState() != Advertisement.State.SHELF.getCode().byteValue()) {

        try {
            advertisementPoMapper.deleteByPrimaryKey(id);
            return new ReturnObject();
        } catch (DataAccessException e) {
            // ???????????????
            logger.error("??????????????????" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } catch (Exception e) {
            // ???????????????
            logger.error("???????????????" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
//        } else
//            return new ReturnObject<>(ResponseCode.ADVERTISEMENT_STATENOTALLOW);    //608 ??????????????????

    }

    /**
     * ID????????????????????????????????????????????????
     *
     * @param id
     * @return
     */
    public ReturnObject deleteAd1(Long id) {

        try {
            advertisementPoMapper.deleteByPrimaryKey(id);
            return new ReturnObject();
        } catch (DataAccessException e) {
            // ???????????????
            logger.error("??????????????????" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } catch (Exception e) {
            // ???????????????
            logger.error("???????????????" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }


    /**
     * ID????????????????????????????????????
     *
     * @param tid ????????????id
     * @return
     */
    public int numBySegID(Long tid) {

        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(tid);

        List<AdvertisementPo> advertisementPos = null;
        try {
            advertisementPos = advertisementPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getSeg_Id: ").append(e.getMessage());
            logger.error(message.toString());
        }
        if (null == advertisementPos || advertisementPos.isEmpty() || advertisementPos.size() < 8) {
            return 1;
        } else
            return 0;

    }

    //
//    /**
//     * ??????????????????????????????
//     *
//     * @param tid ????????????id
//     * @return
//     */
//    public ReturnObject addAdBySegId(Long tid, Long id) {
//
//        AdvertisementPo advertisementPo = new AdvertisementPo();
//        advertisementPo.setId(id);
//        ReturnObject returnObject;
//
//        if (tid != 0) {
//            //?????????????????????????????????
//            TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(tid);
//            if (timeSegmentPo == null || timeSegmentPo.getType() != 0)
//                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//        }
//
//      AdvertisementPo advertisementPo1s = findAdById(id);
//        if (advertisementPo1s==null)
//            return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//
////           //????????????????????????????????????8???
////        int ret = numBySegID(tid);
////        if (ret==1) {
//
//            //??????????????????????????????
//            advertisementPo.setSegId(tid);
//            try {
//                int rets = advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
//                Long ids = advertisementPo.getId();
//
//                //????????????????????????
//                if (rets == 0) {
//                    logger.info("?????????????????????????????????id = " + ids);
//                    return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//                } else {
//                    logger.info("?????? id = " + ids + " ??????????????????");
//
////                    AdvertisementPoExample example = new AdvertisementPoExample();
////                    AdvertisementPoExample.Criteria criteria = example.createCriteria();
////                    criteria.andIdEqualTo(id);
//
//                    //AdvertisementPo advertisementPo1 = advertisementPoMapper.selectByPrimaryKey(id);
//                    AdvertisementPo advertisementPo1 = findAdById(id);
//                  returnObject   = new ReturnObject<>(new AdvertisementRetVo(advertisementPo1));
//                    logger.info("?????? id = " + ids + " ??????");
//
//                }
//            } catch (Exception e) {
//                logger.error("Internal error Happened:" + e.getMessage());
//                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
//            }
//        return returnObject;
////        } else {
////            return new ReturnObject(ResponseCode.ADVERTISEMENT_OUTLIMIT);
//
//       // }
//
//        //     }
//
//
//    }
    public ReturnObject addAdBySegId(Long tid, Long id) {

        AdvertisementPo ishave = findAdById(id);
        if (ishave == null)
            return new ReturnObject(RESOURCE_ID_NOTEXIST);

        AdvertisementPo advertisementPo = new AdvertisementPo();
        advertisementPo.setId(id);
        if (tid != 0) {
            //?????????????????????????????????
            TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(tid);
            if (timeSegmentPo == null || timeSegmentPo.getType() != 0)
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
        }
//        else {

//            //????????????????????????????????????8???
//            ReturnObject ret = numBySegID(tid);
//            if (ret.getCode() == ResponseCode.OK) {

        //??????????????????????????????
        advertisementPo.setSegId(tid);

        try {
            int rets = advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
            Long ids = advertisementPo.getId();
            //????????????????????????
            if (rets == 0) {
                logger.info("?????????????????????????????????id = " + ids);
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("?????? id = " + ids + " ??????????????????");

                AdvertisementPoExample example = new AdvertisementPoExample();
                AdvertisementPoExample.Criteria criteria = example.createCriteria();
                criteria.andIdEqualTo(id);

                AdvertisementPo advertisementPo1 = advertisementPoMapper.selectByExample(example).get(0);
                ReturnObject returnObject = new ReturnObject<>(new AdvertisementRetVo(advertisementPo1));
                return returnObject;
            }
        } catch (Exception e) {
            logger.error("Internal error Happened:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

//            } else {
//                return ret;
//
//            }

        // }


    }

    /**
     * ???????????????????????????????????????
     *
     * @param tid      ??????id
     * @param page     ??????
     * @param pageSize ????????????
     * @return ????????????
     */
    public ReturnObject<PageInfo<VoObject>> getAdBySegID(Long tid, Integer page, Integer pageSize, String beginDate, String endDate) {

        if (tid != 0) {

            //??????tid????????????
            TimeSegmentPo timeSegmentPo = getSegIdPos(tid);
            if (timeSegmentPo == null || timeSegmentPo.getType() != 0)
                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
        }

        if (page <= 0 || pageSize <= 0)
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);

        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(tid);

        AdvertisementPoExample example2 = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andSegIdEqualTo(tid);


        //String->Localdata ?????????????????????????????? ??????LocalData
        //?????????????????????????????????beginDate???endDate??????
        LocalDate enddata2;
        LocalDate begindata1;

        //??????????????????
        if (beginDate != null && beginDate != "" && (endDate == null || endDate == "")) {

            try {

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                begindata1 = LocalDate.parse(beginDate, fmt);

                criteria.andBeginDateGreaterThanOrEqualTo(begindata1);
            } catch (Exception e) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            }

        }

        //??????????????????
        if ((beginDate == null || beginDate == "") && endDate != null && endDate != "") {

            try {

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                enddata2 = LocalDate.parse(endDate, fmt);

                criteria.andEndDateLessThanOrEqualTo(enddata2);

            } catch (Exception e) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            }
        }

        //?????????????????????????????????
        if (beginDate != null && beginDate != "" && endDate != null && endDate != "") {

            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                begindata1 = LocalDate.parse(beginDate, fmt);
                enddata2 = LocalDate.parse(endDate, fmt);

                //??????????????????????????????
                if (begindata1.isAfter(enddata2))
                    return new ReturnObject<>(ResponseCode.Log_Bigger);

                criteria.andBeginDateGreaterThanOrEqualTo(begindata1);
                criteria.andEndDateLessThanOrEqualTo(enddata2);
            } catch (Exception e) {

                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            }

        }

        //  ????????????
        //????????????????????????????????? ?????? ??????
//        String orderBy = "weight desc";
//
//       PageHelper.startPage(page,pageSize,orderBy);
        PageHelper.startPage(page, pageSize, true, true, null);
        // PageHelper.offsetPage(0,8,true);

        List<AdvertisementPo> advertisementPos = null;
        Byte aaa = 1;
        try {

            //????????????????????????
            advertisementPos = advertisementPoMapper.selectByExample(example);

        } catch (DataAccessException e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("??????????????????%s", e.getMessage()));
        }


        if (advertisementPos.size()==0 && beginDate != null && endDate != null) {


            criteria2.andRepeatsEqualTo(aaa);
            PageHelper.startPage(page, pageSize, true, true, null);

            try {
                advertisementPos = advertisementPoMapper.selectByExample(example2);
            }catch  (DataAccessException e) {

                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("??????????????????%s", e.getMessage()));
            }

        }

        List<VoObject> ret = new ArrayList<>(advertisementPos.size());

        for (AdvertisementPo po : advertisementPos) {
            Advertisement advertisement = new Advertisement(po);
            ret.add(advertisement);
        }

        PageInfo<AdvertisementPo> adPoPage = PageInfo.of(advertisementPos);
        PageInfo<VoObject> adPage = new PageInfo<>(ret);
        adPage.setPages(adPoPage.getPages());
        adPage.setPageNum(adPoPage.getPageNum());
        adPage.setPageSize(adPoPage.getPageSize());
        adPage.setTotal(adPoPage.getTotal());
        return new ReturnObject<>(adPage);

    }


    /**
     * ???????????????????????????????????????(??????????????????tid????????????tid?????????tid??????????????????)
     *
     * @return
     */
    public ReturnObject<List> getCurrentAd() {

        LocalTime localTime = LocalTime.now();

        TimeSegmentPoExample timeSegmentPoExample = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = timeSegmentPoExample.createCriteria();

        Byte a = 0;
        criteria.andTypeEqualTo(a);

        List<TimeSegmentPo> list = null;

        try {
            list = timeSegmentPoMapper.selectByExample(timeSegmentPoExample);

        } catch (DataAccessException e) {

            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        //   List<TimeSegmentPo> list1 = null;

        Long tid;

        ArrayList<Advertisement> list2 = new ArrayList<>();

        List<Advertisement> advertisementList = new ArrayList<>(8);

        if (list.size() != 0) {

            for (TimeSegmentPo po : list) {

                LocalDateTime begindate = po.getBeginTime();
                LocalDateTime enddate = po.getEndTime();

                LocalTime begintime = begindate.toLocalTime();
                LocalTime endtime = enddate.toLocalTime();

                if (begintime.isBefore(localTime) && endtime.isAfter(localTime)) {

                    tid = po.getId();

                    AdvertisementPoExample advertisementPoExample = new AdvertisementPoExample();
                    AdvertisementPoExample.Criteria criteria1 = advertisementPoExample.createCriteria();

                    criteria1.andSegIdEqualTo(tid);
                    criteria1.andStateEqualTo(Advertisement.State.SHELF.getCode().byteValue());

                    List<AdvertisementPo> advertisementPos = null;

                    try {
                        advertisementPos = advertisementPoMapper.selectByExample(advertisementPoExample);
                    } catch (DataAccessException e) {

                        logger.error("selectAllRole: DataAccessException:" + e.getMessage());
                        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
                    }

                    for (AdvertisementPo pos : advertisementPos) {
                        Advertisement advertisement = new Advertisement(pos);
                        list2.add(advertisement);
                    }

                }
            }


            if (list2.size() > 8) {
                int aa = 0;
                for (Advertisement poss : list2) {

                    advertisementList.add(poss);
                    a++;
                    if (a == 8) break;
                }
            } else {
                for (Advertisement poss : list2)
                    advertisementList.add(poss);

            }
            return new ReturnObject<List>(advertisementList);
        } else
            return new ReturnObject<>();


        //        criteria.andBeginTimeLessThanOrEqualTo(localDateTime);
//        criteria.andEndTimeGreaterThanOrEqualTo(localDateTime);

//
//            if(list.size()==0)
//                return new ReturnObject<>(RESOURCE_ID_NOTEXIST);
//            else{
//                 TimeSegmentPo timeSegmentPo = list.get(0);
//             Long tid = timeSegmentPo.getId();
//
//            //????????????????????????????????????
//            AdvertisementPoExample advertisementPoExample = new AdvertisementPoExample();
//            AdvertisementPoExample.Criteria criteria1 = advertisementPoExample.createCriteria();
//
//            criteria1.andSegIdEqualTo(tid);
//            criteria1.andStateEqualTo(Advertisement.State.SHELF.getCode().byteValue());//?????????????????????????????????
//
//            List<AdvertisementPo> advertisementPos = null;
//
//            try {
//                advertisementPos = advertisementPoMapper.selectByExample(advertisementPoExample);
//            } catch (DataAccessException e) {
//
//                logger.error("selectAllRole: DataAccessException:" + e.getMessage());
//                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
//            }
//
//            List<Advertisement> ret = new ArrayList<>(advertisementPos.size());
//            for (AdvertisementPo po : advertisementPos) {
//                Advertisement advertisement = new Advertisement(po);
//                ret.add(advertisement);
//            }
//
//            return new ReturnObject<List>(ret);
//
//        }


    }


    /**
     * ????????????
     *
     * @param id
     * @return advertisement
     */

    public ReturnObject<Advertisement> getAdByID(Long id) {

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);
        if (advertisementPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Advertisement advertisement = new Advertisement(advertisementPo);
//        if (!user.authetic()) {
//            StringBuilder message = new StringBuilder().append("getUserById: ").append(ResponseCode.RESOURCE_FALSIFY.getMessage()).append(" id = ")
//                    .append(user.getId()).append(" username =").append(user.getUserName());
//            logger.error(message.toString());
//            return new ReturnObject<>(ResponseCode.RESOURCE_FALSIFY);
//        }
        return new ReturnObject<>(advertisement);
    }

    /**
     * ????????????
     *
     * @param id
     * @return advertisement
     */
    public ReturnObject getAdByIDAndisBe(Long id) {

        AdvertisementPo advertisementPo = advertisementPoMapper.selectByPrimaryKey(id);
        if (advertisementPo == null)
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        else
            return new ReturnObject(ResponseCode.OK);

    }


    /**
     * ??????????????????
     *
     * @param tid
     * @return timeSegmentPo
     */

    public TimeSegmentPo getSegIdPos(Long tid) {

        return timeSegmentPoMapper.selectByPrimaryKey(tid);
    }


    /**
     * ??????????????????
     *
     * @param advertisement
     * @return Advertisement
     */
    public ReturnObject updateAdImg(Advertisement advertisement) {
        ReturnObject returnObject = new ReturnObject();
        AdvertisementPo newAdvertisementPo = new AdvertisementPo();
        newAdvertisementPo.setId(advertisement.getId());
        newAdvertisementPo.setImageUrl(advertisement.getImageUrl());
        newAdvertisementPo.setState(advertisement.getState().getCode().byteValue());
        //???????????????????????????
        int ret = advertisementPoMapper.updateByPrimaryKeySelective(newAdvertisementPo);
        if (ret == 0) {
            logger.debug("updateUserAvatar: update fail. user id: " + advertisement.getId());
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateUserAvatar: update user success : " + advertisement.toString());
            returnObject = new ReturnObject();
        }
        return returnObject;
    }


    /**
     * ??????????????????????????????????????????id??????0
     *
     * @param tid
     * @return Advertisement
     */
    public ReturnObject deleteSegIDThenZero(Long tid) {


        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andSegIdEqualTo(tid);
        List<AdvertisementPo> advertisementPos = null;

        try {

            advertisementPos = advertisementPoMapper.selectByExample(example);
        } catch (DataAccessException e) {

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("??????????????????%s", e.getMessage()));
        }

        ReturnObject retObj = null;
        for (AdvertisementPo po : advertisementPos) {

            po.setSegId(0L);
            retObj = modifyAdvertiseDefault(po);

            if (retObj.getCode() != ResponseCode.OK) {
                break;
            }
        }

        return retObj;

    }


}

