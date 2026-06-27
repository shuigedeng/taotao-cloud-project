#!/usr/bin/env python3
"""
taotao-cloud-project DDD CRUD 代码生成器

生成 DDD 分层架构的标准 CRUD 代码，支持任意业务模块。

用法:
  python generate-crud.py --entity=Order --module=order
  python generate-crud.py --entity=Product --module=goods --output=taotao-cloud-microservice/taotao-cloud-business/taotao-cloud-goods
"""

import argparse
import os
import re
from pathlib import Path


def to_camel(snake_str):
    """snake_case -> PascalCase"""
    return "".join(x.capitalize() for x in snake_str.lower().split("_"))


def to_snake(pascal_str):
    """PascalCase -> snake_case"""
    return re.sub(r"(?<!^)(?=[A-Z])", "_", pascal_str).lower()


TEMPLATES = {
    # === DOMAIN 层 ===
    "domain/aggregate/{Entity}Agg.java": """package com.taotao.cloud.{module}.domain.{entity}.aggregate;

import jakarta.persistence.*;
import java.util.Objects;

@AggregateRoot
@Entity
@Table(name = "{table_name}")
public class {Entity}Agg {{

    @Id
    private Long id;

    protected {Entity}Agg() {{}}

    public static {Entity}Agg create() {{
        {Entity}Agg agg = new {Entity}Agg();
        return agg;
    }}

    public Long getId() {{ return id; }}
}}
""",

    "domain/valobj/{Entity}Status.java": """package com.taotao.cloud.{module}.domain.{entity}.valobj;

public enum {Entity}Status {{
    PENDING("{desc_pending}"),
    PROCESSED("{desc_processed}"),
    CLOSED("{desc_closed}");

    private final String description;

    {Entity}Status(String description) {{
        this.description = description;
    }}

    public String getDescription() {{ return description; }}
}}
""",

    "domain/event/{Entity}CreatedEvent.java": """package com.taotao.cloud.{module}.domain.{entity}.event;

import com.taotao.cloud.{module}.domain.common.event.DomainEvent;
import java.time.LocalDateTime;

public class {Entity}CreatedEvent extends DomainEvent {{
    private final Long {entity}Id;
    private final LocalDateTime occurredAt;

    public {Entity}CreatedEvent(Long {entity}Id) {{
        this.{entity}Id = {entity}Id;
        this.occurredAt = LocalDateTime.now();
    }}

    public Long get{Entity}Id() {{ return {entity}Id; }}
    public LocalDateTime getOccurredAt() {{ return occurredAt; }}
}}
""",

    "domain/repository/{Entity}DomainRepository.java": """package com.taotao.cloud.{module}.domain.{entity}.repository;

import com.taotao.cloud.{module}.domain.{entity}.aggregate.{Entity}Agg;
import java.util.Optional;

public interface {Entity}DomainRepository {{
    Optional<{Entity}Agg> findById(Long id);
    {Entity}Agg save({Entity}Agg {entity}Agg);
    void deleteById(Long id);
}}
""",

    # === APPLICATION 层 ===
    "application/dto/command/Create{Entity}Command.java": """package com.taotao.cloud.{module}.application.dto.{entity}.command;

import jakarta.validation.constraints.NotBlank;

public record Create{Entity}Command() {{
}}
""",

    "application/dto/command/Update{Entity}Command.java": """package com.taotao.cloud.{module}.application.dto.{entity}.command;

import jakarta.validation.constraints.NotNull;

public record Update{Entity}Command(
    @NotNull Long id
) {{
}}
""",

    "application/dto/result/{Entity}Result.java": """package com.taotao.cloud.{module}.application.dto.{entity}.result;

public record {Entity}Result(
    Long id,
    String status
) {{
    public static {Entity}Result fromDomain(com.taotao.cloud.{module}.domain.{entity}.aggregate.{Entity}Agg agg) {{
        return new {Entity}Result(
            agg.getId(),
            null
        );
    }}
}}
""",

    "application/service/command/{Entity}CommandService.java": """package com.taotao.cloud.{module}.application.service.{entity}.command;

import com.taotao.cloud.{module}.application.dto.{entity}.command.Create{Entity}Command;
import com.taotao.cloud.{module}.application.dto.{entity}.command.Update{Entity}Command;
import com.taotao.cloud.{module}.application.dto.{entity}.result.{Entity}Result;

public interface {Entity}CommandService {{
    {Entity}Result create(Create{Entity}Command command);
    {Entity}Result update(Update{Entity}Command command);
    void delete(Long id);
}}
""",

    # === INFRASTRUCTURE 层 ===
    "infrastructure/persistent/po/{Entity}Po.java": """package com.taotao.cloud.{module}.infrastructure.persistent.{entity}.po;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "{table_name}")
public class {Entity}Po {{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    // 基础字段
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    private Boolean isDeleted;
    private Long tenantId;
    private Integer version;

    public {Entity}Po() {{}}

    public Long getId() {{ return id; }}
    public void setId(Long id) {{ this.id = id; }}
    public String getStatus() {{ return status; }}
    public void setStatus(String status) {{ this.status = status; }}
    // ... getters/setters
}}
""",

    # === INTERFACES 层 ===
    "interfaces/controller/buyer/{Entity}BuyerController.java": """package com.taotao.cloud.{module}.interfaces.controller.buyer;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.webagg.controller.BusinessController;
import com.taotao.cloud.{module}.application.dto.{entity}.command.Create{Entity}Command;
import com.taotao.cloud.{module}.application.dto.{entity}.result.{Entity}Result;
import com.taotao.cloud.{module}.application.service.{entity}.command.{Entity}CommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "买家端-{Entity}API")
@RequestMapping("/buyer/{entity}/{entity}")
public class {Entity}BuyerController extends BusinessController {{

    private final {Entity}CommandService {entity}CommandService;

    @PostMapping
    @Operation(summary = "创建{Entity}")
    public Result<{Entity}Result> create(@Valid @RequestBody Create{Entity}Command command) {{
        return Result.success({entity}CommandService.create(command));
    }}

    @DeleteMapping("/{{id}}")
    @Operation(summary = "删除{Entity}")
    public Result<Void> delete(@PathVariable Long id) {{
        {entity}CommandService.delete(id);
        return Result.success();
    }}
}}
""",
}


