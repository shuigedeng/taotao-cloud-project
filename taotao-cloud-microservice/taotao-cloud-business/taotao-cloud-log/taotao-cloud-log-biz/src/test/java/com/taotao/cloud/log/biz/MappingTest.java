/**
 * Copyright 2012-2017 Gunnar Morling (http://www.gunnarmorling.de/)
 * and/or other contributors as indicated by the @authors tag. See the
 * copyright.txt file in the distribution for a full listing of all
 * contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.log.biz;

/*-
 * #%L
 * protobuf-usage
 * %%
 * Copyright (C) 2019 - 2020 Entur
 * %%
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import com.taotao.cloud.log.api.grpc.EnumPostfixOverrideValuesDTO;
import com.taotao.cloud.log.api.grpc.UserDTO;
import com.taotao.cloud.log.biz.grpc.*;
import org.junit.jupiter.api.Test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;


public class MappingTest {

	@Test
	public void testMapAllFields() throws InvalidProtocolBufferException {
		User user = generateUser();

		UserDTO dto = UserMapper.INSTANCE.map(user);
		UserDTO deserialized = UserDTO.parseFrom(dto.toByteArray());
		User back = UserMapper.INSTANCE.map(deserialized);

		assertUser(user, back);
	}

	@Test
	public void testNulls() throws InvalidProtocolBufferException {
		User user = new User();
		user.setEmail("test");

		UserDTO dto = UserMapper.INSTANCE.map(user);
		UserDTO deserialized = UserDTO.parseFrom(dto.toByteArray());
		User back = UserMapper.INSTANCE.map(deserialized);

		assertEquals(null, back.getId());
		assertEquals("test", back.getEmail());
		assertEquals(null, back.getV16());
	}

	@Test
	public void testMaps() throws InvalidProtocolBufferException {
		User user = new User();
		user.getStringMap().put("key1", "value1");
		user.getStringMap().put("key2", "value2");
		user.getEntityMap().put("entityKey1", new Department("DepartmentName1"));
		user.getEntityMap().put("entityKey2", new Department("DepartmentName2"));

		UserDTO dto = UserMapper.INSTANCE.map(user);
		UserDTO deserialized = UserDTO.parseFrom(dto.toByteArray());
		User back = UserMapper.INSTANCE.map(deserialized);

		assertTrue(back.getStringMap().containsKey("key1"));
		assertEquals("value1", back.getStringMap().get("key1"));
		assertTrue(back.getStringMap().containsKey("key2"));
		assertEquals("value2", back.getStringMap().get("key2"));

		assertTrue(back.getEntityMap().containsKey("entityKey1"));
		assertEquals("DepartmentName1", back.getEntityMap().get("entityKey1").getName());
		assertTrue(back.getEntityMap().containsKey("entityKey2"));
		assertEquals("DepartmentName2", back.getEntityMap().get("entityKey2").getName());

	}

	private User generateUser() {
		User user = new User();

		user.setId(null);
		user.setEmail("test");
		user.getMainDepartments().add(new Department("SALES"));
		user.getDepartments().add(new Department("AFTER_MARKET"));

		user.setV1(1.0);
		user.setV2(2);
		user.setV3(3);
		user.setV4(4);
		user.setV5(5);
		user.setV6(6);
		user.setV7(7);
		user.setV8(8);
		user.setV9(9);
		user.setV10(10);
		user.setV11(11);
		user.setV12(12);
		user.setV13(true);
		user.setV14("some string");
		user.setV15(ByteString.copyFromUtf8("byte string"));
		user.setV16(Status.STARTED);

		user.setRv1(Arrays.asList(1.0));
		user.setRv2(Arrays.asList(2.0f));
		user.setRv3(Arrays.asList(3));
		user.setRv4(Arrays.asList(4L));
		user.setRv5(Arrays.asList(5));
		user.setRv6(Arrays.asList(6L));
		user.setRv7(Arrays.asList(7));
		user.setRv8(Arrays.asList(8L));
		user.setRv9(Arrays.asList(9));
		user.setRv10(Arrays.asList(10L));
		user.setRv11(Arrays.asList(11));
		user.setRv12(Arrays.asList(12L));
		user.setRv13(Arrays.asList(true));
		user.setRv14(Arrays.asList("some string"));
		user.setRv15(Arrays.asList(ByteString.copyFromUtf8("some byte string")));
		user.setRv16(Arrays.asList(Status.STARTED));

		MultiNumber mm = new MultiNumber();
		mm.setNumber(1);

		user.setMultiNumber(mm);
		user.setRepMultiNumbers(Arrays.asList(mm));

		user.setPoliceDepartment(new Department("POLICE"));

		return user;
	}

	private void assertUser(User orig, User back) {
		assertEquals(orig.getId(), back.getId());
		assertEquals(orig.getEmail(), back.getEmail());

		assertEquals(orig.getMainDepartments().size(), back.getMainDepartments().size());
		assertEquals(orig.getMainDepartments().get(0).getName(), back.getMainDepartments().get(0).getName());

		assertEquals(orig.getDepartments().size(), back.getDepartments().size());
		assertEquals(orig.getDepartments().get(0).getName(), back.getDepartments().get(0).getName());

		assertEquals(orig.getV1(), back.getV1(), 0.1);
		assertEquals(orig.getV2(), back.getV2(), 0.1);
		assertEquals(orig.getV3(), back.getV3());
		assertEquals(orig.getV4(), back.getV4());
		assertEquals(orig.getV5(), back.getV5());
		assertEquals(orig.getV6(), back.getV6());
		assertEquals(orig.getV7(), back.getV7());
		assertEquals(orig.getV8(), back.getV8());
		assertEquals(orig.getV9(), back.getV9());
		assertEquals(orig.getV10(), back.getV10());
		assertEquals(orig.getV11(), back.getV11());
		assertEquals(orig.getV12(), back.getV12());

		assertEquals(orig.isV13(), back.isV13());

		assertEquals(orig.getV14(), back.getV14());
		assertEquals(orig.getV15(), back.getV15());
		assertEquals(orig.getV16(), back.getV16());
		assertEquals(orig.getV16(), back.getV16());

		assertEquals(orig.getRv1().size(), back.getRv1().size());
		assertEquals(orig.getRv1().get(0), back.getRv1().get(0), 0.1);

		assertEquals(orig.getRv2().size(), back.getRv2().size());
		assertEquals(orig.getRv2().get(0), back.getRv2().get(0), 0.1);

		assertEquals(orig.getRv3().size(), back.getRv3().size());
		assertEquals(orig.getRv3().get(0), back.getRv3().get(0));

		assertEquals(orig.getRv4().size(), back.getRv4().size());
		assertEquals(orig.getRv4().get(0), back.getRv4().get(0));

		assertEquals(orig.getRv5().size(), back.getRv5().size());
		assertEquals(orig.getRv5().get(0), back.getRv5().get(0));

		assertEquals(orig.getRv6().size(), back.getRv6().size());
		assertEquals(orig.getRv6().get(0), back.getRv6().get(0));

		assertEquals(orig.getRv7().size(), back.getRv7().size());
		assertEquals(orig.getRv7().get(0), back.getRv7().get(0));

		assertEquals(orig.getRv8().size(), back.getRv8().size());
		assertEquals(orig.getRv8().get(0), back.getRv8().get(0));

		assertEquals(orig.getRv9().size(), back.getRv9().size());
		assertEquals(orig.getRv9().get(0), back.getRv9().get(0));

		assertEquals(orig.getRv10().size(), back.getRv10().size());
		assertEquals(orig.getRv10().get(0), back.getRv10().get(0));

		assertEquals(orig.getRv11().size(), back.getRv11().size());
		assertEquals(orig.getRv11().get(0), back.getRv11().get(0));

		assertEquals(orig.getRv12().size(), back.getRv12().size());
		assertEquals(orig.getRv12().get(0), back.getRv12().get(0));

		assertEquals(orig.getRv13().size(), back.getRv13().size());
		assertEquals(orig.getRv13().get(0), back.getRv13().get(0));

		assertEquals(orig.getRv14().size(), back.getRv14().size());
		assertEquals(orig.getRv14().get(0), back.getRv14().get(0));

		assertEquals(orig.getRv15().size(), back.getRv15().size());
		assertEquals(orig.getRv15().get(0), back.getRv15().get(0));

		assertEquals(orig.getRv16().size(), back.getRv16().size());
		assertEquals(orig.getRv16().get(0), back.getRv16().get(0));

		assertNotNull(back.getPoliceDepartment());
		assertEquals(back.getPoliceDepartment().getName(), "POLICE");
		// WILL FAIL: Mapstruct cannot handle presence checker on oneOfs : assertNull(back.getFireDepartment());

	}

	@Test
	public void testEnumPostfixOverride() {
		EnumPostfixOverrideValues enumValue = EnumPostfixOverrideValues.TWO;
		EnumPostfixOverrideValuesDTO dto = EnumPostfixOverrideMapper.INSTANCE.map(enumValue);
		EnumPostfixOverrideValues back = EnumPostfixOverrideMapper.INSTANCE.map(dto);

		assertEquals(enumValue, back);
	}
}
