package com.pinocchio.santaclothes.apiserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pinocchio.santaclothes.apiserver.dto.AnalyzeDto;
import com.pinocchio.santaclothes.apiserver.entity.Image;
import com.pinocchio.santaclothes.apiserver.service.ImageService;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
	@Autowired
	public AdminViewController(ImageService imageService) {
		this.imageService = imageService;
	}

	private final ImageService imageService; //불필요하게 변경 가능한 건 지워야한다.

	@GetMapping(value = "/")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		List<Image> imageList = imageService.findAllCareLabelsToProcess();
		modelAndView.addObject("imageList", imageList);
		return modelAndView;
	}

	@GetMapping("/analyze/{imageId}}")
	public ModelAndView getPage(@PathVariable long imageId) {
		ModelAndView modelAndView = new ModelAndView("analyze");
		Image image = imageService.getCareLabelById(imageId);
		String imageURL = image.getFilePath();
		modelAndView.addObject("imageURL", imageURL);
		return modelAndView;
	}

	@PostMapping("/analyze/{imageId}")
	@ResponseBody
	public ModelAndView getData(@PathVariable long imageId, @ModelAttribute("img") AnalyzeDto analyzeDto){
		ModelAndView modelAndView = new ModelAndView("analyzetest");
		modelAndView.addObject("data", analyzeDto);
		return modelAndView;
	}
}