def generate(args):
    entity_pascal = to_camel(args.entity)
    entity_snake = to_snake(entity_pascal)
    module = args.module
    table_name = f"{module}_{entity_snake}"
    base_dir = Path(args.output) if args.output else Path(".")

    # 中文描述
    defaults = {
        "desc_pending": "待处理",
        "desc_processed": "已处理",
        "desc_closed": "已关闭",
    }

    created = []
    for template_path, template_content in TEMPLATES.items():
        content = template_content.format(
            Entity=entity_pascal,
            entity=entity_snake,
            module=module,
            table_name=table_name,
            **defaults,
        )

        rel_path = template_path.replace("{Entity}", entity_pascal).replace("{module}", module).replace("{entity}", entity_snake)
        abs_path = base_dir / rel_path

        abs_path.parent.mkdir(parents=True, exist_ok=True)

        if abs_path.exists():
            print(f"  ⏭️  已存在: {rel_path}")
            continue

        abs_path.write_text(content.lstrip("\n"), encoding="utf-8")
        print(f"  ✅ 生成: {rel_path}")
        created.append(rel_path)

    print(f"\n📊 共生成 {len(created)} 个文件")
    return created


def main():
    parser = argparse.ArgumentParser(description="DDD CRUD 代码生成器")
    parser.add_argument("--entity", required=True, help="实体名（PascalCase，如 Order）")
    parser.add_argument("--module", default="order", help="子模块名（如 order）")
    parser.add_argument("--output", default=None, help="输出目录（默认当前目录）")
    args = parser.parse_args()

    print(f"🔨 生成 {args.entity} CRUD 代码 (module: {args.module})")
    print("=" * 40)
    generate(args)
    print("=" * 40)
    print("✅ 生成完成")


if __name__ == "__main__":
    main()
