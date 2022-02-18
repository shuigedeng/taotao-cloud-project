package com.taotao.cloud.web.docx4j.output.builder.sheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@link XSSFWorkbook} dsl
 */
public class DslWorkbook {
	private final Workbook workbook;
    /**
     * sheet缓存
     */
    private final Map<String, DslSheet> sheets;

    DslWorkbook(Workbook workbook) {
        if (!(workbook instanceof HSSFWorkbook)
            && !(workbook instanceof XSSFWorkbook)
            && !(workbook instanceof SXSSFWorkbook)) {
            // HSSFWorkbook 限制65535行
            // XSSFWorkbook 最多104万行 可能会导致OOM
            // SXSSFWorkbook 硬盘换空间 不会导致OOM
            throw new IllegalArgumentException("exel dsl only support HSSFWorkbook & XSSFWorkbook & SXSSFWorkbook");
        }

        this.workbook = workbook;
        this.sheets = new HashMap<>(4);
    }

    /**
     * 添加单个sheet
     * @param name     sheet名称
     * @param consumer sheet消费
     * @return {@link DslWorkbook}
     */
    public DslWorkbook sheet(String name, Consumer<DslSheet> consumer) {
        DslSheet sheet = this.getOrCreateSheet(name);
        consumer.accept(sheet);
        sheet.doAutoColumnFit();

        return this;
    }

    /**
     * 添加单个sheet并自动追加sheet名称
     * @param consumer sheet消费者
     * @return {@link DslWorkbook}
     */
    public DslWorkbook sheet(Consumer<DslSheet> consumer) {
        return this.sheet(String.format("Sheet%d", this.sheets.size() + 1), consumer);
    }

    /**
     * 批量添加sheet
     * @param iterable          迭代内容
     * @param sheetNameSupplier 根据迭代内容获得sheet名称
     * @param consumer          每个sheet的消费
     * @param <T>               迭代内容类型
     * @return {@link DslWorkbook}
     */
    public <T> DslWorkbook sheets(Iterable<T> iterable, Function<T, String> sheetNameSupplier,
                                  BiConsumer<T, DslSheet> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(it -> this.sheet(sheetNameSupplier.apply(it), s -> consumer.accept(it, s)));
        }

        return this;
    }

    /**
     * 根据sheet名称获取表格中的sheet或者创建新的sheet
     * @param name sheet名称
     * @return {@link DslSheet}
     */
    protected DslSheet getOrCreateSheet(String name) {
        String sheetName = WorkbookUtil.createSafeSheetName(name);

        return
            // 先通过缓存获取sheet
            Optional.ofNullable(this.sheets.get(sheetName))
                // 若缓存不存在则创建新sheet并缓存
                .orElseGet(() -> {
                    DslSheet sheet = new DslSheet(this.workbook.createSheet(sheetName));
                    this.sheets.put(sheetName, sheet);

                    return sheet;
                });
    }
}
