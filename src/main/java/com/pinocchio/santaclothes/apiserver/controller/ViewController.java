package com.pinocchio.santaclothes.apiserver.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pinocchio.santaclothes.apiserver.entity.Image;
import com.pinocchio.santaclothes.apiserver.service.ImageService;

@Controller
public class ViewController {
	List<Image> imageList = new ArrayList<>();

	@Autowired
	private ImageService imageService;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		imageList = imageService.findLabels();
		modelAndView.addObject("imageList", imageList);
		return modelAndView;
	}

	@RequestMapping(path="/analyze/index", method = RequestMethod.GET)
	public String GetPage(Model model, @RequestParam("imageId") String imageId){
		Image image = imageList.get(Integer.parseInt(imageId)-1);
		String imageURL = image.getFilePath();
		model.addAttribute("imageURL", imageURL);
		return "analyze";
	}
}
