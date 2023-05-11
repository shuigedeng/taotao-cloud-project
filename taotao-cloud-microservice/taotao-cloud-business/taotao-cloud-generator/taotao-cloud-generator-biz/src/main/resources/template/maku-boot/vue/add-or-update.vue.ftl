<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
	    <#list formList as field>
			<#if field.formType == 'text'>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-input v-model="dataForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
				</el-form-item>
			<#elseif field.formType == 'textarea'>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-input type="textarea" v-model="dataForm.${field.attrName}"></el-input>
				</el-form-item>
			<#elseif field.formType == 'editor'>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-input type="textarea" v-model="dataForm.${field.attrName}"></el-input>
				</el-form-item>
			<#elseif field.formType == 'select'>
				<#if field.formDict??>
					<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
						<fast-select v-model="dataForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}"></fast-select>
					</el-form-item>
				<#else>
					<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
						<el-select v-model="dataForm.${field.attrName}" placeholder="请选择">
							<el-option label="请选择" value="0"></el-option>
						</el-select>
					</el-form-item>
				</#if>
			<#elseif field.formType == 'radio'>
				<#if field.formDict??>
					<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
						<fast-radio-group v-model="dataForm.${field.attrName}" dict-type="${field.formDict}"></fast-radio-group>
					</el-form-item>
				<#else>
					<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
						<el-radio-group v-model="dataForm.${field.attrName}">
							<el-radio :label="0">启用</el-radio>
							<el-radio :label="1">禁用</el-radio>
						</el-radio-group>
					</el-form-item>
				</#if>
			<#elseif field.formType == 'checkbox'>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-checkbox-group v-model="dataForm.${field.attrName}">
						<el-checkbox label="启用" name="type"></el-checkbox>
						<el-checkbox label="禁用" name="type"></el-checkbox>
					</el-checkbox-group>
				</el-form-item>
			<#elseif field.formType == 'date'>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-date-picker type="date" placeholder="${field.fieldComment!}" v-model="dataForm.${field.attrName}"></el-date-picker>
				</el-form-item>
			<#elseif field.formType == 'datetime'>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-date-picker type="datetime" placeholder="${field.fieldComment!}" v-model="dataForm.${field.attrName}"></el-date-picker>
				</el-form-item>
			<#else>
				<el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
					<el-input v-model="dataForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
				</el-form-item>
			</#if>
	    </#list>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { use${FunctionName}Api, use${FunctionName}SubmitApi } from '@/api/${moduleName}/${functionName}'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	<#list fieldList as field>
	${field.attrName}: ''<#sep>,
	</#list>
})

const init = (id?: number) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	if (id) {
		get${FunctionName}(id)
	}
}

const get${FunctionName} = (id: number) => {
	use${FunctionName}Api(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	<#list formList as field>
	<#if field.formRequired>
	${field.attrName}: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]<#sep>,
	</#if>
	</#list>
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		use${FunctionName}SubmitApi(dataForm).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
				onClose: () => {
					visible.value = false
					emit('refreshDataList')
				}
			})
		})
	})
}

defineExpose({
	init
})
</script>
