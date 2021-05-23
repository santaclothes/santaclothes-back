package com.pinocchio.santaclothes.apiserver.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon;
import com.pinocchio.santaclothes.apiserver.entity.Image;
import com.pinocchio.santaclothes.apiserver.service.AnalysisService;
import com.pinocchio.santaclothes.apiserver.service.ImageService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	public AdminController(ImageService imageService, AnalysisService analysisService) {
		this.imageService = imageService;
		this.analysisService = analysisService;
	}

	private final ImageService imageService; //불필요하게 변경 가능한 건 지워야한다.
	private final AnalysisService analysisService;
	private final static Logger LOG = Logger.getGlobal();

	@GetMapping("")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		List<Image> imageList = imageService.findAllCareLabelsToProcess();
		modelAndView.addObject("imageList", imageList);
		return modelAndView;
	}

	@GetMapping("/analyze/{imageId}")
	public ModelAndView getPage(@PathVariable long imageId) {
		LOG.warning(Long.toString(imageId));
		ModelAndView modelAndView = new ModelAndView("analyze");
		Image image = imageService.getCareLabelById(imageId);
		String imageURL = image.getFilePath();
		modelAndView.addObject("imageURL", imageURL);
		return modelAndView;
	}

	@PostMapping("/analyze/{imageId}")
	public ModelAndView analyze(@PathVariable long imageId, CareLabelIcon careLabel) {
		ModelAndView test = new ModelAndView("redirect:/admin");
		analysisService.analysis(imageId, careLabel);
		return test;
	}
}
