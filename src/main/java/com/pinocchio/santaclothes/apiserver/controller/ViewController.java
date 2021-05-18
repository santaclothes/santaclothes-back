package com.pinocchio.santaclothes.apiserver.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pinocchio.santaclothes.apiserver.entity.Image;
import com.pinocchio.santaclothes.apiserver.service.ImageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor //자동으로 생성자 주입
public class ViewController {
	private final ImageService imageService; //불필요하게 변경 가능한 건 지워야한다.

	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		List<Image> imageList = imageService.findLabels();
		modelAndView.addObject("imageList", imageList);
		return modelAndView;
	}

	@GetMapping("/analyze/index")
	public ModelAndView GetPage(@RequestParam("imageId") Integer imageId){
		ModelAndView modelAndView = new ModelAndView("analyze");
		Image image = imageService.findLabel(imageId);
		String imageURL = image.getFilePath();
		modelAndView.addObject("imageURL", imageURL);
		return modelAndView;
	}
}
