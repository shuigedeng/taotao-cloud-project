package com.taotao.cloud.bigdata.seatunnel;

import org.apache.seatunnel.api.table.type.BasicType;
import org.apache.seatunnel.api.table.type.SeaTunnelDataType;
import org.apache.seatunnel.transform.sql.zeta.ZetaUDF;

import java.util.List;

@AutoService(ZetaUDF.class)
public class ExampleUDF implements ZetaUDF {
	@Override
	public String functionName() {
		return "EXAMPLE";
	}

	@Override
	public SeaTunnelDataType<?> resultType(List<SeaTunnelDataType<?>> argsType) {
		return BasicType.STRING_TYPE;
	}

	@Override
	public Object evaluate(List<Object> args) {
		String arg = (String) args.get(0);
		if (arg == null) return null;
		return "UDF: " + arg;
	}
}
