package com.dongsan.home.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeEntity {
	private int no;
	private String name;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal area;
	private String category;
	private int price;
	private String rentalType;
	private String memberId;
	private int schoolId;
	
	// 조인
	private int score;
	
}
