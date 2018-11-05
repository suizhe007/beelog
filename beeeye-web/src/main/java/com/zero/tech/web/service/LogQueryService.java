package com.zero.tech.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zero.tech.base.constant.Constants;
import com.zero.tech.base.constant.EventType;
import com.zero.tech.base.constant.LogLevel;
import com.zero.tech.base.constant.Opt;
import com.zero.tech.base.dto.LogDto;
import com.zero.tech.base.util.DateUtil;
import com.zero.tech.data.http.HttpRequest;
import com.zero.tech.web.dto.FilterDto;
import com.zero.tech.web.dto.NoneDupeLogDto;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc
 * @date 2016-10-08 20:01:15
 */
@Service
@ConfigurationProperties(prefix = "data.es")
public class LogQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogQueryService.class);

    private String url;

    private String sql;

    private int delay;

    /**
     * 向es请求
     * @param sql
     * @return
     */
    public String query(String sql) {
        String response = HttpRequest.get(this.url, this.sql + Constants.SPACE + Constants.WHERE + Constants.SPACE + sql);
        return response;
    }

    /**
     * 根据app host interval查询日志
     * @param host
     * @param app
     * @param interval
     * @return
     */
    public Map<String, Object> getRealtimeLog(String host, String app, String keyword, int interval) {
        DateTime dateTime = new DateTime(System.currentTimeMillis()).minusSeconds(this.delay);
        String[] intervralDate = dateTime.minusMillis(interval).toString(DateUtil.YYYYMMDDHHMMSSSSS).split(Constants.SPACE);
        String[] now = dateTime.toString(DateUtil.YYYYMMDDHHMMSSSSS).split(Constants.SPACE);
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.HOST).append(Constants.EQUAL)
                .append(Constants.SINGLE_PHE).append(host).append(Constants.SINGLE_PHE).append(Constants.SPACE)
                .append(Constants.AND).append(Constants.SPACE).append(Constants.APP).append(Constants.EQUAL)
                .append(Constants.SINGLE_PHE).append(app).append(Constants.SINGLE_PHE).append(Constants.SPACE);
                if (!StringUtils.isEmpty(keyword)) {
                    sb.append(Constants.AND).append(Constants.SPACE).append(Constants.MESSAGE_MAX).append(Constants.EQUAL)
                      .append(Constants.SINGLE_PHE).append(keyword).append(Constants.SINGLE_PHE).append(Constants.SPACE);
                }
                sb.append(Constants.AND).append(Constants.SPACE).append(Constants.DAY).append(Constants.EQUAL)
                .append(Constants.SINGLE_PHE).append(intervralDate[0]).append(Constants.SINGLE_PHE).append(Constants.SPACE)
                .append(Constants.AND).append(Constants.SPACE).append(Constants.TIME).append(Constants.GEQUAL)
                .append(Constants.SINGLE_PHE).append(intervralDate[1]).append(Constants.SINGLE_PHE).append(Constants.SPACE)
                .append(Constants.AND).append(Constants.SPACE).append(Constants.TIME).append(Constants.LEQUAL)
                .append(Constants.SINGLE_PHE).append(now[1]).append(Constants.SINGLE_PHE)
                .append(Constants.NANO_TIME_ORDER_BY_ASC);

        return this.parseEsResponse(this.query(sb.toString()));
    }

    /**
     * 根据sql进行查询
     * @param sql
     * @return
     */
    public Map<String, Object> getQueryLog(String sql) {
        return this.parseEsResponse(this.query(sql));
    }

    /**
     * 根据filterDto查询日志
     * @param filterDto
     * @return
     */
    public Map<String, Object> getHistoryLog(FilterDto filterDto) {
        return this.parseEsResponse(this.query(filterDto.buildSql()));
    }

    /**
     * 解析log
     * @param response
     * @return
     */
    private Map<String, Object> parseEsResponse(String response) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == response) {
            // 错误
            return null;
        } else {
            JSONObject json = JSON.parseObject(response);
            List<NoneDupeLogDto> logs = new ArrayList<NoneDupeLogDto>();
            JSONObject hits = json.getJSONObject("hits");
            Iterator<Object> iterator = hits.getJSONArray("hits").iterator();
            while (iterator.hasNext()) {
                LogDto logDto = JSON.parseObject(((JSONObject) iterator.next()).getString("_source"), LogDto.class);
                logs.add(new NoneDupeLogDto(logDto.getNanoTime(), logDto.toString()));
            }
            result.put("logs", logs);
            result.put("total", hits.getInteger("total"));
            result.put("current", logs.size());
            return result;
        }
    }

    /**
     * 获取所有的日志级别
     * @return
     */
    public List<String> getLogLevel() {
        List<String> levels = new ArrayList<String>();
        for (LogLevel logLevel : LogLevel.values()) {
            levels.add(logLevel.label());
        }
        return levels;
    }

    /**
     * 获取所有的日志事件
     * @return
     */
    public List<String> getEventType() {
        List<String> eventTypes = new ArrayList<String>();
        for (EventType eventType : EventType.values()) {
            eventTypes.add(eventType.symbol());
        }
        return eventTypes;
    }

    /**
     * 获取所有的操作符
     * @return
     */
    public List<String> getOpt() {
        List<String> opts = new ArrayList<String>();
        for (Opt opt : Opt.values()) {
            opts.add(opt.symbol());
        }
        return opts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
