package com.taotao.cloud.sys.biz.service.business.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.taotao.cloud.sys.biz.mapper.ILogMapper;
import com.taotao.cloud.sys.biz.mapper.IVisitsMapper;
import com.taotao.cloud.sys.biz.model.entity.system.Visits;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VisitsServiceImplTest {

	@Mock
	private ILogMapper mockILogMapper;
	@Mock
	private IVisitsMapper mockIVisitsMapper;

	private VisitsServiceImpl visitsServiceImplUnderTest;

	@BeforeEach
	void setUp() {
		visitsServiceImplUnderTest = new VisitsServiceImpl(mockILogMapper, mockIVisitsMapper);
	}

	@Test
	void testSave() {
		// Setup
		// Run the test
		visitsServiceImplUnderTest.save();

		// Verify the results
	}

	@Test
	void testCount() {
		// Setup
		when(mockILogMapper.findIp("date1", "date2")).thenReturn(0L);

		// Run the test
		visitsServiceImplUnderTest.count(null);

		// Verify the results
	}

	@Test
	void testGet() {
		// Setup
		// Configure IVisitsMapper.findAllVisits(...).
		final List<Visits> visits = List.of(
				new Visits(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L,
						LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, 0, false, "date", 0L, 0L,
						"weekDay"));
		when(mockIVisitsMapper.findAllVisits("time1", "time2")).thenReturn(visits);

		// Run the test
		final Object result = visitsServiceImplUnderTest.get();

		// Verify the results
	}

	@Test
	void testGet_IVisitsMapperReturnsNoItems() {
		// Setup
		when(mockIVisitsMapper.findAllVisits("time1", "time2")).thenReturn(Collections.emptyList());

		// Run the test
		final Object result = visitsServiceImplUnderTest.get();

		// Verify the results
	}

	@Test
	void testGetChartData() {
		assertThat(visitsServiceImplUnderTest.getChartData()).isEqualTo("result");
	}
}
