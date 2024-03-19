package com.taotao.cloud.seatunnel;

import com.google.auto.service.AutoService;
import java.util.List;
import org.apache.seatunnel.api.table.type.BasicType;
import org.apache.seatunnel.api.table.type.SeaTunnelDataType;
import org.apache.seatunnel.transform.sql.zeta.ZetaUDF;

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
