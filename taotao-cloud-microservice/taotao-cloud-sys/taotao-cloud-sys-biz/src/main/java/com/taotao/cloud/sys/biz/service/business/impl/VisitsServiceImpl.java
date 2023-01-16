package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.sys.biz.mapper.ILogMapper;
import com.taotao.cloud.sys.biz.mapper.IVisitsMapper;
import com.taotao.cloud.sys.biz.model.entity.system.Visits;
import com.taotao.cloud.sys.biz.service.business.IVisitsService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * VisitsServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:25:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitsServiceImpl extends ServiceImpl<IVisitsMapper, Visits> implements
		IVisitsService {

	private final ILogMapper ILogMapper;

	private final IVisitsMapper IVisitsMapper;

	public VisitsServiceImpl(ILogMapper ILogMapper, IVisitsMapper IVisitsMapper) {
		this.ILogMapper = ILogMapper;
		this.IVisitsMapper = IVisitsMapper;
	}


	@Override
	public void save() {
		LocalDate localDate = LocalDate.now();
		Visits visits = this.getOne(new LambdaQueryWrapper<Visits>()
				.eq(Visits::getDate, localDate.toString()));
		if (visits == null) {
			visits = new Visits();
			visits.setWeekDay(String.valueOf(DateUtils.getCurrentWeek()));
			visits.setPvCounts(1L);
			visits.setIpCounts(1L);
			visits.setDate(localDate.toString());
			this.save(visits);
		}
	}

	@Override
	public void count(HttpServletRequest request) {
		LocalDate localDate = LocalDate.now();
		Visits visits = this.getOne(new LambdaQueryWrapper<Visits>()
				.eq(Visits::getDate, localDate.toString()));
		if (visits == null) {
			visits = new Visits();
			visits.setPvCounts(1L);
		} else {
			visits.setPvCounts(visits.getPvCounts() + 1);
		}
		long ipCounts = ILogMapper.findIp(localDate.toString(), localDate.plusDays(1).toString());
		visits.setIpCounts(ipCounts);
		this.saveOrUpdate(visits);
	}

	@Override
	public Object get() {
		Map<String, Object> map = new HashMap<>(4);
		LocalDate localDate = LocalDate.now();
		Visits visits = this.getOne(new LambdaQueryWrapper<Visits>()
				.eq(Visits::getDate, localDate.toString()));
		List<Visits> list = IVisitsMapper.findAllVisits(localDate.minusDays(6).toString(),
				localDate.plusDays(1).toString());

		long recentVisits = 0, recentIp = 0;
		for (Visits data : list) {
			recentVisits += data.getPvCounts();
			recentIp += data.getIpCounts();
		}
		map.put("newVisits", visits.getPvCounts());
		map.put("newIp", visits.getIpCounts());
		map.put("recentVisits", recentVisits);
		map.put("recentIp", recentIp);
		return map;
	}

	@Override
	public Object getChartData() {
		Map<String, Object> map = new HashMap<>(3);
//        LocalDate localDate = LocalDate.now();
//        List<Visits> list = visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
//        map.put("weekDays",list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
//        map.put("visitsData",list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
//        map.put("ipData",list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
		return map;
	}
}
