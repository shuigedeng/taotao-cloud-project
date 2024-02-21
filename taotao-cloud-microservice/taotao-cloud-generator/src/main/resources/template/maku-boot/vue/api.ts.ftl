import service from '@/utils/request'

export const use${FunctionName}Api = (id: number) => {
	return service.get('/${moduleName}/${functionName}/' + id)
}

export const use${FunctionName}SubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/${moduleName}/${functionName}', dataForm)
	} else {
		return service.post('/${moduleName}/${functionName}', dataForm)
	}
}