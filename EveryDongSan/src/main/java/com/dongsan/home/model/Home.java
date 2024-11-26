package com.dongsan.home.model;

import java.math.BigDecimal;

import com.dongsan.home.entity.HomeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Home {
	private int no;
	private String name;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal area;
	private String category;
	private String price;
	private String rentalType;
	private String memberId;
	private String school;
	private int score;
	
	public HomeEntity toEntity() {
		return HomeEntity.builder()
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
				.build();	// schoolId는 따로 변경
	}
}
