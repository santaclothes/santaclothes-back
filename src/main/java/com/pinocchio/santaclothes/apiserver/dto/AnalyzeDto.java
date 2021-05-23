package com.pinocchio.santaclothes.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnalyzeDto {
	private long imageId;
	private String[] material;
	private String[] percent;
	private String waterwash;
	private String bleach;
	private String ironing;
	private String dry;
	private String drycleaning;
}
