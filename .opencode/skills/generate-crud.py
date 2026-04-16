
## 5. 技能脚本

**`.opencode/skills/generate-crud.py`**
```python
#!/usr/bin/env python3
"""
生成标准 CRUD 代码
用法: /skills generate-crud --entity User
"""

import sys
import re
from pathlib import Path

def to_camel_case(snake_str):
    return ''.join(x.capitalize() for x in snake_str.lower().split('_'))

def generate_crud(entity_name):
    """生成 Controller、Service、Repository、DTO、Mapper"""
    entity_lower = entity_name.lower()
    entity_upper = to_camel_case(entity_name)

    templates = {
        f"{entity_upper}Controller.java": f"""
@RestController
@RequestMapping("/api/v1/{entity_lower}s")
@Tag(name = "{entity_upper}", description = "{entity_upper}管理接口")
@RequiredArgsConstructor
public class {entity_upper}Controller {{
    private final {entity_upper}Service {entity_lower}Service;

    @PostMapping
    @Operation(summary = "创建{entity_upper}")
    public ResponseEntity<{entity_upper}Response> create(@Valid @RequestBody {entity_upper}Request request) {{
        return ResponseEntity.status(HttpStatus.CREATED)
            .body({entity_lower}Service.create(request));
    }}

    @GetMapping("/{{id}}")
    @Operation(summary = "查询{entity_upper}")
    public ResponseEntity<{entity_upper}Response> findById(@PathVariable Long id) {{
        return ResponseEntity.ok({entity_lower}Service.findById(id));
    }}

    @PutMapping("/{{id}}")
    @Operation(summary = "更新{entity_upper}")
    public ResponseEntity<{entity_upper}Response> update(
            @PathVariable Long id,
            @Valid @RequestBody {entity_upper}Request request) {{
        return ResponseEntity.ok({entity_lower}Service.update(id, request));
    }}

    @DeleteMapping("/{{id}}")
    @Operation(summary = "删除{entity_upper}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {{
        {entity_lower}Service.delete(id);
        return ResponseEntity.noContent().build();
    }}

    @GetMapping
    @Operation(summary = "分页查询{entity_upper}")
    public ResponseEntity<Page<{entity_upper}Response>> page(
            @PageableDefault(size = 20) Pageable pageable) {{
        return ResponseEntity.ok({entity_lower}Service.page(pageable));
    }}
}}
""",
        # 其他模板类似...
    }

    base_dir = Path("src/main/java/com/company/project")
    for filename, content in templates.items():
        filepath = base_dir / filename
        filepath.write_text(content)
        print(f"✅ 生成: {filepath}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("用法: python generate-crud.py <EntityName>")
        sys.exit(1)
    generate_crud(sys.argv[1])
