package com.taotao.cloud.sys.biz.springboot.web;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hrhx.springboot.mongodb.service.GridFsService;
/**
 * 
 * @author duhongming
 *
 */
@RestController
@RequestMapping(value = "/gridfs")
public class GridFsController {
	
	@Autowired
	private GridFsService  gridFsService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list() throws FileNotFoundException{
		gridFsService.saveFs();
	}
	
}
