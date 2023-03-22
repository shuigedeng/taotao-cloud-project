package com.taotao.cloud.log.biz.shortlink.job;

import com.taotao.cloud.log.api.api.enums.ShortLinkDomainTypeEnum;
import com.taotao.cloud.log.api.api.request.ShortLinkCreateRequest;
import com.taotao.cloud.log.biz.shortlink.rpc.ShortLinkServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class FakeRequestJobHandler extends IJobHandler {

//    @Resource
//    private ShortLinkService shortLinkService;

	@Resource
	private ShortLinkServiceImpl shortLinkService;

	@XxlJob(value = "fakeRequest")
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("XXL-JOB, fakeRequest.");
		Random random = new Random();

		List<ShortLinkCreateRequest> requests = new ArrayList<>(5000);
		for (int i = 0; i < 5000; i++) {
			String url =
				"https://estate.zc.net/details/" + i + "/estateid/" + random.nextInt(2000000000)
					+ "?time=" + System.currentTimeMillis();
			ShortLinkCreateRequest createRequest = ShortLinkCreateRequest.builder()
				.title("房源-" + random.nextInt(100000000))
				.originalUrl(url)
				.domainType(ShortLinkDomainTypeEnum.ORIGIN.getCode())
				.groupId((long) (random.nextInt(4) + 1))
				.domainId(1L)
				.accountId(1L)
				.expired(LocalDate.now().plusDays(random.nextInt(10000)))
				.build();

			requests.add(createRequest);
		}

		try {
			shortLinkService.tempBatchCreateCode(requests);
		} catch (Exception e) {
			log.warn("FakeRequestJobHandler: e -> {}", e.toString());
		}

		return SUCCESS;
	}

}
