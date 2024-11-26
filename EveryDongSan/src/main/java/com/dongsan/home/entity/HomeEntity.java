package com.dongsan.home.entity;

import java.math.BigDecimal;

import com.dongsan.home.model.Home;

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
	private String price;
	private String rentalType;
	private String memberId;
	private int schoolId;
	
	// 조인
	private int score;
	
	public Home toDto() {
		return Home.builder()
				.no(no)
				.name(name)
				.latitude(latitude)
				.longitude(longitude)
				.area(area)
				.category(category)
				.price(price)
				.rentalType(rentalType)
				.memberId(memberId)
				.score(score)
				.build(); 
	}
}
