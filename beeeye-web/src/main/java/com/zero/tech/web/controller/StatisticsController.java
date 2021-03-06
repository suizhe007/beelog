package com.zero.tech.web.controller;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.data.db.mapper.NameInfoMapper;
import com.zero.tech.web.message.BaseMessage;
import com.zero.tech.web.message.MessageCode;
import com.zero.tech.web.message.StatusCode;
import com.zero.tech.web.service.StatisticsService;
import com.zero.tech.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 统计相关的controller
 */
@RestController
@RequestMapping("statistics")
public class StatisticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private NameInfoMapper nameInfoMapper;

    @RequestMapping(path = "names", method = RequestMethod.GET)
    public BaseMessage names(@RequestParam(value = "eventType", required = false) final String eventType) {
        BaseMessage msg = new BaseMessage();
        try {
            ResponseUtil.buildResMsg(msg, MessageCode.SUCCESS, StatusCode.SUCCESS);
            msg.setData(nameInfoMapper.findByType(eventType));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询统计类目名称失败。");
            ResponseUtil.buildResMsg(msg, MessageCode.FAILED, StatusCode.SYSTEM_ERROR);
        }
        return msg;
    }


    @RequestMapping(path = "realtime", method = RequestMethod.GET)
    public BaseMessage realtimeStatistics(@RequestParam(value = "eventType", required = false) final String eventType,
                                          @RequestParam(value = "uniqueName", required = false) final String uniqueName,
                                          @RequestParam(value = "begin", required = false) final String begin,
                                          @RequestParam(value = "end", required = false) final String end) {
        BaseMessage msg = new BaseMessage();
        try {
            ResponseUtil.buildResMsg(msg, MessageCode.SUCCESS, StatusCode.SUCCESS);
            msg.setData(this.statisticsService.statisticsSec(eventType, uniqueName, begin, end));
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("实时统计查询失败。");
            ResponseUtil.buildResMsg(msg, MessageCode.FAILED, StatusCode.SYSTEM_ERROR);
        }
        return msg;
    }

    @RequestMapping(path = "offline", method = RequestMethod.GET)
    public BaseMessage offlineStatistics(@RequestParam(value = "eventType", required = false) final String eventType,
                                         @RequestParam(value = "uniqueName", required = false) final String uniqueName,
                                         @RequestParam(value = "begin", required = false) final String begin,
                                         @RequestParam(value = "end", required = false) final String end,
                                         @RequestParam(value = "scope", required = false) final String scope) {
        BaseMessage msg = new BaseMessage();
        try {
            ResponseUtil.buildResMsg(msg, MessageCode.SUCCESS, StatusCode.SUCCESS);
            msg.setData(this.statisticsService.statistics(eventType, uniqueName, begin, end, Constants.DAY, scope));

            return msg;
        } catch (Exception e) {
            LOGGER.error("离线统计查询失败。");
            ResponseUtil.buildResMsg(msg, MessageCode.FAILED, StatusCode.SYSTEM_ERROR);
        }
        return msg;
    }
}
