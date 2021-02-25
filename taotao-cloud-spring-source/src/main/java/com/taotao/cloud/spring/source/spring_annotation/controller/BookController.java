package com.taotao.cloud.spring.source.spring_annotation.controller;

import com.taotao.cloud.spring.source.spring_annotation.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;

}
